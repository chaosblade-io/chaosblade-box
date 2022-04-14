package com.alibaba.chaosblade.box.common.experiment.task.flow.util.concurrent;

/**
 * @author haibin
 *
 *
 */
public interface RunnableDecorator {

    public Runnable decorate(Runnable runnable);
}
