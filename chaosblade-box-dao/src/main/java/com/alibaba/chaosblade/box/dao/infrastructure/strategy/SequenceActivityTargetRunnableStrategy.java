package com.alibaba.chaosblade.box.dao.infrastructure.strategy;

import com.alibaba.chaosblade.box.common.app.sdk.ChaosAppResponse;
import com.alibaba.chaosblade.box.common.app.sdk.scope.Host;
import com.alibaba.chaosblade.box.dao.infrastructure.app.MiniAppInvokerFactory;
import com.alibaba.chaosblade.box.dao.infrastructure.experiment.task.flow.step.ActivityInvokeContext;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

/**
 * @author haibin
 *
 *
 */
public class SequenceActivityTargetRunnableStrategy extends ActivityTargetsRunnableStrategy {

    @Override
    public List<ChaosAppResponse> run(ActivityInvokeContext activityInvokeContext) {
        List<ChaosAppResponse> chaosAppResponses = new ArrayList<>();
        activityInvokeContext.getActivity().getScope().forEach(new Consumer<Host>() {
            @Override
            public void accept(Host host) {
                chaosAppResponses.add(MiniAppInvokerFactory.createInvoker(activityInvokeContext, host).invoke());
            }
        });
        return chaosAppResponses;
    }

    @Override
    protected List<ChaosAppResponse> internalRun(ActivityInvokeContext activityInvokeContext, List<Host> invokeHosts) {
        List<ChaosAppResponse> chaosAppResponses = new ArrayList<>();
        activityInvokeContext.getActivity().getScope().forEach(new Consumer<Host>() {
            @Override
            public void accept(Host host) {
                chaosAppResponses.add(MiniAppInvokerFactory.createInvoker(activityInvokeContext, host).invoke());
            }
        });
        return chaosAppResponses;
    }

    @Override
    public boolean support(ActivityInvokeContext activityInvokeContext) {
        return true;
    }
}
