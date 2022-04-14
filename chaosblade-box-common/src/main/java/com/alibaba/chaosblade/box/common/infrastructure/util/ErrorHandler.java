package com.alibaba.chaosblade.box.common.infrastructure.util;

/**
 * @author haibin
 *
 *
 */
public interface ErrorHandler<E extends Throwable,R> {


    public R handle(E ex);

}
