package com.alibaba.chaosblade.box.common.common.domain.chaosblade.requst;

import com.alibaba.chaosblade.box.common.app.sdk.scope.Host;
import lombok.Getter;

/**
 * @author haibin
 *
 *
 */
@Getter
public class ChaosBladeDestroyExpRequest extends ChaosBladeExpQueryRequest {

    private String appCode;

    public ChaosBladeDestroyExpRequest(Host host, String expId, String appCode) {
        super(host, expId);
        this.appCode = appCode;
    }
}
