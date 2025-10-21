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
import com.alibaba.chaosblade.box.common.experiment.clear.ExperimentFlowInfoClear;
import com.alibaba.chaosblade.box.common.infrastructure.domain.experiment.request.ExperimentExpertiseQueryRequest;
import com.alibaba.chaosblade.box.service.model.expertise.ExpertiseCloneRequest;
import com.alibaba.chaosblade.box.service.model.expertise.ExpertiseInfo;
import org.springframework.stereotype.Component;

/** @author haibin */
@Component
public class CloneExpertiseCommand
    extends SpringBeanCommand<ExpertiseCloneRequest, Response<ExpertiseInfo>> {

  @Override
  public Response<ExpertiseInfo> execute(ExpertiseCloneRequest expertiseCloneRequest) {
    ExperimentExpertiseQueryRequest experimentExpertiseQueryRequest =
        new ExperimentExpertiseQueryRequest();
    experimentExpertiseQueryRequest.setExpertiseId(expertiseCloneRequest.getExpertiseId());
    Response<ExpertiseInfo> response =
        commandBus.syncRun(QueryExpertiseDetailsCommand.class, experimentExpertiseQueryRequest);
    if (!response.isSuccess()) {
      return Response.failedWith(response.getError());
    }
    ExperimentFlowInfoClear.clearAllIds(response.getResult().getExecutableInfo().getFlow());
    ExpertiseInfo expertiseInfo = response.getResult();
    expertiseInfo.setExpertiseId(null);
    return Response.okWithData(expertiseInfo);
  }
}
