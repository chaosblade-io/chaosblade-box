package com.alibaba.chaosblade.box.common.common.domain.chaosblade.requst;

import com.alibaba.chaosblade.box.common.app.sdk.scope.Host;
import lombok.Getter;

/**
 * @author haibin
 *
 *
 */
@Getter
public class ChaosBladeExpQueryRequest extends ChaosBladeHostQueryRequest {

    private String expId;

    private boolean k8s = false;

    public ChaosBladeExpQueryRequest(Host host, String expId) {
        super(host);
        this.expId = expId;
    }

    public ChaosBladeExpQueryRequest(Host host, String expId, boolean isK8s) {
        super(host);
        this.expId = expId;
        this.k8s = isK8s;
    }
}
