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

package com.alibaba.chaosblade.box.service.command.expertise;

import com.alibaba.chaosblade.box.common.common.domain.ChaosError;
import com.alibaba.chaosblade.box.common.common.domain.Response;
import com.alibaba.chaosblade.box.common.common.enums.CommonErrorCode;
import com.alibaba.chaosblade.box.common.infrastructure.error.ThrowableChaosErrorWrappers;
import com.alibaba.chaosblade.box.common.infrastructure.exception.ChaosException;
import com.alibaba.chaosblade.box.dao.infrastructure.event.ExpertiseUpdateEvent;
import com.alibaba.chaosblade.box.dao.infrastructure.experiment.request.ExperimentDefinitionRequest;
import com.alibaba.chaosblade.box.dao.model.ExpertiseDO;
import com.alibaba.chaosblade.box.dao.repository.ExpertiseRepository;
import com.alibaba.chaosblade.box.service.command.experiment.ExperimentDefinitionUpdateCommand;
import com.alibaba.chaosblade.box.service.model.expertise.ExperimentExpertiseUpdateRequest;
import com.alibaba.chaosblade.box.service.model.expertise.ExpertiseBasicInfo;
import com.alibaba.chaosblade.box.service.model.expertise.ExpertiseEvaluationInfo;
import com.alibaba.chaosblade.box.service.model.expertise.ExpertiseExecutableInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/** @author haibin */
@Component
@Slf4j
public class ExpertiseUpdateCommand extends BaseExpertiseCommand<ExperimentExpertiseUpdateRequest> {
  @Autowired private ThrowableChaosErrorWrappers throwableChaosErrorWrappers;

  @Autowired private ExpertiseRepository expertiseRepository;

  @Override
  public Response<String> execute(ExperimentExpertiseUpdateRequest expertiseOperationRequest) {
    return super.execute(expertiseOperationRequest);
  }

  @Override
  protected void handleExpertise(
      ExperimentExpertiseUpdateRequest expertiseOperationRequest, ExpertiseDO expertiseDO) {
    expertiseRepository.update(expertiseDO);
    if (!expertiseOperationRequest.isSyncConfig()) {
      chaosEventDispatcher.fireEvent(new ExpertiseUpdateEvent(expertiseDO));
    }
  }

  @Override
  protected Response<String> handleThrowable(
      ExperimentExpertiseUpdateRequest expertiseOperationRequest, Throwable throwable) {
    return Response.failedWith(throwableChaosErrorWrappers.wrapper(throwable, "更新经验失败"));
  }

  @Override
  protected void handleEvaluationInfo(
      ExpertiseDO expertiseDO, ExpertiseEvaluationInfo evaluationInfo) {
    expertiseEvaluationRepository.deleteByExpertiseId(expertiseDO.getExpertiseId());
    super.handleEvaluationInfo(expertiseDO, evaluationInfo);
  }

  @Override
  protected ChaosError handleExecutionInfo(
      ExperimentExpertiseUpdateRequest expertiseOperationRequest,
      ExpertiseDO expertiseDO,
      ExpertiseExecutableInfo executableInfo) {
    fillRuntimeInfo(expertiseDO, executableInfo);
    ExperimentDefinitionRequest experimentDefinitionRequest =
        buildExperimentDefinitionRequest(
            expertiseOperationRequest, expertiseDO.getExperimentId(), executableInfo.getFlow());
    Response response =
        commandBus.syncRun(ExperimentDefinitionUpdateCommand.class, experimentDefinitionRequest);
    return response.getError();
  }

  @Override
  protected ExpertiseDO handleBasicInfo(
      ExperimentExpertiseUpdateRequest experimentExpertiseUpdateRequest,
      ExpertiseBasicInfo basicInfo) {
    ExpertiseDO expertiseDO =
        expertiseRepository
            .findById(experimentExpertiseUpdateRequest.getExpertiseId())
            .orElse(null);
    if (expertiseDO == null) {
      throw new ChaosException(CommonErrorCode.P_ARGUMENT_ILLEGAL, "没有找到经验");
    }
    expertiseDO.setName(basicInfo.getName());
    expertiseDO.setFunctionDesc(basicInfo.getFunctionDesc());
    expertiseDO.setBackgroundDesc(basicInfo.getBackgroundDesc());
    expertiseDO.setDesignConcept(basicInfo.getDesignConcept());
    expertiseDO.setName(basicInfo.getName());
    if (experimentExpertiseUpdateRequest.isSyncConfig()) {
      expertiseDO.setState(basicInfo.getState());
    }
    return expertiseDO;
  }
}
