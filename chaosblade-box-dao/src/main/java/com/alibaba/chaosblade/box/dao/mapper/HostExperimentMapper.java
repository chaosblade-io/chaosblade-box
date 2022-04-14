package com.alibaba.chaosblade.box.dao.mapper;

import com.alibaba.chaosblade.box.common.infrastructure.util.MybatisMapper;
import com.alibaba.chaosblade.box.dao.infrastructure.entity.ExperimentTaskEntity;
import com.alibaba.chaosblade.box.dao.infrastructure.tunnel.mapper.provider.HostExperimentCompoundSqlProvider;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.SelectProvider;

import java.util.List;

/**
 * @author haibin
 *
 *
 */
@MybatisMapper
public interface HostExperimentMapper {

    @SelectProvider(type = HostExperimentCompoundSqlProvider.class, method = "hostExperiment")
    @Results(value = {
        @Result(property = "userId", column = "user_id"),
        @Result(property = "experimentTaskId", column = "experiment_task_id")
    })
    public List<ExperimentTaskEntity> queryExperimentTaskIdByHost(@Param("hostIp") String host);
}
