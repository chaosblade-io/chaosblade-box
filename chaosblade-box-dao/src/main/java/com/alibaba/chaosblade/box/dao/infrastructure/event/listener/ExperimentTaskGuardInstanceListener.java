package com.alibaba.chaosblade.box.dao.infrastructure.event.listener;

import com.alibaba.chaosblade.box.common.infrastructure.domain.experiment.guard.GuardRunState;
import com.alibaba.chaosblade.box.dao.infrastructure.event.BaseChaosEvent;
import com.alibaba.chaosblade.box.dao.infrastructure.event.BaseChaosEventListener;
import com.alibaba.chaosblade.box.dao.infrastructure.event.ExperimentTaskFinishedEvent;
import com.alibaba.chaosblade.box.dao.model.ExperimentGuardDO;
import com.alibaba.chaosblade.box.dao.model.ExperimentGuardInstanceDO;
import com.alibaba.chaosblade.box.dao.repository.ExperimentGuardInstanceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author haibin
 *
 *
 */
@Component
public class ExperimentTaskGuardInstanceListener extends BaseChaosEventListener {

    @Autowired
    private ExperimentGuardInstanceRepository experimentGuardInstanceRepository;

    @Override
    public boolean support(BaseChaosEvent event) {
        return event instanceof ExperimentTaskFinishedEvent;
    }

    @Override
    public void onChaosEvent(BaseChaosEvent event) {
        ExperimentTaskFinishedEvent experimentTaskFinishedEvent = (ExperimentTaskFinishedEvent)event;
        String experimentTaskId = experimentTaskFinishedEvent.getExperimentTaskDO().getTaskId();
        List<ExperimentGuardInstanceDO> experimentGuardInstanceDOS = experimentGuardInstanceRepository
            .findByExperimentTaskId(experimentTaskId);
        for (ExperimentGuardInstanceDO experimentGuardInstanceDO : experimentGuardInstanceDOS) {
            if (!experimentGuardInstanceDO.isHalted() && !ExperimentGuardDO.ACTION_TYPE_MONITOR.equals(
                experimentGuardInstanceDO.getActionType())) {
                experimentGuardInstanceDO.setState(GuardRunState.FINISHED);
                experimentGuardInstanceRepository.updateRunningExperimentGuardInstanceDO(experimentGuardInstanceDO);
            }
        }
    }
}
