package com.alibaba.chaosblade.box.dao.infrastructure.app;

import com.alibaba.chaosblade.box.common.app.sdk.ChaosAppRequest;
import com.alibaba.chaosblade.box.common.app.sdk.ChaosAppResponse;
import com.alibaba.chaosblade.box.common.app.sdk.scope.Host;
import com.alibaba.chaosblade.box.common.common.util.MiniAppUtils;
import com.alibaba.chaosblade.box.common.experiment.task.flow.ChaosBladeAppResponse;
import com.alibaba.chaosblade.box.dao.infrastructure.experiment.task.flow.InterceptorInvoker;
import com.alibaba.chaosblade.box.dao.infrastructure.experiment.task.flow.invoker.MiniAppInvoker;
import com.alibaba.chaosblade.box.dao.infrastructure.experiment.task.flow.step.InvokeContextFactory;
import com.alibaba.chaosblade.box.dao.infrastructure.experiment.task.flow.step.ActivityInvokeContext;
import com.alibaba.chaosblade.box.dao.infrastructure.experiment.task.flow.step.MiniAppInvokeContext;
import com.alibaba.chaosblade.box.dao.infrastructure.experiment.task.flow.step.StepExecuteContext;
import com.alibaba.chaosblade.box.dao.infrastructure.experiment.task.flow.step.activity.Activity;

/**
 * @author haibin
 *
 *
 */
public class MiniAppExecutor {

    protected Host host;

    protected Activity activity;

    protected StepExecuteContext stepExecuteContext;

    private ActivityInvokeContext activityInvokeContext;

    public MiniAppExecutor(ActivityInvokeContext activityInvokeContext, Host host) {
        this.host = host;
        this.activityInvokeContext = activityInvokeContext;
        this.activity = this.activityInvokeContext.getActivity();
        this.stepExecuteContext = this.activityInvokeContext.getStepExecuteContext();
    }

    public ChaosAppResponse invoke() {
        ChaosAppRequest chaosAppRequest = buildRequest();
        MiniAppInvokeContext miniAppInvokeContext
            = InvokeContextFactory.create(stepExecuteContext, MiniAppInvokeContext.class);
        miniAppInvokeContext.setActivityInvokeContext(activityInvokeContext);
        miniAppInvokeContext.setChaosAppRequest(chaosAppRequest);
        miniAppInvokeContext.setHost(host);
        MiniAppInvoker miniAppInvoker = this.stepExecuteContext.getFlowEngineContext().getMiniAppInvokerSelector()
            .select(miniAppInvokeContext);
        ChaosAppResponseReference chaosAppResponseReference = new ChaosAppResponseReference();
        if (MiniAppUtils.isFromChaosBlade(activityInvokeContext.getExecutableAppCode())) {
            chaosAppResponseReference.setChaosAppResponse(new ChaosBladeAppResponse());
        } else {
            chaosAppResponseReference.setChaosAppResponse(new ChaosAppResponse());
        }
        chaosAppResponseReference = new InterceptorInvoker<>(
            stepExecuteContext.getFlowEngineContext().getAppInvokeInterceptors())
            .invoke(miniAppInvoker, miniAppInvokeContext, chaosAppResponseReference);
        return chaosAppResponseReference.getChaosAppResponse();
    }

    protected ChaosAppRequest buildRequest() {
        ChaosAppRequest appRequest = new ChaosAppRequest();
        appRequest.setScope(host);
        if (activity.getArguments() != null) {
            appRequest.setAction(activity.getArguments().getAllArguments());
            appRequest.setUserArgs(activity.getArguments().getAllArguments());
        }
        return appRequest;
    }

}
