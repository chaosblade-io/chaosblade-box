package com.alibaba.chaosblade.box.dao.infrastructure.experiment.task.interceptor;

import com.alibaba.chaosblade.box.common.common.util.MiniAppUtils;
import com.alibaba.chaosblade.box.common.experiment.task.flow.step.activity.ActivityExecuteResult;
import com.alibaba.chaosblade.box.dao.infrastructure.experiment.task.flow.step.ActivityInvokeContext;
import com.google.common.base.Strings;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * 网络丢包这个故障场景如果用户没有设置端口，为了防止无法恢复，需要将starAgent的端口去掉
 *
 * @author haibin
 *
 * 
 */
@Component
@ConditionalOnProperty(value = "chaos.network.exclude-port", matchIfMissing = false)
public class ChaosBladeNetworkExcludePortActivityInvokeInterceptor extends BaseActivityInvokeInterceptor {

    private static String REMOTE_PORT = "remote-port";

    private static String LOCAL_PORT = "local-port";

    private static String EXCLUDE_PORT = "exclude-port";

    private static String WIN_REMOTE_PORT = "src-port";

    private static String WIN_LOCAL_PORT = "dst-port";

    private static String WIN_EXCLUDE_DST_PORT = "exclude-dst-port";
    private static String WIN_EXCLUDE_SRC_PORT = "exclude-src-port";

    @Value("${chaos.network.exclude-port}")
    private String excludePort;

    @Override
    public boolean preHandle(ActivityInvokeContext activityInvokeContext, ActivityExecuteResult activityExecuteResult) {
        String appCode = activityInvokeContext.getExecutableAppCode();
        if (isChaosBladeNetwork(appCode)) {
            Map<String, String> args = activityInvokeContext.getActivity().getDefinition().getArguments()
                .getAllArguments();
            if(MiniAppUtils.isWin(appCode)) {
                if (Strings.isNullOrEmpty(args.get(WIN_REMOTE_PORT)) && Strings.isNullOrEmpty(args.get(WIN_LOCAL_PORT))) {
                    String port = excludePort;
                    String userPort = activityInvokeContext.getAllArgs()
                            .get(WIN_EXCLUDE_SRC_PORT);
                    if (!Strings.isNullOrEmpty(userPort)) {
                        port = port + "," + userPort;
                        activityInvokeContext.getActivity().getDefinition().getArguments().getAction().remove(EXCLUDE_PORT);
                    }
                    activityInvokeContext.addArgs(WIN_EXCLUDE_SRC_PORT,
                            port);
                }
                return true;
            }
            if (Strings.isNullOrEmpty(args.get(REMOTE_PORT)) && Strings.isNullOrEmpty(args.get(LOCAL_PORT))) {
                String port = excludePort;
                String userPort = activityInvokeContext.getAllArgs()
                    .get(EXCLUDE_PORT);
                if (!Strings.isNullOrEmpty(userPort)) {
                    port = port + "," + userPort;
                    activityInvokeContext.getActivity().getDefinition().getArguments().getAction().remove(EXCLUDE_PORT);
                }
                activityInvokeContext.addArgs(EXCLUDE_PORT,
                    port);
            }
        }
        return true;
    }

    private boolean isChaosBladeNetwork(String appCode) {
        return appCode.endsWith("network.delay") || appCode.endsWith("network.loss");
    }

    @Override
    public void afterHandle(ActivityInvokeContext activityInvokeContext, ActivityExecuteResult activityExecuteResult,
        Throwable throwable) {

    }

}
