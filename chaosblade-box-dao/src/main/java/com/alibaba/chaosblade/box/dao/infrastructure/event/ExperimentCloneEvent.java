package com.alibaba.chaosblade.box.dao.infrastructure.event;


import com.alibaba.chaosblade.box.common.common.domain.user.ChaosUser;

public class ExperimentCloneEvent extends BaseExperimentEvent {
    /**
     * Constructs a prototypical Event.
     *
     * @param user
     * @param experimentId
     * @throws IllegalArgumentException if source is null.
     */
    public ExperimentCloneEvent(ChaosUser user, String experimentId) {
        super(user, experimentId);
    }
}
