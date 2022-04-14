package com.alibaba.chaosblade.box.common.experiment.task.flow.util.concurrent;

import lombok.Data;
import lombok.Getter;

import java.util.concurrent.ThreadFactory;

/**
 * @author haibin
 *
 *
 */

@Data
public class ThreadPoolConfig {

    /**
     * thread pool alias
     */
    @Getter
    private String threadName;

    /**
     * decorate thread runnable
     */

    private RunnableDecorator runnableDecorator;

    /**
     * thread pool taskQueue
     */
    private int queueCapacity = 256;

    private int coreSize = 1;

    private int maxThreadSize = 3;

    private ThreadFactory threadFactory;

    public ThreadPoolConfig(String threadName) {
        this.threadName = threadName;
    }
}
