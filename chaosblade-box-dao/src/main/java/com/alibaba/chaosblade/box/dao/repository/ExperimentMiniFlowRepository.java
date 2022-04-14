package com.alibaba.chaosblade.box.dao.repository;

import com.alibaba.chaosblade.box.dao.mapper.ExperimentMiniFlowMapper;
import com.alibaba.chaosblade.box.dao.model.MiniFlowDO;
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
public class ExperimentMiniFlowRepository implements IRepository<String, MiniFlowDO> {

    @Autowired
    private ExperimentMiniFlowMapper experimentMiniFlowMapper;

    @Override
    public Optional<MiniFlowDO> findById(String s) {
        return Optional.ofNullable(experimentMiniFlowMapper.selectById(s));
    }

    @Override
    public boolean update(MiniFlowDO miniFlowDO) {
        return experimentMiniFlowMapper.updateById(miniFlowDO) > 0;
    }

    @Override
    public boolean add(MiniFlowDO miniFlowDO) {
        return experimentMiniFlowMapper.insert(miniFlowDO) > 0;
    }

    public int deleteByFlowIdIn(List<String> flowIds) {
        if (!flowIds.isEmpty()) {
            return experimentMiniFlowMapper.deleteBatchIds(flowIds);
        }
        return 0;
    }

    public int deleteByGroupIds(Collection<String> groupIds) {
        return experimentMiniFlowMapper.delete(new QueryWrapper<MiniFlowDO>().in("group_id", groupIds));
    }

    public List<MiniFlowDO> findByGroupIdAndExperimentId(String groupId, String experimentId) {
        QueryWrapper<MiniFlowDO> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("group_id", groupId);
        queryWrapper.eq("experiment_id", experimentId);
        return experimentMiniFlowMapper.selectList(queryWrapper);
    }

    public List<MiniFlowDO> findByExperimentId(String experimentId) {
        QueryWrapper<MiniFlowDO> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("experiment_id", experimentId);
        return experimentMiniFlowMapper.selectList(queryWrapper);
    }

}
