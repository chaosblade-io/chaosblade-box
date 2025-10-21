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

import com.alibaba.chaosblade.box.common.commands.CommandExecutorConstant;
import com.alibaba.chaosblade.box.common.commands.SpringBeanCommand;
import com.alibaba.chaosblade.box.common.common.domain.ChaosError;
import com.alibaba.chaosblade.box.common.common.domain.Response;
import com.alibaba.chaosblade.box.common.common.enums.CommonErrorCode;
import com.alibaba.chaosblade.box.common.infrastructure.domain.experiment.request.ExperimentDeleteRequest;
import com.alibaba.chaosblade.box.dao.infrastructure.event.ChaosEventDispatcher;
import com.alibaba.chaosblade.box.dao.infrastructure.event.ExperimentDeletedEvent;
import com.alibaba.chaosblade.box.dao.model.ExperimentDO;
import com.alibaba.chaosblade.box.dao.query.ExperimentQuery;
import com.alibaba.chaosblade.box.dao.repository.ExperimentRepository;
import com.alibaba.chaosblade.box.service.manager.tag.TagManager;
import com.google.common.base.Strings;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/** @author haibin */
@Component
public class ExperimentDeleteCommand
    extends SpringBeanCommand<ExperimentDeleteRequest, Response<Void>> {

  @Autowired private ExperimentRepository experimentRepository;

  @Autowired private TagManager tagManager;

  @Autowired private ChaosEventDispatcher chaosEventDispatcher;

  @Override
  public String getCommandExecutorName() {
    return CommandExecutorConstant.EXECUTOR_EXPERIMENT;
  }

  @Override
  public Response<Void> execute(ExperimentDeleteRequest experimentDeleteRequest) {
    String experimentId = experimentDeleteRequest.getExperimentId();
    Optional<ExperimentDO> experimentDOOptional = experimentRepository.findById(experimentId);
    if (!experimentDOOptional.isPresent()) {
      return Response.failedWith(new ChaosError(CommonErrorCode.P_EXPERIMENT_NOT_FOUND));
    }

    ExperimentDO experimentDO = experimentDOOptional.get();
    if (experimentDO.isRunning()) {
      return Response.failedWith(new ChaosError(CommonErrorCode.B_EXPERIMENT_ALREADY_RUNNING));
    }

    if (Strings.isNullOrEmpty(experimentId)) {
      return Response.ok();
    }

    ExperimentQuery query = new ExperimentQuery();
    query.setExperimentId(experimentId);
    boolean success = experimentRepository.logicDelete(query);
    if (success && !experimentDO.getIsTemplate()) {
      chaosEventDispatcher.fireEvent(
          new ExperimentDeletedEvent(
              experimentDeleteRequest.getUser(), experimentDO.getExperimentId()));
      tagManager.unbindTagsByExperimentId(experimentId);
    }
    return Response.ok();
  }
}
