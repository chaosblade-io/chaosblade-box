package com.alibaba.chaosblade.box.dao.infrastructure.experiment.interceptor.impl;

import com.alibaba.chaosblade.box.common.experiment.task.flow.step.activity.ActivityExecuteResult;
import com.alibaba.chaosblade.box.dao.infrastructure.experiment.interceptor.ActivityTaskExecutionFinishedInterceptor;
import com.alibaba.chaosblade.box.dao.infrastructure.experiment.task.flow.ExperimentTaskRunnableSettings;
import com.alibaba.chaosblade.box.dao.model.ActivityTaskDO;
import org.springframework.stereotype.Component;

/**
 * @author haibin.lhb
 *
 *
 */
@Component
public class MetricActivityTaskExecutionFinishedInterceptor implements ActivityTaskExecutionFinishedInterceptor {
    @Override
    public boolean ignoreFinished(ActivityTaskDO activityTaskDO, ActivityExecuteResult activityExecuteResult,
                                  ExperimentTaskRunnableSettings experimentTaskRunnableSettings) {
        return experimentTaskRunnableSettings.isMetricReload();
    }
}
