package com.alibaba.chaosblade.box.dao.infrastructure.experiment.task.flow;

import com.alibaba.chaosblade.box.common.app.sdk.constants.PhaseType;
import com.alibaba.chaosblade.box.common.common.domain.definition.ExperimentActivityDefinition;
import com.alibaba.chaosblade.box.common.common.domain.user.ChaosUser;
import com.alibaba.chaosblade.box.common.experiment.task.ExperimentTaskContext;
import com.alibaba.chaosblade.box.common.experiment.task.flow.ActivityTaskExecutionAttributes;
import com.alibaba.chaosblade.box.common.experiment.task.flow.util.HashMapSettings;
import com.alibaba.chaosblade.box.dao.model.ActivityTaskDO;
import com.alibaba.chaosblade.box.dao.model.ExperimentActivityDO;
import com.alibaba.chaosblade.box.dao.model.ExperimentDO;
import com.alibaba.chaosblade.box.dao.model.ExperimentTaskDO;
import com.google.common.base.Strings;

import java.util.Map;
import java.util.Optional;

/**
 * @author haibin
 *
 *
 */
public class ExperimentTaskRunnableSettings extends HashMapSettings {

    public void setUser(ChaosUser chaosUser) {
        add(ActivityTaskExecutionAttributes.ATTRIBUTE_KEY_USER, chaosUser);
    }

    public String getSubUserId() {
        ChaosUser chaosUser = getUser();
        if (chaosUser == null) {
            return getAsString(ActivityTaskExecutionAttributes.ATTRIBUTE_KEY_SUB_USER_ID);
        } else {
            return chaosUser.getCurrentUserId();
        }
    }

    public String getUserId() {
        ChaosUser chaosUser = getUser();
        if (chaosUser == null) {
            return getAsString(ActivityTaskExecutionAttributes.ATTRIBUTE_KEY_USER_ID);
        } else {
            if (Strings.isNullOrEmpty(chaosUser.getUserId())) {
                return getAsString(ActivityTaskExecutionAttributes.ATTRIBUTE_KEY_USER_ID);
            }
            return chaosUser.getUserId();
        }
    }

    public Map<String, Object> getMiniAppContext() {
        return getObject(ActivityTaskExecutionAttributes.ATTRIBUTE_KEY_MINI_APP_CONTEXT, Map.class);
    }

    public void setMiniAppContext(Map<String, Object> miniAppContext) {
        add(ActivityTaskExecutionAttributes.ATTRIBUTE_KEY_MINI_APP_CONTEXT, miniAppContext);
    }

    public ChaosUser getUser() {
        return getObject(ActivityTaskExecutionAttributes.ATTRIBUTE_KEY_USER, ChaosUser.class);
    }

    public String getExperimentTaskId() {
        return getExperimentTaskDO().getTaskId();
    }

    public ActivityTaskDO getActivityTaskDO() {
        return getObject(ActivityTaskExecutionAttributes.ATTRIBUTE_KEY_ACTIVITY_TASK_DO, ActivityTaskDO.class);
    }

    public void setActivityTaskDO(ActivityTaskDO activityTaskDO) {
        add(ActivityTaskExecutionAttributes.ATTRIBUTE_KEY_ACTIVITY_TASK_DO, activityTaskDO);
    }

    public String getRequestId() {
        return getAsString(ActivityTaskExecutionAttributes.ATTRIBUTE_REQUEST_ID);
    }

    public boolean isMockMode() {
        return Optional.ofNullable(getExperimentTaskDO().getExperimentTaskContext()).map(
            ExperimentTaskContext::isMockMode).orElse(false);
    }

    public boolean isMiniAppBatchRunning() {
        return ActivityTaskExecutionAttributes.ATTRIBUTE_VALUE_ACTIVITY_RUNNABLE_STRATEGY_BATCH
            .equals(getAsString(ActivityTaskExecutionAttributes.ATTRIBUTE_KEY_ACTIVITY_RUNNABLE_STRATEGY));
    }

    public int getMiniAppBatchCount() {
        return getAsInteger(ActivityTaskExecutionAttributes.ATTRIBUTE_KEY_ACTIVITY_RUNNABLE_BATCH_SIZE, 0);
    }

    public void enableMiniAppBatchRunning() {
        add(ActivityTaskExecutionAttributes.ATTRIBUTE_KEY_ACTIVITY_RUNNABLE_STRATEGY,
            ActivityTaskExecutionAttributes.ATTRIBUTE_VALUE_ACTIVITY_RUNNABLE_STRATEGY_BATCH);
    }

    public void setRequestId(String requestId) {
        add(ActivityTaskExecutionAttributes.ATTRIBUTE_REQUEST_ID, requestId);
    }

    public void setUserId(String userId) {
        add(ActivityTaskExecutionAttributes.ATTRIBUTE_KEY_USER_ID, userId);
    }

    public boolean isRetrying() {
        return getAsBoolean(ActivityTaskExecutionAttributes.ATTRIBUTE_KEY_RETRY, false);
    }

    public void setRetrying() {
        add(ActivityTaskExecutionAttributes.ATTRIBUTE_KEY_RETRY, true);
    }

    public boolean isExperimentTaskPushable() {
        return getAsBoolean(ActivityTaskExecutionAttributes.ATTRIBUTE_KEY_PUSH_EXPERIMENT_TASK, true);
    }

    public void setExperimentTaskPushableStatus(boolean pushable) {
        add(ActivityTaskExecutionAttributes.ATTRIBUTE_KEY_PUSH_EXPERIMENT_TASK, pushable);
    }

    public ExperimentActivityDO getExperimentActivityDO() {
        return getObject(ActivityTaskExecutionAttributes.ATTRIBUTE_KEY_EXPERIMENT_ACTIVITY, ExperimentActivityDO.class);
    }

    public void setExperimentDO(ExperimentDO experimentDO) {
        add(ActivityTaskExecutionAttributes.ATTRIBUTE_KEY_EXPERIMENT, experimentDO);
    }

    public ExperimentDO getExperimentDO() {
        return getObject(ActivityTaskExecutionAttributes.ATTRIBUTE_KEY_EXPERIMENT, ExperimentDO.class);
    }

    public ExperimentActivityDefinition getExperimentActivityDefinition() {
        return getObject(ActivityTaskExecutionAttributes.ATTRIBUTE_KEY_EXPERIMENT_ACTIVITY_DEFINITION, ExperimentActivityDefinition.class);
    }

    public void setExperimentActivityDefinition(ExperimentActivityDefinition experimentActivityDefinition) {
        add(ActivityTaskExecutionAttributes.ATTRIBUTE_KEY_EXPERIMENT_ACTIVITY_DEFINITION, experimentActivityDefinition);
    }

    public void setExperimentActivityDO(ExperimentActivityDO experimentActivityDO) {
        add(ActivityTaskExecutionAttributes.ATTRIBUTE_KEY_EXPERIMENT_ACTIVITY, experimentActivityDO);
    }

    public void setPhase(PhaseType phase) {
        add(ActivityTaskExecutionAttributes.ATTRIBUTE_KEY_PHASE, phase);
    }

    public PhaseType getPhase() {
        return getObject(ActivityTaskExecutionAttributes.ATTRIBUTE_KEY_PHASE, PhaseType.class);
    }

    public ExperimentTaskDO getExperimentTaskDO() {
        return getObject(ActivityTaskExecutionAttributes.ATTRIBUTE_KEY_EXPERIMENT_TASK, ExperimentTaskDO.class);
    }

    public void setExperimentTaskDO(ExperimentTaskDO experimentTaskDO) {
        add(ActivityTaskExecutionAttributes.ATTRIBUTE_KEY_EXPERIMENT_TASK, experimentTaskDO);
    }

    public String getNamespace() {
        return getAsString(ActivityTaskExecutionAttributes.ATTRIBUTE_KEY_NAMESPACE);
    }

    public void setNamespace(String namespace) {
        add(ActivityTaskExecutionAttributes.ATTRIBUTE_KEY_NAMESPACE, namespace);
    }

    public boolean isMetricReload() {
        return getAsBoolean(ActivityTaskExecutionAttributes.ATTRIBUTE_KEY_METRIC_RELOAD, false);
    }

    public void setMetricReload(boolean isMetricReload) {
        add(ActivityTaskExecutionAttributes.ATTRIBUTE_KEY_METRIC_RELOAD, isMetricReload);
    }

    public boolean supportRepetitionExecution() {
        return getAsBoolean(ActivityTaskExecutionAttributes.ATTRIBUTE_ACTIVITY_SUPPORT_REPERTITION_EXECUTION, false);
    }

    public void setRepetitionExecution(boolean isRepetitionExecution) {
        add(ActivityTaskExecutionAttributes.ATTRIBUTE_ACTIVITY_SUPPORT_REPERTITION_EXECUTION, isRepetitionExecution);
    }

    public String getExperimentActivityId() {
        return getAsString(ActivityTaskExecutionAttributes.ATTRIBUTE_KEY_EXPERIMENT_ACTIVITY_ID);
    }

    public void setExperimentActivityId(String
        activityId) {
        add(ActivityTaskExecutionAttributes.ATTRIBUTE_KEY_EXPERIMENT_ACTIVITY_ID, activityId);
    }

    public void setInterruptedExperimentTaskNow() {
        add(ActivityTaskExecutionAttributes.ATTRIBUTE_KEY_INTERRUPTED_EXPERIMENT_TASK_NOW, true);
    }

    public boolean isInterruptedExperimentTaskNow() {
        return getAsBoolean(ActivityTaskExecutionAttributes.ATTRIBUTE_KEY_INTERRUPTED_EXPERIMENT_TASK_NOW, false);
    }
}
