package com.alibaba.chaosblade.box.common.common.domain.chaosblade.requst;

import com.alibaba.chaosblade.box.common.app.sdk.scope.Host;
import lombok.Getter;

/**
 * @author haibin
 *
 *
 */
@Getter
public class ChaosBladeProcessQueryRequest extends ChaosBladeHostQueryRequest {

    protected String processName;

    public ChaosBladeProcessQueryRequest(Host host, String processName) {
        super(host);
        this.processName = processName;
    }
}
