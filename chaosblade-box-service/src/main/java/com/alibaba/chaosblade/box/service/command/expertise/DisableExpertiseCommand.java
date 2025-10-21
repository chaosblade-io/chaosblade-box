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

import com.alibaba.chaosblade.box.common.commands.SpringBeanCommand;
import com.alibaba.chaosblade.box.common.common.domain.Response;
import com.alibaba.chaosblade.box.common.common.enums.CommonErrorCode;
import com.alibaba.chaosblade.box.common.infrastructure.domain.experiment.request.ExperimentExpertiseQueryRequest;
import com.alibaba.chaosblade.box.dao.infrastructure.event.ChaosEventDispatcher;
import com.alibaba.chaosblade.box.dao.infrastructure.event.ExpertiseUpdateEvent;
import com.alibaba.chaosblade.box.dao.infrastructure.model.ExpertiseConstant;
import com.alibaba.chaosblade.box.dao.model.ExpertiseDO;
import com.alibaba.chaosblade.box.dao.repository.ExpertiseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/** @author haibin */
@Component
public class DisableExpertiseCommand
    extends SpringBeanCommand<ExperimentExpertiseQueryRequest, Response> {
  @Autowired private ExpertiseRepository expertiseRepository;

  @Autowired private ChaosEventDispatcher chaosEventDispatcher;

  @Override
  public Response execute(ExperimentExpertiseQueryRequest experimentExpertiseQueryRequest) {
    ExpertiseDO expertiseDO =
        expertiseRepository.findById(experimentExpertiseQueryRequest.getExpertiseId()).orElse(null);
    if (expertiseDO == null) {
      return Response.failedWith(CommonErrorCode.P_NOT_FOUND_EXPERTISE);
    } else {
      expertiseDO.setState(ExpertiseConstant.STATE_DISABLE);
      expertiseRepository.update(expertiseDO);
      chaosEventDispatcher.fireEvent(new ExpertiseUpdateEvent(expertiseDO));
    }
    return Response.ok();
  }
}
