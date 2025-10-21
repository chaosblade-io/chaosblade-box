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

package com.alibaba.chaosblade.box.common.common.enums;

import com.alibaba.chaosblade.box.common.common.util.MiniAppUtils;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/** @author haibin */
public enum ScopeTypeEnum implements IEnum<Integer> {

  /** host安装类型，包括了ecs和用户自己的主机 */
  HOST(0, InstallMode.host),
  K8s(2, InstallMode.k8s, InstallMode.k8s_helm, InstallMode.cs_k8s, InstallMode.cs_k8s_helm);

  private InstallMode[] installModes;

  private int type;

  ScopeTypeEnum(Integer scopeType, InstallMode... installModes) {
    this.type = scopeType;
    this.installModes = installModes;
  }

  public static ScopeTypeEnum judgeScopeByAppType(Integer appType) {
    if (AppType.CLUSTER.getType() == appType) {
      return K8s;
    }
    if (AppType.HOST.getType() == appType) {
      return HOST;
    }
    return null;
  }

  public static ScopeTypeEnum judgeScopeByAppCode(String appCode) {
    if (MiniAppUtils.isK8S(appCode)) {
      return K8s;
    }
    return HOST;
  }

  public List<InstallMode> getAsList() {
    return Collections.unmodifiableList(Arrays.asList(installModes));
  }

  @Override
  public Integer getValue() {
    return type;
  }
}
