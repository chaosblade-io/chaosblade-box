package com.alibaba.chaosblade.box.dao.infrastructure.experiment.task;


import com.alibaba.chaosblade.box.dao.model.ActivityTaskDO;
import com.alibaba.chaosblade.box.dao.model.ExperimentTaskDO;
import lombok.Data;

/**
 * @author haibin
 *
 *
 */
@Data
public class ActivityTaskQueryRequest {

    public ActivityTaskQueryRequest(ActivityTaskDO activityTaskDO) {
        this.activityTaskDO = activityTaskDO;
    }

    private ActivityTaskDO activityTaskDO;

    public ActivityTaskQueryRequest(String activityTaskId) {
        this.activityTaskId = activityTaskId;
    }

    private String activityTaskId;

    /**
     * 是否返回小程序的执行信息，表名每台机器的执行信息
     */
    private boolean isReturnMiniAppTasks = true;

    private ExperimentTaskDO experimentTaskDO;
}
