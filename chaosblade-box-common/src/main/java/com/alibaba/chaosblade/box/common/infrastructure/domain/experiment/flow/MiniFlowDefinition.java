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

import com.alibaba.chaosblade.box.common.common.domain.definition.ExperimentActivityDefinition;
import java.util.List;
import lombok.Data;

/** @author haibin */
@Data
public class MiniFlowDefinition {

  /** 微流程准备环节 */
  private List<ExperimentActivityDefinition> prepare;

  /** 微流程注入环节 */
  private ExperimentActivityDefinition attack;

  /** 微流程校验环节 */
  private List<ExperimentActivityDefinition> check;

  /** 微流程恢复环节 */
  private List<ExperimentActivityDefinition> recover;
}
