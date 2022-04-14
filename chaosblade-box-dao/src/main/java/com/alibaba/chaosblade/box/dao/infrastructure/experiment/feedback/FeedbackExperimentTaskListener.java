package com.alibaba.chaosblade.box.dao.infrastructure.experiment.feedback;

import com.alibaba.chaosblade.box.common.common.domain.FeedBackConstant;
import com.alibaba.chaosblade.box.dao.infrastructure.event.BaseChaosEvent;
import com.alibaba.chaosblade.box.dao.infrastructure.event.BaseChaosEventListener;
import com.alibaba.chaosblade.box.dao.infrastructure.event.ExperimentTaskFinishedEvent;
import com.alibaba.chaosblade.box.dao.model.ExperimentTaskDO;
import com.alibaba.chaosblade.box.dao.repository.ExperimentTaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author haibin.lhb
 *
 * 
 */
@Component
public class FeedbackExperimentTaskListener extends BaseChaosEventListener {

    @Autowired
    private ExperimentTaskRepository experimentTaskRepository;

    @Override
    public boolean support(BaseChaosEvent event) {
        return event instanceof ExperimentTaskFinishedEvent;
    }

    @Override
    public void onChaosEvent(BaseChaosEvent event) {
        ExperimentTaskFinishedEvent experimentTaskFinishedEvent = (ExperimentTaskFinishedEvent)event;
        ExperimentTaskDO experimentTaskDO = experimentTaskFinishedEvent.getExperimentTaskDO();
        experimentTaskRepository.updateFeedbackStatus(experimentTaskDO.getTaskId(),
            FeedBackConstant.FeedbackStatus.FEEDBACK_STATUS_WAITING);
    }
}
