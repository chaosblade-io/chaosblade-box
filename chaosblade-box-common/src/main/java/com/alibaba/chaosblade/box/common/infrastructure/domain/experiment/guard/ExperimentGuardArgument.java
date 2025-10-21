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

package com.alibaba.chaosblade.box.common.infrastructure.domain.experiment.guard;

import com.alibaba.chaosblade.box.common.infrastructure.domain.experiment.SceneArgumentDefinition;
import com.alibaba.chaosblade.box.common.infrastructure.domain.experiment.flow.RecoveryStrategyFieldArgumentDefinition;
import com.alibaba.chaosblade.box.common.infrastructure.domain.experiment.flow.RecoveryStrategyToleranceArgumentDefinition;
import java.util.List;
import lombok.Data;

/** @author haibin */
@Data
public class ExperimentGuardArgument {

  /** 全局恢复策略涉及到的小程序以及小程序配置 */
  private List<SceneArgumentDefinition> arguments;

  /** 容忍度,全局恢复策略触发的阈值 */
  private List<RecoveryStrategyToleranceArgumentDefinition> tolerance;

  /** 当选择小程序之后,需要选择某个类目下面具体某个值,比如选了CPU,那么fields就可能是system.cpu.util */
  private List<RecoveryStrategyFieldArgumentDefinition> fields;
}
