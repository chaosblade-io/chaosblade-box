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
import com.alibaba.chaosblade.box.common.common.domain.Response;
import com.alibaba.chaosblade.box.common.common.domain.experiment.BaseExperimentRequest;
import com.alibaba.chaosblade.box.common.infrastructure.domain.experiment.ExperimentBasicInfo;
import com.alibaba.chaosblade.box.common.infrastructure.domain.experiment.response.ExperimentFlowInfo;
import com.alibaba.chaosblade.box.common.infrastructure.domain.experiment.response.ExperimentInfo;
import com.alibaba.chaosblade.box.dao.infrastructure.checker.ExperimentChecker;
import com.alibaba.chaosblade.box.dao.model.ExperimentDO;
import com.alibaba.chaosblade.box.service.infrastructure.convertor.ExperimentToBasicInfoConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/** @author haibin.lhb */
@Component
public class ExperimentInfoQueryCommand
    extends SpringBeanCommand<BaseExperimentRequest, Response<ExperimentInfo>> {

  @Autowired private ExperimentChecker experimentChecker;

  @Autowired private ExperimentToBasicInfoConverter experimentToBasicInfoConverter;

  @Override
  public Response<ExperimentInfo> execute(BaseExperimentRequest request) {
    ExperimentInfo experimentInfo = new ExperimentInfo();
    experimentInfo.setExperimentId(request.getExperimentId());
    Response<ExperimentDO> experimentDOResponse =
        experimentChecker.assertExperimentExistByIdAndNamespace(
            request.getExperimentId(), request.getNamespace());
    if (!experimentDOResponse.isSuccess()) {
      return Response.failedWith(experimentDOResponse.getError());
    }
    ExperimentDO experimentDO = experimentDOResponse.getResult();
    ExperimentBasicInfo experimentBasicInfo = experimentToBasicInfoConverter.convert(experimentDO);
    experimentInfo.setBasicInfo(experimentBasicInfo);
    ExperimentFlowInfo experimentFlowInfo =
        commandBus.syncRun(ExperimentFlowDefinitionQueryCommand.class, experimentDO);
    experimentInfo.setFlowInfo(experimentFlowInfo);
    experimentInfo.setExperimentAppRisks(
        commandBus
            .syncRun(ExperimentAppRiskCommand.class, experimentDO.getExperimentId())
            .getResult());
    return Response.okWithData(experimentInfo);
  }
}
