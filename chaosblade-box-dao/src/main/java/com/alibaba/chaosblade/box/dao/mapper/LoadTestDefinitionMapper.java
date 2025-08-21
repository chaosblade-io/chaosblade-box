package com.alibaba.chaosblade.box.dao.mapper;

import com.alibaba.chaosblade.box.common.infrastructure.util.MybatisMapper;
import com.alibaba.chaosblade.box.dao.model.LoadTestDefinitionDO;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

/**
 * 压测定义 Mapper 接口
 *
 * @author ZhengMingZhuo
 */
@MybatisMapper
public interface LoadTestDefinitionMapper extends BaseMapper<LoadTestDefinitionDO> {

    /**
     * 分页查询压测定义
     *
     * @param page 分页参数
     * @param userId 用户ID
     * @param namespace 命名空间
     * @param name 名称（模糊查询）
     * @param engineType 引擎类型
     * @return 分页结果
     */
    @Select("<script>" +
            "SELECT " +
            "definition_id, name, engine_type, endpoint, entry, content_ref, " +
            "url_case, created_by, user_id, namespace, is_delete, " +
            "gmt_create, gmt_modified " +
            "FROM t_chaos_load_test_definition " +
            "WHERE is_delete = 0 " +
            "<if test='userId != null and userId != \"\"'>" +
            "AND user_id = #{userId} " +
            "</if>" +
            "<if test='namespace != null and namespace != \"\"'>" +
            "AND namespace = #{namespace} " +
            "</if>" +
            "<if test='name != null and name != \"\"'>" +
            "AND name LIKE CONCAT('%', #{name}, '%') " +
            "</if>" +
            "<if test='engineType != null and engineType != \"\"'>" +
            "AND engine_type = #{engineType} " +
            "</if>" +
            "ORDER BY gmt_create DESC" +
            "</script>")
    IPage<LoadTestDefinitionDO> selectPageByConditions(
            IPage<LoadTestDefinitionDO> page,
            @Param("userId") String userId,
            @Param("namespace") String namespace,
            @Param("name") String name,
            @Param("engineType") String engineType
    );
}
