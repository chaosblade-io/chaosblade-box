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

import com.alibaba.chaosblade.box.dao.mapper.ChaosToolsMapper;
import com.alibaba.chaosblade.box.dao.model.ChaosToolsDO;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

/** @author yefei */
@Repository
public class ChaosToolsRepository implements IRepository<String, ChaosToolsDO> {

  @Autowired private ChaosToolsMapper chaosToolsMapper;

  @Override
  public Optional<ChaosToolsDO> findById(String s) {
    return Optional.ofNullable(chaosToolsMapper.selectById(s));
  }

  @Override
  public boolean update(ChaosToolsDO chaosToolsDO) {
    return chaosToolsMapper.updateById(chaosToolsDO) > 0;
  }

  @Override
  public boolean add(ChaosToolsDO chaosToolsDO) {
    return chaosToolsMapper.insert(chaosToolsDO) == 1;
  }

  public void delete(Long id) {
    chaosToolsMapper.deleteById(id);
  }

  public Optional<ChaosToolsDO> selectByConfigurationId(String configurationId) {
    QueryWrapper<ChaosToolsDO> wrapper = new QueryWrapper<>();
    wrapper.lambda().eq(ChaosToolsDO::getConfigurationId, configurationId);
    return Optional.ofNullable(chaosToolsMapper.selectOne(wrapper));
  }

  public Optional<ChaosToolsDO> selectByClusterId(String clusterId) {
    QueryWrapper<ChaosToolsDO> wrapper = new QueryWrapper<>();
    wrapper.lambda().eq(ChaosToolsDO::getClusterId, clusterId);
    return Optional.ofNullable(chaosToolsMapper.selectOne(wrapper));
  }

  public List<ChaosToolsDO> findByConfigurationId(String configurationId) {
    QueryWrapper<ChaosToolsDO> wrapper = new QueryWrapper<>();
    wrapper.lambda().eq(ChaosToolsDO::getConfigurationId, configurationId);
    return chaosToolsMapper.selectList(wrapper);
  }

  public List<ChaosToolsDO> findByClusterId(String clusterId) {
    QueryWrapper<ChaosToolsDO> wrapper = new QueryWrapper<>();
    wrapper.lambda().eq(ChaosToolsDO::getClusterId, clusterId);
    return chaosToolsMapper.selectList(wrapper);
  }
}
