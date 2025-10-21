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

import com.alibaba.chaosblade.box.dao.mapper.ExperimentMiniFlowGroupMapper;
import com.alibaba.chaosblade.box.dao.model.MiniFlowGroupDO;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/** @author haibin */
@Component
public class ExperimentMiniFlowGroupRepository implements IRepository<String, MiniFlowGroupDO> {
  @Autowired private ExperimentMiniFlowGroupMapper experimentMiniFlowGroupMapper;

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
