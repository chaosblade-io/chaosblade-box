package com.alibaba.chaosblade.box.dao.infrastructure.experiment.task.flow.step;

import com.alibaba.chaosblade.box.common.app.sdk.InvokeMode;
import com.alibaba.chaosblade.box.common.experiment.task.flow.ActivityTaskExecutionAttributes;
import com.alibaba.chaosblade.box.dao.infrastructure.experiment.task.flow.ExperimentTaskRunnableSettings;
import com.alibaba.chaosblade.box.dao.infrastructure.experiment.task.flow.step.activity.Activity;
import com.alibaba.chaosblade.box.dao.model.ActivityTaskDO;
import com.google.common.base.Strings;
import lombok.Data;

import java.util.HashMap;
import java.util.Map;

/**
 * @author haibin
 *
 *
 */
@Data
public class ActivityInvokeContext extends InvokeContext {

    private String userAk;

    private String userSk;

    private Activity activity;

    /**
     * 拒绝演练的机器列表
     * key是 deviceConfigurationId
     * value是原因
     */
    private Map<String, String> blockHosts = new HashMap<>();

    public String getAppCode() {
        return activity.getAppCode();
    }

    public String getExecutableAppCode() {
        return activity.getExecutableAppCode();
    }

    public ActivityTaskDO getActivityTask() {
        return getContextData().getActivityTaskDO();
    }

    public ExperimentTaskRunnableSettings getContextData() {
        return getStepExecuteContext().getRequest().getContextData();
    }

    public void addArgs(String key, String value) {
        if (Strings.isNullOrEmpty(key)) { return; }
        if (activity.getArguments() != null) {
            activity.getArguments().addArgs(key, value);
        }
    }

    public void removeArgs(String key) {
        if (activity.getArguments() != null) {
            activity.getArguments().removeArg(key);
        }
    }

    public Map<String, String> getAllArgs() {
        if (activity.getArguments() == null) { return new HashMap<>(); }
        return activity.getArguments().getAllArguments();
    }

    public void setInvokeMode(InvokeMode invokeMode) {
        if (invokeMode != null) {
            getTempData().add(ActivityTaskExecutionAttributes.ATTRIBUTE_INVOKE_MODE,
                    invokeMode);
        }
    }

    public void setAsync(boolean aysnc)
    {
        getTempData().add(ActivityTaskExecutionAttributes.ATTRIBUTE_ASYNC_MODE,aysnc);
    }

    public boolean isAsync()
    {
        return getTempData().getAsBoolean(ActivityTaskExecutionAttributes.ATTRIBUTE_ASYNC_MODE,false);
    }

    public InvokeMode getInvokeMode() {
        InvokeMode invokeMode = getTempData().getObject(ActivityTaskExecutionAttributes.ATTRIBUTE_INVOKE_MODE,
                InvokeMode.class);
        if (invokeMode == null) {
            invokeMode = InvokeMode.EACH;
        }
        return invokeMode;
    }

}
