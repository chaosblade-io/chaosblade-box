package com.alibaba.chaosblade.box.dao.repository;

import com.alibaba.chaosblade.box.dao.mapper.ExperimentMiniFlowGroupMapper;
import com.alibaba.chaosblade.box.dao.model.MiniFlowGroupDO;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

/**
 * @author haibin
 * 
 * 
 */
@Component
public class ExperimentMiniFlowGroupRepository implements IRepository<String, MiniFlowGroupDO> {
    @Autowired
    private ExperimentMiniFlowGroupMapper experimentMiniFlowGroupMapper;

    @Override
    public Optional<MiniFlowGroupDO> findById(String s) {
        if (s == null) {
            return Optional.empty();
        }
        return Optional.ofNullable(experimentMiniFlowGroupMapper.selectById(s));
    }

    @Override
    public boolean update(MiniFlowGroupDO miniFlowGroupDO) {
        return experimentMiniFlowGroupMapper.updateById(miniFlowGroupDO) > 0;
    }

    @Override
    public boolean add(MiniFlowGroupDO miniFlowGroupDO) {
        return experimentMiniFlowGroupMapper.insert(miniFlowGroupDO) > 0;
    }

    public int deleteMiniGroups(Collection<String> groups) {
        return experimentMiniFlowGroupMapper.deleteBatchIds(groups);
    }

    public List<MiniFlowGroupDO> findByExperimentId(String experimentId) {
        return experimentMiniFlowGroupMapper.selectList(
            new QueryWrapper<MiniFlowGroupDO>().eq("experiment_id", experimentId));
    }
}
