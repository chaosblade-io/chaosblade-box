package com.alibaba.chaosblade.box.dao.infrastructure.experiment.interceptor.impl;

import com.alibaba.chaosblade.box.common.app.sdk.ChaosAppResponse;
import com.alibaba.chaosblade.box.dao.infrastructure.experiment.interceptor.ActivityTaskExecutionFinishedInterceptor;
import com.alibaba.chaosblade.box.dao.infrastructure.experiment.task.ActivityTaskAsyncSupport;
import com.alibaba.chaosblade.box.common.experiment.task.flow.step.activity.ActivityExecuteResult;
import com.alibaba.chaosblade.box.dao.infrastructure.experiment.task.flow.ExperimentTaskRunnableSettings;
import com.alibaba.chaosblade.box.dao.model.ActivityTaskDO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author haibin.lhb
 *
 *
 */
@Component
public class AsyncActivityTaskExecutionFinishedInterceptor implements ActivityTaskExecutionFinishedInterceptor {
    @Autowired
    private ActivityTaskAsyncSupport activityTaskAsyncSupport;

    @Override
    public boolean ignoreFinished(ActivityTaskDO activityTaskDO, ActivityExecuteResult activityExecuteResult,
                                  ExperimentTaskRunnableSettings experimentTaskRunnableSettings) {
        return isNeedAsyncWait(activityTaskDO, activityExecuteResult);
    }

    private boolean isNeedAsyncWait(ActivityTaskDO activityTaskDO, ActivityExecuteResult activityExecuteResult) {
        return activityExecuteResult.getAppResponses().stream().anyMatch(ChaosAppResponse::isSuccess)
            && activityTaskAsyncSupport.isAsync(activityTaskDO);
    }

}
