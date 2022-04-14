package com.alibaba.chaosblade.box.dao.infrastructure.experiment.task.miniapp;

import com.alibaba.chaosblade.box.common.app.sdk.scope.Host;
import com.alibaba.chaosblade.box.common.experiment.task.flow.ChaosBladeAppResponse;
import com.alibaba.chaosblade.box.dao.infrastructure.app.chaosblade.ChaosBladeMiniAppInterceptor;
import com.alibaba.chaosblade.box.dao.infrastructure.experiment.task.flow.step.MiniAppInvokeContext;
import com.alibaba.chaosblade.box.dao.model.ChaosBladeExpUidDO;
import org.springframework.stereotype.Component;

/**
 * @author haibin.lhb
 *
 *
 */
@Component
public class ApplicationDeviceConfigurationSetterChaosBladeMiniAppInterceptor implements ChaosBladeMiniAppInterceptor {

    /**
     * 对于应用设备来说,我们需要在chaosblade的记录里面记录appConfigurationId,而不是deviceConfigurationId
     *
     * @param chaosBladeExpUidDO
     * @param miniAppInvokeContext
     * @param chaosAppResponse
     */
    @Override
    public void preCreate(ChaosBladeExpUidDO chaosBladeExpUidDO, MiniAppInvokeContext miniAppInvokeContext,
                          ChaosBladeAppResponse chaosAppResponse) {
        Host host = miniAppInvokeContext.getHost();
        if (host != null) {
            if (host.isAppScope()) {
                chaosBladeExpUidDO.setConfigurationId(host.getAppConfigurationId());
                chaosBladeExpUidDO.addAttribute(ChaosBladeExpUidDO.ATTRIBUTE_DEVICE_CONFIGURATION_ID,
                    host.getDeviceConfigurationId());
                chaosBladeExpUidDO.addAttribute(ChaosBladeExpUidDO.ATTRIBUTE_DEVICE_IP,
                    miniAppInvokeContext.getHost().getTargetIp());
            } else {
                chaosBladeExpUidDO.setConfigurationId(host.getDeviceConfigurationId());
            }
        }
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
