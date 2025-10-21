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

package com.alibaba.chaosblade.box.dao.infrastructure.experiment.guard;

import com.alibaba.chaosblade.box.common.app.sdk.constants.PhaseType;
import com.alibaba.chaosblade.box.common.app.sdk.scope.Host;
import com.alibaba.chaosblade.box.dao.model.ActivityTaskDO;
import com.alibaba.chaosblade.box.dao.model.ExperimentTaskDO;
import com.alibaba.chaosblade.box.dao.repository.ActivityTaskRepository;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.springframework.beans.factory.annotation.Autowired;

/** @author haibin */
public class DefaultExperimentGuardHostsProvider implements ExperimentGuardHostsProvider {

  @Autowired private ActivityTaskRepository activityTaskRepository;

  @Override
  public List<Host> provide(
      ExperimentGuardInstanceExecutionRequest experimentGuardInstanceExecutionRequest) {
    ExperimentTaskDO experimentTaskDO =
        experimentGuardInstanceExecutionRequest.getExperimentTaskDO();
    List<ActivityTaskDO> activityTaskDOS =
        activityTaskRepository.findByExperimentTaskIdAndPhase(
            experimentTaskDO.getTaskId(), PhaseType.ATTACK);
    return activityTaskDOS.stream()
        .flatMap(
            (Function<ActivityTaskDO, Stream<Host>>)
                activityTaskDO -> {
                  try {
                    return activityTaskDO.getRunParam().getScope().stream();
                  } catch (Exception ex) {
                    return Stream.empty();
                  }
                })
        .distinct()
        .collect(Collectors.toList());
  }
}
