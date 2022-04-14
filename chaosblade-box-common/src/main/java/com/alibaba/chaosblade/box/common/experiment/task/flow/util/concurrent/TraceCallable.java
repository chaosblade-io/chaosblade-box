package com.alibaba.chaosblade.box.common.experiment.task.flow.util.concurrent;

import lombok.Getter;

import java.util.Map;
import java.util.concurrent.Callable;

/**
 * @author haibin
 *
 *
 */
@Getter
public class TraceCallable<V> implements Callable<V> {

    public InvokeTracer invokeTracer;

    private Callable<V> callable;

    private Map<String, String> context;

    public TraceCallable(InvokeTracer invokeTracer, Callable<V> callable) {
        this.invokeTracer = invokeTracer;
        this.callable = callable;
        this.context = invokeTracer.getCopy();
    }

    @Override
    public V call() throws Exception {
        invokeTracer.putContext(this.context);
        return this.callable.call();
    }

}
