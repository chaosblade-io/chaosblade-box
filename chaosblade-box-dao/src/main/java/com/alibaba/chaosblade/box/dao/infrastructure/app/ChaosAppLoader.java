package com.alibaba.chaosblade.box.dao.infrastructure.app;


import com.alibaba.chaosblade.box.dao.infrastructure.app.descriptors.ChaosAppDescriptor;

import java.util.Collection;

/**
 * @author sunju
 *
 */
public interface ChaosAppLoader {

    /**
     * Get descriptor of chaosapp.
     *
     * @param namespace app namespace
     * @return chaosapp descriptor
     */
    ChaosAppDescriptor getChaosAppDescriptor(String namespace);

    /**
     *
     * @return all chaosapp descriptor
     */
    Collection<ChaosAppDescriptor> getAllChaosAppDescriptors();

    /**
     * load all chaosapp
     */
    void load();

}
