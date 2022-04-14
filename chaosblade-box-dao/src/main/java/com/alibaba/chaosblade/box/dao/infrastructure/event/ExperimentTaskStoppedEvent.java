package com.alibaba.chaosblade.box.dao.infrastructure.event;


import com.alibaba.chaosblade.box.common.common.domain.user.ChaosUser;

/**
 * @author haibin
 *
 * 
 */
public class ExperimentTaskStoppedEvent extends BaseExperimentTaskEvent {

    public ExperimentTaskStoppedEvent(ChaosUser user, String experimentTaskId) {
        super(user, experimentTaskId);
    }

}
