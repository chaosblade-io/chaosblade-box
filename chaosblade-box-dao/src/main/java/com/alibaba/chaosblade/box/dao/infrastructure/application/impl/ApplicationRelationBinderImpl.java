package com.alibaba.chaosblade.box.dao.infrastructure.application.impl;

import com.alibaba.chaosblade.box.common.common.enums.StateEnum;
import com.alibaba.chaosblade.box.common.experiment.model.AppNameAndIdPair;
import com.alibaba.chaosblade.box.common.infrastructure.util.CollectionUtil;
import com.alibaba.chaosblade.box.dao.model.ApplicationRelationDO;
import com.alibaba.chaosblade.box.dao.repository.ApplicationRelationRepository;
import com.alibaba.chaosblade.box.dao.infrastructure.application.ApplicationRelationBinder;
//import com.alibaba.chaosblade.box.service.model.application.AppNameAndIdPair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Set;

/**
 * @author haibin
 *
 *
 */
@Component
public class ApplicationRelationBinderImpl implements ApplicationRelationBinder {

    @Autowired
    private ApplicationRelationRepository applicationRelationRepository;

    @Override
    public void addOrUpdateApplicationExperimentRelations(String experimentId,
        Set<AppNameAndIdPair> appNameAndIdPairs) {
        applicationRelationRepository.deleteRelationByOuterId(experimentId, ApplicationRelationDO.RELATION_EXPERIMENT);
        if (!appNameAndIdPairs.isEmpty()) {
            appNameAndIdPairs.forEach(appNameAndIdPair -> {
                ApplicationRelationDO applicationRelationDO = new ApplicationRelationDO();
                applicationRelationDO.setAppId(Long.valueOf(appNameAndIdPair.getAppId()));
                applicationRelationDO.setOuterId(experimentId);
                applicationRelationDO.setRelationType(ApplicationRelationDO.RELATION_EXPERIMENT);
                applicationRelationRepository.add(applicationRelationDO);
            });
        }
    }

    @Override
    public void addApplicationExperimentTaskRelations(String experimentTaskId,
        Set<AppNameAndIdPair> appNameAndIdPairs) {
        if (CollectionUtil.isNullOrEmpty(appNameAndIdPairs)) { return; }
        appNameAndIdPairs.forEach(appNameAndIdPair -> {
            ApplicationRelationDO applicationRelationDO = new ApplicationRelationDO();
            applicationRelationDO.setAppId(Long.valueOf(appNameAndIdPair.getAppId()));
            applicationRelationDO.setOuterId(experimentTaskId);
            applicationRelationDO.setTaskState(StateEnum.RUNNING.getValue());
            applicationRelationDO.setRelationType(ApplicationRelationDO.RELATION_EXPERIMENT_TASK);
            applicationRelationRepository.add(applicationRelationDO);
        });
    }

}
