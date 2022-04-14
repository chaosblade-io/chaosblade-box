package com.alibaba.chaosblade.box.dao.infrastructure.experiment.interceptor;

import com.alibaba.chaosblade.box.common.experiment.task.flow.step.activity.ActivityExecuteResult;
import com.alibaba.chaosblade.box.dao.infrastructure.experiment.task.flow.ExperimentTaskRunnableSettings;
import com.alibaba.chaosblade.box.dao.model.ActivityTaskDO;

/**
 * @author haibin.lhb
 *
 * 
 */
public interface ActivityTaskExecutionFinishedInterceptor {

    /**
     * 是否跳过演练节点任务完结的处理流程
     *
     * @param activityTaskDO
     * @param activityExecuteResult
     * @param experimentTaskRunnableSettings
     * @return true 忽略,false，继续
     */
    public boolean ignoreFinished(ActivityTaskDO activityTaskDO, ActivityExecuteResult activityExecuteResult,
                                  ExperimentTaskRunnableSettings experimentTaskRunnableSettings);
}
