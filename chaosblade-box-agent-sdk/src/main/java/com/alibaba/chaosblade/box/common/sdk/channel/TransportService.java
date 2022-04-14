package com.alibaba.chaosblade.box.common.sdk.channel;


import com.alibaba.chaosblade.box.common.sdk.transport.Request;
import com.alibaba.chaosblade.box.common.common.domain.response.Response;

/**
 * @author Changjun Xiao
 */
public interface TransportService {

    /**
     * @param request
     * @param clazz
     * @param <R>
     * @return
     */
    <R> Response<R> invoke(Request request, Class<?> clazz);

}
