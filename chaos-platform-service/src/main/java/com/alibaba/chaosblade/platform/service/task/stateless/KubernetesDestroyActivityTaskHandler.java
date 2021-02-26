/*
 * Copyright 1999-2021 Alibaba Group Holding Ltd.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.alibaba.chaosblade.platform.service.task.stateless;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.chaosblade.platform.cmmon.TaskLogRecord;
import com.alibaba.chaosblade.platform.cmmon.constants.ChaosConstant;
import com.alibaba.chaosblade.platform.cmmon.enums.ExperimentDimension;
import com.alibaba.chaosblade.platform.cmmon.exception.BizException;
import com.alibaba.chaosblade.platform.cmmon.utils.AnyThrow;
import com.alibaba.chaosblade.platform.dao.model.ExperimentActivityTaskRecordDO;
import com.alibaba.chaosblade.platform.dao.repository.ExperimentActivityTaskRecordRepository;
import com.alibaba.chaosblade.platform.dao.repository.ExperimentActivityTaskRepository;
import com.alibaba.chaosblade.platform.dao.repository.ExperimentTaskRepository;
import com.alibaba.chaosblade.platform.invoker.ChaosInvokerStrategyContext;
import com.alibaba.chaosblade.platform.invoker.RequestCommand;
import com.alibaba.chaosblade.platform.service.task.ActivityTask;
import com.alibaba.chaosblade.platform.service.task.ActivityTaskExecuteContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.CompletableFuture;

/**
 * @author yefei
 */
@Slf4j
@TaskLogRecord
@Component
@ActivityTaskHandlerType(value = ChaosConstant.PHASE_RECOVER, dimension = {
        ExperimentDimension.NODE,
        ExperimentDimension.POD,
        ExperimentDimension.CONTAINER,
})
public class KubernetesDestroyActivityTaskHandler extends DestroyActivityTaskHandler {

    @Autowired
    protected ExperimentActivityTaskRecordRepository experimentActivityTaskRecordRepository;

    @Autowired
    protected ExperimentActivityTaskRepository experimentActivityTaskRepository;

    @Autowired
    protected ExperimentTaskRepository experimentTaskRepository;

    @Autowired
    private ChaosInvokerStrategyContext chaosInvokerStrategyContext;

    @Autowired
    private ActivityTaskExecuteContext activityTaskExecuteContext;

    @Override
    public void handle(ActivityTask activityTask) {
        if (!activityTask.canExecuted()) {
            return;
        }

        String sceneCode = activityTask.getSceneCode();
        List<ExperimentActivityTaskRecordDO> records = experimentActivityTaskRecordRepository.selectBySceneCode(
                activityTask.getExperimentTaskId(),
                sceneCode.replace(".stop", "")
        );

        List<CompletableFuture<Void>> futures = CollUtil.newArrayList();
        for (ExperimentActivityTaskRecordDO recordDO : records) {
            final ExperimentActivityTaskRecordDO experimentActivityTaskRecordDO = ExperimentActivityTaskRecordDO.builder()
                    .experimentTaskId(activityTask.getExperimentTaskId())
                    .flowId(activityTask.getFlowId())
                    .hostname(recordDO.getHostname())
                    .activityTaskId(activityTask.getActivityTaskId())
                    .sceneCode(activityTask.getSceneCode())
                    .gmtStart(DateUtil.date())
                    .phase(activityTask.getPhase())
                    .build();
            experimentActivityTaskRecordRepository.insert(experimentActivityTaskRecordDO);

            ExperimentDimension experimentDimension = activityTask.getExperimentDimension();
            RequestCommand requestCommand = new RequestCommand();
            requestCommand.setScope(experimentDimension.name().toLowerCase());
            requestCommand.setPhase(activityTask.getPhase());
            requestCommand.setSceneCode(activityTask.getSceneCode());
            requestCommand.setArguments(activityTask.getArguments());
            requestCommand.setName(recordDO.getResult());

            futures.add(chaosInvokerStrategyContext.invoke(requestCommand).handleAsync((result, e) -> {
                ExperimentActivityTaskRecordDO record = ExperimentActivityTaskRecordDO.builder().gmtEnd(DateUtil.date()).build();
                if (e != null) {
                    record.setSuccess(false);
                    record.setErrorMessage(e.getMessage());
                } else {
                    record.setSuccess(result.isSuccess());
                    record.setCode(result.getCode());
                    record.setResult(result.getResult());
                    record.setErrorMessage(result.getError());

                    if (!result.isSuccess()) {
                        if (StrUtil.isNotBlank(result.getError())) {
                            e = new BizException(result.getError());
                        } else {
                            e = new BizException(result.getResult());
                        }
                    }
                }
                experimentActivityTaskRecordRepository.updateByPrimaryKey(experimentActivityTaskRecordDO.getId(), record);
                log.info("子任务运行中，任务ID: {}，阶段：{}, 子任务ID: {}, 当前机器: {}, 是否成功: {}, 失败原因: {}",
                        activityTask.getExperimentTaskId(),
                        activityTask.getPhase(),
                        activityTask.getActivityTaskId(),
                        recordDO.getHostname(),
                        record.getSuccess(),
                        record.getErrorMessage());

                if (e != null) {
                    AnyThrow.throwUnchecked(e);
                }
                return null;
            }, activityTaskExecuteContext.executor()));
        }

        CompletableFuture<Void> future = CompletableFuture.allOf(futures.toArray(new CompletableFuture[0]));

        future.handleAsync((r, e) -> {
            postHandle(activityTask, e);
            return null;
        }, activityTaskExecuteContext.executor());

        activityTaskExecuteContext.fireExecute(activityTask.getActivityTaskExecutePipeline());
    }

}
