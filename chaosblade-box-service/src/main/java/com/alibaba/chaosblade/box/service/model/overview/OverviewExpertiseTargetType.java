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

package com.alibaba.chaosblade.box.service.model.overview;

/** @author sunpeng */
public enum OverviewExpertiseTargetType {
  CPU("CPU", "icon-cpu"),
  MEM("内存", "icon-neicun"),
  NETWORK("网络", "icon-wangluo"),
  DISK("磁盘", "icon-cipan"),
  PROCESS("进程", "icon-a-jincheng1"),
  JVM("JVM", "icon-java"),
  KUBERNETES("Kubernetes", "icon-K8S"),
  ;

  String name;

  String icon;

  OverviewExpertiseTargetType(String name, String icon) {
    this.name = name;
    this.icon = icon;
  }

  public String getIcon() {
    return icon;
  }

  public String getName() {
    return name;
  }
}
