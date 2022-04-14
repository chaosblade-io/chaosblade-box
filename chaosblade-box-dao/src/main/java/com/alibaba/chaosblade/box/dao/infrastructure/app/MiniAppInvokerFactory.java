package com.alibaba.chaosblade.box.dao.infrastructure.app;

import com.alibaba.chaosblade.box.common.app.sdk.scope.Host;
import com.alibaba.chaosblade.box.dao.infrastructure.experiment.task.flow.step.ActivityInvokeContext;

/**
 * @author haibin
 *
 *
 */
public class MiniAppInvokerFactory {

    public static MiniAppExecutor createInvoker(ActivityInvokeContext activityInvokeContext, Host host) {
        return new MiniAppExecutor(activityInvokeContext, host);
    }
}
