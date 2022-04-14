package com.alibaba.chaosblade.box.common.infrastructure.util.concurrent;

import java.util.List;
import java.util.concurrent.Callable;

/**
 * Created by haibin on 16/7/19.
 */
public interface ExecutorAction<T> {

    public List<Callable<T>> getCallables();


}
