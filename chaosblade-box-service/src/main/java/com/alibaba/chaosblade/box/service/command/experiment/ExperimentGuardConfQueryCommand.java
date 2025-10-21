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

package com.alibaba.chaosblade.box.service.command.experiment;

import com.alibaba.chaosblade.box.common.commands.SpringBeanCommand;
import com.alibaba.chaosblade.box.common.infrastructure.domain.experiment.guard.ExperimentGuard;
import com.alibaba.chaosblade.box.common.infrastructure.domain.experiment.guard.ExperimentGuardConfiguration;
import com.alibaba.chaosblade.box.dao.model.ExperimentGuardDO;
import com.alibaba.chaosblade.box.dao.repository.ExperimentGuardRepository;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/** @author haibin */
@Component
public class ExperimentGuardConfQueryCommand
    extends SpringBeanCommand<String, ExperimentGuardConfiguration> {

  @Autowired private ExperimentGuardRepository experimentGuardRepository;

  @Override
  public ExperimentGuardConfiguration execute(String experimentId) {
    List<ExperimentGuardDO> experimentGuardDOS =
        experimentGuardRepository.findByExperimentId(experimentId);
    ExperimentGuardConfiguration experimentGuardConfiguration = new ExperimentGuardConfiguration();
    experimentGuardConfiguration.setGuards(
        experimentGuardDOS.stream()
            .map(
                new Function<ExperimentGuardDO, ExperimentGuard>() {
                  @Override
                  public ExperimentGuard apply(ExperimentGuardDO experimentGuardDO) {
                    ExperimentGuard experimentGuard = new ExperimentGuard();
                    experimentGuard.setAppCode(experimentGuardDO.getAppCode());
                    experimentGuard.setGuardId(experimentGuardDO.getGuardId());
                    experimentGuard.setArguments(experimentGuardDO.getArgument().getArguments());
                    experimentGuard.setFields(experimentGuardDO.getArgument().getFields());
                    experimentGuard.setTolerance(experimentGuardDO.getArgument().getTolerance());
                    experimentGuard.setName(experimentGuardDO.getName());
                    experimentGuard.setActionType(experimentGuardDO.getActionType());
                    return experimentGuard;
                  }
                })
            .collect(Collectors.toList()));
    return experimentGuardConfiguration;
  }
}
