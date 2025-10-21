/*
 * Copyright 2025 The ChaosBlade Authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.alibaba.chaosblade.box.dao.repository;

import com.alibaba.chaosblade.box.dao.mapper.SceneFunctionRelationMapper;
import com.alibaba.chaosblade.box.dao.model.SceneFunctionRelationDO;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import javax.annotation.Resource;
import org.springframework.stereotype.Repository;

@Repository
public class SceneFunctionRelationRepository
    implements IRepository<String, SceneFunctionRelationDO> {

  @Resource SceneFunctionRelationMapper sceneFunctionRelationMapper;

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
    UpdateWrapper<SceneFunctionRelationDO> updateWrapper =
        new UpdateWrapper<SceneFunctionRelationDO>()
            .eq("relation_id", sceneFunctionRelationDO.getRelationId());
    return sceneFunctionRelationMapper.update(sceneFunctionRelationDO, updateWrapper) > 0;
  }

  public SceneFunctionRelationDO findByRelationId(String relationId) {
    return sceneFunctionRelationMapper.selectOne(
        new QueryWrapper<SceneFunctionRelationDO>().eq("relation_id", relationId));
  }

  public SceneFunctionRelationDO findByFunctionId(String functionId) {
    return sceneFunctionRelationMapper.selectOne(
        new QueryWrapper<SceneFunctionRelationDO>().eq("function_id", functionId));
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
