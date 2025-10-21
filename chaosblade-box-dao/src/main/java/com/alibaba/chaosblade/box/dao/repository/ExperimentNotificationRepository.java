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

import com.alibaba.chaosblade.box.dao.mapper.ExperimentNotificationMapper;
import com.alibaba.chaosblade.box.dao.model.ExperimentNotificationDO;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import java.util.Optional;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/** @author haibin */
@Data
@Component
public class ExperimentNotificationRepository
    implements IRepository<String, ExperimentNotificationDO> {

  @Autowired private ExperimentNotificationMapper experimentNotificationMapper;

  @Override
  public Optional<ExperimentNotificationDO> findById(String s) {
    return Optional.ofNullable(experimentNotificationMapper.selectById(s));
  }

  @Override
  public boolean update(ExperimentNotificationDO experimentNotificationDO) {
    experimentNotificationMapper.updateById(experimentNotificationDO);
    return true;
  }

  @Override
  public boolean add(ExperimentNotificationDO experimentNotificationDO) {
    if (experimentNotificationDO.getEnabled() == null) {
      experimentNotificationDO.setEnabled(true);
    }
    experimentNotificationMapper.insert(experimentNotificationDO);
    return true;
  }

  public Optional<ExperimentNotificationDO> findByExperimentId(String experimentId) {
    return Optional.ofNullable(
        experimentNotificationMapper.selectOne(
            new QueryWrapper<ExperimentNotificationDO>()
                .eq(ExperimentNotificationDO.FIELD_EXPERIMENT_ID, experimentId)));
  }
}
