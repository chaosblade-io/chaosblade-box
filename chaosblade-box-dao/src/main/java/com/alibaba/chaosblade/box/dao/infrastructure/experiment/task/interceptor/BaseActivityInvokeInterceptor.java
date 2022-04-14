package com.alibaba.chaosblade.box.dao.infrastructure.experiment.task.interceptor;

import com.alibaba.chaosblade.box.common.experiment.task.flow.step.OnceInvoke;
import com.alibaba.chaosblade.box.common.experiment.task.flow.step.activity.ActivityExecuteResult;
import com.alibaba.chaosblade.box.dao.infrastructure.experiment.task.flow.step.ActivityInvokeContext;

/**
 * @author haibin
 *
 *
 */
public abstract class BaseActivityInvokeInterceptor
    implements InvokeInterceptor<ActivityInvokeContext, ActivityExecuteResult> {

    @Override
    public void postHandle(OnceInvoke<ActivityInvokeContext, ActivityExecuteResult> onceInvoke,
                           ActivityInvokeContext activityInvokeContext, ActivityExecuteResult activityExecuteResult) {

    }

    @Override
    public Integer getOrder() {
        return 0;
    }
}
