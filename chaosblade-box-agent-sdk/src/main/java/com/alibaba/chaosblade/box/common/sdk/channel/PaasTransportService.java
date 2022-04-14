package com.alibaba.chaosblade.box.common.sdk.channel;

import com.alibaba.chaosblade.box.common.sdk.constant.Header;
import com.alibaba.chaosblade.box.common.sdk.transport.Request;
import com.alibaba.chaosblade.box.common.common.domain.response.Response;

/**
 * @author Changjun Xiao
 */
public class PaasTransportService implements TransportService {


    /**
     * 单位是毫秒
     */
    private int timeout;

    public PaasTransportService(int timeout) {
        this.timeout = timeout;
    }

    @Override
    public <R> Response<R> invoke(Request request, Class<?> clazz) {
        String host = request.getHeader(Header.HOST);
        String port = request.getHeader(Header.PORT);

        PaasTransportChannel transportChannel = new PaasTransportChannel(host, port);
        return transportChannel.invoke(request, clazz, timeout);
    }
}
