package com.alibaba.chaosblade.box.dao.infrastructure.experiment.task.interceptor;

import com.alibaba.chaosblade.box.common.experiment.task.flow.step.activity.ActivityExecuteResult;
import com.alibaba.chaosblade.box.dao.infrastructure.experiment.task.flow.step.ActivityInvokeContext;
import com.alibaba.chaosblade.box.dao.model.ActivityTaskDO;
import com.alibaba.chaosblade.box.dao.model.ExperimentTaskDO;
import org.springframework.stereotype.Component;

import static com.alibaba.chaosblade.box.common.experiment.task.flow.ActivityTaskExecutionAttributes.ATTRIBUTE_KEY_ACTIVITY_TASK_DO;

/**
 * 用来准备小程序上下文的参数,供自定义小程序还有小程序脚本来调用
 *
 * @author haibin
 *
 *
 */
@Component
public class MiniAppContextPrepareInvokeInterceptor extends BaseActivityInvokeInterceptor {

    public static String EXPERIMENT_TASK_ID = "experiment_task_id";

    public static String EXPERIMENT_ID = "experiment_id";

    public static String ACTIVITY_TASK_DO = "activity_task";
    public static String CONTEXT_SETTINGS = "context_settings";

    @Override
    public boolean preHandle(ActivityInvokeContext activityInvokeContext, ActivityExecuteResult activityExecuteResult) {
        ExperimentTaskDO experimentTaskDO = activityInvokeContext.getContextData().getExperimentTaskDO();
        activityInvokeContext.getStepExecuteContext().getChaosAppContext().addData(EXPERIMENT_TASK_ID,
            experimentTaskDO.getTaskId());
        activityInvokeContext.getStepExecuteContext().getChaosAppContext().addData(EXPERIMENT_ID,
            experimentTaskDO.getExperimentId());
        activityInvokeContext.getStepExecuteContext().getChaosAppContext().addData(ACTIVITY_TASK_DO,
            activityInvokeContext.getContextData()
                .getObject(ATTRIBUTE_KEY_ACTIVITY_TASK_DO,
                    ActivityTaskDO.class));
        activityInvokeContext.getStepExecuteContext().getChaosAppContext().addData(CONTEXT_SETTINGS,
            activityInvokeContext.getContextData());
        return true;
    }

    @Override
    public void afterHandle(ActivityInvokeContext activityInvokeContext, ActivityExecuteResult activityExecuteResult,
        Throwable throwable) {
        activityInvokeContext.getStepExecuteContext().getChaosAppContext().getData().remove(EXPERIMENT_TASK_ID);
        activityInvokeContext.getStepExecuteContext().getChaosAppContext().getData().remove(ACTIVITY_TASK_DO);
        activityInvokeContext.getStepExecuteContext().getChaosAppContext().getData().remove(
            ATTRIBUTE_KEY_ACTIVITY_TASK_DO);
        activityInvokeContext.getStepExecuteContext().getChaosAppContext().getData().remove(CONTEXT_SETTINGS);
    }
}
