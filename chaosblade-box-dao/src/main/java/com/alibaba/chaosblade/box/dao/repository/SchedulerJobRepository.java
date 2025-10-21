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

import com.alibaba.chaosblade.box.dao.mapper.SchedulerTaskMapper;
import com.alibaba.chaosblade.box.dao.model.SchedulerJobDO;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/** @author haibin */
@Component
public class SchedulerJobRepository implements IRepository<String, SchedulerJobDO> {

  @Autowired private SchedulerTaskMapper schedulerTaskMapper;

  public SchedulerJobDO findByBusinessIdAndBusinessType(Integer businessType, String businessId) {
    QueryWrapper<SchedulerJobDO> queryWrapper = new QueryWrapper<>();
    queryWrapper.eq("business_type", businessType);
    queryWrapper.eq("business_id", businessId);
    return schedulerTaskMapper.selectOne(queryWrapper);
  }

  @Override
  public Optional<SchedulerJobDO> findById(String taskId) {
    return Optional.ofNullable(schedulerTaskMapper.selectById(taskId));
  }

  public List<SchedulerJobDO> findEnabledSchedulerJob() {
    QueryWrapper<SchedulerJobDO> queryWrapper = new QueryWrapper<>();
    queryWrapper.eq("enabled", true);
    return schedulerTaskMapper.selectList(queryWrapper);
  }

  @Override
  public boolean update(SchedulerJobDO schedulerJobDO) {
    return schedulerTaskMapper.updateById(schedulerJobDO) > 0;
  }

  @Override
  public boolean add(SchedulerJobDO schedulerJobDO) {
    return schedulerTaskMapper.insert(schedulerJobDO) > 0;
  }
}
