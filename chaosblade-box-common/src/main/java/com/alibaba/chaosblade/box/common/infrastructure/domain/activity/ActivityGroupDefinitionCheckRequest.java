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

package com.alibaba.chaosblade.box.common.infrastructure.domain.activity;

import com.alibaba.chaosblade.box.common.app.sdk.scope.Host;
import com.alibaba.chaosblade.box.common.common.domain.BaseRequest;
import com.alibaba.chaosblade.box.common.infrastructure.domain.experiment.flow.MiniFlow;
import java.util.List;
import lombok.Data;

/** @author haibin.lhb */
@Data
public class ActivityGroupDefinitionCheckRequest extends BaseRequest {

  private List<Host> hosts;

  private List<MiniFlow> flows;

  /** 顺序 */
  private int order;

  /** 组件是否可以删除 */
  private boolean required;

  /** 机器类型 */
  private Integer scopeType;

  /** appName */
  private String appName;

  private String appId;

  private Integer appType;

  private List<String> appGroups;
}
