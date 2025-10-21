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

package com.alibaba.chaosblade.box.dao.infrastructure.model.scene;

import com.alibaba.chaosblade.box.common.infrastructure.domain.experiment.flow.RecoveryStrategyFieldArgumentDefinition;
import com.alibaba.chaosblade.box.common.infrastructure.domain.experiment.flow.RecoveryStrategyToleranceArgumentDefinition;
import com.alibaba.chaosblade.box.common.infrastructure.domain.experiment.flow.RecoveryStrategyToleranceArgumentDefinitionRepo;
import com.alibaba.chaosblade.box.dao.model.SceneFunctionParameterDO;
import com.google.common.collect.ImmutableList;
import java.util.List;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

/**
 * Author: sunju
 *
 * <p>Date: 2019/11/15
 */
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class GuardRule {

  List<SceneFunctionParameterDO> parameters;

  List<RecoveryStrategyFieldArgumentDefinition> fields;

  List<RecoveryStrategyToleranceArgumentDefinition> tolerance;

  public void useDefaultTolerance() {
    tolerance =
        ImmutableList.of(
            RecoveryStrategyToleranceArgumentDefinitionRepo.TOLERANCE_HOST_PERCENT,
            RecoveryStrategyToleranceArgumentDefinitionRepo.TOLERANCE_DURATION);
  }
}
