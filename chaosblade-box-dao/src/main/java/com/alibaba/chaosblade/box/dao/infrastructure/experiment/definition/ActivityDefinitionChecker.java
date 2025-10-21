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

package com.alibaba.chaosblade.box.dao.infrastructure.experiment.definition;

import com.alibaba.chaosblade.box.common.common.domain.definition.ExperimentActivityDefinition;

/** @author haibin */
public interface ActivityDefinitionChecker {

  /**
   * 校验节点定义
   *
   * @param experimentActivityDefinition 演练定义
   * @param fromApi 是否来自api
   * @throws ActivityDefinitionIllegalException
   */
  public void check(ExperimentActivityDefinition experimentActivityDefinition, boolean fromApi)
      throws ActivityDefinitionIllegalException;
}
