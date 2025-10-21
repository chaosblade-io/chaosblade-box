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

package com.alibaba.chaosblade.box.dao.infrastructure.experiment.interceptor;

import com.alibaba.chaosblade.box.common.app.sdk.constants.PhaseType;
import com.alibaba.chaosblade.box.common.common.domain.definition.ExperimentActivityDefinition;
import com.alibaba.chaosblade.box.dao.infrastructure.experiment.definition.ExperimentFlowDefinitionMeta;

/**
 * 保存前的definition处理
 *
 * @author haibin
 */
public interface ExperimentActivityDefinitionInterceptor {

  /**
   * 处理activity definition
   *
   * @param originDefinition 原始的定义
   * @param phaseType 阶段类型
   * @param experimentFlowDefinitionMeta experimentFlowDefintionMetaData
   */
  public void preInterceptor(
      ExperimentActivityDefinition originDefinition,
      PhaseType phaseType,
      ExperimentFlowDefinitionMeta experimentFlowDefinitionMeta);
}
