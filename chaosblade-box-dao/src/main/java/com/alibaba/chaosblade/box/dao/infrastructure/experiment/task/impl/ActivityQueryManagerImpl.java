package com.alibaba.chaosblade.box.dao.infrastructure.experiment.task.impl;

import com.alibaba.chaosblade.box.common.app.sdk.constants.PhaseType;
import com.alibaba.chaosblade.box.dao.infrastructure.experiment.task.ActivityQueryManager;
import com.alibaba.chaosblade.box.dao.model.ExperimentActivityDO;
import com.alibaba.chaosblade.box.dao.repository.ActivityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

/**
 * @author haibin
 *
 * 
 */
@Component
public class ActivityQueryManagerImpl implements ActivityQueryManager {

    @Autowired
    private ActivityRepository activityRepository;

    @Override
    public List<ExperimentActivityDO> findActivitiesByExperimentId(String experimentId) {
        return activityRepository.findActivities(
            experimentId);
    }

    @Override
    public Optional<ExperimentActivityDO> findActivity(String activityId) {
        return activityRepository.findById(activityId);
    }

    @Override
    public List<ExperimentActivityDO> findActivitiesByPhase(String experimentId, PhaseType phaseType) {
        return activityRepository.findByExperimentIdAndPhase(
            experimentId, phaseType);
    }

}
