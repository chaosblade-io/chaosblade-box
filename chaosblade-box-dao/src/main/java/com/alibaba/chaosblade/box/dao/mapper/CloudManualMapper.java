package com.alibaba.chaosblade.box.dao.mapper;

import com.alibaba.chaosblade.box.common.infrastructure.util.MybatisMapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author haibin
 *
 *
 */
@MybatisMapper
public interface CloudManualMapper {

    @Select("SELECT app_code ,COUNT(id) as total from t_chaos_activity_task where phase =\"ATTACK\" GROUP BY app_code  "
        + "ORDER BY total desc limit #{top}")
    List<Map<String, Object>> findTopTenAppCodes(@Param("top") Integer top);

    @Select("SELECT app_code,\n"
        + "       COUNT(DISTINCT(experiment_task_id)) as total\n"
        + "  from t_chaos_app_execute_result\n"
        + " where phase= \"ATTACK\"\n"
        + "   and device_configuration_id= #{configurationId} \n"
        + "GROUP BY app_code")
    List<Map<String, Object>> countAppCodeByHost(@Param("configurationId") String configurationId);

    @Select("select DATE_FORMAT(gmt_create, '%Y-%m-%d') as date,\n"
        + "       count(DISTINCT(experiment_task_id)) as total \n"
        + "  from t_chaos_app_execute_result\n"
        + " WHERE gmt_create>= #{mountDate,jdbcType=DATE,javaType=java.util.Date} and device_configuration_id= "
        + "#{configurationId,javaType=java.lang.String} \n"
        + "group by date")
    List<Map<String, Object>> countInvocationByHostAndDate(@Param("configurationId") String configurationId,
        @Param("mountDate") Date mountDate);

    @Select("SELECT app_code,\n"
            + "       COUNT(DISTINCT(experiment_task_id)) as total\n"
            + "  from t_chaos_app_execute_result\n"
            + " where phase= \"ATTACK\"\n"
            + " and experiment_task_id in (select task_id from t_chaos_experiment_task where user_id = #{userId,javaType=java.lang.String}\n"
            + " and gmt_create>= #{mountDate,jdbcType=DATE,javaType=java.util.Date}) \n"
            + "GROUP BY app_code")
    List<Map<String, Object>> countAppCodeByUserId(@Param("userId") String userId, @Param("mountDate") Date mountDate);

    @Select("select DATE_FORMAT(gmt_create, '%Y-%m-%d') as date,\n"
            + "       count(0) as total \n"
            + "  from t_chaos_experiment_task\n"
            + " WHERE gmt_create>= #{mountDate,jdbcType=DATE,javaType=java.util.Date} and user_id = "
            + "#{userId,javaType=java.lang.String} \n"
            + "group by date")
    List<Map<String, Object>> countExperimentTaskByDate(@Param("userId") String userId,
                                                        @Param("mountDate") Date mountDate);

}
