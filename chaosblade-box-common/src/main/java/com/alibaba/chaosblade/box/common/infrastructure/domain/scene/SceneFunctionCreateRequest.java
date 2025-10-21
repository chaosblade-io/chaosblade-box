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

package com.alibaba.chaosblade.box.common.infrastructure.domain.scene;

import com.alibaba.chaosblade.box.common.common.domain.user.ChaosUser;
import java.util.List;
import lombok.Data;

/** @author haibin */
@Data
public class SceneFunctionCreateRequest {

  /** 用户 */
  ChaosUser user;

  /** 函数code，全局唯一 */
  String code;

  /** 函数名字 */
  String name;

  /** 函数描述 */
  String description;

  /** 阶段标志位,支持哪个阶段 */
  private Integer phaseFlag;

  /**
   * 是否生效，影响是否对用户可见
   *
   * <p>0: 下架 1: 上架 2: 待发布
   */
  private Integer enabled;

  /** 包含系统列表 */
  private List<SceneFunctionSystemVersion> systemVersions;

  /** 版本信息 */
  private String version;
}
