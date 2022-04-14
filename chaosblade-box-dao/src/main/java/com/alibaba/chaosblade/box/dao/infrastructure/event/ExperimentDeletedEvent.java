package com.alibaba.chaosblade.box.dao.infrastructure.event;

import com.alibaba.chaosblade.box.common.common.domain.user.ChaosUser;
import lombok.Getter;

/**
 * @author haibin
 * 
 *
 */
@Getter
public class ExperimentDeletedEvent extends BaseExperimentEvent {

    /**
     * Constructs a prototypical Event.
     *
     * @param user
     * @param experimentId
     * @throws IllegalArgumentException if source is null.
     */
    public ExperimentDeletedEvent(ChaosUser user, String experimentId) {
        super(user, experimentId);
    }
}
