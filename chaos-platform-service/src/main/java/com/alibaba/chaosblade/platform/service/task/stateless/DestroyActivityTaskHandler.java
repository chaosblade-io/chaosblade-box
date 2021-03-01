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
import com.alibaba.chaosblade.platform.cmmon.enums.RunStatus;
import com.alibaba.chaosblade.platform.cmmon.exception.BizException;
import com.alibaba.chaosblade.platform.cmmon.utils.AnyThrow;
import com.alibaba.chaosblade.platform.dao.model.ExperimentActivityTaskRecordDO;
import com.alibaba.chaosblade.platform.dao.model.ExperimentTaskDO;
import com.alibaba.chaosblade.platform.http.model.reuest.HttpChannelRequest;
import com.alibaba.chaosblade.platform.invoker.ResponseCommand;
import com.alibaba.chaosblade.platform.service.task.ActivityTask;
import com.alibaba.chaosblade.platform.service.task.log.i18n.TaskLogType;
import com.alibaba.chaosblade.platform.service.task.log.i18n.TaskLogUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.CompletableFuture;

import static com.alibaba.chaosblade.platform.cmmon.exception.ExceptionMessageEnum.EXPERIMENT_TASK_NOT_FOUNT;

/**
 * @author yefei
 */
@Slf4j
@Component
@TaskLogRecord
@ActivityTaskHandlerType(ChaosConstant.PHASE_RECOVER)
public class DestroyActivityTaskHandler extends DefaultActivityTaskPhaseHandler {

    @Override
    public boolean preHandle(ActivityTask activityTask) {
        // check status
        Byte status = experimentTaskRepository.selectById(activityTask.getExperimentTaskId())
                .map(ExperimentTaskDO::getRunStatus)
                .orElseThrow(() -> new BizException(EXPERIMENT_TASK_NOT_FOUNT));
        RunStatus runStatus = RunStatus.parse(status);

        TaskLogUtil.info(log, TaskLogType.RECOVER_CHECK_SUB_TASK_STATUS, activityTask.getExperimentTaskId(), runStatus.name());

        if (runStatus != RunStatus.STOPPING) {
            TaskLogUtil.info(log, TaskLogType.RECOVER_PHASE_UNABLE_EXECUTE, activityTask.getExperimentTaskId());
            return false;
        }
        return true;
    }

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

        List<CompletableFuture<ResponseCommand>> futures = CollUtil.newArrayList();
        for (ExperimentActivityTaskRecordDO record : records) {
            final ExperimentActivityTaskRecordDO experimentActivityTaskRecordDO = ExperimentActivityTaskRecordDO.builder()
                    .ip(record.getIp())
                    .deviceId(record.getDeviceId())
                    .hostname(record.getHostname())
                    .experimentTaskId(activityTask.getExperimentTaskId())
                    .flowId(activityTask.getFlowId())
                    .activityTaskId(activityTask.getActivityTaskId())
                    .sceneCode(activityTask.getSceneCode())
                    .gmtStart(DateUtil.date())
                    .phase(activityTask.getPhase())
                    .build();
            experimentActivityTaskRecordRepository.insert(experimentActivityTaskRecordDO);

            ExperimentDimension experimentDimension = activityTask.getExperimentDimension();
            HttpChannelRequest requestCommand = new HttpChannelRequest();
            requestCommand.setScope(experimentDimension.name().toLowerCase());
            requestCommand.setSceneCode(activityTask.getSceneCode());
            requestCommand.setArguments(activityTask.getArguments());
            requestCommand.setHost(record.getIp());
            requestCommand.setPort(chaosAgentPort);
            requestCommand.setPhase(activityTask.getPhase());
            requestCommand.setSceneCode(activityTask.getSceneCode());
            if (record.getSuccess()) {
                requestCommand.setName(record.getResult());
            }

            CompletableFuture<ResponseCommand> invoke = chaosInvokerStrategyContext.invoke(requestCommand);
            futures.add(invoke.handleAsync((result, e) -> {
                ExperimentActivityTaskRecordDO recordDO = ExperimentActivityTaskRecordDO.builder().gmtEnd(DateUtil.date()).build();
                if (e != null) {
                    recordDO.setSuccess(false);
                    recordDO.setErrorMessage(e.getMessage());
                } else {
                    recordDO.setSuccess(result.isSuccess());
                    recordDO.setCode(result.getCode());
                    recordDO.setResult(result.getResult());
                    recordDO.setErrorMessage(result.getError());

                    if (!result.isSuccess()) {
                        if (StrUtil.isNotBlank(result.getError())) {
                            e = new BizException(result.getError());
                        } else {
                            e = new BizException(result.getResult());
                        }
                    }
                }
                experimentActivityTaskRecordRepository.updateByPrimaryKey(experimentActivityTaskRecordDO.getId(), recordDO);

                TaskLogUtil.info(log, TaskLogType.SUB_EXECUTE_EXECUTING, activityTask.getExperimentTaskId(),
                        activityTask.getPhase(),
                        String.valueOf(activityTask.getActivityTaskId()),
                        record.getHostname() + "-" + record.getIp(),
                        String.valueOf(record.getSuccess()),
                        record.getErrorMessage()
                );

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
