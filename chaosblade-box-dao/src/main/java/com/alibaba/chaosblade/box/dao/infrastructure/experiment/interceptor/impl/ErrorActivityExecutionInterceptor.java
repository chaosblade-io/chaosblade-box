package com.alibaba.chaosblade.box.dao.infrastructure.experiment.interceptor.impl;

import com.alibaba.chaosblade.box.common.common.enums.CommonErrorCode;
import com.alibaba.chaosblade.box.common.common.enums.ResultEnum;
import com.alibaba.chaosblade.box.common.experiment.activity.cluster.ActivityTaskExecutionResponse;
import com.alibaba.chaosblade.box.common.infrastructure.error.ThrowableChaosErrorWrappers;
import com.alibaba.chaosblade.box.dao.infrastructure.experiment.interceptor.ActivityExecutionInterceptor;
import com.alibaba.chaosblade.box.dao.infrastructure.experiment.task.ActivityTaskTerminator;
import com.alibaba.chaosblade.box.dao.infrastructure.experiment.task.flow.ExperimentTaskRunnableSettings;
import com.alibaba.chaosblade.box.dao.model.ActivityTaskDO;
import com.alibaba.chaosblade.box.dao.repository.ActivityTaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author haibin.lhb
 *
 * 
 */
@Component
public class ErrorActivityExecutionInterceptor implements ActivityExecutionInterceptor {
    @Autowired
    private ActivityTaskRepository activityTaskRepository;

    @Autowired
    private ThrowableChaosErrorWrappers throwableChaosErrorWrappers;

    @Autowired
    private ActivityTaskTerminator activityTaskTerminator;

    @Override
    public boolean forbid(ActivityTaskDO activityTaskDO,
                          ExperimentTaskRunnableSettings experimentTaskRunnableSettings) {
        return false;
    }

    @Override
    public void onStarted(ActivityTaskDO activityTaskDO,
        ExperimentTaskRunnableSettings experimentTaskRunnableSettings) {

    }

    @Override
    public void onReturn(ActivityTaskDO activityTaskDO, ActivityTaskExecutionResponse activityTaskExecutionResponse,
        ExperimentTaskRunnableSettings experimentTaskRunnableSettings) {

    }

    @Override
    public void onError(ActivityTaskDO activityTaskDO, Throwable throwable,
        ExperimentTaskRunnableSettings experimentTaskRunnableSettings) {
        if (!activityTaskDO.isFinished()) {
            activityTaskDO.setResult(ResultEnum.ERROR.getValue());
            activityTaskDO.setErrorMessage(
                throwableChaosErrorWrappers.wrapper(throwable, CommonErrorCode.S_ACTIVITY_EXECUTE_FAILED)
                    .getErrorMessage());
            activityTaskTerminator.handleActivityTaskAfterTerminate(activityTaskDO);
        }
    }

}
