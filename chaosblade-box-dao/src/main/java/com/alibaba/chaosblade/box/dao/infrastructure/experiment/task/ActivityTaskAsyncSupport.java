package com.alibaba.chaosblade.box.dao.infrastructure.experiment.task;


import com.alibaba.chaosblade.box.dao.model.ActivityTaskDO;

/**
 * @author haibin
 *
 *
 */

public interface ActivityTaskAsyncSupport {

    /**
     * activity是否是异步
     *
     * @return
     */
    public boolean isAsync(ActivityTaskDO activityTaskDO);

}
