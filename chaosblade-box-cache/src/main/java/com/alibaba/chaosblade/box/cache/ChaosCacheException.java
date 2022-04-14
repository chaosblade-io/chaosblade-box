package com.alibaba.chaosblade.box.cache;

/**
 * Author: sunju
 *
 * Date:   2019/11/7
 */
public class ChaosCacheException extends RuntimeException {

    public ChaosCacheException(String message) {
        super(message);
    }

    public ChaosCacheException(Throwable throwable) {
        super(throwable);
    }

    public ChaosCacheException(String message, Throwable throwable) {
        super(message, throwable);
    }

}
