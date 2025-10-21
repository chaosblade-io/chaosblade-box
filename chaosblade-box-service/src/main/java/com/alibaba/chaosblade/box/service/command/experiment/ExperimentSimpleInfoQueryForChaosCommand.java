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
import com.alibaba.chaosblade.box.common.infrastructure.domain.experiment.ExperimentBasicInfo;
import com.alibaba.chaosblade.box.common.infrastructure.domain.experiment.response.ExperimentFlowSimpleInfo;
import com.alibaba.chaosblade.box.common.infrastructure.domain.experiment.response.ExperimentSimpleInfo;
import com.alibaba.chaosblade.box.dao.infrastructure.checker.ExperimentChecker;
import com.alibaba.chaosblade.box.dao.infrastructure.experiment.task.ActivityQueryManager;
import com.alibaba.chaosblade.box.dao.model.ExperimentActivityDO;
import com.alibaba.chaosblade.box.dao.model.ExperimentDO;
import com.alibaba.chaosblade.box.service.infrastructure.convertor.ExperimentToBasicInfoConverter;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/** @author sunpeng */
@Component
public class ExperimentSimpleInfoQueryForChaosCommand
    extends SpringBeanCommand<String, Response<ExperimentSimpleInfo>> {

  @Autowired private ExperimentChecker experimentChecker;

  @Autowired private ExperimentToBasicInfoConverter experimentToBasicInfoConverter;

  @Autowired private ActivityQueryManager activityQueryManager;

  @Override
  public Response<ExperimentSimpleInfo> execute(String experimentId) {

    ExperimentSimpleInfo experimentSimpleInfo = new ExperimentSimpleInfo();
    experimentSimpleInfo.setExperimentId(experimentId);
    Response<ExperimentDO> experimentDOResponse =
        experimentChecker.assertExperimentExist(experimentId);
    if (!experimentDOResponse.isSuccess()) {
      return Response.failedWith(experimentDOResponse.getError());
    }
    ExperimentDO experimentDO = experimentDOResponse.getResult();
    ExperimentBasicInfo experimentBasicInfo = experimentToBasicInfoConverter.convert(experimentDO);
    experimentSimpleInfo.setBasicInfo(experimentBasicInfo);

    if (experimentDO.isDraft()) {
      return Response.okWithData(experimentSimpleInfo);
    }
    List<ExperimentActivityDO> experimentActivityDOS =
        activityQueryManager.findActivitiesByExperimentId(experimentId);
    dealActivityDO(experimentSimpleInfo, experimentActivityDOS);

    return Response.okWithData(experimentSimpleInfo);
  }

  private void dealActivityDO(
      ExperimentSimpleInfo experimentSimpleInfo, List<ExperimentActivityDO> experimentActivityDOS) {
    List<ExperimentFlowSimpleInfo> prepare = new ArrayList<>();
    List<ExperimentFlowSimpleInfo> attack = new ArrayList<>();
    List<ExperimentFlowSimpleInfo> check = new ArrayList<>();
    List<ExperimentFlowSimpleInfo> recover = new ArrayList<>();

    for (ExperimentActivityDO experimentActivityDO : experimentActivityDOS) {

      switch (experimentActivityDO.getPhase()) {
        case PREPARE:
          prepare.add(
              new ExperimentFlowSimpleInfo(
                  experimentActivityDO.getAppCode(), experimentActivityDO.getActivityName()));
          break;
        case ATTACK:
          attack.add(
              new ExperimentFlowSimpleInfo(
                  experimentActivityDO.getAppCode(), experimentActivityDO.getActivityName()));
          break;
        case CHECK:
          check.add(
              new ExperimentFlowSimpleInfo(
                  experimentActivityDO.getAppCode(), experimentActivityDO.getActivityName()));
          break;
        case RECOVER:
          recover.add(
              new ExperimentFlowSimpleInfo(
                  experimentActivityDO.getAppCode(), experimentActivityDO.getActivityName()));
          break;
      }
    }
    experimentSimpleInfo.setPrepare(prepare);
    experimentSimpleInfo.setAttack(attack);
    experimentSimpleInfo.setCheck(check);
    experimentSimpleInfo.setRecover(recover);
  }
}
