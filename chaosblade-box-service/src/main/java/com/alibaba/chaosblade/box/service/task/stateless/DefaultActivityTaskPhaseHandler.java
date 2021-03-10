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

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.chaosblade.box.service.task.ActivityTask;
import com.alibaba.chaosblade.box.service.task.ActivityTaskExecuteContext;
import com.alibaba.chaosblade.box.common.DeviceMeta;
import com.alibaba.chaosblade.box.common.TaskLogRecord;
import com.alibaba.chaosblade.box.common.constants.ChaosConstant;
import com.alibaba.chaosblade.box.common.enums.ExperimentDimension;
import com.alibaba.chaosblade.box.common.enums.RunStatus;
import com.alibaba.chaosblade.box.common.exception.BizException;
import com.alibaba.chaosblade.box.common.utils.AnyThrow;
import com.alibaba.chaosblade.box.dao.model.ExperimentActivityTaskDO;
import com.alibaba.chaosblade.box.dao.model.ExperimentActivityTaskRecordDO;
import com.alibaba.chaosblade.box.dao.model.ExperimentTaskDO;
import com.alibaba.chaosblade.box.dao.repository.ExperimentActivityTaskRecordRepository;
import com.alibaba.chaosblade.box.dao.repository.ExperimentActivityTaskRepository;
import com.alibaba.chaosblade.box.dao.repository.ExperimentTaskRepository;
import com.alibaba.chaosblade.box.invoker.http.model.reuest.HttpChannelRequest;
import com.alibaba.chaosblade.box.invoker.ChaosInvokerStrategyContext;
import com.alibaba.chaosblade.box.invoker.ResponseCommand;
import com.alibaba.chaosblade.box.service.task.log.i18n.TaskLogType;
import com.alibaba.chaosblade.box.service.task.log.i18n.TaskLogUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

import static com.alibaba.chaosblade.box.common.exception.ExceptionMessageEnum.EXPERIMENT_SUB_TASK_NOT_FOUNT;
import static com.alibaba.chaosblade.box.common.exception.ExceptionMessageEnum.EXPERIMENT_TASK_NOT_FOUNT;

/**
 * @author yefei
 */
@Slf4j
@TaskLogRecord
@Component
@ActivityTaskHandlerType(value = ChaosConstant.PHASE_PREPARE)
public class DefaultActivityTaskPhaseHandler implements ActivityTaskHandler {

    @Autowired
    protected ExperimentActivityTaskRecordRepository experimentActivityTaskRecordRepository;

    @Autowired
    protected ExperimentActivityTaskRepository experimentActivityTaskRepository;

    @Autowired
    protected ExperimentTaskRepository experimentTaskRepository;

    @Value("${chaos.agent.port}")
    protected int chaosAgentPort;

    @Autowired
    protected ChaosInvokerStrategyContext chaosInvokerStrategyContext;

    @Autowired
    protected ActivityTaskExecuteContext activityTaskExecuteContext;

    @Autowired
    protected MessageSource messageSource;

    @Override
    public boolean preHandle(ActivityTask activityTask) {
        // check status
        Byte status = experimentTaskRepository.selectById(activityTask.getExperimentTaskId())
                .map(ExperimentTaskDO::getRunStatus)
                .orElseThrow(() -> new BizException(EXPERIMENT_TASK_NOT_FOUNT));
        RunStatus runStatus = RunStatus.parse(status);

        TaskLogUtil.info(log, TaskLogType.CHECK_TASK_STATUS, activityTask.getExperimentTaskId(), runStatus.name());
        if (runStatus == RunStatus.READY || runStatus == RunStatus.RUNNING) {

            ExperimentActivityTaskDO activityTaskDO = experimentActivityTaskRepository.selectById(activityTask.getActivityTaskId())
                    .orElseThrow(() -> new BizException(EXPERIMENT_SUB_TASK_NOT_FOUNT));
            RunStatus subRunStatus = RunStatus.parse(activityTaskDO.getRunStatus());

            TaskLogUtil.info(log, TaskLogType.CHECK_SUB_TASK_STATUS, activityTask.getExperimentTaskId(), subRunStatus.name());

            if (subRunStatus == RunStatus.READY) {
                TaskLogUtil.info(log, TaskLogType.EXECUTE_SUB_TASK, activityTask.getExperimentTaskId(), activityTask.getPhase(),
                        String.valueOf(activityTask.getActivityTaskId()));

                // update activity task status -> RUNNING
                experimentActivityTaskRepository.updateByPrimaryKey(activityTask.getActivityTaskId(), ExperimentActivityTaskDO.builder()
                        .phase(activityTask.getPhase())
                        .runStatus(RunStatus.RUNNING.getValue())
                        .gmtStart(DateUtil.date())
                        .build());

                experimentTaskRepository.updateByPrimaryKey(activityTask.getExperimentTaskId(), ExperimentTaskDO.builder()
                        .activityTaskId(activityTask.getActivityId())
                        .activityId(activityTask.getActivityId())
                        .build());

                return true;
            } else {
                TaskLogUtil.info(log, TaskLogType.SUB_TASK_UNABLE_EXECUTE, activityTask.getExperimentTaskId(), activityTask.getPhase(),
                        String.valueOf(activityTask.getActivityTaskId()));
            }
        } else {
            TaskLogUtil.info(log, TaskLogType.TASK_UNABLE_EXECUTE, activityTask.getExperimentTaskId(), activityTask.getPhase(), runStatus.name());
        }
        return false;
    }


    @Override
    public void handle(ActivityTask activityTask) {
        if (!activityTask.canExecuted()) {
            return;
        }

        List<CompletableFuture<ResponseCommand>> futures = CollUtil.newArrayList();

        for (DeviceMeta deviceMeta : activityTask.getDeviceMetas()) {

            final ExperimentActivityTaskRecordDO experimentActivityTaskRecordDO = ExperimentActivityTaskRecordDO.builder()
                    .ip(deviceMeta.getIp())
                    .deviceId(deviceMeta.getDeviceId())
                    .hostname(deviceMeta.getHostname())
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
            requestCommand.setHost(deviceMeta.getIp());
            requestCommand.setPort(chaosAgentPort);
            requestCommand.setPhase(activityTask.getPhase());
            requestCommand.setSceneCode(activityTask.getSceneCode());

            CompletableFuture<ResponseCommand> invoke = chaosInvokerStrategyContext.invoke(requestCommand);
            futures.add(invoke.handleAsync((result, e) -> {
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
                        deviceMeta.getHostname() + "-" + deviceMeta.getIp(),
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

        // 执行后等待, 同步执行后续所有任务
        Long waitOfAfter = activityTask.getWaitOfAfter();
        if (waitOfAfter != null) {

            TaskLogUtil.info(log, TaskLogType.EXPERIMENT_WAIT_OF_AFTER, activityTask.getExperimentTaskId(),
                    String.valueOf(activityTask.getActivityTaskId()),
                    String.valueOf(waitOfAfter)
            );

            activityTaskExecuteContext.timer().newTimeout(timeout ->
                            future.thenRunAsync(
                                    () -> activityTaskExecuteContext.fireExecute(activityTask.getActivityTaskExecutePipeline()),
                                    activityTaskExecuteContext.executor()),
                    waitOfAfter,
                    TimeUnit.MILLISECONDS);
        } else {
            activityTaskExecuteContext.fireExecute(activityTask.getActivityTaskExecutePipeline());
        }
    }

    @Override
    public void postHandle(ActivityTask activityTask, Throwable e) {

        if (e != null) {
            TaskLogUtil.error(log, TaskLogType.SUB_EXECUTE_ERROR, activityTask.getExperimentTaskId(),
                    e,
                    activityTask.getPhase(),
                    String.valueOf(activityTask.getActivityTaskId())
            );
            activityTask.future().completeExceptionally(e);
        } else {
            TaskLogUtil.info(log, TaskLogType.SUB_EXECUTE_SUCCESS, activityTask.getExperimentTaskId(),
                    activityTask.getPhase(),
                    String.valueOf(activityTask.getActivityTaskId())
            );

            Long waitOfAfter = activityTask.getWaitOfAfter();
            if (waitOfAfter != null) {

                TaskLogUtil.info(log, TaskLogType.EXPERIMENT_WAIT_OF_AFTER, activityTask.getExperimentTaskId(),
                        String.valueOf(activityTask.getActivityTaskId()),
                        String.valueOf(waitOfAfter)
                );

                activityTaskExecuteContext.timer().newTimeout(timeout -> activityTask.future().complete(null),
                        waitOfAfter,
                        TimeUnit.MILLISECONDS);
            } else {
                activityTask.future().complete(null);
            }
        }
    }

}
