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
import com.alibaba.chaosblade.box.common.common.domain.workspace.WorkspaceRelationQuery;
import com.alibaba.chaosblade.box.common.common.enums.ExperimentStateEnum;
import com.alibaba.chaosblade.box.common.experiment.clientobject.ExperimentStat;
import com.alibaba.chaosblade.box.common.infrastructure.domain.workspace.WorkspaceQueryRequest;
import com.alibaba.chaosblade.box.dao.model.ExperimentDO;
import com.alibaba.chaosblade.box.dao.model.WorkspaceRelationDO;
import com.alibaba.chaosblade.box.dao.repository.ExperimentRepository;
import com.alibaba.chaosblade.box.dao.repository.WorkspaceRelationRepository;
import com.google.common.collect.Lists;
import java.util.List;
import java.util.stream.Collectors;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/** @author haibin */
@Component
public class WorkspaceExperimentStatCommand
    extends SpringBeanCommand<WorkspaceQueryRequest, ExperimentStat> {

  @Autowired private ExperimentRepository experimentRepository;

  @Autowired private WorkspaceRelationRepository workspaceRelationRepository;

  @Override
  public ExperimentStat execute(WorkspaceQueryRequest request) {
    WorkspaceRelationQuery workspaceRelationQuery = new WorkspaceRelationQuery();
    workspaceRelationQuery.setWorkspaceIds(Lists.newArrayList(request.getWorkspaceId()));
    workspaceRelationQuery.setNamespace(request.getNamespace());
    List<WorkspaceRelationDO> relationDOList =
        workspaceRelationRepository.find(workspaceRelationQuery);

    List<String> experimentIds =
        relationDOList.stream().map(WorkspaceRelationDO::getOuterId).collect(Collectors.toList());

    ExperimentStat experimentStat = new ExperimentStat();
    if (CollectionUtils.isEmpty(experimentIds)) {
      return experimentStat;
    }

    List<ExperimentDO> experiments = experimentRepository.findByExperimentIdsIn(experimentIds);
    experimentStat.setTotal(experiments.size());

    long active = 0L, running = 0L, failed = 0L, success = 0L, idle = 0L, finishedCount = 0L;

    for (ExperimentDO experiment : experiments) {
      if (experiment.getExperimentTaskId() == null) {
        idle++;
      } else {
        active++;
        ExperimentStateEnum experimentStateEnum = experiment.getExperimentStateEnum();
        if (ExperimentStateEnum.RUNNING.equals(experimentStateEnum)) {
          running++;
        } else {
          finishedCount++;
          if (experiment.isSuccess()) {
            success++;
          } else {
            failed++;
          }
        }
      }
    }

    experimentStat.setIdle(idle);
    experimentStat.setFinished(finishedCount);
    experimentStat.setRunning(running);
    experimentStat.setActive(active);
    experimentStat.setFailure(failed);
    experimentStat.setSuccess(success);
    // 老数据
    experimentStat.setTotalCount(experiments.size());
    experimentStat.setFailureCount(experimentStat.getFailure());
    experimentStat.setFinished(experimentStat.getFinishedCount());
    experimentStat.setRunningCount(experimentStat.getRunningCount());
    return experimentStat;
  }
}
