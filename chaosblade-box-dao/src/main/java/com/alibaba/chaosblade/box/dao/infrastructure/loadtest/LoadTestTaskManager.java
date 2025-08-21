package com.alibaba.chaosblade.box.dao.infrastructure.loadtest;

import com.alibaba.chaosblade.box.common.common.domain.Response;
import com.alibaba.chaosblade.box.dao.model.LoadTestTaskDO;
import com.alibaba.chaosblade.box.dao.infrastructure.loadtest.model.*;

/**
 * 压测任务管理器接口
 *
 * @author ZhengMingZhuo
 */
public interface LoadTestTaskManager {

    /**
     * 创建并启动压测任务
     *
     * @param strategyId 压测策略ID
     * @param experimentTaskId 演练任务ID
     * @param userId 用户ID
     * @param namespace 命名空间
     * @return 压测任务
     */
    Response<LoadTestTaskDO> createAndStartLoadTestTask(String strategyId, String experimentTaskId, 
                                                        String userId, String namespace);

    /**
     * 停止压测任务
     *
     * @param taskId 压测任务ID
     * @return 停止结果
     */
    Response<Boolean> stopLoadTestTask(String taskId);

    /**
     * 根据演练任务ID停止压测任务
     *
     * @param experimentTaskId 演练任务ID
     * @return 停止结果
     */
    Response<Boolean> stopLoadTestTaskByExperimentTaskId(String experimentTaskId);

    /**
     * 查询压测任务状态
     *
     * @param taskId 压测任务ID
     * @return 压测任务状态
     */
    Response<LoadTestTaskDO> getLoadTestTaskStatus(String taskId);

    /**
     * 根据演练任务ID查询压测任务状态
     *
     * @param experimentTaskId 演练任务ID
     * @return 压测任务状态
     */
    Response<LoadTestTaskDO> getLoadTestTaskStatusByExperimentTaskId(String experimentTaskId);

    /**
     * 获取压测结果
     *
     * @param taskId 压测任务ID
     * @return 压测结果
     */
    Response<LoadTestResultResponse> getLoadTestResults(String taskId);

    /**
     * 获取压测事件流水
     *
     * @param taskId 压测任务ID
     * @param tail 返回最近N条记录
     * @return 事件流水
     */
    Response<LoadTestEventsResponse> getLoadTestEvents(String taskId, Integer tail);

    /**
     * 同步压测任务状态
     * 从压测引擎同步最新状态到本地数据库
     *
     * @param taskId 压测任务ID
     * @return 同步结果
     */
    Response<LoadTestTaskDO> syncLoadTestTaskStatus(String taskId);

    /**
     * 检查并处理超时的压测任务
     *
     * @param userId 用户ID
     * @param namespace 命名空间
     * @return 处理结果
     */
    Response<Integer> handleTimeoutTasks(String userId, String namespace);

    /**
     * 获取性能指标时序数据
     *
     * @param executionId 执行ID
     * @return 性能指标时序数据
     */
    Response<PerformanceTimeseries> getPerformanceTimeseries(String executionId);

    /**
     * 根据演练ID查找压测任务ID
     *
     * @param experimentId 演练ID
     * @param userId 用户ID
     * @param namespace 命名空间
     * @return 压测任务ID
     */
    String findTaskIdByExperimentId(String experimentId, String userId, String namespace);

    /**
     * 获取压测定义的endpoint信息
     *
     * @param taskId 压测任务ID
     * @return endpoint信息
     */
    String getLoadTestEndpoint(String taskId);
}
