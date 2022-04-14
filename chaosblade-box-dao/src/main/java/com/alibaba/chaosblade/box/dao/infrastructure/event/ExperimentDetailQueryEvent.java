package com.alibaba.chaosblade.box.dao.infrastructure.event;

import com.alibaba.chaosblade.box.common.common.domain.user.ChaosUser;

/**
 * @author haibin
 *
 *
 */
public class ExperimentDetailQueryEvent extends BaseExperimentEvent {
    /**
     * Constructs a prototypical Event.
     *
     * @param user
     * @param experimentId
     * @throws IllegalArgumentException if source is null.
     */
    public ExperimentDetailQueryEvent(ChaosUser user, String experimentId) {
        super(user, experimentId);
    }
}
