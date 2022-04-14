package com.alibaba.chaosblade.box.common.commands;

import lombok.Data;

/**
 * @author haibin
 *
 *
 */
@Data
public class CommandExecutorInfo {

    private String name;
    private long completedTask;
    private int queueSize;
    private int activeCount;
    private int hangCount;
    private int blockingCount;

    private int taskQueueFinishedCount;

    public CommandExecutorInfo(String name) {
        this.name = name;
    }

    private int maxPoolSize;

    private int corePoolSize;

    public void setCompletedTask(long completedTask) {
        this.completedTask = completedTask;
    }

    public long getCompletedTask() {
        return completedTask;
    }

    public void setQueueSize(int queueSize) {

        this.queueSize = queueSize;
    }

    public int getQueueSize() {
        return queueSize;
    }

    public void setActiveCount(int activeCount) {
        this.activeCount = activeCount;
    }

    public int getActiveCount() {
        return activeCount;
    }
}
