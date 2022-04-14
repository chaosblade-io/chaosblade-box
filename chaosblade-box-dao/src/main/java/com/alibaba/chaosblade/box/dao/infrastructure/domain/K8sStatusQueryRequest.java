package com.alibaba.chaosblade.box.dao.infrastructure.domain;

import com.alibaba.chaosblade.box.common.app.sdk.scope.Host;
import com.alibaba.chaosblade.box.common.common.domain.chaosblade.requst.ChaosBladeExpQueryRequest;
import lombok.Getter;

/**
 * @author haibin
 * 
 *
 */
@Getter
public class K8sStatusQueryRequest extends ChaosBladeExpQueryRequest {
    private boolean isCreate;

    public K8sStatusQueryRequest(Host host, String expId, boolean isCreate) {
        super(host, expId);
        this.isCreate = isCreate;
    }
}
