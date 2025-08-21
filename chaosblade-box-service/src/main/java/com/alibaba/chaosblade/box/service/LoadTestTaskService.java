package com.alibaba.chaosblade.box.service;

import com.alibaba.chaosblade.box.common.common.domain.Response;
import com.alibaba.chaosblade.box.dao.infrastructure.loadtest.model.LoadTestEventsResponse;
import com.alibaba.chaosblade.box.dao.infrastructure.loadtest.model.LoadTestResultResponse;
import com.alibaba.chaosblade.box.dao.model.base.PageableResponse;
import com.alibaba.chaosblade.box.service.model.loadtest.LoadTestTaskQueryRequest;
import com.alibaba.chaosblade.box.service.model.loadtest.LoadTestTaskVO;
import com.alibaba.chaosblade.box.dao.infrastructure.loadtest.model.PerformanceTimeseries;

import java.util.List;

/**
 * 压测任务服务接口
 *
 * @author ZhengMingZhuo
 */
public interface LoadTestTaskService {

    /**
     * 根据任务ID查询压测任务
     *
     * @param taskId 任务ID
     * @param userId 用户ID
     * @param namespace 命名空间
     * @return 压测任务详情
     */
    Response<LoadTestTaskVO> getLoadTestTask(String taskId, String userId, String namespace);

    /**
     * 根据演练任务ID查询压测任务
     *
     * @param experimentTaskId 演练任务ID
     * @param userId 用户ID
     * @param namespace 命名空间
     * @return 压测任务详情
     */
    Response<LoadTestTaskVO> getLoadTestTaskByExperimentTaskId(String experimentTaskId, String userId, String namespace);

    /**
     * 分页查询压测任务
     *
     * @param request 查询请求
     * @return 分页结果
     */
    Response<PageableResponse<LoadTestTaskVO>> queryLoadTestTasks(LoadTestTaskQueryRequest request);

    /**
     * 查询所有压测任务（不分页）
     *
     * @param userId 用户ID
     * @param namespace 命名空间
     * @return 压测任务列表
     */
    Response<List<LoadTestTaskVO>> listAllLoadTestTasks(String userId, String namespace);

    /**
     * 停止压测任务
     *
     * @param taskId 任务ID
     * @param userId 用户ID
     * @param namespace 命名空间
     * @return 停止结果
     */
    Response<Boolean> stopLoadTestTask(String taskId, String userId, String namespace);

    /**
     * 获取压测结果
     *
     * @param taskId 任务ID（可选）
     * @param experimentId 演练ID（可选）
     * @param userId 用户ID
     * @param namespace 命名空间
     * @return 压测结果
     */
    Response<LoadTestResultResponse> getLoadTestResults(String taskId, String experimentId, String userId, String namespace);

    /**
     * 获取压测事件流水
     *
     * @param taskId 任务ID（可选）
     * @param experimentId 演练ID（可选）
     * @param tail 返回最近N条记录
     * @param userId 用户ID
     * @param namespace 命名空间
     * @return 事件流水
     */
    Response<LoadTestEventsResponse> getLoadTestEvents(String taskId, String experimentId, Integer tail, String userId, String namespace);

    /**
     * 同步压测任务状态
     *
     * @param taskId 任务ID
     * @param userId 用户ID
     * @param namespace 命名空间
     * @return 同步后的任务状态
     */
    Response<LoadTestTaskVO> syncLoadTestTaskStatus(String taskId, String userId, String namespace);

    /**
     * 获取性能指标时序数据
     *
     * @param executionId 执行ID
     * @param userId 用户ID
     * @param namespace 命名空间
     * @return 性能指标时序数据
     */
    Response<PerformanceTimeseries> getPerformanceTimeseries(String executionId, String userId, String namespace);
}
