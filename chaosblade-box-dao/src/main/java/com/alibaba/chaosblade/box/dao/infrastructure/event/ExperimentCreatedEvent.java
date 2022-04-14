package com.alibaba.chaosblade.box.dao.infrastructure.event;

import com.alibaba.chaosblade.box.common.common.domain.user.ChaosUser;
import com.alibaba.chaosblade.box.dao.model.ExperimentDO;
import lombok.Getter;

/**
 * @author haibin
 * 
 *
 */
@Getter
public class ExperimentCreatedEvent extends BaseExperimentEvent {

    private ExperimentDO experimentDO;

    /**
     * Constructs a prototypical Event.
     *
     * @param user
     * @param experimentDO
     *
     * @throws IllegalArgumentException if source is null.
     */
    public ExperimentCreatedEvent(ChaosUser user, ExperimentDO experimentDO) {
        super(user, experimentDO.getExperimentId());
        this.experimentDO = experimentDO;
    }

    @Override
    public String getExperimentId() {
        return this.experimentDO.getExperimentId();
    }

}
