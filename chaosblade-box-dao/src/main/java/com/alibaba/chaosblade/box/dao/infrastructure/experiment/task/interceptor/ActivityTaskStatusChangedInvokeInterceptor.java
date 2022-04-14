package com.alibaba.chaosblade.box.dao.infrastructure.experiment.task.interceptor;

import com.alibaba.chaosblade.box.common.experiment.task.flow.ActivityTaskExecutionAttributes;
import com.alibaba.chaosblade.box.common.experiment.task.flow.step.activity.ActivityExecuteResult;
import com.alibaba.chaosblade.box.common.experiment.task.flow.util.Settings;
import com.alibaba.chaosblade.box.dao.infrastructure.experiment.task.ActivityTaskExecutionFinishedProcessor;
import com.alibaba.chaosblade.box.dao.infrastructure.experiment.task.flow.step.ActivityInvokeContext;
import com.alibaba.chaosblade.box.dao.model.ActivityTaskDO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author haibin
 *
 * 
 */
@Component
@Slf4j
public class ActivityTaskStatusChangedInvokeInterceptor extends BaseActivityInvokeInterceptor {

    @Autowired
    private ActivityTaskExecutionFinishedProcessor activityTaskExecutionFinishedProcessor;

    @Override
    public boolean preHandle(ActivityInvokeContext activityInvokeContext, ActivityExecuteResult activityExecuteResult) {
        return true;
    }

    @Override
    public void afterHandle(ActivityInvokeContext activityInvokeContext, ActivityExecuteResult activityExecuteResult,
        Throwable throwable) {
        Settings contextData = activityInvokeContext.getContextData();
        ActivityTaskDO activityTaskDO = contextData.getObject(
            ActivityTaskExecutionAttributes.ATTRIBUTE_KEY_ACTIVITY_TASK_DO, ActivityTaskDO.class);
        activityExecuteResult.setError(throwable);
        activityExecuteResult.setMiniAppContextData(
            activityInvokeContext.getStepExecuteContext().getChaosAppContext().getData());
        activityTaskExecutionFinishedProcessor.afterFinished(activityTaskDO, activityExecuteResult,
            activityInvokeContext.getContextData());
    }

    @Override
    public Integer getOrder() {
        return Integer.MAX_VALUE;
    }
}
