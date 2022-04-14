package com.alibaba.chaosblade.box.dao.infrastructure.experiment.task.flow.step.activity;

import com.alibaba.chaosblade.box.common.app.sdk.ChaosAppResponse;
import com.alibaba.chaosblade.box.common.app.sdk.scope.Host;
import com.alibaba.chaosblade.box.common.experiment.task.flow.step.activity.ActivityExecuteResult;
import com.alibaba.chaosblade.box.dao.infrastructure.app.MiniAppInvokerFactory;
import com.alibaba.chaosblade.box.dao.infrastructure.experiment.task.flow.InterceptorInvoker;
import com.alibaba.chaosblade.box.dao.infrastructure.experiment.task.flow.step.ActivityInvokeContext;
import com.alibaba.chaosblade.box.dao.infrastructure.experiment.task.flow.step.InvokeContextFactory;
import com.alibaba.chaosblade.box.dao.infrastructure.experiment.task.flow.step.StepExecuteContext;
import com.alibaba.chaosblade.box.dao.infrastructure.experiment.task.interceptor.BaseActivityInvokeInterceptor;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

/**
 * @author haibin
 *
 *
 */
@Slf4j
public class ActivityExecutor {

    private Activity activity;

    private StepExecuteContext stepExecuteContext;

    private ActivityInvokeContext activityInvokeContext;

    public ActivityExecutor(Activity activity, StepExecuteContext stepExecuteContext) {
        this.activity = activity;
        this.stepExecuteContext = stepExecuteContext;
        this.activityInvokeContext = new ActivityInvokeContext();
        this.activityInvokeContext.setActivity(activity);
    }

    public ActivityExecuteResult execute() {
        ActivityInvokeContext activityInvokeContext = InvokeContextFactory.create(stepExecuteContext,
                ActivityInvokeContext.class);
        activityInvokeContext.setActivity(activity);
        ActivityExecuteResult activityExecuteResult = new ActivityExecuteResult();
        InterceptorInvoker<BaseActivityInvokeInterceptor, ActivityInvokeContext, ActivityExecuteResult>
                interceptorInvoker
                = new InterceptorInvoker<>(
                stepExecuteContext.getFlowEngineContext().getActivityInvokeInterceptors());
        return interceptorInvoker.invoke(this::innerExecute,
                activityInvokeContext,
                activityExecuteResult);
    }

    private ActivityExecuteResult innerExecute(ActivityInvokeContext activityInvokeContext) {
        ActivityExecuteResult activityExecuteResult = new ActivityExecuteResult();
        List<Host> hosts = activity.getScope();
        if (hosts == null || hosts.isEmpty()) {
            activityExecuteResult.addChaosAppResponse(
                    MiniAppInvokerFactory.createInvoker(activityInvokeContext, null).invoke());
        } else {
            List<ChaosAppResponse> chaosAppResponses = stepExecuteContext.getFlowEngineContext()
                    .getActivityTargetRunnableStrategySelector().select(activityInvokeContext).run(activityInvokeContext);
            activityExecuteResult.setAppResponses(chaosAppResponses);
        }
        boolean success = activityExecuteResult.getAppResponses().stream().allMatch(
                ChaosAppResponse::isSuccess);
        activityExecuteResult.setSuccess(success);
        activityExecuteResult.setAppCode(activity.getAppCode());
        return activityExecuteResult;
    }

}
