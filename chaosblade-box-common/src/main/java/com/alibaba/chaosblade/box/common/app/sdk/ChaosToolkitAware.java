package com.alibaba.chaosblade.box.common.app.sdk;

/**
 * @author sunju
 *
 */
public interface ChaosToolkitAware {

    /**
     * Get chaos toolkit instance
     *
     * @return toolkit instance
     */
    ChaosToolkit toolkit();

    /**
     * Set chaos toolkit instance
     *
     * @param toolkit toolkit instance
     */
    void setChaosToolkit(ChaosToolkit toolkit);
}
