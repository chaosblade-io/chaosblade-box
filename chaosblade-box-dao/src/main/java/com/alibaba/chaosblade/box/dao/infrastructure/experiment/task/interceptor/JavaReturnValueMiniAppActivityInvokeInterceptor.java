package com.alibaba.chaosblade.box.dao.infrastructure.experiment.task.interceptor;

import com.alibaba.chaosblade.box.common.experiment.task.flow.step.activity.ActivityExecuteResult;
import com.alibaba.chaosblade.box.dao.infrastructure.experiment.task.flow.step.ActivityInvokeContext;
import com.alibaba.chaosblade.box.dao.infrastructure.experiment.task.flow.step.activity.Activity;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * @author haibin
 *
 *
 */
@Component
public class JavaReturnValueMiniAppActivityInvokeInterceptor extends BaseActivityInvokeInterceptor {

    private String JAVA_RETURN_VALUE = "chaos.jvm.return";

    private String FALSE = "false";

    @Override
    public boolean preHandle(ActivityInvokeContext activityInvokeContext, ActivityExecuteResult activityExecuteResult) {
        String appCode = activityInvokeContext.getExecutableAppCode();
        if (JAVA_RETURN_VALUE.equals(appCode)) {
            Activity activity = activityInvokeContext.getActivity();
            Map<String, String> actions = activity.getDefinition().getArguments().getAction();
            String value = actions.get("value");
            if (FALSE.equals(value)) {
                actions.put("value", "0");
            }
        }
        return true;
    }

    @Override
    public void afterHandle(ActivityInvokeContext activityInvokeContext, ActivityExecuteResult activityExecuteResult,
        Throwable throwable) {

    }
}
