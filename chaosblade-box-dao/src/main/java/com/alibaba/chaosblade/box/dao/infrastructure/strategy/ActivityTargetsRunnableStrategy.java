package com.alibaba.chaosblade.box.dao.infrastructure.strategy;

import com.alibaba.chaosblade.box.common.app.sdk.InvokeMode;
import com.alibaba.chaosblade.box.common.app.sdk.ChaosAppResponse;
import com.alibaba.chaosblade.box.common.app.sdk.scope.Host;
import com.alibaba.chaosblade.box.common.experiment.task.flow.ActivityTaskExecutionAttributes;
import com.alibaba.chaosblade.box.common.infrastructure.util.CollectionUtil;
import com.alibaba.chaosblade.box.dao.infrastructure.app.MiniAppInvokerFactory;
import com.alibaba.chaosblade.box.dao.infrastructure.experiment.task.flow.step.ActivityInvokeContext;

import java.util.ArrayList;
import java.util.List;

/**
 * 活动对象的运行策略
 *
 * @author haibin
 *
 * 
 */
public abstract class ActivityTargetsRunnableStrategy {

    public List<ChaosAppResponse> run(ActivityInvokeContext activityInvokeContext) {
        List<ChaosAppResponse> chaosAppResponses = new ArrayList<>();
        List<Host> hosts = activityInvokeContext.getActivity().getScope();
        if (hosts == null) {
            hosts = new ArrayList<>();
        }
        if (InvokeMode.ONCE.equals(activityInvokeContext.getInvokeMode())) {
            ChaosAppResponse chaosAppResponse = null;
            if (CollectionUtil.isNullOrEmpty(hosts)) {
                chaosAppResponse = MiniAppInvokerFactory.createInvoker(activityInvokeContext, null).invoke();
                activityInvokeContext.getTempData().add(
                        ActivityTaskExecutionAttributes.ATTRIBUTE_INVOKE_ONCE_HOST_RESPONSE,
                        chaosAppResponse);
            } else {
                chaosAppResponse = MiniAppInvokerFactory.createInvoker(activityInvokeContext, hosts.get(0)).invoke();
                activityInvokeContext.getTempData().add(
                        ActivityTaskExecutionAttributes.ATTRIBUTE_INVOKE_ONCE_HOST_RESPONSE,
                        chaosAppResponse);
                if (hosts.size() > 1) {
                    chaosAppResponses.addAll(internalRun(activityInvokeContext, hosts.subList(1, hosts.size())));
                }
            }
            chaosAppResponses.add(chaosAppResponse);
            return chaosAppResponses;
        }
        if (hosts.isEmpty()) {
            chaosAppResponses.add(MiniAppInvokerFactory.createInvoker(activityInvokeContext, null).invoke());
            return chaosAppResponses;
        }
        return internalRun(activityInvokeContext, hosts);
    }

    protected abstract List<ChaosAppResponse> internalRun(ActivityInvokeContext activityInvokeContext,
                                                       List<Host> invokeHosts);

    public abstract boolean support(ActivityInvokeContext activityInvokeContext);
}
