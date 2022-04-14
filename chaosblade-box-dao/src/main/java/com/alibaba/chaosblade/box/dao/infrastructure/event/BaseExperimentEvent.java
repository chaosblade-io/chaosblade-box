package com.alibaba.chaosblade.box.dao.infrastructure.event;

import com.alibaba.chaosblade.box.common.common.domain.user.ChaosUser;
import lombok.Getter;

/**
 * @author haibin
 *
 * 
 */

@Getter
public class BaseExperimentEvent extends BaseChaosEvent {

    private ChaosUser user;

    private String experimentId;

    /**
     * Constructs a prototypical Event.
     *
     * @throws IllegalArgumentException if source is null.
     */
    public BaseExperimentEvent(ChaosUser user, String experimentId) {
        this.user = user;
        this.experimentId = experimentId;
    }

}
