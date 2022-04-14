package com.alibaba.chaosblade.box.dao.infrastructure.experiment.task.flow;

import com.alibaba.chaosblade.box.dao.infrastructure.app.ChaosAppLoaderConfig;
import com.alibaba.chaosblade.box.dao.infrastructure.app.chaosblade.ChaosBladeInvoker;
import com.alibaba.chaosblade.box.dao.infrastructure.app.chaosblade.ChaosBladeListener;
import com.alibaba.chaosblade.box.dao.infrastructure.app.litmuschaos.LitmusChaosInvoker;
import com.alibaba.chaosblade.box.dao.infrastructure.app.litmuschaos.LitmusChaosListener;
import com.alibaba.chaosblade.box.dao.infrastructure.experiment.task.flow.invoker.MiniAppInvoker;
import com.alibaba.chaosblade.box.dao.infrastructure.strategy.ActivityTargetsRunnableStrategy;

import java.util.List;

/**
 * @author haibin
 * 
 *
 */
public class ChaosFlowConfig implements FlowEngineConfig {

    public void setChaosBladeListener(ChaosBladeListener chaosBladeListener) {
        this.chaosBladeListener = chaosBladeListener;
    }

    public void setChaosBladeInvoker(ChaosBladeInvoker chaosBladeInvoker) {
        this.chaosBladeInvoker = chaosBladeInvoker;
    }

    public void setLitmusChaosInvoker(LitmusChaosInvoker litmusChaosInvoker) {
        this.litmusChaosInvoker = litmusChaosInvoker;
    }

    private ChaosBladeInvoker chaosBladeInvoker;

    private LitmusChaosInvoker litmusChaosInvoker;

    protected ChaosBladeListener chaosBladeListener;

    private LitmusChaosListener litmusChaosListener;

    public void setMiniAppInvokers(List<MiniAppInvoker> miniAppInvokers) {
        this.miniAppInvokers = miniAppInvokers;
    }

    private List<MiniAppInvoker> miniAppInvokers;

    public void setActivityTargetsRunnableStrategies(
        List<ActivityTargetsRunnableStrategy> activityTargetsRunnableStrategies) {
        this.activityTargetsRunnableStrategies = activityTargetsRunnableStrategies;
    }

    private List<ActivityTargetsRunnableStrategy> activityTargetsRunnableStrategies;

    public void setChaosAppLoaderConfig(ChaosAppLoaderConfig chaosAppLoaderConfig) {
        this.chaosAppLoaderConfig = chaosAppLoaderConfig;
    }

    private ChaosAppLoaderConfig chaosAppLoaderConfig;

    @Override
    public ChaosBladeListener getChaosBladeListener() {
        return this.chaosBladeListener;
    }

    @Override
    public LitmusChaosListener getLitmusChaosListener() {
        return litmusChaosListener;
    }

    @Override
    public ChaosAppLoaderConfig getChaosAppLoaderConfig() {
        return this.chaosAppLoaderConfig;
    }

    @Override
    public List<ActivityTargetsRunnableStrategy> getActivityTargetsRunnableStrategies() {
        return this.activityTargetsRunnableStrategies;
    }

    @Override
    public List<MiniAppInvoker> getMiniAppInvokers() {
        return this.miniAppInvokers;
    }

    @Override
    public ChaosBladeInvoker getChaosBladeInvoker() {
        return this.chaosBladeInvoker;
    }

    @Override
    public LitmusChaosInvoker getLitmusChaosInvoker() {
        return litmusChaosInvoker;
    }

    public void setLitmusChaosListener(LitmusChaosListener litmusChaosListener) {
        this.litmusChaosListener = litmusChaosListener;
    }
}
