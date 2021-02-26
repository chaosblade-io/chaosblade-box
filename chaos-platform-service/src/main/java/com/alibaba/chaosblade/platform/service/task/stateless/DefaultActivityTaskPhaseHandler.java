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
import com.alibaba.chaosblade.platform.cmmon.DeviceMeta;
import com.alibaba.chaosblade.platform.cmmon.TaskLogRecord;
import com.alibaba.chaosblade.platform.cmmon.constants.ChaosConstant;
import com.alibaba.chaosblade.platform.cmmon.enums.ExperimentDimension;
import com.alibaba.chaosblade.platform.cmmon.enums.RunStatus;
import com.alibaba.chaosblade.platform.cmmon.exception.BizException;
import com.alibaba.chaosblade.platform.cmmon.utils.AnyThrow;
import com.alibaba.chaosblade.platform.dao.model.ExperimentActivityTaskDO;
import com.alibaba.chaosblade.platform.dao.model.ExperimentActivityTaskRecordDO;
import com.alibaba.chaosblade.platform.dao.model.ExperimentTaskDO;
import com.alibaba.chaosblade.platform.dao.repository.ExperimentActivityTaskRecordRepository;
import com.alibaba.chaosblade.platform.dao.repository.ExperimentActivityTaskRepository;
import com.alibaba.chaosblade.platform.dao.repository.ExperimentTaskRepository;
import com.alibaba.chaosblade.platform.http.model.reuest.HttpChannelRequest;
import com.alibaba.chaosblade.platform.invoker.ChaosInvokerStrategyContext;
import com.alibaba.chaosblade.platform.invoker.ResponseCommand;
import com.alibaba.chaosblade.platform.service.task.ActivityTask;
import com.alibaba.chaosblade.platform.service.task.ActivityTaskExecuteContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

import static com.alibaba.chaosblade.platform.cmmon.exception.ExceptionMessageEnum.EXPERIMENT_TASK_NOT_FOUNT;

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

    @Override
    public boolean preHandle(ActivityTask activityTask) {
        // check status
        Byte status = experimentTaskRepository.selectById(activityTask.getExperimentTaskId())
                .map(ExperimentTaskDO::getRunStatus)
                .orElseThrow(() -> new BizException(EXPERIMENT_TASK_NOT_FOUNT));
        RunStatus runStatus = RunStatus.parse(status);

        log.info("检查任务状态，任务ID: {}，任务状态: {} ", activityTask.getExperimentTaskId(), runStatus.name());
        if (runStatus == RunStatus.READY || runStatus == RunStatus.RUNNING) {

            ExperimentActivityTaskDO activityTaskDO = experimentActivityTaskRepository.selectById(activityTask.getActivityTaskId())
                    .orElseThrow(() -> new BizException("子任务不存在"));
            RunStatus subRunStatus = RunStatus.parse(activityTaskDO.getRunStatus());
            log.info("检查子任务状态，子任务ID: {}，任务状态: {} ", activityTaskDO.getExperimentTaskId(), subRunStatus.name());
            if (subRunStatus == RunStatus.READY) {
                log.info("开始运行子运行，任务ID: {}，阶段：{}, 子任务ID：{} ",
                        activityTask.getExperimentTaskId(),
                        activityTask.getPhase(),
                        activityTask.getActivityTaskId()
                );

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
                log.warn("子运行状态不可运行，任务ID: {}，阶段：{}, 子任务ID：{} ",
                        activityTask.getExperimentTaskId(),
                        activityTask.getPhase(),
                        activityTask.getActivityTaskId()
                );
            }
        } else {
            log.warn("当前任务状态不可运行，任务ID: {}，阶段：{}, 状态：{} ",
                    activityTask.getExperimentTaskId(),
                    activityTask.getPhase(),
                    runStatus.name());
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
                log.info("子任务运行中，任务ID: {}，阶段：{}, 子任务ID: {}, 当前机器: {}, 是否成功: {}, 失败原因: {}",
                        activityTask.getExperimentTaskId(),
                        activityTask.getPhase(),
                        activityTask.getActivityTaskId(),
                        deviceMeta.getHostname() + "-" + deviceMeta.getIp(),
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

        // 执行后等待, 同步执行后续所有任务
        Long waitOfAfter = activityTask.getWaitOfAfter();
        if (waitOfAfter != null) {
            log.info("演练阶段完成后等待, 任务ID：{}, 子任务ID: {}, 等待时间：{} 毫秒",
                    activityTask.getExperimentTaskId(),
                    activityTask.getActivityTaskId(),
                    waitOfAfter);

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
            log.error("子任务运行失败，任务ID: {}，阶段：{}, 子任务ID: {}, 失败原因: ",
                    activityTask.getExperimentTaskId(),
                    activityTask.getPhase(),
                    activityTask.getActivityTaskId(),
                    e);
            activityTask.future().completeExceptionally(e);
        } else {
            log.info("子任务运行完成，任务ID: {}，阶段：{}, 子任务ID：{} ",
                    activityTask.getExperimentTaskId(),
                    activityTask.getPhase(),
                    activityTask.getActivityTaskId());

            Long waitOfAfter = activityTask.getWaitOfAfter();
            if (waitOfAfter != null) {
                log.info("演练阶段完成后等待通知, 任务ID：{}, 子任务ID: {} 等待时间：{} 毫秒",
                        activityTask.getExperimentTaskId(),
                        activityTask.getActivityTaskId(),
                        waitOfAfter);
                activityTaskExecuteContext.timer().newTimeout(timeout -> activityTask.future().complete(null),
                        waitOfAfter,
                        TimeUnit.MILLISECONDS);
            } else {
                activityTask.future().complete(null);
            }
        }
    }

}
