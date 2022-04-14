package com.alibaba.chaosblade.box.common.experiment.activity.cluster;

import com.alibaba.chaosblade.box.common.experiment.task.flow.step.activity.ActivityExecuteResult;
import lombok.Data;

/**
 * @author haibin
 *
 *
 */

@Data
public class ActivityTaskExecutionResponse {

    private boolean isExecuted;

    private ActivityExecuteResult activityExecuteResult;

    public ActivityTaskExecutionResponse() {
        this.activityExecuteResult = new ActivityExecuteResult();
    }

    public void setActivityExecuteResult(
        ActivityExecuteResult activityExecuteResult) {
        this.activityExecuteResult = activityExecuteResult;
        this.isExecuted = true;
    }
}
