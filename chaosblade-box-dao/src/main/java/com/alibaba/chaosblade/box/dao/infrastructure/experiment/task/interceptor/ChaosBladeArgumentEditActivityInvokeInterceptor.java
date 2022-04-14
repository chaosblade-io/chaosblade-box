package com.alibaba.chaosblade.box.dao.infrastructure.experiment.task.interceptor;

import com.alibaba.chaosblade.box.common.app.sdk.constants.PhaseType;
import com.alibaba.chaosblade.box.common.common.domain.definition.ExperimentNodeArgumentsDefinition;
import com.alibaba.chaosblade.box.common.common.util.MiniAppUtils;
import com.alibaba.chaosblade.box.common.experiment.task.flow.step.activity.ActivityExecuteResult;
import com.alibaba.chaosblade.box.common.infrastructure.util.CollectionUtil;
import com.alibaba.chaosblade.box.dao.infrastructure.experiment.task.flow.step.ActivityInvokeContext;
import com.google.common.base.Strings;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * 用来预处理chaosblade相关的参数
 *
 * @author haibin
 *
 *
 */
@Component
@Slf4j
public class ChaosBladeArgumentEditActivityInvokeInterceptor extends BaseActivityInvokeInterceptor {

    private static String HSF = "chaos.hsf";

    private static String APP_NAME = "appname";

    @Override
    public boolean preHandle(ActivityInvokeContext activityInvokeContext, ActivityExecuteResult activityExecuteResult) {
        String appCode = activityInvokeContext.getExecutableAppCode();
        if (MiniAppUtils.isFromChaosBlade(appCode) && PhaseType.ATTACK.equals(
            activityInvokeContext.getContextData().getPhase())) {
            if (CollectionUtil.isNullOrEmpty(activityInvokeContext.getActivity().getScope())) {
                return true;
            }
            handleHsfScene(activityInvokeContext);
            handleMybatisWhereRule(activityInvokeContext);
        }
        return true;
    }

    private void handleHsfScene(ActivityInvokeContext activityInvokeContext) {
        if (activityInvokeContext.getExecutableAppCode().startsWith(HSF) && !MiniAppUtils.isChaosRecovery(
            activityInvokeContext.getExecutableAppCode())) {
            ExperimentNodeArgumentsDefinition experimentNodeArgumentsDefinition = activityInvokeContext.getActivity()
                .getDefinition().getArguments();
            mapRemoveIf(experimentNodeArgumentsDefinition.getAction(), APP_NAME);
            mapRemoveIf(experimentNodeArgumentsDefinition.getMatcher(), APP_NAME);
        }
    }

    private void handleMybatisWhereRule(ActivityInvokeContext activityInvokeContext) {
        ExperimentNodeArgumentsDefinition experimentNodeArgumentsDefinition = activityInvokeContext.getActivity()
            .getDefinition().getArguments();
        mapRemoveBlank(experimentNodeArgumentsDefinition.getAction());
        mapRemoveBlank(experimentNodeArgumentsDefinition.getMatcher());
    }

    private void mapRemoveIf(Map<String, String> map, String key) {
        if (map == null) { return; }
        map.remove(key);
    }

    @Override
    public void afterHandle(ActivityInvokeContext activityInvokeContext, ActivityExecuteResult activityExecuteResult,
        Throwable throwable) {

    }

    @Override
    public Integer getOrder() {
        return Integer.MAX_VALUE - 3;
    }

    private void mapRemoveBlank(Map<String, String> map) {
        if (map == null) { return; }
        String value = map.get("where_rule");
        if (!Strings.isNullOrEmpty(value)) {
            map.put("where_rule", value.replaceAll(" ", "").replaceAll("\n", ""));
        }
    }
}
