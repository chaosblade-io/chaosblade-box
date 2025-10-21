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

import com.google.common.collect.ImmutableList;
import java.util.List;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

/** @author haibin */
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@NoArgsConstructor
@AllArgsConstructor(staticName = "of")
public class ConditionArgumentDefinitionOperation {

  public static final ConditionArgumentDefinitionOperation EQ =
      ConditionArgumentDefinitionOperation.of("=", "eq");
  public static final ConditionArgumentDefinitionOperation GT =
      ConditionArgumentDefinitionOperation.of(">", "gt");
  public static final ConditionArgumentDefinitionOperation GTE =
      ConditionArgumentDefinitionOperation.of(">=", "gte");
  public static final ConditionArgumentDefinitionOperation LT =
      ConditionArgumentDefinitionOperation.of("<", "lt");
  public static final ConditionArgumentDefinitionOperation LTE =
      ConditionArgumentDefinitionOperation.of("<=", "lte");

  public static final ConditionArgumentDefinitionOperation CONTAINS =
      ConditionArgumentDefinitionOperation.of("包含", "contains");
  public static final ConditionArgumentDefinitionOperation START_WITH =
      ConditionArgumentDefinitionOperation.of("开头为", "startsWith");
  public static final ConditionArgumentDefinitionOperation END_WITH =
      ConditionArgumentDefinitionOperation.of("结尾为", "endsWith");
  public static final ConditionArgumentDefinitionOperation LENGTH =
      ConditionArgumentDefinitionOperation.of("长度", "length");

  public static final List<ConditionArgumentDefinitionOperation> NUMERIC_OPERATIONS =
      ImmutableList.of(
          ConditionArgumentDefinitionOperation.EQ,
          ConditionArgumentDefinitionOperation.GT,
          ConditionArgumentDefinitionOperation.GTE,
          ConditionArgumentDefinitionOperation.LT,
          ConditionArgumentDefinitionOperation.LTE);

  public static final List<ConditionArgumentDefinitionOperation> TEXT_OPERATIONS =
      ImmutableList.of(
          ConditionArgumentDefinitionOperation.CONTAINS,
          ConditionArgumentDefinitionOperation.START_WITH,
          ConditionArgumentDefinitionOperation.END_WITH,
          ConditionArgumentDefinitionOperation.LENGTH);

  String label;
  String value;
}
