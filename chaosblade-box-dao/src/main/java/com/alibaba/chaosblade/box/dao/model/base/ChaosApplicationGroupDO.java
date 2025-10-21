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

package com.alibaba.chaosblade.box.dao.model.base;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * 应用分组表
 *
 * @author haibin
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ToString
public class ChaosApplicationGroupDO extends BaseDO {

  private static final long serialVersionUID = -4260038555263063763L;
  /** 应用分组名 */
  private String name;

  /** 分组名对应的展示名称 */
  private String display;

  /** 应用id */
  private String appName;

  /** appId */
  private Long appId;
}
