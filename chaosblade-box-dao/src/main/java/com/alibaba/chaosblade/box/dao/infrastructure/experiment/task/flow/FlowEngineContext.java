package com.alibaba.chaosblade.box.dao.infrastructure.experiment.task.flow;

import com.alibaba.chaosblade.box.common.app.sdk.constants.EnvironmentEnum;
import com.alibaba.chaosblade.box.common.experiment.task.flow.step.Lifecycle;
import com.alibaba.chaosblade.box.dao.infrastructure.experiment.task.flow.step.activity.ActivityProcessor;
import com.alibaba.chaosblade.box.dao.infrastructure.experiment.task.flow.step.activity.ActivityProcessorImpl;
import com.alibaba.chaosblade.box.common.experiment.task.flow.util.concurrent.ThreadPoolExecutors;
import com.alibaba.chaosblade.box.dao.infrastructure.app.LocalChaosAppLoader;
import com.alibaba.chaosblade.box.dao.infrastructure.app.ChaosAppLoaderConfig;
import com.alibaba.chaosblade.box.dao.infrastructure.experiment.task.flow.invoker.ChaosBladeAppInvoker;
import com.alibaba.chaosblade.box.dao.infrastructure.experiment.task.flow.invoker.LitmusChaosAppInvoker;
import com.alibaba.chaosblade.box.dao.infrastructure.experiment.task.flow.invoker.MiniAppInvoker;
import com.alibaba.chaosblade.box.dao.infrastructure.experiment.task.flow.invoker.MiniAppInvokerSelector;
import com.alibaba.chaosblade.box.dao.infrastructure.experiment.task.interceptor.BaseActivityInvokeInterceptor;
import com.alibaba.chaosblade.box.dao.infrastructure.experiment.task.interceptor.BaseMiniAppInvokeInterceptor;
import com.alibaba.chaosblade.box.dao.infrastructure.experiment.task.interceptor.InvokeInterceptor;
import com.alibaba.chaosblade.box.dao.infrastructure.strategy.ActivityTargetRunnableStrategySelector;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.ExecutorService;

/**
 * framework context,it contains all components in framework,
 * if you want to extend the framework ,you can set the component interface or override the
 * flowEngineContext
 *
 * @author haibin
 *
 *
 */
@Data
@Slf4j
public class FlowEngineContext implements Lifecycle {

    private Logger logger = LoggerFactory.getLogger(FlowEngineContext.class);

    /**
     * framework configuration
     */
    private FlowEngineConfig flowEngineConfig;

    /**
     * the processor to attack all request for activity
     */
    private ActivityProcessor activityProcessor;

    ;

    /**
     * because the background will run in async,so need a ExecutorService
     */
    private ExecutorService backgroundActivityExecutorService;

    private ExecutorService appActivityExecutorService;

    private EnvironmentEnum environment;

    private List<BaseMiniAppInvokeInterceptor> appInvokeInterceptors = new ArrayList<>();

    private List<BaseActivityInvokeInterceptor> activityInvokeInterceptors = new ArrayList<>();

    private ActivityTargetRunnableStrategySelector activityTargetRunnableStrategySelector;

    private MiniAppInvokerSelector miniAppInvokerSelector;

    public FlowEngineContext(FlowEngineConfig flowEngineConfig) {
        this.flowEngineConfig = flowEngineConfig;
    }

    public synchronized void init() throws Exception {
        logger.info("init flow engine context");
        initChaosAppLoader();
        initExperimentComponent();
        initProcessor();
        initInvokeInterceptor();
        initChaosBlade();
        initLitmusChaos();
    }

    private void initChaosAppLoader() {
        ChaosAppLoaderConfig chaosAppLoaderConfig = flowEngineConfig.getChaosAppLoaderConfig() == null ? new ChaosAppLoaderConfig()
            : flowEngineConfig.getChaosAppLoaderConfig();
        LocalChaosAppLoader.init(chaosAppLoaderConfig);
    }

    private void initChaosBlade() throws Exception {
        this.flowEngineConfig.getChaosBladeListener().onCompleted(this.getFlowEngineConfig().getChaosBladeInvoker());
    }

    private void initLitmusChaos() throws Exception {
        this.flowEngineConfig.getLitmusChaosListener().onCompleted(this.getFlowEngineConfig().getLitmusChaosInvoker());
    }

    private void initInvokeInterceptor() {
        activityInvokeInterceptors.sort(comparator());
        appInvokeInterceptors.sort(comparator());
    }

    private Comparator<InvokeInterceptor> comparator() {
        return new Comparator<InvokeInterceptor>() {
            @Override
            public int compare(InvokeInterceptor o1, InvokeInterceptor o2) {
                return -o1.getOrder().compareTo(o2.getOrder());
            }
        };
    }

    private void initExperimentComponent() {
        this.activityTargetRunnableStrategySelector = new ActivityTargetRunnableStrategySelector(
            flowEngineConfig.getActivityTargetsRunnableStrategies());
        List<MiniAppInvoker> miniAppInvokers = flowEngineConfig.getMiniAppInvokers();
        if (miniAppInvokers == null) {
            miniAppInvokers = new ArrayList<>();
        }
        miniAppInvokers.add(new ChaosBladeAppInvoker());
        miniAppInvokers.add(new LitmusChaosAppInvoker());
        this.miniAppInvokerSelector = new MiniAppInvokerSelector(miniAppInvokers);
    }

    private void initProcessor() {
        if (this.backgroundActivityExecutorService == null) {
            this.backgroundActivityExecutorService = ThreadPoolExecutors.newSingleThreadScheduledExecutor(
                "activity-background-processor");
        }
        this.activityProcessor = new ActivityProcessorImpl(this);
    }

    public FlowEngineConfig getFlowEngineConfig() {
        return flowEngineConfig;
    }

    @Override
    public void start() {
    }

    @Override
    public void close() {

    }

}
