package com.alibaba.chaosblade.box.dao.infrastructure.experiment.request;

import com.alibaba.chaosblade.box.dao.infrastructure.experiment.task.flow.ExperimentTaskRunnableSettings;
import lombok.Getter;

/**
 * @author haibin
 *
 *
 */
@Getter
public class ExperimentTaskPushCommandRequest {

    private String activityTaskId;

    public ExperimentTaskPushCommandRequest(String activityTaskId, ExperimentTaskRunnableSettings contextData) {
        this.activityTaskId = activityTaskId;
        if (contextData == null) {
            contextData = new ExperimentTaskRunnableSettings();
        }
        this.contextData = contextData;
    }

    private ExperimentTaskRunnableSettings contextData;

}
