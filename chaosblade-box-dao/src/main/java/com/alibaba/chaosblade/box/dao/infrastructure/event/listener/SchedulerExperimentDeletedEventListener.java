package com.alibaba.chaosblade.box.dao.infrastructure.event.listener;

import com.alibaba.chaosblade.box.common.infrastructure.constant.CloudConstant;
import com.alibaba.chaosblade.box.dao.infrastructure.event.BaseChaosEvent;
import com.alibaba.chaosblade.box.dao.infrastructure.event.BaseChaosEventListener;
import com.alibaba.chaosblade.box.dao.infrastructure.event.ExperimentDeletedEvent;
import com.alibaba.chaosblade.box.dao.model.SchedulerJobDO;
import com.alibaba.chaosblade.box.dao.repository.SchedulerJobRepository;
import com.alibaba.chaosblade.box.dao.scheduler.SchedulerJobService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author haibin
 *
 *
 */
@Component
public class SchedulerExperimentDeletedEventListener extends BaseChaosEventListener {

    @Autowired
    private SchedulerJobRepository schedulerJobRepository;

    @Autowired
    private SchedulerJobService schedulerJobService;

    @Override
    public boolean support(BaseChaosEvent event) {
        return event instanceof ExperimentDeletedEvent;
    }

    @Override
    public void onChaosEvent(BaseChaosEvent event) {
        ExperimentDeletedEvent experimentDeletedEvent = (ExperimentDeletedEvent)event;
        String experimentId = experimentDeletedEvent.getExperimentId();
        SchedulerJobDO schedulerJobDO = schedulerJobRepository.findByBusinessIdAndBusinessType(
            CloudConstant.SCHEDULER_BUSINESS_TYPE_EXPERIMENT_SCHEDULER_RUN, experimentId);
        if (schedulerJobDO != null) {
            schedulerJobService.disableSchedulerJob(schedulerJobDO.getJobId());
        }
    }
}
