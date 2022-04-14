package com.alibaba.chaosblade.box.dao.infrastructure.event;


import com.alibaba.chaosblade.box.common.common.domain.user.ChaosUser;

/**
 * @author haibin
 *
 *
 */
public class ExperimentTaskCreateEvent extends BaseExperimentTaskEvent {

    public ExperimentTaskCreateEvent(ChaosUser user, String experimentTaskId) {
        super(user, experimentTaskId);
    }

}
