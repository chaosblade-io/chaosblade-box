package com.alibaba.chaosblade.box.dao.infrastructure.experiment.task;

import com.alibaba.chaosblade.box.common.common.domain.Response;
import com.alibaba.chaosblade.box.common.common.domain.activity.ActivityTask;
import com.alibaba.chaosblade.box.dao.model.ActivityTaskDO;

import java.util.List;

/**
 * @author haibin
 * 
 * 
 */
public interface ActivityTaskManager {

    /**
     * 查询活动任务
     *
     * @param activityTaskId 活动任务ID
     * @return
     */
    public Response<ActivityTask> queryFullActivityTaskInfo(String activityTaskId);

    /**
     * 查询节点任务概况
     *
     * @param activityTaskDO
     * @return
     */
    public Response<ActivityTask> querySimpleActivityTaskInfo(ActivityTaskDO activityTaskDO);

    /**
     * 放弃activity任务
     *
     * @param activityTaskDO 活动任务ID
     * @param rejectReason   放弃的理由
     */
    public void rejectActivityTask(ActivityTaskDO activityTaskDO, String rejectReason);

    /**
     * 放弃演练的所有任务
     *
     * @param experimentTaskId 实验任务ID
     * @param rejectReason     放弃的理由
     * @return 放弃的活动任务数目
     */
    public long rejectActivityTasksByExperimentTaskId(String experimentTaskId, String rejectReason);

    /**
     * 创建节点任务
     *
     * @param activityTasksCreateRequest
     * @return
     */
    public List<ActivityTaskDO> createActivityTasks(ActivityTasksCreateRequest activityTasksCreateRequest);

}
