package com.alibaba.chaosblade.box.dao.infrastructure.experiment.task.flow.step;

import com.alibaba.chaosblade.box.common.app.sdk.ChaosAppRequest;
import com.alibaba.chaosblade.box.common.app.sdk.scope.Host;
import com.alibaba.chaosblade.box.dao.infrastructure.experiment.task.flow.invoker.ChaosBladeAppInvoker;
import com.alibaba.chaosblade.box.dao.model.ExperimentMiniAppTaskDO;
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
public class MiniAppInvokeContext extends InvokeContext {

    private Host host;

    private String miniAppTaskId;

    private ActivityInvokeContext activityInvokeContext;

    private ChaosAppRequest chaosAppRequest;

    private ExperimentMiniAppTaskDO miniAppTaskDO;

    public void addArgs(String key, String value) {
        if (Strings.isNullOrEmpty(key)) { return; }
        chaosAppRequest.getAction().put(key, value);
        chaosAppRequest.getUserArgs().put(key, value);
    }

    public Map<String, String> getAllArgs() {
        Map<String, String> allArgs = new HashMap<>(chaosAppRequest.getAction());
        allArgs.putAll(chaosAppRequest.getUserArgs());
        return allArgs;
    }

    public String getArgs(String key) {
        return getAllArgs().get(key);
    }

    public void removeArgs(String key) {
        chaosAppRequest.getAction().remove(key);
        chaosAppRequest.getUserArgs().remove(key);
    }


    public String getActivityTaskId()
    {
        return getStepExecuteContext().getRequest()
                .getActivityTaskId();
    }

    public String getChaosBladeExpId()
    {
        return  getTempData().getAsString(
                ChaosBladeAppInvoker.CHAOS_BLADE_EXP_ID);
    }

    public String getAppCode() {
        return activityInvokeContext.getExecutableAppCode();
    }

}
