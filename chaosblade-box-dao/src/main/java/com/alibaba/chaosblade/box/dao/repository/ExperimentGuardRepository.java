package com.alibaba.chaosblade.box.dao.repository;

import com.alibaba.chaosblade.box.dao.mapper.ExperimentGuardMapper;
import com.alibaba.chaosblade.box.dao.model.ExperimentGuardDO;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

/**
 * @author haibin
 *
 *
 */
@Component
public class ExperimentGuardRepository implements IRepository<String, ExperimentGuardDO> {
    @Autowired
    private ExperimentGuardMapper experimentGuardMapper;

    @Override
    public Optional<ExperimentGuardDO> findById(String s) {
        return Optional.ofNullable(experimentGuardMapper.selectById(s));
    }

    @Override
    public boolean update(ExperimentGuardDO experimentGuardDO) {
        return experimentGuardMapper.updateById(experimentGuardDO) > 0;
    }

    @Override
    public boolean add(ExperimentGuardDO experimentGuardDO) {
        return experimentGuardMapper.insert(experimentGuardDO) > 0;
    }

    public List<ExperimentGuardDO> findByExperimentId(String experimentId) {
        return experimentGuardMapper.selectList(
            new QueryWrapper<ExperimentGuardDO>().eq("experiment_id", experimentId));
    }

    public int deleteByExperimentId(String experimentId) {
        if (experimentId == null) { return 0; }
        return experimentGuardMapper.delete(new QueryWrapper<ExperimentGuardDO>().eq("experiment_id", experimentId));
    }
}
