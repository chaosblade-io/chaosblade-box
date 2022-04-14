package com.alibaba.chaosblade.box.common.experiment.task.flow.util.concurrent;

/**
 * @author haibin
 *
 *
 */
public abstract class AbstractRunnable implements Runnable {
    /**
     * Should the runnable force its execution in case it gets rejected?
     */
    public boolean isForceExecution() {
        return false;
    }

    @Override
    public final void run() {
        try {
            doRun();
        } catch (Exception t) {
            onFailure(t);
        } finally {
            onAfter();
        }
    }

    /**
     * This method is called in a finally block check success execution
     * or on a rejection.
     */
    public void onAfter() {
        // nothing by default
    }

    /**
     * This method is invoked for all exception thrown by {@link #doRun()}
     */
    public abstract void onFailure(Throwable e);

    /**
     * This should be executed if the thread-pool executing this action rejected the execution.
     * The default implementation forwards to {@link #onFailure(Exception)}
     */
    public void onRejection(Throwable e) {
        onFailure(e);
    }

    /**
     * This method has the same semantics as {@link Runnable#run()}
     *
     * @throws InterruptedException if the run method throws an InterruptedException
     */
    protected abstract void doRun() throws Exception;

}
