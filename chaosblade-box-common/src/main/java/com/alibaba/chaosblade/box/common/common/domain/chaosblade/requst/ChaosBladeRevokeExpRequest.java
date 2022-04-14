package com.alibaba.chaosblade.box.common.common.domain.chaosblade.requst;


import com.alibaba.chaosblade.box.common.app.sdk.scope.Host;

/**
 * @author haibin
 *
 * 
 */
public class ChaosBladeRevokeExpRequest extends ChaosBladeExpQueryRequest {

    public ChaosBladeRevokeExpRequest(Host host, String expId) {
        super(host, expId);
    }
}
