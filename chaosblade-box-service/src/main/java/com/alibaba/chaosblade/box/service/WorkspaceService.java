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

package com.alibaba.chaosblade.box.service;

import com.alibaba.chaosblade.box.common.common.domain.Response;
import com.alibaba.chaosblade.box.common.common.domain.experiment.WorkspaceShortInfo;
import com.alibaba.chaosblade.box.common.common.domain.user.ChaosUser;
import com.alibaba.chaosblade.box.common.experiment.clientobject.ExperimentStat;
import com.alibaba.chaosblade.box.common.infrastructure.domain.workspace.AddExperimentResponse;
import com.alibaba.chaosblade.box.common.infrastructure.domain.workspace.WorkspaceExperimentPageableQueryRequest;
import com.alibaba.chaosblade.box.common.infrastructure.domain.workspace.WorkspaceExperimentRequest;
import com.alibaba.chaosblade.box.common.infrastructure.domain.workspace.WorkspaceQueryRequest;
import com.alibaba.chaosblade.box.dao.infrastructure.domain.workspace.WorkspaceBaseInfo;
import com.alibaba.chaosblade.box.dao.infrastructure.domain.workspace.WorkspaceExperimentPageableQueryResponse;
import com.alibaba.chaosblade.box.service.auth.perimission.PermissionResult;
import java.util.List;

public interface WorkspaceService {
  Response<ExperimentStat> getGeneralWorkSpaceStatInfo(
      ChaosUser user, WorkspaceQueryRequest request);

  PermissionResult getWorkspacePermission(ChaosUser user, String experimentid);

  WorkspaceExperimentPageableQueryResponse pageableGeneralExperiments(
      ChaosUser user, WorkspaceExperimentPageableQueryRequest request, Integer permission);

  AddExperimentResponse addWorkspaceExperiment(ChaosUser user, WorkspaceExperimentRequest request);

  PermissionResult getWorkspaceExperimentTaskPermission(
      ChaosUser chaosUser, String experimentTaskId);

  List<String> listExperimentTagsInWorkspace(String workspaceId, String namespace);

  List<WorkspaceBaseInfo> getWorkspaceByExperimentId(ChaosUser user, WorkspaceQueryRequest request);

  /**
   * 增加空间演练
   *
   * @param experimentId
   * @param name
   * @param namespace
   * @param workspaceIds
   */
  void addWorkspaceExperiments(
      String experimentId, String name, String namespace, List<String> workspaceIds);

  List<WorkspaceShortInfo> getWorkspacesShortInfoByExperimentId(String experimentId);
}
