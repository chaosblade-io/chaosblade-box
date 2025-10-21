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

import com.alibaba.chaosblade.box.dao.mapper.ExperimentMiniFlowMapper;
import com.alibaba.chaosblade.box.dao.model.MiniFlowDO;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/** @author haibin */
@Component
public class ExperimentMiniFlowRepository implements IRepository<String, MiniFlowDO> {

  @Autowired private ExperimentMiniFlowMapper experimentMiniFlowMapper;

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
