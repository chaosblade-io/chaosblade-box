package com.alibaba.chaosblade.box.dao.infrastructure.experiment.task;


import com.alibaba.chaosblade.box.dao.model.ExperimentTaskDO;

/**
 * @author haibin.lhb
 *
 * 
 */
public interface ExperimentTaskFinishedInterceptor {

    /**
     * 在变成结束状态之前
     *
     * @param experimentTaskDO
     */
    public void beforeTurnToFinishedState(ExperimentTaskDO experimentTaskDO);
}
