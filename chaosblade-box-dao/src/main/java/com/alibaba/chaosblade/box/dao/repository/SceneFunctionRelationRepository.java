package com.alibaba.chaosblade.box.dao.repository;

import com.alibaba.chaosblade.box.dao.mapper.SceneFunctionRelationMapper;
import com.alibaba.chaosblade.box.dao.model.SceneFunctionRelationDO;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
public class SceneFunctionRelationRepository implements IRepository<String, SceneFunctionRelationDO>{

    @Resource
    SceneFunctionRelationMapper sceneFunctionRelationMapper;

    @Override
    public Optional<SceneFunctionRelationDO> findById(String relationId) {
        return Optional.ofNullable(sceneFunctionRelationMapper.selectById(relationId));
    }

    @Override
    public boolean add(SceneFunctionRelationDO sceneFunctionRelationDO) {
        return sceneFunctionRelationMapper.insert(sceneFunctionRelationDO) > 0;
    }

    @Override
    public boolean update(SceneFunctionRelationDO sceneFunctionRelationDO) {
        UpdateWrapper<SceneFunctionRelationDO> updateWrapper = new UpdateWrapper<SceneFunctionRelationDO>()
            .eq("relation_id", sceneFunctionRelationDO.getRelationId());
        return sceneFunctionRelationMapper.update(sceneFunctionRelationDO, updateWrapper) > 0;
    }

    public SceneFunctionRelationDO findByRelationId(String relationId) {
        return sceneFunctionRelationMapper.selectOne(new QueryWrapper<SceneFunctionRelationDO>()
            .eq("relation_id", relationId));
    }

    public SceneFunctionRelationDO findByFunctionId(String functionId) {
        return sceneFunctionRelationMapper.selectOne(
            new QueryWrapper<SceneFunctionRelationDO>()
            .eq("function_id", functionId)
        );
    }

    public List<String> findFunctionIdsByOutFunctionId(String outFunctionId) {
        QueryWrapper<SceneFunctionRelationDO> wrapper = new QueryWrapper<>();
        wrapper.select("function_id").eq("out_function_id", outFunctionId);
        return sceneFunctionRelationMapper.selectList(wrapper).stream()
            .map(SceneFunctionRelationDO::getFunctionId)
            .distinct()
            .collect(Collectors.toList());
    }

    public Integer countByFunctionId(String functionId) {
        return sceneFunctionRelationMapper.selectCount(
            new QueryWrapper<SceneFunctionRelationDO>().eq("function_id", functionId));
    }

    public Integer countByOutFunctionId(String functionId) {
        return sceneFunctionRelationMapper.selectCount(
            new QueryWrapper<SceneFunctionRelationDO>().eq("out_function_id", functionId));
    }

}
