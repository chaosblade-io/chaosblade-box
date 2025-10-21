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

package com.alibaba.chaosblade.box.common.common.domain.experiment;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

/** @author sunju */
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ExperimentRelation {

  /** GOC应急场景 */
  public static final String RELATION_TYPE_GOC_EMERGENCY_SCENE = "emergency_scene";

  /** 关联ID，如果是创建则可以不用填 */
  String relationId;

  /** 外部ID,比如goc的ref */
  String outerId;
  /** 关系描述 */
  String outerDescription;

  /** 关系类型 */
  String relationType;
}
