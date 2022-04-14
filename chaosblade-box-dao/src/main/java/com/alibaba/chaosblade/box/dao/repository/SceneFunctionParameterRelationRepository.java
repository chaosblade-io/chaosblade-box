package com.alibaba.chaosblade.box.dao.repository;

import com.alibaba.chaosblade.box.dao.mapper.SceneFunctionParameterRelationMapper;
import com.alibaba.chaosblade.box.dao.model.SceneFunctionParameterRelationDO;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author jiumu
 *
 */

@Repository
public class SceneFunctionParameterRelationRepository implements IRepository<String, SceneFunctionParameterRelationDO>{
    @Resource
    SceneFunctionParameterRelationMapper sceneFunctionParameterRelationMapper;

    @Override
    public Optional<SceneFunctionParameterRelationDO> findById(String relationId) {
        return Optional.ofNullable(sceneFunctionParameterRelationMapper.selectById(relationId));
    }

    @Override
    public boolean add(SceneFunctionParameterRelationDO sceneFunctionParameterRelationDO) {
        return sceneFunctionParameterRelationMapper.insert(sceneFunctionParameterRelationDO) > 0;
    }

    @Override
    public boolean update(SceneFunctionParameterRelationDO sceneFunctionParameterRelationDO) {
        UpdateWrapper<SceneFunctionParameterRelationDO> updateWrapper = new UpdateWrapper<SceneFunctionParameterRelationDO>()
            .eq("relation_id", sceneFunctionParameterRelationDO.getRelationId());
        return sceneFunctionParameterRelationMapper.update(sceneFunctionParameterRelationDO, updateWrapper) > 0;
    }

    public List<String> findParameterIdsByOutParameterId(String parameterId) {
        QueryWrapper<SceneFunctionParameterRelationDO> queryWrapper = new QueryWrapper<SceneFunctionParameterRelationDO>()
            .select("parameter_id")
            .eq("out_parameter_id", parameterId);
        return sceneFunctionParameterRelationMapper.selectList(queryWrapper).stream()
            .map(SceneFunctionParameterRelationDO::getParameterId).distinct().collect(Collectors.toList());
    }
}
