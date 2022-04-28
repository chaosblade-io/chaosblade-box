package com.alibaba.chaosblade.box.dao.infrastructure.event.listener;

import com.alibaba.chaosblade.box.common.app.sdk.constants.PhaseType;
import com.alibaba.chaosblade.box.common.common.util.MiniAppUtils;
import com.alibaba.chaosblade.box.common.infrastructure.domain.experiment.SceneArgumentDefinition;
import com.alibaba.chaosblade.box.common.infrastructure.domain.experiment.flow.RecoveryStrategyFieldArgumentDefinition;
import com.alibaba.chaosblade.box.common.infrastructure.domain.experiment.flow.RecoveryStrategyToleranceArgumentDefinition;
import com.alibaba.chaosblade.box.common.infrastructure.domain.experiment.guard.ExperimentGuardArgument;
import com.alibaba.chaosblade.box.common.infrastructure.domain.experiment.guard.GuardRunState;
import com.alibaba.chaosblade.box.common.infrastructure.monitor.metric.miniapp.MiniAppMetricGuardInstanceFilter;
import com.alibaba.chaosblade.box.dao.infrastructure.event.BaseChaosEvent;
import com.alibaba.chaosblade.box.dao.infrastructure.event.BaseChaosEventListener;
import com.alibaba.chaosblade.box.dao.infrastructure.event.ExperimentTaskStartedEvent;
import com.alibaba.chaosblade.box.dao.model.ActivityTaskDO;
import com.alibaba.chaosblade.box.dao.model.ExperimentGuardDO;
import com.alibaba.chaosblade.box.dao.model.ExperimentGuardInstanceDO;
import com.alibaba.chaosblade.box.dao.model.ExperimentTaskDO;
import com.alibaba.chaosblade.box.dao.repository.ActivityTaskRepository;
import com.alibaba.chaosblade.box.dao.repository.ExperimentGuardInstanceRepository;
import com.alibaba.chaosblade.box.dao.repository.ExperimentGuardRepository;
import com.alibaba.chaosblade.box.dao.scheduler.SchedulerConstant;
import com.alibaba.chaosblade.box.dao.scheduler.SchedulerJobService;
import com.alibaba.chaosblade.box.dao.scheduler.domain.SchedulerJobCreateRequest;
import com.alibaba.chaosblade.box.dao.scheduler.job.ExperimentGuardInstanceSchedulerJob;
import com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * 演练开始时候需要创建全局任务
 *
 * @author haibin
 *
 *
 */
@Component
public class ExperimentTaskGuardInstanceCreatedListener extends BaseChaosEventListener {

    @Autowired
    private ExperimentGuardRepository experimentGuardRepository;

    @Autowired
    private SchedulerJobService schedulerJobService;

    @Autowired
    private ExperimentGuardInstanceRepository experimentGuardInstanceRepository;

    @Autowired
    private ActivityTaskRepository activityTaskRepository;

    @Autowired
    private MiniAppMetricGuardInstanceFilter miniAppMetricGuardInstanceFilter;

    /**
     * 每隔15秒钟执行一次吧
     */
    private static String CronExpression = "0/15 * * * * ?";

    @Override
    public boolean support(BaseChaosEvent event) {
        return event instanceof ExperimentTaskStartedEvent;
    }

    /**
     * 演练任务开始时候如果有守护策略，就需要创建一个定时任务
     *
     * @param event
     */
    @Override
    public void onChaosEvent(BaseChaosEvent event) {
        ExperimentTaskStartedEvent experimentTaskStartedEvent = (ExperimentTaskStartedEvent)event;
        List<ExperimentGuardDO> experimentGuardDOS = experimentGuardRepository.findByExperimentId(
            experimentTaskStartedEvent.getExperimentId());
        boolean created = addDurationGuardInstance(experimentTaskStartedEvent.getExperimentTaskDO());
        batchCreateExperimentGuardInstances(experimentTaskStartedEvent, experimentGuardDOS);
        addJvmHitCountGuardInstance(experimentTaskStartedEvent.getExperimentTaskDO());
        if (created || !experimentGuardDOS.isEmpty()) {
            String name = "experiment_task_guard_task_" + experimentTaskStartedEvent.getExperimentTaskId();
            SchedulerJobCreateRequest schedulerJobCreateRequest = new SchedulerJobCreateRequest(CronExpression, 0, name,
                SchedulerConstant.BUSINESS_TYPE_EXPERIMENT_TASK_GUARD, experimentTaskStartedEvent.getExperimentTaskId(),
                ExperimentGuardInstanceSchedulerJob.class.getName());
            schedulerJobService.addSchedulerJob(schedulerJobCreateRequest);
        }
    }

    /**
     * 首先创建演练守护的实例
     *
     * @param experimentTaskStartedEvent
     * @param experimentGuardDOS
     */
    private void batchCreateExperimentGuardInstances(ExperimentTaskStartedEvent experimentTaskStartedEvent,
        List<ExperimentGuardDO> experimentGuardDOS) {
        for (ExperimentGuardDO experimentGuardDO : experimentGuardDOS) {
            ExperimentGuardInstanceDO experimentGuardInstanceDO = new ExperimentGuardInstanceDO();
            experimentGuardInstanceDO.setState(GuardRunState.RUNNING);
            experimentGuardInstanceDO.setAppCode(experimentGuardDO.getAppCode());
            experimentGuardInstanceDO.setExperimentTaskId(experimentTaskStartedEvent.getExperimentTaskId());
            experimentGuardInstanceDO.setGuardId(experimentGuardDO.getGuardId());
            experimentGuardInstanceDO.setName(experimentGuardDO.getName());
            experimentGuardInstanceDO.setActionType(experimentGuardDO.getActionType());
            experimentGuardInstanceDO.setArgument(experimentGuardDO.getArgument());
            experimentGuardInstanceRepository.add(experimentGuardInstanceDO);
        }
    }

    private boolean addDurationGuardInstance(ExperimentTaskDO experimentTaskDO) {
        if (experimentTaskDO.getDuration() > 0) {
            ExperimentGuardInstanceDO experimentGuardInstanceDO = new ExperimentGuardInstanceDO();
            experimentGuardInstanceDO.setState(GuardRunState.RUNNING);
            experimentGuardInstanceDO.setExperimentTaskId(experimentTaskDO.getTaskId());
            experimentGuardInstanceDO.setGuardId("-1");
            experimentGuardInstanceDO.setAppCode("chaosapp.timeout.recovery");
            experimentGuardInstanceDO.setName("Timeout-Recovery");
            experimentGuardInstanceDO.setActionType(ExperimentGuardDO.ACTION_TYPE_TIMEOUT_RECOVERY);
            ExperimentGuardArgument experimentGuardArgument = new ExperimentGuardArgument();
            RecoveryStrategyToleranceArgumentDefinition recoveryStrategyToleranceArgumentDefinition
                = new RecoveryStrategyToleranceArgumentDefinition();
            recoveryStrategyToleranceArgumentDefinition.setName("Auto-Recovery-Time");
            String timeDesc = convertTime(experimentTaskDO.getDuration());
            recoveryStrategyToleranceArgumentDefinition.setValue(timeDesc);
            RecoveryStrategyFieldArgumentDefinition recoveryStrategyFieldArgumentDefinition
                = new RecoveryStrategyFieldArgumentDefinition();
            recoveryStrategyFieldArgumentDefinition.setName("Auto-Recovery-Time");
            recoveryStrategyFieldArgumentDefinition.setValue(timeDesc);
            experimentGuardArgument.setFields(Lists.newArrayList(recoveryStrategyFieldArgumentDefinition));
            experimentGuardArgument.setTolerance(Lists.newArrayList(recoveryStrategyToleranceArgumentDefinition));
            experimentGuardInstanceDO.setArgument(experimentGuardArgument);
            experimentGuardInstanceDO.setExperimentTaskId(experimentTaskDO.getTaskId());
            experimentGuardInstanceRepository.add(experimentGuardInstanceDO);
            return true;
        }
        return false;
    }

    private void addJvmHitCountGuardInstance(ExperimentTaskDO experimentTaskDO) {
        List<ActivityTaskDO> activityTaskDOS = activityTaskRepository.findByExperimentTaskIdAndPhase(
            experimentTaskDO.getTaskId(),
            PhaseType.ATTACK);
        activityTaskDOS.stream().filter(
            activityTaskDO -> miniAppMetricGuardInstanceFilter.support(activityTaskDO.getExecutableAppCode())).forEach(
            activityTaskDO -> {
                ExperimentGuardInstanceDO experimentGuardInstanceDO = new ExperimentGuardInstanceDO();
                experimentGuardInstanceDO.setState(GuardRunState.RUNNING);
                experimentGuardInstanceDO.setExperimentTaskId(experimentTaskDO.getTaskId());
                experimentGuardInstanceDO.setGuardId("-1");
                experimentGuardInstanceDO.setAppCode(MiniAppUtils.JVM_HIT_COUNT);
                experimentGuardInstanceDO.setActivityTaskId(activityTaskDO.getTaskId());
                experimentGuardInstanceDO.setName("[Affect the number of requests]" + activityTaskDO.getActivityName());
                experimentGuardInstanceDO.setActionType(ExperimentGuardDO.ACTION_TYPE_MONITOR);
                ExperimentGuardArgument experimentGuardArgument = new ExperimentGuardArgument();
                SceneArgumentDefinition sceneArgumentDefinition = new SceneArgumentDefinition();
                sceneArgumentDefinition.setAlias("metric");
                sceneArgumentDefinition.setValue("scene.hit.count");
                experimentGuardArgument.setArguments(Lists.newArrayList(sceneArgumentDefinition));
                experimentGuardInstanceDO.setArgument(experimentGuardArgument);
                experimentGuardInstanceDO.setExperimentTaskId(experimentTaskDO.getTaskId());
                experimentGuardInstanceRepository.add(experimentGuardInstanceDO);
            });
    }

    private String convertTime(Long duration) {
        if (duration <= 60 * 3) {
            return duration + "sec";
        } else {
            return (TimeUnit.SECONDS.toMinutes(duration)) + "min";
        }
    }

}
