package com.alibaba.chaosblade.box.dao.infrastructure.experiment.task.miniapp.chain;

import com.alibaba.chaosblade.box.common.app.sdk.ChaosAppResponse;
import com.alibaba.chaosblade.box.common.infrastructure.OnceInvokeReference;
import com.alibaba.chaosblade.box.common.infrastructure.chain.ChainContext;
import com.alibaba.chaosblade.box.dao.infrastructure.app.ChaosAppResponseReference;
import com.alibaba.chaosblade.box.dao.infrastructure.experiment.task.flow.step.MiniAppInvokeContext;
import lombok.Getter;
import lombok.Setter;

/**
 * @author haibin
 *
 *
 */
public class ActivityTargetRetryInvokeContext implements ChainContext {

    @Getter
    private ChaosAppResponse preAppResponse;

    @Getter
    private OnceInvokeReference<MiniAppInvokeContext, ChaosAppResponseReference> invokeReference;

    @Getter
    private MiniAppInvokeContext miniAppInvokeContext;

    @Setter
    @Getter
    private ChaosAppResponse curAppResponse;

    public ActivityTargetRetryInvokeContext(
        OnceInvokeReference<MiniAppInvokeContext, ChaosAppResponseReference> onceInvokeReference,
        MiniAppInvokeContext miniAppInvokeContext, ChaosAppResponse preAppResponse) {
        this.invokeReference = onceInvokeReference;
        this.miniAppInvokeContext = miniAppInvokeContext;
        this.preAppResponse = preAppResponse;
        this.curAppResponse = preAppResponse;
    }

}
