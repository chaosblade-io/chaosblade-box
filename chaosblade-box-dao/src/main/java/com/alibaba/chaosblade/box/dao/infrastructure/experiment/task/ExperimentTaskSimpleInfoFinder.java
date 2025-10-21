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

package com.alibaba.chaosblade.box.dao.infrastructure.experiment.task;

import com.alibaba.chaosblade.box.common.infrastructure.domain.experiment.ExperimentTaskSimple;
import com.alibaba.chaosblade.box.dao.model.ExperimentDO;
import com.alibaba.chaosblade.box.dao.model.ExperimentTaskDO;
import com.alibaba.chaosblade.box.dao.repository.ActivityTargetExecutionResultRepository;
import com.alibaba.chaosblade.box.dao.repository.ActivityTaskRepository;
import com.alibaba.chaosblade.box.dao.repository.ExperimentRepository;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import java.util.concurrent.TimeUnit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/** @author haibin */
@Component
public class ExperimentTaskSimpleInfoFinder {

  @Autowired ActivityTaskManager activityTaskManager;

  @Autowired private ActivityTaskRepository activityTaskRepository;

  @Autowired private ExperimentRepository experimentRepository;

  @Autowired ActivityTargetExecutionResultRepository activityTargetExecutionResultRepository;

  private final Cache<String, ExperimentTaskSimple> experimentTaskSimpleCache =
      CacheBuilder.newBuilder()
          .expireAfterAccess(10, TimeUnit.MINUTES)
          .expireAfterWrite(20, TimeUnit.MINUTES)
          .maximumSize(200)
          .build();

  public ExperimentTaskSimple findByExperimentTaskDO(ExperimentTaskDO experimentTaskDO) {
    ExperimentDO experimentDO =
        experimentRepository.findById(experimentTaskDO.getExperimentId()).orElse(null);
    return findByExperimentTaskDO(experimentDO, experimentTaskDO);
  }

  /**
   * @param experimentTaskDO
   * @return
   */
  public ExperimentTaskSimple findByExperimentTaskDO(
      ExperimentDO experimentDO, ExperimentTaskDO experimentTaskDO) {
    ExperimentTaskSimple experimentTaskSimple =
        experimentTaskSimpleCache.getIfPresent(experimentTaskDO.getTaskId());
    if (experimentTaskSimple == null) {
      experimentTaskSimple = new ExperimentTaskSimple();
      if (experimentDO != null) {
        experimentTaskSimple.setExperimentName(experimentDO.getName());
      }
      experimentTaskSimple.setExperimentId(experimentTaskDO.getExperimentId());
      experimentTaskSimple.setTaskId(experimentTaskDO.getTaskId());
      experimentTaskSimple.setState(experimentTaskDO.getStateEnum());
      experimentTaskSimple.setResult(experimentTaskDO.getResultEnum());
      experimentTaskSimple.setStartTime(experimentTaskDO.getGmtCreate());
      experimentTaskSimple.setEndTime(experimentTaskDO.getGmtEnd());
      experimentTaskSimple.setMessage(experimentTaskDO.getErrorMessage());
      experimentTaskSimple.setHostIps(
          activityTargetExecutionResultRepository.findHostIpsByExperimentTaskId(
              experimentTaskDO.getTaskId()));
      experimentTaskSimple.setAppDescs(
          activityTaskRepository.findAppCodesByExperimentTaskId(experimentTaskDO.getTaskId()));
      experimentTaskSimpleCache.put(experimentTaskDO.getTaskId(), experimentTaskSimple);
    }
    return experimentTaskSimple;
  }
}
