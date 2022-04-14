package com.alibaba.chaosblade.box.common.sdk.transport;

import com.alibaba.chaosblade.box.common.common.domain.response.Response;

/**
 * Handle request from remote
 *
 * @author changjun.xcj
 */
public interface RequestHandler {

    /**
     * Handle request
     *
     * @param request
     * @return
     * @throws RequestException
     */
    <R> Response<R> handle(Request request) throws RequestException;
}
