package com.alibaba.chaosblade.box.dao.infrastructure.experiment.interceptor;

import com.alibaba.chaosblade.box.common.experiment.activity.cluster.ActivityTaskExecutionResponse;
import com.alibaba.chaosblade.box.dao.infrastructure.experiment.task.flow.ExperimentTaskRunnableSettings;
import com.alibaba.chaosblade.box.dao.model.ActivityTaskDO;

/**
 * @author haibin.lhb
 *
 *
 */
public interface ActivityExecutionInterceptor {

    /**
     * 是否允许运行
     *
     * @param activityTaskDO
     * @param experimentTaskRunnableSettings
     * @return true代表禁止, false代表允许,默认允许
     */
    public boolean forbid(ActivityTaskDO activityTaskDO,
                          ExperimentTaskRunnableSettings experimentTaskRunnableSettings);

    /**
     * 开始运行时候
     *
     * @param activityTaskDO
     * @param experimentTaskRunnableSettings
     */
    public void onStarted(ActivityTaskDO activityTaskDO,
        ExperimentTaskRunnableSettings experimentTaskRunnableSettings);

    public void onReturn(ActivityTaskDO activityTaskDO,
        ActivityTaskExecutionResponse activityTaskExecutionResponse,
        ExperimentTaskRunnableSettings experimentTaskRunnableSettings);

    public void onError(ActivityTaskDO activityTaskDO, Throwable throwable,
        ExperimentTaskRunnableSettings experimentTaskRunnableSettings);
}
