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

package com.alibaba.chaosblade.box.toolsmgr.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class MgrRequest {
  String license;

  /** 应用名 */
  String appName;

  /** 应用分组名 */
  String appGroupName;

  /** chaosbox ip:port */
  String chaosboxEndpoint;

  /** 目标ip */
  String instanceIp;

  /** 目标port */
  String instancePort;

  /** 目标机器用户名 */
  String instanceUser;

  /** 目标机器密码 */
  String instancePassword;

  /** command */
  String command;

  /** agent download url */
  String agentCtl;

  /** need password or not */
  Boolean needPassword;
}
