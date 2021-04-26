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

package com.alibaba.chaosblade.box.service.task.stateless;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.chaosblade.box.dao.model.ClusterDO;
import com.alibaba.chaosblade.box.dao.repository.ClusterRepository;
import com.alibaba.chaosblade.box.service.task.ActivityTask;
import com.alibaba.chaosblade.box.service.task.ActivityTaskExecuteContext;
import com.alibaba.chaosblade.box.common.TaskLogRecord;
import com.alibaba.chaosblade.box.common.constants.ChaosConstant;
import com.alibaba.chaosblade.box.common.enums.ExperimentDimension;
import com.alibaba.chaosblade.box.common.exception.BizException;
import com.alibaba.chaosblade.box.common.utils.JsonUtils;
import com.alibaba.chaosblade.box.dao.model.ExperimentActivityTaskRecordDO;
import com.alibaba.chaosblade.box.dao.repository.ExperimentActivityTaskRecordRepository;
import com.alibaba.chaosblade.box.dao.repository.ExperimentActivityTaskRepository;
import com.alibaba.chaosblade.box.dao.repository.ExperimentTaskRepository;
import com.alibaba.chaosblade.box.invoker.ChaosInvokerStrategyContext;
import com.alibaba.chaosblade.box.invoker.RequestCommand;
import com.alibaba.chaosblade.box.service.task.log.i18n.TaskLogType;
import com.alibaba.chaosblade.box.service.task.log.i18n.TaskLogUtil;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author yefei
 */
@Slf4j
@TaskLogRecord
@Component
@ActivityTaskHandlerType(value = ChaosConstant.PHASE_ATTACK, dimension = {
        ExperimentDimension.NODE,
        ExperimentDimension.POD,
        ExperimentDimension.CONTAINER,
})
public class KubernetesAttackActivityTaskHandler extends AttackActivityTaskHandler {

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

    @Autowired
    private ClusterRepository clusterRepository;

    @Override
    public void handle(ActivityTask activityTask) {
        if (!activityTask.canExecuted()) {
            return;
        }
        activityTask.getDeviceMetas().forEach((deviceMeta -> {
            ExperimentActivityTaskRecordDO experimentActivityTaskRecordDO = ExperimentActivityTaskRecordDO.builder()
                    .experimentTaskId(activityTask.getExperimentTaskId())
                    .flowId(activityTask.getFlowId())
                    .deviceId(deviceMeta.getDeviceId())
                    .hostname(deviceMeta.identity())
                    .ip(deviceMeta.getIp())
                    .activityTaskId(activityTask.getActivityTaskId())
                    .sceneCode(activityTask.getSceneCode())
                    .gmtStart(DateUtil.date())
                    .phase(activityTask.getPhase())
                    .build();
            experimentActivityTaskRecordRepository.insert(experimentActivityTaskRecordDO);
        }));

        ExperimentDimension experimentDimension = activityTask.getExperimentDimension();
        RequestCommand requestCommand = new RequestCommand();
        requestCommand.setScope(experimentDimension.name().toLowerCase());
        requestCommand.setPhase(activityTask.getPhase());
        requestCommand.setSceneCode(activityTask.getSceneCode());
        requestCommand.setArguments(activityTask.getArguments());
        requestCommand.setConfig(clusterRepository
                .selectById(activityTask.getDeviceMetas().get(0).getClusterId())
                .map(ClusterDO::getConfig)
                .orElse(null));

        chaosInvokerStrategyContext.invoke(requestCommand).handleAsync((result, e) -> {
            try {
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
                UpdateWrapper<ExperimentActivityTaskRecordDO> wrapper = new UpdateWrapper<>();
                wrapper.lambda().eq(ExperimentActivityTaskRecordDO::getActivityTaskId, activityTask.getActivityTaskId());
                experimentActivityTaskRecordRepository.update(record, wrapper);

                TaskLogUtil.info(log, TaskLogType.SUB_EXECUTE_EXECUTING, activityTask.getExperimentTaskId(),
                        activityTask.getPhase(),
                        String.valueOf(activityTask.getActivityTaskId()),
                        JsonUtils.writeValueAsString(activityTask.getDeviceMetas()),
                        String.valueOf(record.getSuccess()),
                        record.getErrorMessage()
                );

            } catch (Exception exception) {
                e = exception;
            } finally {
                postHandle(activityTask, e);
            }
            return null;
        }, activityTaskExecuteContext.executor());

        activityTaskExecuteContext.fireExecute(activityTask.getActivityTaskExecutePipeline());
    }

}
