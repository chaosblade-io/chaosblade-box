package com.alibaba.chaosblade.box.dao.infrastructure.event;

/**
 * @author haibin
 *
 *
 */
public abstract class BaseChaosEvent {

    private final long timestamp;

    /**
     * Constructs a prototypical Event.
     *
     * @throws IllegalArgumentException if source is null.
     */
    public BaseChaosEvent() {
        this.timestamp = System.currentTimeMillis();
    }

    public final long getTimestamp() {
        return this.timestamp;
    }

}
