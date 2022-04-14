package com.alibaba.chaosblade.box.common.sdk.transport;

import com.alibaba.chaosblade.box.common.common.domain.response.Response;

/**
 * Invoke request to remote
 *
 * @author changjun.xcj
 */
public interface RequestInvoker {

    /**
     * Invoke request
     *
     * @param uri
     * @param request
     * @param clazz
     * @param <R>
     * @return
     * @throws RequestException
     * @throws RequestException
     */
    <R> Response<R> invoke(RequestUri uri, Request request, Class<?> clazz) throws RequestException;


}
