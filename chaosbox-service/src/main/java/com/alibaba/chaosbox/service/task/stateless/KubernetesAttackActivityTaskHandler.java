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

package com.alibaba.chaosbox.service.task.stateless;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.chaosbox.common.TaskLogRecord;
import com.alibaba.chaosbox.common.constants.ChaosConstant;
import com.alibaba.chaosbox.common.enums.ExperimentDimension;
import com.alibaba.chaosbox.common.exception.BizException;
import com.alibaba.chaosbox.common.utils.JsonUtils;
import com.alibaba.chaosbox.dao.model.ExperimentActivityTaskRecordDO;
import com.alibaba.chaosbox.dao.repository.ExperimentActivityTaskRecordRepository;
import com.alibaba.chaosbox.dao.repository.ExperimentActivityTaskRepository;
import com.alibaba.chaosbox.dao.repository.ExperimentTaskRepository;
import com.alibaba.chaosbox.invoker.ChaosInvokerStrategyContext;
import com.alibaba.chaosbox.invoker.RequestCommand;
import com.alibaba.chaosbox.service.task.ActivityTask;
import com.alibaba.chaosbox.service.task.ActivityTaskExecuteContext;
import com.alibaba.chaosbox.service.task.log.i18n.TaskLogType;
import com.alibaba.chaosbox.service.task.log.i18n.TaskLogUtil;
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

    @Override
    public void handle(ActivityTask activityTask) {
        if (!activityTask.canExecuted()) {
            return;
        }

        String hostname = JsonUtils.writeValueAsString(activityTask.getDeviceMetas());
        final ExperimentActivityTaskRecordDO experimentActivityTaskRecordDO = ExperimentActivityTaskRecordDO.builder()
                .experimentTaskId(activityTask.getExperimentTaskId())
                .flowId(activityTask.getFlowId())
                .hostname(hostname)
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
                experimentActivityTaskRecordRepository.updateByPrimaryKey(experimentActivityTaskRecordDO.getId(), record);

                TaskLogUtil.info(log, TaskLogType.SUB_EXECUTE_EXECUTING, activityTask.getExperimentTaskId(),
                        activityTask.getPhase(),
                        String.valueOf(activityTask.getActivityTaskId()),
                        hostname,
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
