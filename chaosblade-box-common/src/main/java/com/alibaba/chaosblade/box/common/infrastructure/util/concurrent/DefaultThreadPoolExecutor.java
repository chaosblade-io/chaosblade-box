package com.alibaba.chaosblade.box.common.infrastructure.util.concurrent;

import java.util.concurrent.*;

/**
 * @author haibin
 * 
 * 
 */
public class DefaultThreadPoolExecutor extends ThreadPoolExecutor {

    private volatile ShutdownListener shutdownListener;

    private final Object monitor = new Object();

    private RunnableDecorator runnableDecorator;

    public DefaultThreadPoolExecutor(int corePoolSize, int maximumPoolSize, long keepAliveTime,
        TimeUnit unit,
        BlockingQueue<Runnable> workQueue) {
        super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue);
    }

    public DefaultThreadPoolExecutor(int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit,
        BlockingQueue<Runnable> workQueue, ThreadFactory threadFactory, RunnableDecorator runnableDecorator) {
        super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue, threadFactory);
        this.runnableDecorator = runnableDecorator;
    }

    public DefaultThreadPoolExecutor(int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit,
        BlockingQueue<Runnable> workQueue, RejectedExecutionHandler handler) {
        super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue, handler);
    }

    public DefaultThreadPoolExecutor(int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit,
        BlockingQueue<Runnable> workQueue, ThreadFactory threadFactory,
        RejectedExecutionHandler handler) {
        super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue, threadFactory, handler);
    }

    public void shutdown(ShutdownListener shutdownListener) {
        synchronized (monitor) {
            if (this.shutdownListener != null) {
                throw new IllegalStateException("Shutdown was already called on this thread pool");
            }
            if (isTerminated()) {
                shutdownListener.onTerminated();
            } else {
                this.shutdownListener = shutdownListener;
            }
        }
    }



    @Override
    protected synchronized void terminated() {
        super.terminated();
        synchronized (monitor) {
            if (shutdownListener != null) {
                try {
                    shutdownListener.onTerminated();
                } finally {
                    shutdownListener = null;
                }
            }
        }
    }

    public interface ShutdownListener {
        void onTerminated();
    }

    @Override
    public void execute(Runnable command) {
        try {
            if (this.runnableDecorator != null) {
                super.execute(runnableDecorator.decorate(command));
            } else {
                super.execute(command);
            }
        } catch (Throwable throwable) {
            if (command instanceof AbstractRunnable) {
                try {
                    ((AbstractRunnable)command).onRejection(throwable);
                } finally {
                    ((AbstractRunnable)command).onAfter();
                }
            } else {
                throw throwable;
            }
        }
    }
}
