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
public class ExperimentTaskStartedEvent extends BaseExperimentEvent {

    private ExperimentTaskDO experimentTaskDO;

    /**
     * Constructs a prototypical Event.
     *
     * @param user
     * @param experimentTaskDO
     */
    public ExperimentTaskStartedEvent(ChaosUser user, ExperimentTaskDO experimentTaskDO) {
        super(user, experimentTaskDO.getExperimentId());
        this.experimentTaskDO = experimentTaskDO;
    }

    public String getExperimentTaskId() {
        return experimentTaskDO.getTaskId();
    }

}
