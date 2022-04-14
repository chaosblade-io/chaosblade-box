package com.alibaba.chaosblade.box.common.sdk.transport;

/**
 * @author changjun.xcj
 */
public interface RequestInterceptor {

    /**
     * Intercept request handling from remote
     *
     * @param request
     * @throws RequestException
     */
    void handle(Request request) throws RequestException;

    /**
     * Intercept request invoking to remote
     *
     * @param request
     * @throws RequestException
     */
    void invoke(Request request) throws RequestException;
}
