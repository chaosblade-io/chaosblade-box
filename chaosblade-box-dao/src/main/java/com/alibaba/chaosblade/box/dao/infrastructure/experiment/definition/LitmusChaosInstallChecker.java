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

package com.alibaba.chaosblade.box.dao.infrastructure.experiment.definition;

import com.alibaba.chaosblade.box.common.app.sdk.scope.Host;
import com.alibaba.chaosblade.box.common.common.domain.definition.ExperimentActivityDefinition;
import com.alibaba.chaosblade.box.common.common.enums.CommonErrorCode;
import com.alibaba.chaosblade.box.common.common.util.MiniAppUtils;
import com.alibaba.chaosblade.box.common.infrastructure.exception.ChaosException;
import com.alibaba.chaosblade.box.dao.repository.ChaosToolsRepository;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

/** @author sunpeng */
@Component
@Order(value = 1200)
public class LitmusChaosInstallChecker implements ActivityDefinitionChecker {

  @Autowired private ChaosToolsRepository chaosToolsRepository;

  @Override
  public void check(ExperimentActivityDefinition experimentActivityDefinition, boolean fromApi)
      throws ActivityDefinitionIllegalException {
    String appCode = experimentActivityDefinition.getAppCode();
    if (MiniAppUtils.isFromLitmusChaos(appCode)) {
      List<Host> hostList = experimentActivityDefinition.getScope();
      Set<String> clusterIds =
          hostList.stream().map(Host::getClusterId).distinct().collect(Collectors.toSet());
      for (String clusterId : clusterIds) {
        chaosToolsRepository
            .selectByClusterId(clusterId)
            .orElseThrow(
                () -> new ChaosException(CommonErrorCode.B_LITMUS_CHAOS_NOT_INSTALL_ERROR));
      }
    }
  }
}
