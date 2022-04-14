package com.alibaba.chaosblade.box.dao.infrastructure.experiment.interceptor.impl;

import com.alibaba.chaosblade.box.common.experiment.activity.cluster.ActivityTaskExecutionResponse;
import com.alibaba.chaosblade.box.common.experiment.task.flow.util.concurrent.InvokeTracer;
import com.alibaba.chaosblade.box.common.infrastructure.constant.MDCConstant;
import com.alibaba.chaosblade.box.dao.infrastructure.experiment.interceptor.ActivityExecutionInterceptor;
import com.alibaba.chaosblade.box.dao.infrastructure.experiment.task.flow.ExperimentTaskRunnableSettings;
import com.alibaba.chaosblade.box.dao.model.ActivityTaskDO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

/**
 * @author haibin.lhb
 *
 *
 */
@Order(Ordered.HIGHEST_PRECEDENCE)
@Component
public class LogTraceActivityExecutionInterceptor implements ActivityExecutionInterceptor {

    @Autowired
    private InvokeTracer invokeTracer;

    @Override
    public boolean forbid(ActivityTaskDO activityTaskDO,
                          ExperimentTaskRunnableSettings experimentTaskRunnableSettings) {
        return false;
    }

    @Override
    public void onStarted(ActivityTaskDO activityTaskDO,
        ExperimentTaskRunnableSettings experimentTaskRunnableSettings) {
        invokeTracer.addTrace(MDCConstant.MDC_ACTIVITY_TASK_ID,
            activityTaskDO.getTaskId());
        invokeTracer.addTrace(MDCConstant.MDC_KEY_REQUEST_ID, experimentTaskRunnableSettings.getRequestId());
        invokeTracer.addTrace(MDCConstant.MDC_KEY_REQUEST_USER_ID,
            experimentTaskRunnableSettings.getUserId());
    }

    @Override
    public void onReturn(ActivityTaskDO activityTaskDomainWrapper,
        ActivityTaskExecutionResponse activityTaskExecutionResponse,
        ExperimentTaskRunnableSettings experimentTaskRunnableSettings) {
        invokeTracer.clear();
    }

    @Override
    public void onError(ActivityTaskDO activityTaskDomainWrapper, Throwable throwable,
        ExperimentTaskRunnableSettings experimentTaskRunnableSettings) {
        invokeTracer.clear();
    }
}
