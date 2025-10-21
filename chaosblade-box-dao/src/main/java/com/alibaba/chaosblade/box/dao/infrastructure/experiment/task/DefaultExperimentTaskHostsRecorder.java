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

import com.alibaba.chaosblade.box.common.app.sdk.scope.Scope;
import com.alibaba.chaosblade.box.dao.model.ExperimentHostRelationDO;
import com.alibaba.chaosblade.box.dao.model.ExperimentTaskDO;
import com.alibaba.chaosblade.box.dao.repository.ExperimentHostRelationRepository;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;

/** @author haibin */
public class DefaultExperimentTaskHostsRecorder implements ExperimentTaskHostRecorder {

  @Autowired private ExperimentHostRelationRepository experimentHostRelationRepository;

  @Override
  public void record(ExperimentTaskDO experimentTaskDO, Set<Scope> hosts) {
    List<ExperimentHostRelationDO> experimentHostRelationDOS =
        hosts.stream()
            .map(
                scope -> {
                  ExperimentHostRelationDO experimentHostRelationDO =
                      ExperimentHostRelationDO.build(scope, experimentTaskDO);
                  if (scope.isK8s()
                      && !Objects.equals(
                          scope.getDeviceConfigurationId(), scope.getAppConfigurationId())) {
                    experimentHostRelationDO.setInjectionTargetType(
                        ExperimentHostRelationDO.INJECTION_TYPE_POD);
                  } else {
                    experimentHostRelationDO.setInjectionTargetType(
                        ExperimentHostRelationDO.INJECTION__TYPE_HOST);
                  }
                  experimentHostRelationDO.setInjectionTargetName(scope.getDeviceName());
                  return experimentHostRelationDO;
                })
            .collect(Collectors.toList());
    experimentHostRelationRepository.saveBatch(experimentHostRelationDOS);
  }
}
