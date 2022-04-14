package com.alibaba.chaosblade.box.dao.infrastructure.app.chaosblade;

import com.alibaba.chaosblade.box.common.experiment.task.flow.ChaosBladeAppResponse;
import com.alibaba.chaosblade.box.dao.infrastructure.experiment.task.flow.step.MiniAppInvokeContext;
import com.alibaba.chaosblade.box.dao.model.ChaosBladeExpUidDO;
import org.springframework.stereotype.Component;


/**
 * @author haibin
 *
 * 
 */
@Component
public class EmptyChaosBladeMiniAppInterceptor implements ChaosBladeMiniAppInterceptor {

    @Override
    public void preCreate(ChaosBladeExpUidDO chaosBladeExpUidDO, MiniAppInvokeContext miniAppInvokeContext,
                          ChaosBladeAppResponse chaosAppResponse) {

    }

    @Override
    public void afterCreate(ChaosBladeExpUidDO chaosBladeExpUidDO, MiniAppInvokeContext miniAppInvokeContext,
        ChaosBladeAppResponse chaosAppResponse) {

    }

    @Override
    public void preUpdate(ChaosBladeExpUidDO chaosBladeExpUidDO, MiniAppInvokeContext miniAppInvokeContext,
        ChaosBladeAppResponse chaosAppResponse) {

    }
}
