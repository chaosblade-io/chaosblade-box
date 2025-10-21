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

package com.alibaba.chaosblade.box.service.auth.perimission;

import com.alibaba.chaosblade.box.common.app.sdk.scope.Host;
import com.alibaba.chaosblade.box.common.common.domain.user.ChaosUser;
import com.alibaba.chaosblade.box.common.common.enums.CommonErrorCode;
import com.alibaba.chaosblade.box.common.infrastructure.exception.PermissionDeniedException;
import java.util.List;

/** @author haibin */
public class UserPermissionServiceImpl extends AbstractUserPermissionService {

  @Override
  public PermissionResult checkExperimentPermission(
      int permissionType,
      ChaosUser chaosUser,
      String experimentId,
      String nameSpace,
      List<Host> hosts)
      throws PermissionDeniedException {
    PermissionResult permissionResult =
        getUserExperimentPermission(chaosUser, experimentId, nameSpace);
    if (!permissionResult.contains(permissionType)) {
      throw new PermissionDeniedException(CommonErrorCode.P_USER_PERMISSION_DENIED, null);
    }
    return permissionResult;
  }

  @Override
  public void checkExperimentDeletePermission(
      int permissionType,
      ChaosUser chaosUser,
      String experimentId,
      String nameSpace,
      List<Host> hosts)
      throws PermissionDeniedException {}

  @Override
  public PermissionResult checkExperimentTaskPermission(
      int permissionType, ChaosUser chaosUser, String experimentTaskId) {
    PermissionResult permissionResult =
        getUserExperimentTaskPermission(chaosUser, experimentTaskId);
    if (!permissionResult.contains(permissionType)) {
      throw new PermissionDeniedException(CommonErrorCode.P_USER_PERMISSION_DENIED, null);
    }
    return permissionResult;
  }

  @Override
  public void checkUserApplicationPermission(int permissionType, ChaosUser chaosUser, Long appId)
      throws PermissionDeniedException {}

  @Override
  public void checkUserApplicationPermission(
      int permissionType, ChaosUser chaosUser, String namespace, String appName)
      throws PermissionDeniedException {}

  @Override
  public void checkOpenApiForChaosPermission(ChaosUser chaosUser)
      throws PermissionDeniedException {}
}
