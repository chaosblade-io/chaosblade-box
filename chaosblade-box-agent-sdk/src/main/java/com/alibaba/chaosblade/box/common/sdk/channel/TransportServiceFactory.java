package com.alibaba.chaosblade.box.common.sdk.channel;

/**
 * @author Changjun Xiao
 */
public class TransportServiceFactory {

    /**
     * 创建 Pass 通道调用
     *
     * @param timeout 毫秒
     * @return
     */
    public static PaasTransportService createPassService(int timeout) {
        return new PaasTransportService(timeout);
    }
}
