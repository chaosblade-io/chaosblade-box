package com.alibaba.chaosblade.box.dao.repository;

import com.alibaba.chaosblade.box.common.infrastructure.domain.experiment.guard.GuardRunState;
import com.alibaba.chaosblade.box.dao.mapper.ExperimentGuardInstanceMapper;
import com.alibaba.chaosblade.box.dao.model.ExperimentGuardInstanceDO;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;


@Component
public class ExperimentGuardInstanceRepository implements IRepository<String, ExperimentGuardInstanceDO> {

    @Autowired
    private ExperimentGuardInstanceMapper experimentGuardInstanceMapper;

    @Override
    public Optional<ExperimentGuardInstanceDO> findById(String s) {
        return Optional.ofNullable(experimentGuardInstanceMapper.selectById(s));
    }

    @Override
    public boolean update(ExperimentGuardInstanceDO experimentGuardInstanceDO) {
        return experimentGuardInstanceMapper.updateById(experimentGuardInstanceDO) > 0;
    }

    public boolean updateByInstanceId(ExperimentGuardInstanceDO experimentGuardInstanceDO) {
        QueryWrapper<ExperimentGuardInstanceDO> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("instance_id", experimentGuardInstanceDO.getInstanceId());
        return experimentGuardInstanceMapper.update(experimentGuardInstanceDO, queryWrapper) > 0;
    }

    public boolean updateRunningExperimentGuardInstanceDO(ExperimentGuardInstanceDO experimentGuardInstanceDO) {
        QueryWrapper<ExperimentGuardInstanceDO> experimentGuardInstanceDOQueryWrapper
                = new QueryWrapper<>();
        experimentGuardInstanceDOQueryWrapper.eq("instance_id", experimentGuardInstanceDO.getInstanceId());
        experimentGuardInstanceDOQueryWrapper.notIn("state", GuardRunState.FINISHED, GuardRunState.TRIGGERED);
        return experimentGuardInstanceMapper.update(experimentGuardInstanceDO, experimentGuardInstanceDOQueryWrapper)
                > 0;
    }

    @Override
    public boolean add(ExperimentGuardInstanceDO experimentGuardInstanceDO) {
        return experimentGuardInstanceMapper.insert(experimentGuardInstanceDO) > 0;
    }

    public List<ExperimentGuardInstanceDO> findByExperimentTaskId(String experimentTaskId) {
        QueryWrapper<ExperimentGuardInstanceDO> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("experiment_task_id", experimentTaskId);
        return experimentGuardInstanceMapper.selectList(queryWrapper);
    }
}
