package com.alibaba.chaosblade.box.dao.scheduler;

import com.alibaba.chaosblade.box.dao.scheduler.domain.SchedulerJobCreateRequest;
import com.alibaba.chaosblade.box.dao.scheduler.domain.SchedulerJobUpdateRequest;

import java.util.Map;

/**
 * @author haibin
 *
 *
 */
public interface SchedulerJobService {

    /**
     * 增加一次定时任务
     */
    public String addSchedulerJob(SchedulerJobCreateRequest schedulerJobCreateRequest);

    /**
     * 启动一次定时任务
     *
     * @return
     */
    public boolean enableSchedulerJob(String jobId);

    /**
     * 停止定时任务
     *
     * @param jobId
     * @return
     */
    public String disableSchedulerJob(String jobId);

    /**
     * 根据业务ID来停止定时任务
     *
     * @param businessType 业务类型
     * @param businessId   业务ID
     * @return jobId is exist
     */
    public String disableSchedulerJobByBusinessId(Integer businessType, String businessId);

    boolean triggerSchedulerJob(String taskId, Map<String, String> params);

    /**
     * 删除schedulerTaskid
     *
     * @param jobId
     * @return
     */
    public boolean deleteSchedulerJob(String jobId);

    /**
     * 更新定时任务
     *
     * @param schedulerJobUpdateRequest
     * @return
     */
    public boolean updateSchedulerJob(SchedulerJobUpdateRequest schedulerJobUpdateRequest);

    String stopSchedulerJob(String jobId);
}
