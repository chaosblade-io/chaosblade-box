package com.alibaba.chaosblade.box.dao.infrastructure.event;

import com.alibaba.chaosblade.box.dao.model.ExperimentMiniAppTaskDO;
import lombok.Getter;

/**
 * @author haibin
 *
 *
 */
@Getter
public class ExperimentActivityTargetTaskBeforeCreateEvent extends BaseChaosEvent {

    protected ExperimentMiniAppTaskDO experimentMiniAppTaskDO;

    /**
     * Constructs a prototypical Event.
     *
     * @param source The object on which the Event initially occurred.
     * @throws IllegalArgumentException if source is null.
     */
    public ExperimentActivityTargetTaskBeforeCreateEvent(ExperimentMiniAppTaskDO experimentMiniAppTaskDO) {
        this.experimentMiniAppTaskDO = experimentMiniAppTaskDO;

    }
}
