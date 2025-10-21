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
import com.alibaba.chaosblade.box.dao.infrastructure.checker.ExperimentChecker;
import com.alibaba.chaosblade.box.dao.model.ExperimentDO;
import com.alibaba.chaosblade.box.service.infrastructure.convertor.ExperimentToBasicInfoConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/** @author haibin */
@Component
public class ExperimentBasicInfoQueryCommand
    extends SpringBeanCommand<BaseExperimentRequest, Response<ExperimentBasicInfo>> {

  @Autowired private ExperimentChecker experimentChecker;

  @Autowired private ExperimentToBasicInfoConverter experimentToBasicInfoConverter;

  @Override
  public Response<ExperimentBasicInfo> execute(BaseExperimentRequest baseExperimentRequest) {
    String experimentId = baseExperimentRequest.getExperimentId();
    Response<ExperimentDO> experimentDOResponse =
        experimentChecker.assertExperimentExist(experimentId);
    if (!experimentDOResponse.isSuccess()) {
      return Response.failedWith(experimentDOResponse.getError());
    }
    ExperimentDO experimentDO = experimentDOResponse.getResult();
    return Response.okWithData(experimentToBasicInfoConverter.convert(experimentDO));
  }
}
