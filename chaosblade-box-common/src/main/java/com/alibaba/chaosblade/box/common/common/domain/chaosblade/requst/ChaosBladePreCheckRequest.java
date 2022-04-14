package com.alibaba.chaosblade.box.common.common.domain.chaosblade.requst;

import com.alibaba.chaosblade.box.common.app.sdk.ChaosAppRequest;
import com.alibaba.chaosblade.box.common.app.sdk.scope.Host;
import com.alibaba.chaosblade.box.common.common.domain.chaosblade.ChaosBladeAction;
import lombok.Getter;

/**
 * @author sunpeng
 * 
 *
 */
@Getter
public class ChaosBladePreCheckRequest {

    private final String ak;

    private final String sk;

    private final Host host;

    private final ChaosBladeAction chaosBladeAction;

    private final ChaosAppRequest chaosAppRequest;

    private final String evnType;

    public ChaosBladePreCheckRequest(String ak, String sk, Host host, ChaosBladeAction chaosBladeAction, ChaosAppRequest chaosAppRequest, String evnType) {
        this.ak = ak;
        this.sk = sk;
        this.host = host;
        this.chaosBladeAction = chaosBladeAction;
        this.chaosAppRequest = chaosAppRequest;
        this.evnType = evnType;
    }
}
