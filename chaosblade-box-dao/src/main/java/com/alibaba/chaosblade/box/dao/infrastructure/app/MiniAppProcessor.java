package com.alibaba.chaosblade.box.dao.infrastructure.app;


import com.alibaba.chaosblade.box.dao.infrastructure.app.descriptors.ChaosAppDescriptor;

/**
 * @author haibin
 * 
 *
 */
public interface MiniAppProcessor {

    /**
     * invoke after chaos-app init
     */
    public void afterInit(ChaosAppDescriptor chaosAppDescriptor);

}
