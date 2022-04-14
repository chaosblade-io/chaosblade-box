package com.alibaba.chaosblade.box.dao.infrastructure.event.listener;

import com.alibaba.chaosblade.box.common.common.domain.experiment.ExperimentSchedulerConfig;
import com.alibaba.chaosblade.box.common.infrastructure.constant.CloudConstant;
import com.alibaba.chaosblade.box.dao.infrastructure.event.BaseChaosEvent;
import com.alibaba.chaosblade.box.dao.infrastructure.event.BaseChaosEventListener;
import com.alibaba.chaosblade.box.dao.infrastructure.event.ExperimentCreatedEvent;
import com.alibaba.chaosblade.box.dao.model.ExperimentDO;
import com.alibaba.chaosblade.box.dao.scheduler.SchedulerJobService;
import com.alibaba.chaosblade.box.dao.scheduler.domain.SchedulerJobCreateRequest;
import com.alibaba.chaosblade.box.dao.scheduler.job.ExperimentSchedulerRunnableJob;
import com.google.common.base.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.support.CronSequenceGenerator;
import org.springframework.stereotype.Component;

/**
 * @author haibin
 *
 *
 */
@Component
public class SchedulerExperimentCreateEventListener extends BaseChaosEventListener {

    @Autowired
    private SchedulerJobService schedulerJobService;

    @Override
    public boolean support(BaseChaosEvent event) {
        return event instanceof ExperimentCreatedEvent;
    }

    @Override
    public void onChaosEvent(BaseChaosEvent event) {
        ExperimentCreatedEvent experimentCreatedEvent = (ExperimentCreatedEvent)event;
        ExperimentDO experimentDO = experimentCreatedEvent.getExperimentDO();
        if (experimentDO.getSchedulerConfig() != null && !Strings.isNullOrEmpty(
            experimentDO.getSchedulerConfig().getCronExpression())) {
            checkCronExpression(experimentDO.getSchedulerConfig());
            ExperimentSchedulerConfig experimentSchedulerConfig = experimentDO.getSchedulerConfig();
            SchedulerJobCreateRequest schedulerJobCreateRequest = new SchedulerJobCreateRequest(
                experimentSchedulerConfig.getCronExpression(), 0, experimentDO.getName(),
                CloudConstant.SCHEDULER_BUSINESS_TYPE_EXPERIMENT_SCHEDULER_RUN, experimentDO.getExperimentId(),
                ExperimentSchedulerRunnableJob.class.getName());
            schedulerJobCreateRequest.setUserId(experimentDO.getUserId());
            schedulerJobCreateRequest.setStartTime(experimentSchedulerConfig.getFixedTime());
            schedulerJobService.addSchedulerJob(schedulerJobCreateRequest);
        }
    }

    private void checkCronExpression(ExperimentSchedulerConfig schedulerConfig) {
        if (schedulerConfig.getCronExpression() != null) {
            CronSequenceGenerator.isValidExpression(schedulerConfig.getCronExpression());
        }
    }

}