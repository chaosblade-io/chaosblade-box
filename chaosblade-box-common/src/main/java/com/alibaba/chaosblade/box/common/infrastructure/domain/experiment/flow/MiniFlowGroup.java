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

package com.alibaba.chaosblade.box.common.infrastructure.domain.experiment.flow;

import com.alibaba.chaosblade.box.common.app.sdk.scope.Host;
import java.util.List;
import lombok.Data;

/** @author haibin */
@Data
public class MiniFlowGroup {

  private String groupId;

  private String groupName;

  private List<Host> hosts;

  private List<MiniFlow> flows;

  /** 顺序 */
  private int order;

  /** 组件是否可以删除 */
  private boolean required;

  /** 机器类型 */
  private Integer scopeType;

  private String appName;

  private String appId;

  private Integer appType;

  private List<String> appGroups;

  /** 机器选择方式 {@link HostSelectTypes} */
  private Integer selectType;

  /** 百分比 */
  private Integer hostPercent;

  /** 云服务类型 */
  private String cloudServiceType;

  /** 云服务类型名称 */
  private String cloudServiceName;

  /** 机器操作系统类型 */
  private Integer osType;
}
