package com.alibaba.chaosblade.box.dao.infrastructure.event.listener;

import com.alibaba.chaosblade.box.common.common.domain.experiment.ExperimentSchedulerConfig;
import com.alibaba.chaosblade.box.common.infrastructure.constant.CloudConstant;
import com.alibaba.chaosblade.box.dao.infrastructure.event.BaseChaosEvent;
import com.alibaba.chaosblade.box.dao.infrastructure.event.BaseChaosEventListener;
import com.alibaba.chaosblade.box.dao.infrastructure.event.ExperimentUpdatedEvent;
import com.alibaba.chaosblade.box.dao.model.ExperimentDO;
import com.alibaba.chaosblade.box.dao.model.SchedulerJobDO;
import com.alibaba.chaosblade.box.dao.repository.SchedulerJobRepository;
import com.alibaba.chaosblade.box.dao.scheduler.SchedulerJobService;
import com.alibaba.chaosblade.box.dao.scheduler.domain.SchedulerJobCreateRequest;
import com.alibaba.chaosblade.box.dao.scheduler.domain.SchedulerJobUpdateRequest;
import com.alibaba.chaosblade.box.dao.scheduler.job.ExperimentSchedulerRunnableJob;
import com.google.common.base.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.support.CronSequenceGenerator;
import org.springframework.stereotype.Component;

import java.util.Objects;

/**
 * @author haibin
 *
 *
 */
@Component
public class SchedulerExperimentUpdateEventListener extends BaseChaosEventListener {

    @Autowired
    private SchedulerJobService schedulerJobService;

    @Autowired
    private SchedulerJobRepository schedulerJobRepository;

    @Override
    public boolean support(BaseChaosEvent event) {
        return event instanceof ExperimentUpdatedEvent;
    }

    @Override
    public void onChaosEvent(BaseChaosEvent event) {
        ExperimentUpdatedEvent experimentUpdatedEvent = (ExperimentUpdatedEvent)event;
        ExperimentDO experimentDO = experimentUpdatedEvent.getExperimentDO();
        if (experimentDO.getSchedulerConfig() != null) {
            ExperimentSchedulerConfig experimentSchedulerConfig = experimentDO.getSchedulerConfig();
            String cronExpression = experimentSchedulerConfig.getCronExpression();
            if (!Strings.isNullOrEmpty(cronExpression)) {
                CronSequenceGenerator.isValidExpression(experimentSchedulerConfig.getCronExpression());
            }
            String businessId = experimentDO.getExperimentId();
            //查找是否存在定时任务
            SchedulerJobDO schedulerJobDO = schedulerJobRepository.findByBusinessIdAndBusinessType(
                CloudConstant.SCHEDULER_BUSINESS_TYPE_EXPERIMENT_SCHEDULER_RUN, experimentDO.getExperimentId());
            if (schedulerJobDO != null) {
                //如果定时任务为空，说明要停止
                if (Strings.isNullOrEmpty(cronExpression)) {
                    schedulerJobService.disableSchedulerJob(schedulerJobDO.getJobId());
                } else {
                    //定时表达式是否变化
                    if (Objects.equals(schedulerJobDO.getCronExpression(), cronExpression)) {
                        return;
                    }
                    SchedulerJobUpdateRequest schedulerJobUpdateRequest = new SchedulerJobUpdateRequest(
                        CloudConstant.SCHEDULER_BUSINESS_TYPE_EXPERIMENT_SCHEDULER_RUN, businessId);
                    schedulerJobUpdateRequest.setCronExpression(cronExpression);
                    schedulerJobService.updateSchedulerJob(schedulerJobUpdateRequest);
                }
            } else {
                //创建定时任务
                if (Strings.isNullOrEmpty(cronExpression)) {
                    return;
                }
                //增加定时任务
                SchedulerJobCreateRequest schedulerJobCreateRequest = new SchedulerJobCreateRequest(
                    experimentSchedulerConfig.getCronExpression(), 0, experimentDO.getName(),
                    CloudConstant.SCHEDULER_BUSINESS_TYPE_EXPERIMENT_SCHEDULER_RUN, experimentDO.getExperimentId(),
                    ExperimentSchedulerRunnableJob.class.getName());
                schedulerJobCreateRequest.setStartTime(experimentSchedulerConfig.getFixedTime());
                schedulerJobCreateRequest.setUserId(experimentDO.getUserId());
                schedulerJobService.addSchedulerJob(schedulerJobCreateRequest);
            }
        } else {
            //如果不存在的话，就禁止
            SchedulerJobDO schedulerJobDO = schedulerJobRepository.findByBusinessIdAndBusinessType(
                CloudConstant.SCHEDULER_BUSINESS_TYPE_EXPERIMENT_SCHEDULER_RUN, experimentDO.getExperimentId());
            if (schedulerJobDO != null && schedulerJobDO.getEnabled()) {
                schedulerJobService.disableSchedulerJob(schedulerJobDO.getJobId());
            }
        }
    }
}
