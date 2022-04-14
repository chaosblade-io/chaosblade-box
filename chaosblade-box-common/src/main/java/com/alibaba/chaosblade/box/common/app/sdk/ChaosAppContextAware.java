package com.alibaba.chaosblade.box.common.app.sdk;

/**
 * @author sunju
 *
 */
public interface ChaosAppContextAware {

    /**
     * Set app context
     *
     * @param context app context
     */
    void setContext(ChaosAppContext context);

    /**
     * Get context of ChaosApp
     *
     * @return context
     */
    ChaosAppContext getContext();

}
