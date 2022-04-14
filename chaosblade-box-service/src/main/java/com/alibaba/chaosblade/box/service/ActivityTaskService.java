package com.alibaba.chaosblade.box.service;

import com.alibaba.chaosblade.box.common.common.domain.Response;
import com.alibaba.chaosblade.box.common.common.domain.activity.ActivityTask;
import com.alibaba.chaosblade.box.common.common.domain.task.ActivityTaskResultConfirmRequest;
import com.alibaba.chaosblade.box.common.common.domain.user.ChaosUser;
import com.alibaba.chaosblade.box.dao.infrastructure.experiment.request.ActivityRetryRequest;

/**
 * @author haibin
 *
 *
 */
public interface ActivityTaskService {

    /**
     * 查找activityTask
     *
     * @param activityTaskId 活动任务ID
     * @return
     */
    Response<ActivityTask> findActivityTaskByActivityTaskId(String activityTaskId);

    /**
     * 重试当前任务
     *
     * @param activityRetryRequest
     * @return
     */
    Response<String> retryActivity(ActivityRetryRequest activityRetryRequest);

    /**
     * 确认活动结果
     *
     * @param activityTaskResultConfirmRequest
     * @return
     */
    Response<Void> confirmActivityTaskResult(ChaosUser user,
                                             ActivityTaskResultConfirmRequest activityTaskResultConfirmRequest);
}
