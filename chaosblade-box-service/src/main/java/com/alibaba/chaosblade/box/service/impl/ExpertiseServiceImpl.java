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

package com.alibaba.chaosblade.box.service.impl;

import com.alibaba.chaosblade.box.common.commands.CommandBus;
import com.alibaba.chaosblade.box.common.common.domain.PageQueryResponse;
import com.alibaba.chaosblade.box.common.common.domain.Response;
import com.alibaba.chaosblade.box.common.common.domain.experiment.BaseExperimentRequest;
import com.alibaba.chaosblade.box.common.infrastructure.domain.experiment.request.ExperimentExpertiseQueryRequest;
import com.alibaba.chaosblade.box.common.infrastructure.domain.experiment.request.ExperimentFlowInitByExpertiseRequest;
import com.alibaba.chaosblade.box.common.infrastructure.domain.experiment.response.ExperimentFlowInfo;
import com.alibaba.chaosblade.box.common.infrastructure.domain.experiment.response.ExperimentInfo;
import com.alibaba.chaosblade.box.service.ExpertiseService;
import com.alibaba.chaosblade.box.service.command.expertise.*;
import com.alibaba.chaosblade.box.service.model.expertise.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/** @author haibin */
@Component
public class ExpertiseServiceImpl implements ExpertiseService {

  @Autowired private CommandBus commandBus;

  @Override
  public Response<String> createExpertise(
      ExperimentExpertiseCreateRequest experimentExpertiseCreateRequest) {
    return commandBus.syncRun(ExpertiseCreateCommand.class, experimentExpertiseCreateRequest);
  }

  @Override
  public Response<String> convertExperimentToExpertise(
      BaseExperimentRequest baseExperimentRequest) {
    return commandBus.syncRun(ExperimentToExpertiseConvertCommand.class, baseExperimentRequest);
  }

  @Override
  public Response disableExpertise(
      ExperimentExpertiseQueryRequest experimentExpertiseQueryRequest) {
    return commandBus.syncRun(DisableExpertiseCommand.class, experimentExpertiseQueryRequest);
  }

  @Override
  public Response enableExpertise(ExperimentExpertiseQueryRequest experimentExpertiseQueryRequest) {
    return commandBus.syncRun(EnableExpertiseCommand.class, experimentExpertiseQueryRequest);
  }

  @Override
  public Response<String> updateExpertise(
      ExperimentExpertiseUpdateRequest experimentTemplateUpdateRequest) {
    return commandBus.syncRun(ExpertiseUpdateCommand.class, experimentTemplateUpdateRequest);
  }

  @Override
  public Response deleteExpertise(ExperimentExpertiseQueryRequest experimentExpertiseQueryRequest) {
    return commandBus.syncRun(DeleteExpertiseCommand.class, experimentExpertiseQueryRequest);
  }

  @Override
  public Response<ExpertiseInfo> cloneExpertise(ExpertiseCloneRequest expertiseCloneRequest) {
    return commandBus.syncRun(CloneExpertiseCommand.class, expertiseCloneRequest);
  }

  @Override
  public Response<ExpertiseInfo> queryExpertiseDetails(
      ExperimentExpertiseQueryRequest experimentExpertiseQueryRequest) {
    return commandBus.syncRun(QueryExpertiseDetailsCommand.class, experimentExpertiseQueryRequest);
  }

  @Override
  public Response<PageQueryResponse<ExpertiseView>> searchExpertise(
      ExpertiseSearchRequest expertiseSearchRequest) {
    return commandBus.syncRun(ExpertiseSearchCommand.class, expertiseSearchRequest);
  }

  @Override
  public Response<PageQueryResponse<AdminExpertiseView>> listExpertise(
      ExpertiseSearchRequest expertiseSearchRequest) {
    return commandBus.syncRun(PageableQueryExpertiseListCommand.class, expertiseSearchRequest);
  }

  @Override
  public Response<ExperimentFlowInfo> initFlowByExpertise(
      ExperimentFlowInitByExpertiseRequest experimentExpertiseQueryRequest) {
    return commandBus.syncRun(InitFlowByExpertiseCommand.class, experimentExpertiseQueryRequest);
  }

  @Override
  public Response<ExperimentInfo> initExperimentByExpertise(
      ExperimentFlowInitByExpertiseRequest experimentExpertiseQueryRequest) {
    return commandBus.syncRun(
        InitExperimentByExpertiseCommand.class, experimentExpertiseQueryRequest);
  }
}
