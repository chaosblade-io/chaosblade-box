package com.alibaba.chaosblade.box.dao.infrastructure.experiment.task.flow.invoker;

import com.alibaba.chaosblade.box.common.app.sdk.ChaosAppResponse;
import com.alibaba.chaosblade.box.dao.infrastructure.app.ChaosAppResponseReference;
import com.alibaba.chaosblade.box.dao.infrastructure.experiment.task.flow.step.MiniAppInvokeContext;

/**
 * @author haibin
 *
 *
 */
public class DefaultChaosAppInvoker implements MiniAppInvoker {

    @Override
    public boolean support(MiniAppInvokeContext miniAppInvokeContext) {
        return true;
    }

    @Override
    public ChaosAppResponseReference invoke(MiniAppInvokeContext miniAppInvokeContext) {
        ChaosAppResponse chaosAppResponse = ChaosAppInvoker.invokeFromMiniApp(miniAppInvokeContext);
        ChaosAppResponseReference chaosAppResponseReference = new ChaosAppResponseReference();
        chaosAppResponseReference.setChaosAppResponse(chaosAppResponse);
        return chaosAppResponseReference;
    }
}
