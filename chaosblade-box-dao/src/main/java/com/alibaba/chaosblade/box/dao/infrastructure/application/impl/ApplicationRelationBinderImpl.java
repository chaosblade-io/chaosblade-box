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

package com.alibaba.chaosblade.box.dao.infrastructure.application.impl;

import com.alibaba.chaosblade.box.common.common.enums.StateEnum;
import com.alibaba.chaosblade.box.common.experiment.model.AppNameAndIdPair;
import com.alibaba.chaosblade.box.common.infrastructure.util.CollectionUtil;
import com.alibaba.chaosblade.box.dao.infrastructure.application.ApplicationRelationBinder;
import com.alibaba.chaosblade.box.dao.model.ApplicationRelationDO;
import com.alibaba.chaosblade.box.dao.repository.ApplicationRelationRepository;
import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/** @author haibin */
@Component
public class ApplicationRelationBinderImpl implements ApplicationRelationBinder {

  @Autowired private ApplicationRelationRepository applicationRelationRepository;

  @Override
  public void addOrUpdateApplicationExperimentRelations(
      String experimentId, Set<AppNameAndIdPair> appNameAndIdPairs) {
    applicationRelationRepository.deleteRelationByOuterId(
        experimentId, ApplicationRelationDO.RELATION_EXPERIMENT);
    if (!appNameAndIdPairs.isEmpty()) {
      appNameAndIdPairs.forEach(
          appNameAndIdPair -> {
            ApplicationRelationDO applicationRelationDO = new ApplicationRelationDO();
            applicationRelationDO.setAppId(Long.valueOf(appNameAndIdPair.getAppId()));
            applicationRelationDO.setOuterId(experimentId);
            applicationRelationDO.setRelationType(ApplicationRelationDO.RELATION_EXPERIMENT);
            applicationRelationRepository.add(applicationRelationDO);
          });
    }
  }

  @Override
  public void addApplicationExperimentTaskRelations(
      String experimentTaskId, Set<AppNameAndIdPair> appNameAndIdPairs) {
    if (CollectionUtil.isNullOrEmpty(appNameAndIdPairs)) {
      return;
    }
    appNameAndIdPairs.forEach(
        appNameAndIdPair -> {
          ApplicationRelationDO applicationRelationDO = new ApplicationRelationDO();
          applicationRelationDO.setAppId(Long.valueOf(appNameAndIdPair.getAppId()));
          applicationRelationDO.setOuterId(experimentTaskId);
          applicationRelationDO.setTaskState(StateEnum.RUNNING.getValue());
          applicationRelationDO.setRelationType(ApplicationRelationDO.RELATION_EXPERIMENT_TASK);
          applicationRelationRepository.add(applicationRelationDO);
        });
  }
}
