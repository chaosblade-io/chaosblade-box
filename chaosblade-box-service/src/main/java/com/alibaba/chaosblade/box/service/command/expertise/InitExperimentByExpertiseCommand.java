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
import com.alibaba.chaosblade.box.common.common.domain.user.ChaosUser;
import com.alibaba.chaosblade.box.common.common.enums.ExperimentStateEnum;
import com.alibaba.chaosblade.box.common.experiment.clear.ExperimentFlowInfoClear;
import com.alibaba.chaosblade.box.common.infrastructure.domain.experiment.ExperimentBasicInfo;
import com.alibaba.chaosblade.box.common.infrastructure.domain.experiment.request.ExperimentExpertiseQueryRequest;
import com.alibaba.chaosblade.box.common.infrastructure.domain.experiment.request.ExperimentFlowInitByExpertiseRequest;
import com.alibaba.chaosblade.box.common.infrastructure.domain.experiment.response.ExperimentFlowInfo;
import com.alibaba.chaosblade.box.common.infrastructure.domain.experiment.response.ExperimentInfo;
import com.alibaba.chaosblade.box.dao.model.ExperimentDO;
import com.alibaba.chaosblade.box.dao.repository.ExperimentRepository;
import com.alibaba.chaosblade.box.service.model.expertise.ExpertiseBasicInfo;
import com.alibaba.chaosblade.box.service.model.expertise.ExpertiseInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/** @author haibin */
@Component
public class InitExperimentByExpertiseCommand
    extends SpringBeanCommand<ExperimentFlowInitByExpertiseRequest, Response<ExperimentInfo>> {

  @Autowired private ExperimentRepository experimentRepository;

  @Override
  public Response<ExperimentInfo> execute(
      ExperimentFlowInitByExpertiseRequest experimentFlowInitByExpertiseRequest) {
    ExperimentExpertiseQueryRequest experimentExpertiseQueryRequest =
        new ExperimentExpertiseQueryRequest();
    experimentExpertiseQueryRequest.setExpertiseId(
        experimentFlowInitByExpertiseRequest.getExpertiseId());
    experimentExpertiseQueryRequest.setUser(experimentFlowInitByExpertiseRequest.getUser());
    experimentExpertiseQueryRequest.setNamespace(
        experimentFlowInitByExpertiseRequest.getNamespace());
    Response<ExpertiseInfo> expertiseInfoResponse =
        commandBus.syncRun(QueryExpertiseDetailsCommand.class, experimentExpertiseQueryRequest);
    if (!expertiseInfoResponse.isSuccess()) {
      return Response.failedWith(expertiseInfoResponse.getError());
    }
    ExperimentInfo experimentInfo = new ExperimentInfo();
    ExpertiseInfo expertiseInfo = expertiseInfoResponse.getResult();
    ExperimentBasicInfo experimentBasicInfo =
        createExperimentBasicInfo(experimentFlowInitByExpertiseRequest, expertiseInfo);
    ExperimentFlowInfo experimentFlowInfo =
        createExperimentFlowInfo(experimentBasicInfo.getExperimentId(), expertiseInfo);
    ExperimentFlowInfoClear.clearAllIds(experimentFlowInfo);
    experimentInfo.setBasicInfo(experimentBasicInfo);
    experimentInfo.setFlowInfo(experimentFlowInfo);
    experimentInfo.setExperimentId(experimentBasicInfo.getExperimentId());
    return Response.okWithData(experimentInfo);
  }

  private ExperimentFlowInfo createExperimentFlowInfo(
      String experimentId, ExpertiseInfo expertiseInfo) {
    ExperimentFlowInfo experimentFlowInfo = expertiseInfo.getExecutableInfo().getFlow();
    experimentFlowInfo.setExperimentId(experimentId);
    return experimentFlowInfo;
  }

  private ExperimentBasicInfo createExperimentBasicInfo(
      ExperimentFlowInitByExpertiseRequest experimentFlowInitByExpertiseRequest,
      ExpertiseInfo expertiseInfo) {
    ExperimentBasicInfo experimentBasicInfo = new ExperimentBasicInfo();
    ExpertiseBasicInfo expertiseBasicInfo = expertiseInfo.getBasicInfo();
    experimentBasicInfo.setDescription(expertiseBasicInfo.getFunctionDesc());
    experimentBasicInfo.setName(expertiseBasicInfo.getName());

    ChaosUser user = experimentFlowInitByExpertiseRequest.getUser();
    if (null != user) {
      experimentBasicInfo.setOwnerUserId(user.getUserId());
    }
    experimentBasicInfo.setNamespace(experimentFlowInitByExpertiseRequest.getNamespace());
    ExperimentDO experimentDO =
        createInvisibleExperiment(experimentFlowInitByExpertiseRequest, expertiseBasicInfo);
    experimentBasicInfo.setExperimentId(experimentDO.getExperimentId());
    return experimentBasicInfo;
  }

  private ExperimentDO createInvisibleExperiment(
      ExperimentFlowInitByExpertiseRequest experimentFlowInitByExpertiseRequest,
      ExpertiseBasicInfo expertiseBasicInfo) {
    ExperimentDO experimentDO = new ExperimentDO();
    experimentDO.setName(expertiseBasicInfo.getName());
    ChaosUser user = experimentFlowInitByExpertiseRequest.getUser();
    experimentDO.setUserId(user.getUserId());
    experimentDO.setState(ExperimentStateEnum.INVISIBLE.getValue());
    experimentDO.setDescription(expertiseBasicInfo.getFunctionDesc());
    experimentDO.setNamespace(experimentFlowInitByExpertiseRequest.getNamespace());
    experimentRepository.add(experimentDO);
    return experimentDO;
  }
}
