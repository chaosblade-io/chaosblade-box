package com.alibaba.chaosblade.box.dao.infrastructure.event;

import com.alibaba.chaosblade.box.common.common.domain.user.ChaosUser;
import com.alibaba.chaosblade.box.dao.model.ExperimentTaskDO;
import lombok.Getter;

/**
 * @author haibin
 *
 *
 */
@Getter
public class ExperimentTaskFinishedEvent extends BaseExperimentEvent {

    private ExperimentTaskDO experimentTaskDO;

    /**
     * Constructs a prototypical Event.
     *
     * @param user
     * @param experimentTaskDO
     *
     * @throws IllegalArgumentException if source is null.
     */
    public ExperimentTaskFinishedEvent(ChaosUser user, ExperimentTaskDO experimentTaskDO) {
        super(user, experimentTaskDO.getExperimentId());
        this.experimentTaskDO = experimentTaskDO;
    }

}
