package com.alibaba.chaosblade.box.dao.mapper;

import com.alibaba.chaosblade.box.common.infrastructure.util.MybatisMapper;
import com.alibaba.chaosblade.box.dao.model.LoadTestStrategyDO;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

/**
 * 压测策略 Mapper 接口
 *
 * @author ZhengMingZhuo
 */
@MybatisMapper
public interface LoadTestStrategyMapper extends BaseMapper<LoadTestStrategyDO> {

    /**
     * 分页查询压测策略
     *
     * @param page 分页参数
     * @param userId 用户ID
     * @param namespace 命名空间
     * @param definitionId 压测定义ID（可选）
     * @param experimentId 实验ID（可选）
     * @param enable 是否启用（可选）
     * @return 分页结果
     */
    @Select("<script>" +
            "SELECT " +
            "strategy_id, enable, definition_id, experiment_id, " +
            "start_before_fault_sec, traffic_duration_sec, abort_on_load_failure, " +
            "user_id, namespace, is_delete, gmt_create, gmt_modified " +
            "FROM t_chaos_load_test_strategy " +
            "WHERE is_delete = 0 " +
            "<if test='userId != null and userId != \"\"'>" +
            "AND user_id = #{userId} " +
            "</if>" +
            "<if test='namespace != null and namespace != \"\"'>" +
            "AND namespace = #{namespace} " +
            "</if>" +
            "<if test='definitionId != null and definitionId != \"\"'>" +
            "AND definition_id = #{definitionId} " +
            "</if>" +
            "<if test='experimentId != null and experimentId != \"\"'>" +
            "AND experiment_id = #{experimentId} " +
            "</if>" +
            "<if test='enable != null'>" +
            "AND enable = #{enable} " +
            "</if>" +
            "ORDER BY gmt_create DESC" +
            "</script>")
    IPage<LoadTestStrategyDO> selectPageByConditions(
            IPage<LoadTestStrategyDO> page,
            @Param("userId") String userId,
            @Param("namespace") String namespace,
            @Param("definitionId") String definitionId,
            @Param("experimentId") String experimentId,
            @Param("enable") Integer enable
    );

    /**
     * 根据实验ID查询压测策略
     *
     * @param experimentId 实验ID
     * @return 压测策略
     */
    @Select("SELECT strategy_id, enable, definition_id, experiment_id, " +
            "start_before_fault_sec, traffic_duration_sec, abort_on_load_failure, " +
            "user_id, namespace, is_delete, gmt_create, gmt_modified " +
            "FROM t_chaos_load_test_strategy " +
            "WHERE experiment_id = #{experimentId} AND is_delete = 0")
    LoadTestStrategyDO selectByExperimentId(@Param("experimentId") String experimentId);

    /**
     * 根据压测定义ID查询压测策略列表
     *
     * @param definitionId 压测定义ID
     * @return 压测策略列表
     */
    @Select("SELECT strategy_id, enable, definition_id, experiment_id, " +
            "start_before_fault_sec, traffic_duration_sec, abort_on_load_failure, " +
            "user_id, namespace, is_delete, gmt_create, gmt_modified " +
            "FROM t_chaos_load_test_strategy " +
            "WHERE definition_id = #{definitionId} AND is_delete = 0 " +
            "ORDER BY gmt_create DESC")
    java.util.List<LoadTestStrategyDO> selectByDefinitionId(@Param("definitionId") String definitionId);
}
