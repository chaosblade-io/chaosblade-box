package com.alibaba.chaosblade.box.dao.infrastructure.experiment.task.flow.step.activity;

import com.alibaba.chaosblade.box.dao.infrastructure.experiment.task.flow.step.BaseStepInvocation;
import com.alibaba.chaosblade.box.common.experiment.task.flow.step.activity.ActivityExecuteResult;
import com.alibaba.chaosblade.box.dao.infrastructure.experiment.task.flow.step.StepExecuteContext;
import lombok.extern.slf4j.Slf4j;

/**
 * the atomic invocation to invoke activity
 *
 * @author haibin
 *
 *
 */
@Slf4j
public class ActivityInvocation extends BaseStepInvocation<Activity, ActivityExecuteResult> {

    public ActivityInvocation(StepExecuteContext stepExecuteContext) {
        super(stepExecuteContext);
    }

    /**
     * point功能暂时不支持了
     *
     * @param activity
     * @return
     */
    @Override
    public ActivityExecuteResult execute(Activity activity) {
        ActivityExecuteResult activityExecuteResult = new ActivityExecutor(activity, stepExecuteContext).execute();
        if (!activityExecuteResult.isSuccess()) {
            return activityExecuteResult;
        }
        return activityExecuteResult;
    }

}
