package com.alibaba.chaosblade.box.service;

import com.alibaba.chaosblade.box.common.common.domain.Response;

/**
 * 压测监控服务接口
 *
 * @author ZhengMingZhuo
 */
public interface LoadTestMonitorService {

    /**
     * 监控所有运行中的压测任务
     * 定期检查压测任务状态，同步最新状态，处理异常情况
     *
     * @return 监控结果
     */
    Response<String> monitorRunningLoadTestTasks();

    /**
     * 监控指定用户的压测任务
     *
     * @param userId 用户ID
     * @param namespace 命名空间
     * @return 监控结果
     */
    Response<String> monitorUserLoadTestTasks(String userId, String namespace);

    /**
     * 处理超时的压测任务
     *
     * @return 处理结果
     */
    Response<String> handleTimeoutLoadTestTasks();

    /**
     * 定时监控压测任务状态
     * 每30秒执行一次
     */
    void scheduledMonitor();

    /**
     * 定时处理超时任务
     * 每5分钟执行一次
     */
    void scheduledTimeoutHandler();
}
