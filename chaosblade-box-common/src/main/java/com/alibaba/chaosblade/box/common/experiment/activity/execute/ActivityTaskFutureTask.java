package com.alibaba.chaosblade.box.common.experiment.activity.execute;

import com.alibaba.chaosblade.box.common.infrastructure.ChaosApplicationContext;
import com.alibaba.chaosblade.box.common.infrastructure.ChaosRequestContextHolder;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.Callable;
import java.util.concurrent.FutureTask;

/**
 *
 * @author haibin.lhb
 * 
 *
 */
public class ActivityTaskFutureTask<T> extends FutureTask<T> {

    final Object rpcContext = null;

    final ChaosApplicationContext chaosApplicationContext;

    final ActivityTaskCallable<T> activityTaskCallable;

    public void setRunnableThread(Thread runnableThread) {
        this.runnableThread = runnableThread;
    }

    private Thread runnableThread;

    public ActivityTaskFutureTask(@NotNull Callable<T> callable) {
        super(callable);
//        rpcContext = EagleEye.currentRpcContext();
        activityTaskCallable = (ActivityTaskCallable<T>) callable;
        chaosApplicationContext = ChaosRequestContextHolder.getApplicationContext().orElse(null);
    }

    public String getId() {
        return activityTaskCallable.getId();
    }

    @Override
    public void run() {
//        EagleEye.setRpcContext(rpcContext);
        ChaosRequestContextHolder.setApplicationContext(chaosApplicationContext);
        try {
            super.run();
        } finally {
//            EagleEye.clearRpcContext();
            ChaosRequestContextHolder.resetApplicationContextContext();
        }
    }

    @Override
    public boolean cancel(boolean mayInterruptIfRunning) {
        if (runnableThread != null && runnableThread == Thread.currentThread()) {
            this.runnableThread.interrupt();
        }
        return super.cancel(mayInterruptIfRunning);
    }
}
