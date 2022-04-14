package com.alibaba.chaosblade.box.dao.infrastructure.event;

import lombok.Getter;

/**
 * @author haibin
 * 
 *
 */
public class ExperimentActivityTargetTaskAfterCreateEvent extends BaseChaosEvent {

    @Getter
    protected String executionId;

    /**
     * Constructs a prototypical Event.
     *
     * @param source The object on which the Event initially occurred.
     * @throws IllegalArgumentException if source is null.
     */
    public ExperimentActivityTargetTaskAfterCreateEvent(String executionId) {
        this.executionId = executionId;
    }
}
