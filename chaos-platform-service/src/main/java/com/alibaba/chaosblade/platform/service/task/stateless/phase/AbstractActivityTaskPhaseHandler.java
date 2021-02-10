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

package com.alibaba.chaosblade.platform.service.task.stateless.phase;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.chaosblade.platform.cmmon.constants.ChaosConstant;
import com.alibaba.chaosblade.platform.cmmon.enums.ResultStatus;
import com.alibaba.chaosblade.platform.cmmon.enums.RunStatus;
import com.alibaba.chaosblade.platform.cmmon.exception.BizException;
import com.alibaba.chaosblade.platform.dao.model.ExperimentActivityTaskDO;
import com.alibaba.chaosblade.platform.dao.model.ExperimentActivityTaskRecordDO;
import com.alibaba.chaosblade.platform.dao.model.ExperimentTaskDO;
import com.alibaba.chaosblade.platform.dao.repository.ExperimentActivityTaskRecordRepository;
import com.alibaba.chaosblade.platform.dao.repository.ExperimentActivityTaskRepository;
import com.alibaba.chaosblade.platform.dao.repository.ExperimentTaskRepository;
import com.alibaba.chaosblade.platform.service.logback.TaskLogRecord;
import com.alibaba.chaosblade.platform.service.model.experiment.activity.ActivityTaskDTO;
import com.alibaba.chaosblade.platform.service.task.ActivityTask;
import com.alibaba.chaosblade.platform.service.task.ActivityTaskExecuteContext;
import com.alibaba.chaosblade.platform.service.task.TimerFactory;
import com.alibaba.chaosblade.platform.service.task.stateless.phase.ActivityTaskPhaseHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

import static com.alibaba.chaosblade.platform.cmmon.exception.ExceptionMessageEnum.EXPERIMENT_TASK_NOT_FOUNT;

/**
 * @author yefei
 */
@Slf4j
@TaskLogRecord
public abstract class AbstractActivityTaskPhaseHandler implements ActivityTaskPhaseHandler {

    @Autowired
    protected ExperimentActivityTaskRecordRepository experimentActivityTaskRecordRepository;

    @Autowired
    protected ExperimentActivityTaskRepository experimentActivityTaskRepository;

    @Autowired
    protected ExperimentTaskRepository experimentTaskRepository;

    @Autowired
    protected TimerFactory timerFactory;

    @Override
    public boolean preHandle(ActivityTaskExecuteContext context, ActivityTask activityTask) {
        final ActivityTaskDTO activityTaskDTO = activityTask.activityTaskDTO();
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

                return true;
            } else {
                log.warn("子运行状态不可运行，任务ID: {}，阶段：{}, 子任务ID：{} ",
                        activityTaskDTO.getExperimentTaskId(),
                        activityTaskDTO.getPhase(),
                        activityTaskDTO.getActivityTaskId()
                );
            }
        } else {
            log.warn("当前任务状态不可运行，任务ID: {}，阶段：{}, 状态：{} ",
                    activityTaskDTO.getExperimentTaskId(),
                    activityTaskDTO.getPhase(),
                    runStatus.name());
        }
        return false;
    }

    @Override
    public void postHandle(ActivityTaskExecuteContext context, ActivityTask activityTask, Throwable e) {
        final ActivityTaskDTO activityTaskDTO = activityTask.activityTaskDTO();
        final CompletableFuture<Void> future = activityTask.future();

        List<ExperimentActivityTaskRecordDO> records = experimentActivityTaskRecordRepository.selectExperimentTaskId(activityTaskDTO.getExperimentTaskId());
        long count = records.stream().filter(r ->
                r.getPhase().equals(ChaosConstant.PHASE_ATTACK)
                        && Optional.ofNullable(r.getSuccess()).orElse(false)).count();

        // update activity task
        experimentActivityTaskRepository.updateByPrimaryKey(activityTaskDTO.getActivityTaskId(),
                ExperimentActivityTaskDO.builder()
                        .runStatus(RunStatus.FINISHED.getValue())
                        .resultStatus(count > 0 ? ResultStatus.SUCCESS.getValue() : ResultStatus.FAILED.getValue())
                        .errorMessage(e != null ? e.getMessage() : StrUtil.EMPTY)
                        .gmtEnd(DateUtil.date())
                        .build());

        if (e == null) {
            log.info("子任务运行完成，任务ID: {}，阶段：{}, 子任务ID：{} ",
                    activityTaskDTO.getExperimentTaskId(),
                    activityTaskDTO.getPhase(),
                    activityTaskDTO.getActivityTaskId());
        } else {
            log.error("子任务运行失败，任务ID: {}，阶段：{}, 子任务ID: {}, 失败原因: {} ",
                    activityTaskDTO.getExperimentTaskId(),
                    activityTaskDTO.getPhase(),
                    activityTaskDTO.getActivityTaskId(),
                    e.getMessage());
        }

        Long waitOfAfter = activityTaskDTO.getWaitOfAfter();
        if (waitOfAfter != null) {
            log.info("演练阶段完成后等待通知, 任务ID：{}, 子任务ID: {}, 等待时间：{} 毫秒",
                    activityTaskDTO.getExperimentTaskId(),
                    activityTaskDTO.getActivityTaskId(),
                    waitOfAfter);

            timerFactory.getTimer().newTimeout(timeout ->
                    {
                        if (e != null) {
                            future.completeExceptionally(e);
                        } else {
                            future.complete(null);
                        }
                    },
                    waitOfAfter,
                    TimeUnit.MILLISECONDS);
        } else {
            if (e != null) {
                future.completeExceptionally(e);
            } else {
                future.complete(null);
            }
        }
    }

}
