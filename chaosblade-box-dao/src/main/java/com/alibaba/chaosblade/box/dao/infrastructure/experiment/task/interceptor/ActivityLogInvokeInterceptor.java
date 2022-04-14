package com.alibaba.chaosblade.box.dao.infrastructure.experiment.task.interceptor;

import com.alibaba.chaosblade.box.common.experiment.task.flow.step.activity.ActivityExecuteResult;
import com.alibaba.chaosblade.box.dao.infrastructure.monitor.log.LogFormats;
import com.alibaba.chaosblade.box.dao.infrastructure.experiment.task.flow.activity.ExperimentExecuteContext;
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
public class ActivityLogInvokeInterceptor extends BaseActivityInvokeInterceptor {

    @Override
    public boolean preHandle(ActivityInvokeContext activityInvokeContext, ActivityExecuteResult activityExecuteResult) {
        ExperimentExecuteContext experimentExecuteContext = activityInvokeContext.getStepExecuteContext().getRequest();
        log.info(LogFormats.Activity.buildStartRunActivity(experimentExecuteContext.getActivityTaskId()));
        return true;
    }

    @Override
    public void afterHandle(ActivityInvokeContext activityInvokeContext, ActivityExecuteResult activityExecuteResult,
        Throwable throwable) {
        ExperimentExecuteContext experimentExecuteContext = activityInvokeContext.getStepExecuteContext().getRequest();
        if (throwable != null) {
            log.error("run activity failed,taskId:{}", experimentExecuteContext.getActivityTaskId(), throwable);
            activityExecuteResult.setError(throwable);
        }
        log.info(LogFormats.Activity.buildFinishedRunActivity(experimentExecuteContext.getActivityTaskId()));
    }
}
