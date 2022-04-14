package com.alibaba.chaosblade.box.dao.infrastructure.experiment.task.flow.activity;

import com.alibaba.chaosblade.box.common.common.domain.user.ChaosUser;
import com.alibaba.chaosblade.box.common.experiment.activity.execute.Task;
import com.alibaba.chaosblade.box.common.experiment.task.flow.BaseRunRequest;
import com.alibaba.chaosblade.box.dao.infrastructure.experiment.task.flow.ExperimentTaskRunnableSettings;
import com.alibaba.chaosblade.box.dao.model.ActivityTaskDO;
import lombok.Getter;

import java.util.Objects;

/**
 * @author haibin
 *
 *
 */
@Getter
public class ExperimentExecuteContext extends BaseRunRequest implements Task {

    private String requestId;

    public ExperimentExecuteContext(String requestId, ChaosUser chaosUser,
                                    ActivityTaskDO activityTaskDO, ExperimentTaskRunnableSettings settings) {
        this.requestId = requestId;
        this.currentActivityTask = activityTaskDO;
        this.contextData = settings;
        settings.setActivityTaskDO(activityTaskDO);
        settings.setUser(chaosUser);
    }

    public String getActivityTaskId() {
        return currentActivityTask.getTaskId();
    }

    private ActivityTaskDO currentActivityTask;

    private ExperimentTaskRunnableSettings contextData;

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    @Override
    public boolean equals(Object o) {
        if (o == null) {
            return false;
        }
        if (o instanceof ExperimentExecuteContext) {
            ExperimentExecuteContext experimentExecuteContext = (ExperimentExecuteContext)o;
            return Objects.equals(experimentExecuteContext.getActivityTaskId(), this.getActivityTaskId());
        }
        return false;
    }

    @Override
    public Long getId() {
        return Long.parseLong(getActivityTaskId());
    }
}
