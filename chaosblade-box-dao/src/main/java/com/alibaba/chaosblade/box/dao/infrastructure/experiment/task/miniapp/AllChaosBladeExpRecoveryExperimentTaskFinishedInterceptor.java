package com.alibaba.chaosblade.box.dao.infrastructure.experiment.task.miniapp;

import com.alibaba.chaosblade.box.dao.infrastructure.app.chaosblade.ChaosBladeExpDirectlyRecovery;
import com.alibaba.chaosblade.box.dao.infrastructure.experiment.task.ExperimentTaskFinishedInterceptor;
import com.alibaba.chaosblade.box.dao.model.ExperimentTaskDO;
import com.alibaba.chaosblade.box.dao.repository.ChaosBladeExpUidRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author haibin.lhb
 *
 *
 */
@Component
public class AllChaosBladeExpRecoveryExperimentTaskFinishedInterceptor implements ExperimentTaskFinishedInterceptor {

    @Autowired
    private ChaosBladeExpUidRepository chaosBladeExpUidRepository;

    @Autowired
    private ChaosBladeExpDirectlyRecovery chaosBladeExpDirectlyRecovery;

    @Override
    public void beforeTurnToFinishedState(ExperimentTaskDO experimentTaskDO) {
        chaosBladeExpUidRepository.findByExperimentTaskIdAndNotExpired(experimentTaskDO.getTaskId()).forEach(
            chaosBladeExpUidDO -> chaosBladeExpDirectlyRecovery.recoveryByExpRecord(chaosBladeExpUidDO));
    }
}
