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

import com.alibaba.chaosblade.box.common.app.sdk.scope.Host;
import com.alibaba.chaosblade.box.common.commands.SpringBeanCommand;
import com.alibaba.chaosblade.box.common.common.domain.ChaosError;
import com.alibaba.chaosblade.box.common.common.domain.Response;
import com.alibaba.chaosblade.box.common.common.domain.definition.ExperimentActivityDefinition;
import com.alibaba.chaosblade.box.common.common.enums.CommonErrorCode;
import com.alibaba.chaosblade.box.common.infrastructure.domain.Hosts;
import com.alibaba.chaosblade.box.common.infrastructure.domain.experiment.request.ExperimentHostUpdateRequest;
import com.alibaba.chaosblade.box.dao.infrastructure.checker.ExperimentChecker;
import com.alibaba.chaosblade.box.dao.infrastructure.checker.definition.DefaultMiniGroupCheckNode;
import com.alibaba.chaosblade.box.dao.model.ExperimentActivityDO;
import com.alibaba.chaosblade.box.dao.model.ExperimentDO;
import com.alibaba.chaosblade.box.dao.model.MiniFlowDO;
import com.alibaba.chaosblade.box.dao.model.MiniFlowGroupDO;
import com.alibaba.chaosblade.box.dao.query.ExperimentActivityQuery;
import com.alibaba.chaosblade.box.dao.repository.ActivityRepository;
import com.alibaba.chaosblade.box.dao.repository.ExperimentMiniFlowGroupRepository;
import com.alibaba.chaosblade.box.dao.repository.ExperimentMiniFlowRepository;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/** @author sunpeng */
@Slf4j
@Component
public class ExperimentUpdateHostCommand
    extends SpringBeanCommand<ExperimentHostUpdateRequest, Response<Boolean>> {

  @Autowired private ExperimentChecker experimentChecker;

  @Autowired private ExperimentMiniFlowGroupRepository experimentMiniFlowGroupRepository;

  @Autowired private ExperimentMiniFlowRepository experimentMiniFlowRepository;

  @Autowired private ActivityRepository activityRepository;

  @Autowired private DefaultMiniGroupCheckNode defaultMiniGroupCheckNode;

  @Override
  public Response<Boolean> execute(ExperimentHostUpdateRequest experimentHostUpdateRequest) {
    Response<ExperimentDO> response =
        experimentChecker.assertExperimentExist(experimentHostUpdateRequest.getExperimentId());
    if (!response.isSuccess()) {
      return Response.failedWith(response.getError());
    }
    ExperimentDO experimentDO = response.getResult();
    if (experimentDO.isRunning()) {
      return Response.failedWith(new ChaosError(CommonErrorCode.B_EXPERIMENT_ALREADY_RUNNING));
    }
    Optional<MiniFlowGroupDO> optional =
        experimentMiniFlowGroupRepository.findById(experimentHostUpdateRequest.getMiniGroupId());
    if (!optional.isPresent()) {
      return Response.failedWith(CommonErrorCode.P_ERROR_CODE_MINI_GROUP_ID_NOT_EXIST);
    }
    MiniFlowGroupDO miniFlowGroupDO = optional.get();
    List<Host> hostList = experimentHostUpdateRequest.getHosts();
    // 校验机器
    defaultMiniGroupCheckNode.checkHost(hostList, experimentHostUpdateRequest.getSelectType());
    // 更新数据 分别更新group和activity
    Hosts hosts = miniFlowGroupDO.getHosts();
    hosts.setContent(experimentHostUpdateRequest.getHosts());
    hosts.setAppGroups(experimentHostUpdateRequest.getAppGroups());
    hosts.setSelectType(experimentHostUpdateRequest.getSelectType());
    hosts.setHostPercent(experimentHostUpdateRequest.getHostPercent());
    experimentMiniFlowGroupRepository.update(miniFlowGroupDO);
    List<MiniFlowDO> miniFlows =
        experimentMiniFlowRepository.findByGroupIdAndExperimentId(
            miniFlowGroupDO.getGroupId(), experimentHostUpdateRequest.getExperimentId());
    ExperimentActivityQuery experimentActivityQuery = new ExperimentActivityQuery();
    experimentActivityQuery.setDeleted(false);
    experimentActivityQuery.setFlowIds(
        miniFlows.stream().map(MiniFlowDO::getFlowId).collect(Collectors.toList()));
    List<ExperimentActivityDO> experimentActivities =
        activityRepository.find(experimentActivityQuery);
    experimentActivities.forEach(
        activityDO -> {
          ExperimentActivityDefinition experimentActivityDefinition =
              activityDO.getActivityDefinition();
          if (null != experimentActivityDefinition) {
            experimentActivityDefinition.setScope(hostList);
            activityRepository.update(activityDO);
          }
        });
    return Response.okWithData(true);
  }
}
