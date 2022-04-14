package com.alibaba.chaosblade.box.dao.mapper;

import com.alibaba.chaosblade.box.common.infrastructure.domain.workspace.ExperimentSummaryInfo;
import com.alibaba.chaosblade.box.common.infrastructure.util.MybatisMapper;
import com.alibaba.chaosblade.box.dao.model.ExperimentDO;
import com.alibaba.chaosblade.box.dao.query.ExperimentPageQuery;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@MybatisMapper
public interface ExperimentMapper extends BaseMapper<ExperimentDO> {

    @Select("<script>" +
            "select " +
            "mke.experiment_id," +
            "mke.name," +
            "mke.description," +
            "mke.level," +
            "mke.owner_user_id," +
            "mke.user_id," +
            "mke.user_type," +
            "mke.state," +
            "mke.result_state as result," +
            "mke.task_id as experimentTaskId," +
            "mke.namespace," +
            "mke.mini_app_desc," +
            "mke.is_delete," +
            "mke.outer_id," +
            "mke.scheduler_config," +
            "mke.run_mode," +
            "mke.attributes," +
            "mke.duration," +
            "mke.is_template," +
            "mke.source," +
            "mke.gmt_create," +
            "mke.gmt_modified " +
            "from t_chaos_experiment mke WHERE 1=1 " +
            "<if test='null != query.userId and query.userId != \"\" '>" +
                "AND mke.user_id = #{query.userId} " +
            "</if>" +
            "<if test='null != query.namespace and query.namespace != \"\" '>" +
                "AND mke.namespace = #{query.namespace} " +
            "</if>" +
            "<if test='null != query.partName and query.partName != \"\" '>" +
                "AND mke.name like #{query.partName} " +
            "</if>" +
            "AND mke.is_template = 'false' " +
            "<if test='true != query.containsDeleted'>" +
                "AND mke.is_delete = 'false' " +
            "</if>" +
            "AND mke.state != -2 " +
            "<if test='null != query.stateValues and query.stateValues.size != 0'>" +
                "AND ( 1 != 1 " +
                "<foreach collection='query.stateValues' item='state' >" +
                    "<if test='1 == state'>" +
                        "OR ( mke.state = 1 ) " +
                    "</if>" +
                    "<if test='1 != state'>" +
                        "OR ( mke.state = #{state} " +
                        "<if test='null != query.runResults and query.runResults.size != 0'>" +
                            "AND mke.result_state in " +
                            "<foreach collection='query.runResults' item='runResult' index='index' open='(' close=')' separator=',' >" +
                                "#{runResult} " +
                            "</foreach>" +
                        "</if>" +
                        ") " +
                    "</if>" +
                "</foreach>" +
                ") " +
            "</if>" +
            "<if test='null != query.experimentIds and query.experimentIds.size != 0'>" +
                "AND mke.experiment_id in " +
                "<foreach collection='query.experimentIds' item='experimentId' index='index' open='(' close=')' separator=',' >" +
                    "#{experimentId}" +
                "</foreach>" +
            "</if>" +
            "<if test='null != query.tagNames and query.tagNames.size != 0'>" +
                " AND mke.experiment_id IN " +
                "(select experiment_id from t_chaos_experiment_tag t where t.tag_name in " +
                "<foreach collection='query.tagNames' item='tag' index='index' open='(' close=')' separator=',' >" +
                    "#{tag}" +
                "</foreach>" +
                ")" +
            "</if>" +
            "<if test='null != query.scheduler and query.scheduler'>" +
                " AND mke.experiment_id IN " +
                "(select sj.business_id from t_chaos_scheduler_job sj where " +
                    " cron_expression != \"\" " +
                    " and " +
                    " sj.user_id = #{query.userId} " +
                        "AND sj.enabled = 1 " +
                ")" +
            "</if>" +
            "ORDER BY mke.gmt_create DESC" +
            "</script>")
    IPage<ExperimentDO> selectExperimentsByPage(IPage<ExperimentDO> page, @Param("query") ExperimentPageQuery experimentQuery);


    @Select("<script>" +
            "SELECT\n" +
            "\tdate( gmt_create ) AS date,\n" +
            "\tresult_state,\n" +
            "\tcount( 1 ) AS size \n" +
            "FROM\n" +
            "\tt_chaos_experiment_task \n" +
            "WHERE\n" +
            "\tgmt_create BETWEEN date_sub( now(), INTERVAL 30 DAY ) \n" +
            "\tAND now() \n" +
            " AND result_state is not null " +
            "<if test='null != userId and userId != \"\" '>" +
            " AND user_id = #{userId} " +
            "</if>" +
            "<if test='null != namespace and namespace != \"\" '>" +
            "AND namespace = #{namespace} " +
            "</if>" +
            " GROUP BY\n" +
            "\tdate( gmt_create ),\n" +
            "\tresult_state" +
            "</script>")
    @Results(id = "experimentSummaryInfoIn30Days", value = {
            @Result(property = "date", column = "date"),
            @Result(property = "resultState", column = "result_state"),
            @Result(property = "size", column = "size"),
    })
    List<ExperimentSummaryInfo> getExperimentSummaryInfoIn30Days(
            @Param("userId") String userId,
            @Param("namespace") String namespace
    );

    @Select("<script>" +
            "SELECT\n" +
            "\tdate( gmt_create ) AS date,\n" +
            "\tresult_state,\n" +
            "\tcount( 1 ) AS size \n" +
            "FROM\n" +
            "\tt_chaos_experiment_task \n" +
            "WHERE\n" +
            " result_state is not null " +
            "<if test='null != userId and userId != \"\" '>" +
            " AND user_id = #{userId} " +
            "</if>" +
            "<if test='null != namespace and namespace != \"\" '>" +
            "AND namespace = #{namespace} " +
            "</if>" +
            " GROUP BY\n" +
            "\tresult_state" +
            "</script>")
    @Results(id = "experimentSummaryInfo", value = {
            @Result(property = "resultState", column = "result_state"),
            @Result(property = "size", column = "size"),
    })
    List<ExperimentSummaryInfo> getExperimentSummaryInfo(@Param("userId") String userId,
                                                         @Param("namespace") String namespace);
}
