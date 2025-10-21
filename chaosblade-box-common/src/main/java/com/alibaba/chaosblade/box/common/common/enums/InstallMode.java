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

/** Agent 安装模式，每一个namespace只能绑定一种安装模式； */
public enum InstallMode {
  host,
  k8s,
  k8s_helm,
  cs_k8s,
  cs_k8s_helm;

  public static InstallMode getByName(String mode) {
    for (InstallMode item : InstallMode.values()) {
      if (item.name().equalsIgnoreCase(mode)) {
        return item;
      }
    }

    return null;
  }

  public static boolean isKubernetes(String mode) {
    if (mode != null && mode.contains("k8s")) {
      return true;
    }

    return false;
  }
}
