package com.alibaba.chaosblade.box.dao.infrastructure.experiment.task.interceptor;

import com.alibaba.chaosblade.box.common.common.domain.definition.ExperimentActivityDefinition;
import com.alibaba.chaosblade.box.common.common.domain.definition.ExperimentNodeArgumentsDefinition;
import com.alibaba.chaosblade.box.common.experiment.task.flow.step.activity.ActivityExecuteResult;
import com.alibaba.chaosblade.box.common.experiment.task.flow.util.Pair;
import com.alibaba.chaosblade.box.dao.infrastructure.experiment.task.flow.step.ActivityInvokeContext;
import com.alibaba.chaosblade.box.dao.infrastructure.monitor.metric.MetricUtil;
import com.alibaba.chaosblade.box.dao.model.ExperimentTaskDO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * metric特殊处理，填充开始和结束结束时间
 *
 * @author haibin
 *
 * 
 */
@Component
@Slf4j
public class ActivityMetricInvokeInterceptor extends BaseActivityInvokeInterceptor {

    public static String METRIC_APP_PREFIX = "chaosapp.metric";

    @Override
    public boolean preHandle(ActivityInvokeContext activityInvokeContext, ActivityExecuteResult activityExecuteResult) {
        if (activityInvokeContext.getAppCode().startsWith(METRIC_APP_PREFIX)) {
            ExperimentActivityDefinition experimentActivityDefinition = activityInvokeContext.getActivity()
                .getDefinition();
            ExperimentNodeArgumentsDefinition experimentNodeArgumentsDefinition = experimentActivityDefinition
                .getArguments();
            if (experimentNodeArgumentsDefinition == null) {
                experimentNodeArgumentsDefinition = new ExperimentNodeArgumentsDefinition();
                experimentActivityDefinition.setArguments(experimentNodeArgumentsDefinition);
            }
            ExperimentTaskDO experimentTaskDO = activityInvokeContext.getContextData().getExperimentTaskDO();
            Map<String, String> userArgs = experimentNodeArgumentsDefinition.getUserArgs();
            if (userArgs == null) {
                userArgs = new HashMap<>();
                experimentNodeArgumentsDefinition.setUserArgs(userArgs);
            }
            Pair<String, String> timeRange = MetricUtil.getMetricDateRangeInTimeStamp(experimentTaskDO);
            userArgs.put("from", timeRange.getLeft());
            userArgs.put("to", timeRange.getRight());
        }
        return true;
    }

    @Override
    public void afterHandle(ActivityInvokeContext activityInvokeContext, ActivityExecuteResult activityExecuteResult,
        Throwable throwable) {

    }

    @Override
    public Integer getOrder() {
        return Integer.MAX_VALUE - 1;
    }
}
