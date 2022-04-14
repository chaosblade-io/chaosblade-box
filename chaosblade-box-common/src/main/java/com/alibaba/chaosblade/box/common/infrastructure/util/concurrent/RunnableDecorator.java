package com.alibaba.chaosblade.box.common.infrastructure.util.concurrent;

/**
 * @author haibin
 *
 *
 */
public interface RunnableDecorator {

    public Runnable decorate(Runnable runnable);
}
