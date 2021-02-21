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
import com.alibaba.chaosblade.platform.cmmon.constants.ChaosConstant;
import com.alibaba.chaosblade.platform.cmmon.enums.ExperimentDimension;
import com.alibaba.chaosblade.platform.cmmon.enums.RunStatus;
import com.alibaba.chaosblade.platform.cmmon.exception.BizException;
import com.alibaba.chaosblade.platform.cmmon.utils.AnyThrow;
import com.alibaba.chaosblade.platform.dao.model.ExperimentActivityTaskRecordDO;
import com.alibaba.chaosblade.platform.dao.model.ExperimentTaskDO;
import com.alibaba.chaosblade.platform.http.model.reuest.HttpChannelRequest;
import com.alibaba.chaosblade.platform.invoker.ResponseCommand;
import com.alibaba.chaosblade.platform.service.logback.TaskLogRecord;
import com.alibaba.chaosblade.platform.service.model.experiment.activity.ActivityTaskDTO;
import com.alibaba.chaosblade.platform.service.task.ActivityTask;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

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

        final ActivityTaskDTO activityTaskDTO = activityTask.activityTaskDTO();
        // check status
        Byte status = experimentTaskRepository.selectById(activityTaskDTO.getExperimentTaskId())
                .map(ExperimentTaskDO::getRunStatus)
                .orElseThrow(() -> new BizException(EXPERIMENT_TASK_NOT_FOUNT));
        RunStatus runStatus = RunStatus.parse(status);

        log.info("恢复任务阶段, 检查任务状态，任务ID: {}，任务状态: {} ", activityTaskDTO.getExperimentTaskId(), runStatus.name());
        if (runStatus != RunStatus.STOPPING) {
            log.info("恢复任务阶段不可执行，状态不为 STOPPING 的任务，任务ID: {}", activityTaskDTO.getExperimentTaskId());
            return false;
        }
        return true;
    }

    @Override
    public void handle(ActivityTask activityTask) {
        if (!activityTask.canExecuted()) {
            return;
        }
        final ActivityTaskDTO activityTaskDTO = activityTask.activityTaskDTO();

        String sceneCode = activityTaskDTO.getSceneCode();
        List<ExperimentActivityTaskRecordDO> records = experimentActivityTaskRecordRepository.selectBySceneCode(
                activityTaskDTO.getExperimentTaskId(),
                sceneCode.replace(".stop", "")
        );

        List<CompletableFuture<ResponseCommand>> futures = CollUtil.newArrayList();
        for (ExperimentActivityTaskRecordDO record : records) {
            final ExperimentActivityTaskRecordDO experimentActivityTaskRecordDO = ExperimentActivityTaskRecordDO.builder()
                    .ip(record.getIp())
                    .deviceId(record.getDeviceId())
                    .hostname(record.getHostname())
                    .experimentTaskId(activityTaskDTO.getExperimentTaskId())
                    .flowId(activityTaskDTO.getFlowId())
                    .activityTaskId(activityTaskDTO.getActivityTaskId())
                    .sceneCode(activityTaskDTO.getSceneCode())
                    .gmtStart(DateUtil.date())
                    .phase(activityTaskDTO.getPhase())
                    .build();
            experimentActivityTaskRecordRepository.insert(experimentActivityTaskRecordDO);

            ExperimentDimension experimentDimension = activityTaskDTO.getExperimentDimension();
            HttpChannelRequest requestCommand = new HttpChannelRequest();
            requestCommand.setScope(experimentDimension.name().toLowerCase());
            requestCommand.setSceneCode(activityTaskDTO.getSceneCode());
            requestCommand.setArguments(activityTaskDTO.getArguments());
            requestCommand.setHost(record.getIp());
            requestCommand.setPort(chaosAgentPort);
            requestCommand.setPhase(activityTaskDTO.getPhase());
            requestCommand.setSceneCode(activityTaskDTO.getSceneCode());
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
                log.info("子任务运行中，任务ID: {}，阶段：{}, 子任务ID: {}, 当前机器: {}, 是否成功: {}, 失败原因: {}",
                        activityTaskDTO.getExperimentTaskId(),
                        activityTaskDTO.getPhase(),
                        activityTaskDTO.getActivityTaskId(),
                        record.getHostname() + "-" + record.getIp(),
                        recordDO.getSuccess(),
                        recordDO.getErrorMessage());

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
        Long waitOfAfter = activityTaskDTO.getWaitOfAfter();
        if (waitOfAfter != null) {
            log.info("演练阶段完成后等待, 任务ID：{}, 子任务ID: {}, 等待时间：{} 毫秒",
                    activityTaskDTO.getExperimentTaskId(),
                    activityTaskDTO.getActivityTaskId(),
                    waitOfAfter);

            timerFactory.getTimer().newTimeout(timeout ->
                            future.thenRunAsync(
                                    () -> activityTaskExecuteContext.fireExecute(activityTask.getActivityTaskExecutePipeline()),
                                    activityTaskExecuteContext.executor()),
                    waitOfAfter,
                    TimeUnit.MILLISECONDS);
        } else {
            activityTaskExecuteContext.fireExecute(activityTask.getActivityTaskExecutePipeline());
        }
    }
}
