package com.alibaba.chaosblade.box.dao.infrastructure.application;


import com.alibaba.chaosblade.box.common.experiment.model.AppNameAndIdPair;

import java.util.Set;

/**
 * @author haibin
 *
 *
 */
public interface ApplicationRelationBinder {

    /**
     * 绑定应用和演练的关系
     */
    public void addOrUpdateApplicationExperimentRelations(String experimentId,
        Set<AppNameAndIdPair> appNameAndIdPairs);

    /**
     * 绑定应用和演练任务的关系
     *
     * @param experimentTaskId
     * @param appNameAndIdPairs
     */
    public void addApplicationExperimentTaskRelations(String experimentTaskId,
        Set<AppNameAndIdPair> appNameAndIdPairs);

}
