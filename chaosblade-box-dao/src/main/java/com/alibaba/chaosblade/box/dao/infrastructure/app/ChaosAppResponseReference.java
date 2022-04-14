package com.alibaba.chaosblade.box.dao.infrastructure.app;


import com.alibaba.chaosblade.box.common.app.sdk.ChaosAppResponse;

/**
 * @author haibin
 *
 * 
 */
public class ChaosAppResponseReference {

    public ChaosAppResponse getChaosAppResponse() {
        return chaosAppResponse;
    }

    public void setChaosAppResponse(ChaosAppResponse chaosAppResponse) {
        this.chaosAppResponse = chaosAppResponse;
    }

    private ChaosAppResponse chaosAppResponse;



}
