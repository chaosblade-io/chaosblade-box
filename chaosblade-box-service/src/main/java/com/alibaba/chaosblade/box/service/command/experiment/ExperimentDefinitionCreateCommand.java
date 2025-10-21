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

import com.alibaba.chaosblade.box.common.common.domain.Response;
import com.alibaba.chaosblade.box.common.common.domain.user.ChaosUser;
import com.alibaba.chaosblade.box.common.common.enums.CommonErrorCode;
import com.alibaba.chaosblade.box.common.infrastructure.domain.experiment.flow.MiniFlow;
import com.alibaba.chaosblade.box.common.infrastructure.domain.experiment.flow.MiniFlowGroup;
import com.alibaba.chaosblade.box.common.infrastructure.domain.experiment.guard.ExperimentGuard;
import com.alibaba.chaosblade.box.dao.infrastructure.event.ExperimentCreatedEvent;
import com.alibaba.chaosblade.box.dao.infrastructure.experiment.request.ExperimentDefinitionRequest;
import com.alibaba.chaosblade.box.dao.infrastructure.model.ExperimentDefinitionContext;
import com.alibaba.chaosblade.box.dao.model.ExperimentDO;
import com.alibaba.chaosblade.box.dao.model.MiniFlowDO;
import com.alibaba.chaosblade.box.dao.model.MiniFlowGroupDO;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class ExperimentDefinitionCreateCommand extends AbstractExperimentDefinitionCommand {

  @Override
  protected void handleMiniFlowGroup(
      ExperimentDefinitionContext experimentDefinitionContext,
      ExperimentDO experimentDO,
      MiniFlowGroup miniFlowGroup) {
    MiniFlowGroupDO miniFlowGroupDO =
        experimentMiniFlowGroupRepository.findById(miniFlowGroup.getGroupId()).orElse(null);
    if (miniFlowGroupDO == null) {
      addMiniGroup(experimentDefinitionContext, experimentDO, miniFlowGroup);
    }
  }

  @Override
  protected void handleGuards(ExperimentDO experimentDO, List<ExperimentGuard> experimentGuards) {
    for (ExperimentGuard experimentGuard : experimentGuards) {
      addGuard(experimentDO, experimentGuard);
    }
  }

  @Override
  protected MiniFlowDO addMiniFlowIfNotExist(MiniFlow miniFlow, MiniFlowGroupDO miniFlowGroupDO) {
    miniFlow.setFlowId(null);
    return super.addMiniFlowIfNotExist(miniFlow, miniFlowGroupDO);
  }

  @Override
  protected void fireEvents(ChaosUser user, ExperimentDO experimentDO) {
    if (!experimentDO.getIsTemplate()) {
      chaosEventDispatcher.fireEvent(new ExperimentCreatedEvent(user, experimentDO));
    }
  }

  @Override
  protected void saveExperimentActivityDefinitions(
      ExperimentDefinitionContext experimentDefinitionContext) {
    if (experimentDefinitionContext.getExperimentDefinitionRequest().isOldAdapter()) {
      experimentFlowDefinitionManager.updateFlowDefinition(
          experimentDefinitionContext.getExperimentDO(),
          experimentDefinitionContext.getExperimentFlowDefinition());
    } else {
      experimentFlowDefinitionManager.addFlowDefinition(
          experimentDefinitionContext.getExperimentDO(),
          experimentDefinitionContext.getExperimentFlowDefinition());
    }
  }

  @Override
  protected Response<ExperimentDO> getExperimentDO(
      ExperimentDefinitionRequest experimentDefinitionRequest) {
    Response<ExperimentDO> experimentDOResponse =
        super.getExperimentDO(experimentDefinitionRequest);
    if (experimentDOResponse.isSuccess()) {
      if (experimentDOResponse.getResult().isNewExperimentDefinition()) {
        return Response.failedWith(CommonErrorCode.B_DUMP_EXPERIMENT_DEFINITION);
      }
    }
    return experimentDOResponse;
  }
}
