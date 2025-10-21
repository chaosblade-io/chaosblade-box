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

package com.alibaba.chaosblade.box.common.common.constant;

public class ChaosFunctionConstant {

  /** 待发布 */
  public static final Integer ENABLED_READY = 0;

  /** 已发布 */
  public static final Integer ENABLED_PUBLISH = 1;

  /** 已上线 */
  public static final Integer ENABLED_ONLINE = 2;

  /** 来自chaos-blade的能力 */
  public static Integer SOURCE_CHAOS_BLADE = 1;

  /** 来自定义小程序的能力 */
  public static Integer SOURCE_CUSTOM_APP = 0;

  /** 来自小程序脚本 */
  public static Integer SOURCE_CUSTOM_APP_SCRIPT = 2;

  /** 裂变的小程序 */
  public static Integer SOURCE_FISSION_APP = 3;

  /** 来自litmus-chaos */
  public static Integer SOURCE_LITMUS_CHAOS = 4;

  /** 准备阶段 */
  public static final int PHASE_FLAG_PREPARE = 1; // 1 << 0, 00001

  /** 注入阶段 */
  public static final int PHASE_FLAG_ATTACK = 1 << 1; // 00010

  /** 检查阶段 */
  public static final int PHASE_FLAG_CHECK = 1 << 2; // 00100

  /** 恢复阶段 */
  public static final int PHASE_FLAG_RECOVER = 1 << 3; // 01000

  /** 全局 */
  public static final int PHASE_FLAG_GLOBAL = 1 << 4; // 10000

  /** 需要调用chaos-blade的安装接口 */
  public static final int CHAOS_BLADE_ACTION_INSTALL = 0;

  /** 需要调用chaos-blade的注入接口 */
  public static final int CHAOS_BLADE_ACTION_ATTACK = 1;

  /** 需要调用chaos-blade的停止接口 */
  public static final int CHAOS_BLADE_ACTION_STOP = 2;

  /** 需要调用chaos-blade的卸载接口 */
  public static final int CHAOS_BLADE_ACTION_UNINSTALL = 3;

  public static final int SUPPORT_SCOPE_TYPE_HOST = 0;

  public static final int SUPPORT_SCOPE_TYPE_K8S = 2;

  public static final int SUPPORT_SCOPE_TYPE_CLOUD = 3;

  public static final int SUPPORT_SCOPE_TYPE_WINDOWS = 4;

  public static final int K8_RESOURCE_TYPE_CONTAINER = 1;

  public static final int K8_RESOURCE_TYPE_NODE = 2;

  public static final int K8_RESOURCE_TYPE_POD = 3;

  public static final int DEVICE_OS_TYPE_LINUX = 0;
  public static final int DEVICE_OS_TYPE_WINDOWS = 1;
}
