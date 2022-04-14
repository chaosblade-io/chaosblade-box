package com.alibaba.chaosblade.box.dao.mapper;

import com.alibaba.chaosblade.box.common.infrastructure.domain.experiment.guard.GuardRunState;
import com.alibaba.chaosblade.box.common.infrastructure.util.MybatisMapper;
import com.alibaba.chaosblade.box.dao.model.ExperimentGuardInstanceDO;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;

/**
 * @author haibin
 *
 * 
 */
@MybatisMapper
public interface ExperimentGuardInstanceMapper extends BaseMapper<ExperimentGuardInstanceDO> {

    @Update(
        value = "update t_chaos_experiment_guard_instance set value=#{value} where instanceId=#{instanceId} and "
            + "state=#{state}")
    public int updateExperimentGuardMonitorMetricResultEntity(@Param("instanceId") String instanceId,
        @Param("value") String experimentGuardMonitorMetricResultEntity,
        @Param(value = "#{state}")
                                                                      GuardRunState guardRunState);

}
