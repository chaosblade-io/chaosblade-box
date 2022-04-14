package com.alibaba.chaosblade.box.dao.infrastructure.experiment.interceptor;

import com.alibaba.chaosblade.box.dao.infrastructure.experiment.task.flow.ExperimentTaskRunnableSettings;
import com.alibaba.chaosblade.box.dao.model.ActivityTaskDO;
import com.alibaba.chaosblade.box.dao.model.ExperimentTaskDO;

/**
 * @author haibin.lhb
 *
 * 
 */
public interface ExperimentTaskPushInterceptor {

    /**
     * 是否忽略推进
     *
     * @param activityTaskDO
     * @param experimentTaskDO
     * @param experimentTaskRunnableSettings
     * @return
     */
    public boolean ignorePush(ActivityTaskDO activityTaskDO, ExperimentTaskDO experimentTaskDO,
                              ExperimentTaskRunnableSettings experimentTaskRunnableSettings);
}
