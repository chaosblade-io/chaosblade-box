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

package com.alibaba.chaosblade.box.common.infrastructure.domain.experiment;

import com.alibaba.chaosblade.box.common.common.domain.definition.SceneFunctionParameterComponent;
import lombok.Data;

/** @author haibin */
@Data
public class SceneArgumentDefinition {

  private String alias;

  private String value;

  private String name;

  private String description;

  private SceneFunctionParameterComponent component;
  /** 是否有效 */
  private boolean enabled;

  private String unit;

  private String parameterId;

  private String functionId;

  private Integer order;

  /** 参数分级 */
  private Integer grade;
}
