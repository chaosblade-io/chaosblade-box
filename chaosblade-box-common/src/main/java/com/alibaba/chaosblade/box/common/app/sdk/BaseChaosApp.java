package com.alibaba.chaosblade.box.common.app.sdk;

/**
 * @author sunju
 *
 */
public abstract class BaseChaosApp implements ChaosApp, ChaosAppContextAware, ChaosToolkitAware {

    private ChaosAppContext context;

    private ChaosToolkit toolkit;

    @Override
    public void setContext(ChaosAppContext context) {
        this.context = context;
    }

    @Override
    public ChaosAppContext getContext() {
        return this.context;
    }

    @Override
    public void setChaosToolkit(ChaosToolkit toolkit) {
        this.toolkit = toolkit;
    }

    @Override
    public ChaosToolkit toolkit() {
        return this.toolkit;
    }
}
