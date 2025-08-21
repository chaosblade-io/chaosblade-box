package com.alibaba.chaosblade.box.dao.repository;

import com.alibaba.chaosblade.box.dao.model.LoadTestTaskDO;
import com.alibaba.chaosblade.box.dao.model.base.PageableResponse;

import java.util.Date;
import java.util.List;
import java.util.Optional;

/**
 * 压测任务Repository接口
 *
 * @author ZhengMingZhuo
 */
public interface LoadTestTaskRepository {

    /**
     * 保存压测任务
     *
     * @param loadTestTaskDO 压测任务实体
     * @return 保存结果
     */
    boolean save(LoadTestTaskDO loadTestTaskDO);

    /**
     * 根据任务ID查询压测任务
     *
     * @param taskId 任务ID
     * @return 压测任务
     */
    Optional<LoadTestTaskDO> findByTaskId(String taskId);

    /**
     * 根据演练任务ID查询压测任务
     *
     * @param experimentTaskId 演练任务ID
     * @return 压测任务
     */
    Optional<LoadTestTaskDO> findByExperimentTaskId(String experimentTaskId);

    /**
     * 根据执行ID查询压测任务
     *
     * @param executionId 执行ID
     * @return 压测任务
     */
    Optional<LoadTestTaskDO> findByExecutionId(String executionId);

    /**
     * 根据压测策略ID查询压测任务列表
     *
     * @param strategyId 压测策略ID
     * @return 压测任务列表
     */
    List<LoadTestTaskDO> findByStrategyId(String strategyId);

    /**
     * 查询正在运行的压测任务
     *
     * @param userId 用户ID
     * @param namespace 命名空间
     * @return 正在运行的压测任务列表
     */
    List<LoadTestTaskDO> findRunningTasks(String userId, String namespace);

    /**
     * 分页查询压测任务
     *
     * @param pageNum 页码
     * @param pageSize 页大小
     * @param userId 用户ID
     * @param namespace 命名空间
     * @param strategyId 压测策略ID（可选）
     * @param experimentTaskId 演练任务ID（可选）
     * @param status 任务状态（可选）
     * @return 分页结果
     */
    PageableResponse<LoadTestTaskDO> findByPage(int pageNum, int pageSize,
                                                String userId, String namespace,
                                                String strategyId, String experimentTaskId,
                                                String status);

    /**
     * 更新任务状态
     *
     * @param taskId 任务ID
     * @param status 新状态
     * @param errorMessage 错误信息（可选）
     * @return 更新是否成功
     */
    boolean updateStatus(String taskId, String status, String errorMessage);

    /**
     * 更新任务执行信息
     *
     * @param taskId 任务ID
     * @param executionId 执行ID
     * @param status 状态
     * @param startTime 开始时间
     * @return 更新是否成功
     */
    boolean updateExecutionInfo(String taskId, String executionId, String status, Date startTime);

    /**
     * 更新任务完成信息
     *
     * @param taskId 任务ID
     * @param status 最终状态
     * @param endTime 结束时间
     * @param resultPath 结果路径
     * @param reportPath 报告路径
     * @param logPath 日志路径
     * @param errorMessage 错误信息
     * @return 更新是否成功
     */
    boolean updateCompletionInfo(String taskId, String status, Date endTime,
                                 String resultPath, String reportPath, String logPath,
                                 String errorMessage);

    /**
     * 删除压测任务（逻辑删除）
     *
     * @param taskId 任务ID
     * @return 删除是否成功
     */
    boolean deleteByTaskId(String taskId);

    /**
     * 查询所有压测任务（不分页）
     *
     * @param userId 用户ID
     * @param namespace 命名空间
     * @return 压测任务列表
     */
    List<LoadTestTaskDO> findAll(String userId, String namespace);
}
