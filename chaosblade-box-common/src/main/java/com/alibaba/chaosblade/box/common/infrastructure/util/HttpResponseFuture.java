package com.alibaba.chaosblade.box.common.infrastructure.util;

import lombok.Setter;

import java.util.function.Consumer;
import java.util.function.Function;

/**
 * @author haibin
 *
 *
 */
@Setter
public class HttpResponseFuture<T> {

    private T response;

    private Throwable throwable;

    public T get() {
        return response;
    }

    public <R> R apply(Function<T, R> function) {
        if (response != null) {
            return function.apply(response);
        }
        return null;
    }

    public HttpResponseFuture<T> onThrowable(Consumer<Throwable> throwableConsumer) {
        if (throwable != null) {
            throwableConsumer.accept(throwable);
        }
        return this;
    }

    public HttpResponseFuture<T> onResponse(Consumer<T> responseConsumer) {
        if (response != null) {
            responseConsumer.accept(response);
        }
        return this;
    }

}
