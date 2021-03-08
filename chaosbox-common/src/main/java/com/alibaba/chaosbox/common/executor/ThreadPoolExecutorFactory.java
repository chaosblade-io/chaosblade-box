package com.alibaba.chaosbox.common.executor;

import java.util.concurrent.*;

/**
 * @author yefei
 */
public class ThreadPoolExecutorFactory implements ExecutorFactory {

    private static final int AVAILABLE_PROCESSORS = Runtime.getRuntime().availableProcessors();

    @Override
    public ExecutorService createExecutorService(ThreadFactory threadFactory) {
        return new ThreadPoolExecutor(
                AVAILABLE_PROCESSORS << 1,
                512,
                120L,
                TimeUnit.SECONDS,
                new LinkedBlockingQueue(32768),
                threadFactory
        );
    }
}
