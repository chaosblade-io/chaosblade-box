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

package com.alibaba.chaosblade.platform.service.task;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.chaosblade.platform.cmmon.DeviceMeta;
import com.alibaba.chaosblade.platform.cmmon.constants.ChaosConstant;
import com.alibaba.chaosblade.platform.cmmon.enums.DeviceType;
import com.alibaba.chaosblade.platform.cmmon.enums.ResultStatus;
import com.alibaba.chaosblade.platform.cmmon.enums.RunStatus;
import com.alibaba.chaosblade.platform.cmmon.exception.BizException;
import com.alibaba.chaosblade.platform.cmmon.utils.AnyThrow;
import com.alibaba.chaosblade.platform.dao.model.ExperimentActivityTaskDO;
import com.alibaba.chaosblade.platform.dao.model.ExperimentActivityTaskRecordDO;
import com.alibaba.chaosblade.platform.dao.model.ExperimentTaskDO;
import com.alibaba.chaosblade.platform.dao.repository.*;
import com.alibaba.chaosblade.platform.http.model.reuest.HttpChannelRequest;
import com.alibaba.chaosblade.platform.invoker.ChaosInvokerStrategyContext;
import com.alibaba.chaosblade.platform.invoker.ResponseCommand;
import com.alibaba.chaosblade.platform.service.logback.TaskLogRecord;
import com.alibaba.chaosblade.platform.service.model.experiment.activity.ActivityTaskDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

import static com.alibaba.chaosblade.platform.cmmon.exception.ExceptionMessageEnum.EXPERIMENT_TASK_NOT_FOUNT;

/**
 * @author yefei
 */
@Slf4j
@TaskLogRecord
public abstract class AbstractActivityTask<R extends HttpChannelRequest> implements ActivityTask {

    protected final ActivityTaskDTO activityTaskDTO;

    protected final CompletableFuture<Void> completableFuture;

    protected final Long activityTaskId;

    @Autowired
    protected ExperimentActivityTaskRepository experimentActivityTaskRepository;

    @Autowired
    protected ExperimentTaskRepository experimentTaskRepository;

    @Autowired
    protected ChaosInvokerStrategyContext chaosInvokerStrategyContext;

    @Autowired
    protected TimerFactory timerFactory;

    private final AtomicBoolean atomicBoolean = new AtomicBoolean(false);

    @Autowired
    protected ExperimentActivityTaskRecordRepository experimentActivityTaskRecordRepository;

    @Autowired
    protected DeviceRepository deviceRepository;

    public AbstractActivityTask(ActivityTaskDTO activityTaskDTO) {
        this.activityTaskDTO = activityTaskDTO;
        this.completableFuture = new CompletableFuture<>();
        this.activityTaskId = activityTaskDTO.getActivityTaskId();
    }

    @Override
    public boolean preHandler(ActivityTaskExecuteContext context) {

        // check status
        Byte status = experimentTaskRepository.selectById(activityTaskDTO.getExperimentTaskId())
                .map(ExperimentTaskDO::getRunStatus)
                .orElseThrow(() -> new BizException(EXPERIMENT_TASK_NOT_FOUNT));
        RunStatus runStatus = RunStatus.parse(status);

        log.info("检查任务状态，任务ID: {}，任务状态: {} ", activityTaskDTO.getExperimentTaskId(), runStatus.name());
        if (runStatus == RunStatus.READY || runStatus == RunStatus.RUNNING) {

            ExperimentActivityTaskDO activityTaskDO = experimentActivityTaskRepository.selectById(activityTaskDTO.getActivityTaskId())
                    .orElseThrow(() -> new BizException("子任务不存在"));
            RunStatus subRunStatus = RunStatus.parse(activityTaskDO.getRunStatus());
            log.info("检查子任务状态，子任务ID: {}，任务状态: {} ", activityTaskDO.getExperimentTaskId(), subRunStatus.name());
            if (subRunStatus == RunStatus.READY) {
                log.info("开始运行子运行，任务ID: {}，阶段：{}, 子任务ID：{} ",
                        activityTaskDTO.getExperimentTaskId(),
                        activityTaskDTO.getPhase(),
                        activityTaskDTO.getActivityTaskId()
                );

                // update activity task status -> RUNNING
                experimentActivityTaskRepository.updateByPrimaryKey(activityTaskDTO.getActivityTaskId(), ExperimentActivityTaskDO.builder()
                        .phase(activityTaskDTO.getPhase())
                        .runStatus(RunStatus.RUNNING.getValue())
                        .gmtStart(DateUtil.date())
                        .build());

                experimentTaskRepository.updateByPrimaryKey(activityTaskDTO.getExperimentTaskId(), ExperimentTaskDO.builder()
                        .activityTaskId(activityTaskDTO.getActivityId())
                        .activityId(activityTaskDTO.getActivityId())
                        .build());

                // update scene use count
                String sceneCode = activityTaskDO.getSceneCode();


                return true;
            } else {
                log.warn("子运行状态不可运行，任务ID: {}，阶段：{}, 子任务ID：{} ",
                        activityTaskDTO.getExperimentTaskId(),
                        activityTaskDTO.getPhase(),
                        activityTaskDTO.getActivityTaskId()
                );
            }
        } else {
            log.warn("当前任务状态不可运行，任务ID: {}，状态：{} ", activityTaskDTO.getExperimentTaskId(), runStatus.name());
        }
        return false;
    }

    @Override
    public void postHandler(ActivityTaskExecuteContext context, Throwable throwable) {

        List<ExperimentActivityTaskRecordDO> records = experimentActivityTaskRecordRepository.selectExperimentTaskId(activityTaskDTO.getExperimentTaskId());
        long count = records.stream().filter(r -> r.getPhase().equals(ChaosConstant.PHASE_ATTACK) && r.getSuccess()).count();

        // update activity task
        experimentActivityTaskRepository.updateByPrimaryKey(activityTaskDTO.getActivityTaskId(),
                ExperimentActivityTaskDO.builder()
                        .runStatus(RunStatus.FINISHED.getValue())
                        .resultStatus(count > 0 ? ResultStatus.SUCCESS.getValue() : ResultStatus.FAILED.getValue())
                        .errorMessage(throwable != null ? throwable.getMessage() : StrUtil.EMPTY)
                        .gmtEnd(DateUtil.date())
                        .build());

        if (throwable == null) {
            log.info("子任务运行完成，任务ID: {}，阶段：{}, 子任务ID：{} ",
                    activityTaskDTO.getExperimentTaskId(),
                    activityTaskDTO.getPhase(),
                    activityTaskDTO.getActivityTaskId(),
                    throwable
            );
        } else {
            log.error("子任务运行失败，任务ID: {}，阶段：{}, 子任务ID: {}, 失败原因: {} ",
                    activityTaskDTO.getExperimentTaskId(),
                    activityTaskDTO.getPhase(),
                    activityTaskDTO.getActivityTaskId(),
                    throwable.getMessage()
            );
        }

        completableFuture.complete(null);
    }

    @Override
    public void execute(ActivityTaskExecuteContext context) {
        if (!atomicBoolean.compareAndSet(false, true)) {
            return;
        }

        List<CompletableFuture<ResponseCommand>> futures = CollUtil.newArrayList();
        for (DeviceMeta deviceMeta : activityTaskDTO.getDeviceMetas()) {

            final ExperimentActivityTaskRecordDO experimentActivityTaskRecordDO = ExperimentActivityTaskRecordDO.builder()
                    .ip(deviceMeta.getIp())
                    .deviceId(deviceMeta.getDeviceId())
                    .hostname(deviceMeta.getHostname())
                    .experimentTaskId(activityTaskDTO.getExperimentTaskId())
                    .flowId(activityTaskDTO.getFlowId())
                    .activityTaskId(activityTaskDTO.getActivityTaskId())
                    .sceneCode(activityTaskDTO.getSceneCode())
                    .gmtStart(DateUtil.date())
                    .phase(activityTaskDTO.getPhase())
                    .build();
            experimentActivityTaskRecordRepository.insert(experimentActivityTaskRecordDO);

            R requestCommand = requestCommand(deviceMeta);
            if (requestCommand == null) {
                experimentActivityTaskRecordRepository.updateByPrimaryKey(experimentActivityTaskRecordDO.getId(),
                        ExperimentActivityTaskRecordDO.builder()
                                .gmtEnd(DateUtil.date())
                                .success(true)
                                .build()
                );
                continue;
            }

            requestCommand.setHost(deviceMeta.getIp());
            requestCommand.setPort(19527);

            requestCommand.setScope( DeviceType.transByCode(deviceMeta.getDeviceType()).name().toLowerCase());
            requestCommand.setPhase(activityTaskDTO.getPhase());
            requestCommand.setSceneCode(activityTaskDTO.getSceneCode());

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
                }
                experimentActivityTaskRecordRepository.updateByPrimaryKey(experimentActivityTaskRecordDO.getId(), record);
                if (e != null) {
                    AnyThrow.throwUnchecked(e);
                }
                return null;
            }, context.executor()));
        }
        CompletableFuture<Void> voidCompletableFuture = CompletableFuture.allOf(futures.stream().toArray(CompletableFuture[]::new))
                .handleAsync((r, e) -> {
                    postHandler(context, e);
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
                            voidCompletableFuture.thenRunAsync(context::fireExecute, context.executor()),
                    waitOfAfter,
                    TimeUnit.MILLISECONDS);
            return;
        } else {
            context.fireExecute();
        }
    }

    @Override
    public CompletableFuture<Void> future() {
        return completableFuture;
    }

    @Override
    public Long activityTaskId() {
        return activityTaskId;
    }

    @Override
    public ActivityTaskDTO activityTaskDTO() {
        return activityTaskDTO;
    }

    /**
     * @return
     */
    public abstract R requestCommand(DeviceMeta deviceMeta);
}
