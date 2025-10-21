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

import com.alibaba.chaosblade.box.common.common.domain.definition.SceneFunctionParameterComponent;
import com.alibaba.chaosblade.box.common.common.domain.definition.SceneFunctionParameterLinkage;

/** @author haibin */
public class RecoveryStrategyToleranceArgumentDefinitionRepo {
  public static String PERCENT = "percent";

  public static String DURATION = "duration";

  public static final RecoveryStrategyToleranceArgumentDefinition TOLERANCE_HOST_PERCENT =
      createTolerance("机器占比", "percent", "指标达到阈值的机器数超过总机器数指定比例时执行恢复操作", "%");

  public static final RecoveryStrategyToleranceArgumentDefinition TOLERANCE_DURATION =
      createTolerance("持续时长", "duration", "指标达到阈值的持续时长超过指定持续时长时执行恢复操作", "秒");

  private static RecoveryStrategyToleranceArgumentDefinition createTolerance(
      String name, String alias, String description, String unit) {
    RecoveryStrategyToleranceArgumentDefinition tolerance =
        new RecoveryStrategyToleranceArgumentDefinition();
    tolerance.setName(name);
    tolerance.setAlias(alias);
    tolerance.setUnit(unit);
    tolerance.setDescription(description);
    tolerance.setEnabled(true);

    SceneFunctionParameterComponent component = new SceneFunctionParameterComponent();
    component.setType("number");
    component.setRequired(true);
    component.setUnit(unit);

    SceneFunctionParameterLinkage linkage = new SceneFunctionParameterLinkage();
    linkage.setDefaultState(true);

    component.setLinkage(linkage);
    tolerance.setComponent(component);

    return tolerance;
  }
}
