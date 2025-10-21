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

package com.alibaba.chaosblade.box.common.experiment.task.flow.util;

/** @author haibin */
public enum PluginType {
  /** chaos-agent */
  CHAOS_AGENT,
  /* AGENT 采用POD方式部署*/
  CHAOS_POD_AGENT,
  /*attach 模式*/
  JAVA_AGENT,
  /*依赖jar模式*/
  JAVA_SDK,
  /*ChaosBlade*/
  CHAOS_BLADE;

  private PluginType() {}
}
