package com.alibaba.chaosblade.box.dao.infrastructure.experiment.task.flow.step.activity;

import com.alibaba.chaosblade.box.common.app.sdk.scope.Host;
import com.alibaba.chaosblade.box.common.common.domain.definition.ExperimentActivityDefinition;
import com.alibaba.chaosblade.box.common.common.domain.definition.ExperimentNodeArgumentsDefinition;
import com.alibaba.chaosblade.box.common.experiment.task.flow.step.Step;
import com.alibaba.chaosblade.box.dao.model.ActivityTaskDO;
import lombok.Data;

import java.util.List;

/**
 * <p>
 * Activity represents the steps that can be performed,
 * There are many ways to implement activities，like PluginActivity,ComponentActivity and so on
 * </p>
 *
 * @author haibin
 *
 *
 */
@Data
public class Activity implements Step {

    private ActivityTaskDO activityTaskDO;

    private List<Host> scope;

    public Activity(String activityId, List<Host> scopes, ActivityTaskDO activityTaskDO, ExperimentActivityDefinition definition) {
        this.activityId = activityId;
        this.definition = definition;
        this.activityTaskDO = activityTaskDO;
        this.scope = scopes;
    }

    private String activityId;

    private ExperimentActivityDefinition definition;

    /**
     * 是应用的节点
     *
     * @return
     */
    public boolean isApplication() {
        return activityTaskDO.getAppId() != null;
    }

    @Override
    public String getId() {
        return this.definition.getActivityId();
    }

    @Override
    public StepType getStepType() {
        return StepType.ACTIVITY;
    }

    public String getAppCode() {
        return this.activityTaskDO.getAppCode();
    }

    public String getExecutableAppCode() {
        return this.activityTaskDO.getExecutableAppCode();
    }

    public Long getBefore() {
        return definition.getPauses() == null ? null : definition.getPauses().getBefore();
    }

    public Long getAfter() {
        return definition.getPauses() == null ? null : definition.getPauses().getAfter();
    }

    public ExperimentNodeArgumentsDefinition getArguments() {
        return this.definition.getArguments();
    }
}
