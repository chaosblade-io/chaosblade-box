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

package com.alibaba.chaosblade.box.common.common.domain.chaosblade;

/** @author haibin */
public enum ChaosBladeActionType {

  /** 安装agent */
  INSTALL_AGENT,
  /** 卸载agent */
  UNINSTALL_AGENT,

  /** 注入故障 */
  ATTACK,

  /** 停止故障注入 */
  STOP_ATTACK
}
