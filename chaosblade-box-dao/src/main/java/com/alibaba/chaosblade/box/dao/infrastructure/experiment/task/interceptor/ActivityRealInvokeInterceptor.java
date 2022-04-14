package com.alibaba.chaosblade.box.dao.infrastructure.experiment.task.interceptor;

import com.alibaba.chaosblade.box.common.experiment.task.flow.step.OnceInvoke;
import com.alibaba.chaosblade.box.common.experiment.task.flow.step.activity.ActivityExecuteResult;
import com.alibaba.chaosblade.box.dao.infrastructure.experiment.task.flow.step.ActivityInvokeContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @author haibin
 *
 * 
 */
@Component
@Slf4j
public class ActivityRealInvokeInterceptor extends BaseActivityInvokeInterceptor {

    @Override
    public boolean preHandle(ActivityInvokeContext activityInvokeContext, ActivityExecuteResult activityExecuteResult) {
        return true;
    }

    @Override
    public void postHandle(OnceInvoke<ActivityInvokeContext, ActivityExecuteResult> onceInvoke,
                           ActivityInvokeContext activityInvokeContext, ActivityExecuteResult activityExecuteResult) {
        ActivityExecuteResult result = onceInvoke.invoke(activityInvokeContext);
        activityExecuteResult.setMiniAppContextData(result.getMiniAppContextData());
        activityExecuteResult.setError(result.getError());
        activityExecuteResult.setAppResponses(result.getAppResponses());
        activityExecuteResult.setAppCode(result.getAppCode());
        activityExecuteResult.setSuccess(result.isSuccess());
    }

    @Override
    public void afterHandle(ActivityInvokeContext activityInvokeContext, ActivityExecuteResult activityExecuteResult,
        Throwable throwable) {

    }

    @Override
    public Integer getOrder() {
        return Integer.MIN_VALUE + 5;
    }
}
