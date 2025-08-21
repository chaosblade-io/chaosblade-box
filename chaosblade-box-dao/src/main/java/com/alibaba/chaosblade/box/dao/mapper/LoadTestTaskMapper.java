package com.alibaba.chaosblade.box.dao.mapper;

import com.alibaba.chaosblade.box.dao.model.LoadTestTaskDO;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

/**
 * 压测任务Mapper接口
 *
 * @author ZhengMingZhuo
 */
@Mapper
public interface LoadTestTaskMapper extends BaseMapper<LoadTestTaskDO> {

    /**
     * 分页查询压测任务
     *
     * @param page 分页参数
     * @param userId 用户ID
     * @param namespace 命名空间
     * @param strategyId 压测策略ID（可选）
     * @param experimentTaskId 演练任务ID（可选）
     * @param status 任务状态（可选）
     * @return 分页结果
     */
    @Select("<script>" +
            "SELECT " +
            "task_id, strategy_id, experiment_task_id, execution_id, status, " +
            "start_time, end_time, error_message, result_path, report_path, log_path, " +
            "user_id, namespace, is_delete, gmt_create, gmt_modified " +
            "FROM t_chaos_load_test_task " +
            "WHERE is_delete = 0 " +
            "<if test='userId != null and userId != \"\"'>" +
            "AND user_id = #{userId} " +
            "</if>" +
            "<if test='namespace != null and namespace != \"\"'>" +
            "AND namespace = #{namespace} " +
            "</if>" +
            "<if test='strategyId != null and strategyId != \"\"'>" +
            "AND strategy_id = #{strategyId} " +
            "</if>" +
            "<if test='experimentTaskId != null and experimentTaskId != \"\"'>" +
            "AND experiment_task_id = #{experimentTaskId} " +
            "</if>" +
            "<if test='status != null and status != \"\"'>" +
            "AND status = #{status} " +
            "</if>" +
            "ORDER BY gmt_create DESC" +
            "</script>")
    Page<LoadTestTaskDO> selectByPage(Page<LoadTestTaskDO> page,
                                      @Param("userId") String userId,
                                      @Param("namespace") String namespace,
                                      @Param("strategyId") String strategyId,
                                      @Param("experimentTaskId") String experimentTaskId,
                                      @Param("status") String status);

    /**
     * 根据演练任务ID查询压测任务
     *
     * @param experimentTaskId 演练任务ID
     * @return 压测任务
     */
    @Select("SELECT task_id, strategy_id, experiment_task_id, execution_id, status, " +
            "start_time, end_time, error_message, result_path, report_path, log_path, " +
            "user_id, namespace, is_delete, gmt_create, gmt_modified " +
            "FROM t_chaos_load_test_task " +
            "WHERE experiment_task_id = #{experimentTaskId} AND is_delete = 0")
    LoadTestTaskDO selectByExperimentTaskId(@Param("experimentTaskId") String experimentTaskId);

    /**
     * 根据压测策略ID查询压测任务列表
     *
     * @param strategyId 压测策略ID
     * @return 压测任务列表
     */
    @Select("SELECT task_id, strategy_id, experiment_task_id, execution_id, status, " +
            "start_time, end_time, error_message, result_path, report_path, log_path, " +
            "user_id, namespace, is_delete, gmt_create, gmt_modified " +
            "FROM t_chaos_load_test_task " +
            "WHERE strategy_id = #{strategyId} AND is_delete = 0 " +
            "ORDER BY gmt_create DESC")
    List<LoadTestTaskDO> selectByStrategyId(@Param("strategyId") String strategyId);

    /**
     * 根据执行ID查询压测任务
     *
     * @param executionId 执行ID
     * @return 压测任务
     */
    @Select("SELECT task_id, strategy_id, experiment_task_id, execution_id, status, " +
            "start_time, end_time, error_message, result_path, report_path, log_path, " +
            "user_id, namespace, is_delete, gmt_create, gmt_modified " +
            "FROM t_chaos_load_test_task " +
            "WHERE execution_id = #{executionId} AND is_delete = 0")
    LoadTestTaskDO selectByExecutionId(@Param("executionId") String executionId);

    /**
     * 查询正在运行的压测任务
     *
     * @param userId 用户ID
     * @param namespace 命名空间
     * @return 正在运行的压测任务列表
     */
    @Select("SELECT task_id, strategy_id, experiment_task_id, execution_id, status, " +
            "start_time, end_time, error_message, result_path, report_path, log_path, " +
            "user_id, namespace, is_delete, gmt_create, gmt_modified " +
            "FROM t_chaos_load_test_task " +
            "WHERE user_id = #{userId} AND namespace = #{namespace} " +
            "AND status IN ('PENDING', 'RUNNING') AND is_delete = 0 " +
            "ORDER BY gmt_create DESC")
    List<LoadTestTaskDO> selectRunningTasks(@Param("userId") String userId,
                                            @Param("namespace") String namespace);

    /**
     * 更新任务状态
     *
     * @param taskId 任务ID
     * @param status 新状态
     * @param errorMessage 错误信息（可选）
     * @return 更新行数
     */
    @Update("<script>" +
            "UPDATE t_chaos_load_test_task SET " +
            "status = #{status}, " +
            "gmt_modified = NOW() " +
            "<if test='errorMessage != null'>" +
            ", error_message = #{errorMessage} " +
            "</if>" +
            "WHERE task_id = #{taskId} AND is_delete = 0" +
            "</script>")
    int updateStatus(@Param("taskId") String taskId,
                     @Param("status") String status,
                     @Param("errorMessage") String errorMessage);

    /**
     * 更新任务执行信息
     *
     * @param taskId 任务ID
     * @param executionId 执行ID
     * @param status 状态
     * @param startTime 开始时间
     * @return 更新行数
     */
    @Update("UPDATE t_chaos_load_test_task SET " +
            "execution_id = #{executionId}, " +
            "status = #{status}, " +
            "start_time = #{startTime}, " +
            "gmt_modified = NOW() " +
            "WHERE task_id = #{taskId} AND is_delete = 0")
    int updateExecutionInfo(@Param("taskId") String taskId,
                            @Param("executionId") String executionId,
                            @Param("status") String status,
                            @Param("startTime") java.util.Date startTime);

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
     * @return 更新行数
     */
    @Update("<script>" +
            "UPDATE t_chaos_load_test_task SET " +
            "status = #{status}, " +
            "end_time = #{endTime}, " +
            "gmt_modified = NOW() " +
            "<if test='resultPath != null'>" +
            ", result_path = #{resultPath} " +
            "</if>" +
            "<if test='reportPath != null'>" +
            ", report_path = #{reportPath} " +
            "</if>" +
            "<if test='logPath != null'>" +
            ", log_path = #{logPath} " +
            "</if>" +
            "<if test='errorMessage != null'>" +
            ", error_message = #{errorMessage} " +
            "</if>" +
            "WHERE task_id = #{taskId} AND is_delete = 0" +
            "</script>")
    int updateCompletionInfo(@Param("taskId") String taskId,
                             @Param("status") String status,
                             @Param("endTime") java.util.Date endTime,
                             @Param("resultPath") String resultPath,
                             @Param("reportPath") String reportPath,
                             @Param("logPath") String logPath,
                             @Param("errorMessage") String errorMessage);
}
