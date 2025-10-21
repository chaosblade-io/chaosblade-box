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

import java.util.ArrayList;
import java.util.List;

/** @author haibin */
public class PermissionConstant {

  public static final String PERMISSION_EXPERIMENT_RUN = "experiment_run";

  /** 应用访问权限 */
  public static final String PERMISSION_APP_VISIT = "app_visit";

  public static final String PERMISSION_WORKSPACE_VISIT = "workspace_visit";

  public static List<String> PermissionConstants = new ArrayList<>();

  public static boolean existPermission(String permission) {
    return PermissionConstants.contains(permission);
  }

  static {
    PermissionConstants.add(PERMISSION_EXPERIMENT_RUN);
    PermissionConstants.add(PERMISSION_APP_VISIT);
    PermissionConstants.add(PERMISSION_WORKSPACE_VISIT);
  }
}
