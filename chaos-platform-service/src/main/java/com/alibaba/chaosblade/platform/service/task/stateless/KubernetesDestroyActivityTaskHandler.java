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
import com.alibaba.chaosblade.platform.blade.kubeapi.model.StatusResponseCommand;
import com.alibaba.chaosblade.platform.blade.kubeapi.model.UIDRequest;
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
import com.alibaba.chaosblade.platform.invoker.ResponseCommand;
import com.alibaba.chaosblade.platform.service.logback.TaskLogRecord;
import com.alibaba.chaosblade.platform.service.task.ActivityTask;
import com.alibaba.chaosblade.platform.service.task.ActivityTaskExecuteContext;
import com.alibaba.chaosblade.platform.service.task.TimerFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

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
    protected TimerFactory timerFactory;

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

            CompletableFuture<Void> future = new CompletableFuture<>();
            futures.add(future);
            chaosInvokerStrategyContext.invoke(requestCommand).handleAsync((result, e) -> {
                ExperimentActivityTaskRecordDO record = ExperimentActivityTaskRecordDO.builder().gmtEnd(DateUtil.date()).build();
                if (e != null) {
                    record.setSuccess(false);
                    record.setErrorMessage(e.getMessage());
                } else {
                    record.setResult(result.getResult());
                    record.setCode(result.getCode());
                    record.setErrorMessage(record.getErrorMessage());
                    checkStatus(future, activityTask, result.getResult());
                }
                experimentActivityTaskRecordRepository.updateByPrimaryKey(experimentActivityTaskRecordDO.getId(), record);
                if (e != null) {
                    future.completeExceptionally(e);
                    AnyThrow.throwUnchecked(e);
                }
                return null;
            }, activityTaskExecuteContext.executor());

            future.handleAsync((result, e) -> {
                ExperimentActivityTaskRecordDO record = ExperimentActivityTaskRecordDO.builder().gmtEnd(DateUtil.date()).build();
                if (e != null) {
                    record.setSuccess(false);
                    record.setErrorMessage(e.getMessage());
                } else {
                    record.setSuccess(true);
                }
                experimentActivityTaskRecordRepository.updateByPrimaryKey(experimentActivityTaskRecordDO.getId(), record);
                return null;
            }, activityTaskExecuteContext.executor());
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

    @Override
    public void postHandle(ActivityTask activityTask, Throwable e) {
        super.postHandle(activityTask, e);
    }

    private void checkStatus(CompletableFuture<Void> future, ActivityTask activityTask, String name) {

        timerFactory.getTimer().newTimeout(timeout ->
                {
                    RequestCommand requestCommand = UIDRequest.builder().build();
                    requestCommand.setName(name);
                    requestCommand.setPhase(ChaosConstant.PHASE_STATUS);
                    requestCommand.setSceneCode(activityTask.getSceneCode());
                    requestCommand.setScope(activityTask.getExperimentDimension().name().toLowerCase());
                    CompletableFuture<ResponseCommand> completableFuture = chaosInvokerStrategyContext.invoke(requestCommand);

                    completableFuture.handleAsync((r, e) -> {
                        if (e != null) {
                            future.completeExceptionally(e);
                        } else {
                            StatusResponseCommand statusResponseCommand = (StatusResponseCommand) r;
                            log.info("子任务运行中，检查 CRD 状态，任务ID: {}, 子任务ID: {}, PHASE: {},  是否成功: {}, 失败原因: {}",
                                    activityTask.getExperimentTaskId(),
                                    activityTask.getActivityTaskId(),
                                    statusResponseCommand.getPhase(),
                                    statusResponseCommand.isSuccess(),
                                    statusResponseCommand.getError());

                            String error = statusResponseCommand.getError();

                            if (activityTask.getPhase().equals(ChaosConstant.PHASE_ATTACK)) {
                                if (StrUtil.isNotEmpty(error)) {
                                    future.completeExceptionally(new BizException(error));
                                } else {
                                    if ("Running".equals(statusResponseCommand.getPhase())) {
                                        future.complete(null);
                                    } else {
                                        checkStatus(future, activityTask, name);
                                    }
                                }
                            } else {
                                if ("404".equals(statusResponseCommand.getCode())) {
                                    future.complete(null);
                                    return null;
                                }
                                if (StrUtil.isNotEmpty(error)) {
                                    future.completeExceptionally(new BizException(error));
                                    return null;
                                }
                                if ("Destroyed".equals(statusResponseCommand.getPhase())) {
                                    future.complete(null);
                                } else {
                                    checkStatus(future, activityTask, name);
                                }
                            }
                        }
                        return null;
                    }, activityTaskExecuteContext.executor());
                },
                3000,
                TimeUnit.MILLISECONDS);
    }

}
