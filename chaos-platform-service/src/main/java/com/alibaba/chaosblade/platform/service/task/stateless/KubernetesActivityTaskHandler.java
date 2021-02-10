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

import cn.hutool.core.date.DateUtil;
import com.alibaba.chaosblade.platform.cmmon.enums.ExperimentDimension;
import com.alibaba.chaosblade.platform.cmmon.utils.AnyThrow;
import com.alibaba.chaosblade.platform.dao.model.ExperimentActivityTaskRecordDO;
import com.alibaba.chaosblade.platform.dao.repository.ExperimentActivityTaskRecordRepository;
import com.alibaba.chaosblade.platform.dao.repository.ExperimentActivityTaskRepository;
import com.alibaba.chaosblade.platform.dao.repository.ExperimentTaskRepository;
import com.alibaba.chaosblade.platform.invoker.ChaosInvokerStrategyContext;
import com.alibaba.chaosblade.platform.invoker.RequestCommand;
import com.alibaba.chaosblade.platform.service.logback.TaskLogRecord;
import com.alibaba.chaosblade.platform.service.model.experiment.activity.ActivityTaskDTO;
import com.alibaba.chaosblade.platform.service.task.ActivityTask;
import com.alibaba.chaosblade.platform.service.task.ActivityTaskExecuteContext;
import com.alibaba.chaosblade.platform.service.task.TimerFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

/**
 * @author yefei
 */
@Slf4j
@TaskLogRecord
@Component
public class KubernetesActivityTaskHandler extends AbstractActivityTaskHandler {

    @Autowired
    protected ExperimentActivityTaskRecordRepository experimentActivityTaskRecordRepository;

    @Autowired
    protected ExperimentActivityTaskRepository experimentActivityTaskRepository;

    @Autowired
    protected ExperimentTaskRepository experimentTaskRepository;

    @Autowired
    protected TimerFactory timerFactory;

    @Autowired
    private ChaosInvokerStrategyContext chaosInvokerStrategyContext;

    @Override
    public void handle(ActivityTaskExecuteContext context, ActivityTask activityTask) {

        final ActivityTaskDTO activityTaskDTO = activityTask.activityTaskDTO();

        ExperimentDimension experimentDimension = activityTaskDTO.getExperimentDimension();

        String hosts = activityTaskDTO.getArguments().get("names");
        final ExperimentActivityTaskRecordDO experimentActivityTaskRecordDO = ExperimentActivityTaskRecordDO.builder()
                .experimentTaskId(activityTaskDTO.getExperimentTaskId())
                .flowId(activityTaskDTO.getFlowId())
                .hostname(hosts)
                .activityTaskId(activityTaskDTO.getActivityTaskId())
                .sceneCode(activityTaskDTO.getSceneCode())
                .gmtStart(DateUtil.date())
                .phase(activityTaskDTO.getPhase())
                .build();
        experimentActivityTaskRecordRepository.insert(experimentActivityTaskRecordDO);

        RequestCommand requestCommand = requestCommand(activityTask, null);

        experimentActivityTaskRecordRepository.updateByPrimaryKey(experimentActivityTaskRecordDO.getId(),
                ExperimentActivityTaskRecordDO.builder()
                        .gmtEnd(DateUtil.date())
                        .success(true)
                        .build()
        );

        requestCommand.setScope(experimentDimension.name().toLowerCase());
        requestCommand.setPhase(activityTaskDTO.getPhase());
        requestCommand.setSceneCode(activityTaskDTO.getSceneCode());
        requestCommand.setArguments(activityTaskDTO.getArguments());

        CompletableFuture<Void> future = new CompletableFuture<>();;
        chaosInvokerStrategyContext.invoke(requestCommand).handleAsync((result, e) -> {
            ExperimentActivityTaskRecordDO record = ExperimentActivityTaskRecordDO.builder().gmtEnd(DateUtil.date()).build();
            if (e != null) {
                record.setSuccess(false);
                record.setErrorMessage(e.getMessage());
            } else {
                //checkStatus(context, finalFuture, result.getResult());
                record.setCode(result.getCode());
                record.setResult(result.getResult());
            }
            experimentActivityTaskRecordRepository.updateByPrimaryKey(experimentActivityTaskRecordDO.getId(), record);

            if (e != null) {
                future.completeExceptionally(e);
                AnyThrow.throwUnchecked(e);
            }
            return null;
        }, context.executor());


        future.handleAsync((r, e) -> {
            postHandle(context, activityTask, e);
            return null;
        }, context.executor());

        // 执行后等待, 同步执行后续所有任务
        Long waitOfAfter = activityTaskDTO.getWaitOfAfter();
        if (waitOfAfter != null) {
            log.info("演练阶段完成后等待, 任务ID：{}, 子任务ID: {}, 等待时间：{} 毫秒",
                    activityTaskDTO.getExperimentTaskId(),
                    activityTaskDTO.getActivityTaskId(),
                    waitOfAfter);

            timerFactory.getTimer().newTimeout(timeout ->
                            future.thenRunAsync(context::fireExecute, context.executor()),
                    waitOfAfter,
                    TimeUnit.MILLISECONDS);
        } else {
            context.fireExecute();
        }
    }

}
