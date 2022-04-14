package com.alibaba.chaosblade.box.common.sdk;

import com.alibaba.chaosblade.box.common.sdk.channel.PaasTransportService;
import com.alibaba.chaosblade.box.common.sdk.channel.TransportServiceFactory;

/**
 * @author Changjun Xiao
 */
public class ChaosFactory {

    public static final int DEFAULT_TIMEOUT_FOR_CLOUD_HSF = 3;


    /**
     * 专有云版本
     *
     * @return
     */
    public static ChaosBladeForCloud forPaas(int timeout) {
        PaasTransportService transportService = TransportServiceFactory.createPassService(timeout);
        return new ChaosBladeForCloud(transportService);
    }
}
