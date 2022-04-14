package com.alibaba.chaosblade.box.dao.infrastructure.experiment.task.flow;

import com.alibaba.chaosblade.box.dao.infrastructure.experiment.task.flow.invoker.MiniAppInvoker;
import com.alibaba.chaosblade.box.dao.infrastructure.app.ChaosAppLoaderConfig;
import com.alibaba.chaosblade.box.dao.infrastructure.app.chaosblade.ChaosBladeInvoker;
import com.alibaba.chaosblade.box.dao.infrastructure.app.chaosblade.ChaosBladeListener;
import com.alibaba.chaosblade.box.dao.infrastructure.app.litmuschaos.LitmusChaosInvoker;
import com.alibaba.chaosblade.box.dao.infrastructure.app.litmuschaos.LitmusChaosListener;
import com.alibaba.chaosblade.box.dao.infrastructure.strategy.ActivityTargetsRunnableStrategy;

import java.util.List;

/**
 * @author haibin
 *
 *
 */
public interface FlowEngineConfig {

    /**
     * get chaosblade listener
     *
     * @return listener
     */
    ChaosBladeListener getChaosBladeListener();

    /**
     *
     * @return
     */
    LitmusChaosListener getLitmusChaosListener();

    /**
     * get chaos apploader config
     *
     * @return
     */
    ChaosAppLoaderConfig getChaosAppLoaderConfig();

    List<ActivityTargetsRunnableStrategy> getActivityTargetsRunnableStrategies();

    List<MiniAppInvoker> getMiniAppInvokers();

    ChaosBladeInvoker getChaosBladeInvoker();

    LitmusChaosInvoker getLitmusChaosInvoker();

}
