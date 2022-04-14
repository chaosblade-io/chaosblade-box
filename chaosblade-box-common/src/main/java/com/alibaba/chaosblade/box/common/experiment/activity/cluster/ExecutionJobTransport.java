package com.alibaba.chaosblade.box.common.experiment.activity.cluster;


import com.alibaba.chaosblade.box.common.common.domain.Response;
import feign.Param;
import feign.RequestLine;

/**
 * @author haibin
 *
 *
 */
public interface ExecutionJobTransport {

    /**
     * kill taskId
     *
     * @param taskId
     * @return
     */
    @RequestLine("GET /inner/isActivityTaskRunning?taskId={taskId}")
    Response<Void> isActivityTaskRunning(
        @Param(value = "taskId") String taskId);

    @RequestLine("GET /inner/shutdownActivityTask?taskId={taskId}")
    Response<Void> shutdownTask(
        @Param(value = "taskId") String experimentTaskId);
}
