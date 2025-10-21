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

import com.alibaba.chaosblade.box.common.infrastructure.constant.PermissionTypes;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

/** @author sunju */
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@RequiredArgsConstructor(staticName = "of")
@AllArgsConstructor(staticName = "of")
public class PermissionResult {

  public static final PermissionResult NONE = PermissionResult.of(PermissionTypes.NONE);

  public static final PermissionResult ALL = PermissionResult.of(PermissionTypes.ALL);

  public static final PermissionResult W = PermissionResult.of(PermissionTypes.W);

  public static final PermissionResult X = PermissionResult.of(PermissionTypes.X);

  public static final PermissionResult R = PermissionResult.of(PermissionTypes.R);
  final int permission;
  String applyUrl;

  public boolean contains(int perm) {
    return (this.permission & perm) == perm;
  }

  public boolean equals(PermissionResult permissionResult) {
    return null != permissionResult && permissionResult.getPermission() == permission;
  }
}
