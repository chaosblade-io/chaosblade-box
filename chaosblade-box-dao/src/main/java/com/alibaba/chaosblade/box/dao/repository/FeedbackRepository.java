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

import com.alibaba.chaosblade.box.dao.mapper.FeedbackMapper;
import com.alibaba.chaosblade.box.dao.model.ExperimentTaskFeedbackDO;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.google.common.base.Preconditions;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/** @author haibin.lhb */
@Component
public class FeedbackRepository implements IRepository<String, ExperimentTaskFeedbackDO> {
  @Autowired private FeedbackMapper feedbackMapper;

  @Override
  public Optional<ExperimentTaskFeedbackDO> findById(String aLong) {
    return Optional.ofNullable(feedbackMapper.selectById(aLong));
  }

  @Override
  public boolean update(ExperimentTaskFeedbackDO experimentTaskFeedbackDO) {
    Preconditions.checkArgument(experimentTaskFeedbackDO.getId() != null, "id Required");
    return feedbackMapper.updateById(experimentTaskFeedbackDO) > 0;
  }

  @Override
  public boolean add(ExperimentTaskFeedbackDO experimentTaskFeedbackDO) {
    return feedbackMapper.insert(experimentTaskFeedbackDO) > 0;
  }

  public Optional<ExperimentTaskFeedbackDO> findByExperimentTaskId(String experimentTaskId) {
    Preconditions.checkArgument(experimentTaskId != null, "experimentTaskId Required");
    QueryWrapper<ExperimentTaskFeedbackDO> queryWrapper = new QueryWrapper<>();
    queryWrapper.eq("experiment_task_id", experimentTaskId);
    return Optional.ofNullable(feedbackMapper.selectOne(queryWrapper));
  }
}
