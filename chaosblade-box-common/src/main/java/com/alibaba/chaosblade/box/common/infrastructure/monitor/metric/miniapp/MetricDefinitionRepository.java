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

package com.alibaba.chaosblade.box.common.infrastructure.monitor.metric.miniapp;

import java.util.List;
import java.util.Optional;

/** @author haibin */
public interface MetricDefinitionRepository {

  /**
   * 根据具体的metric获取metric定义
   *
   * @param metricKey
   * @return
   */
  public <T extends MetricDefinition> Optional<T> findByMetric(String metricKey);

  /**
   * 根据category获取所有
   *
   * @param category
   * @return
   */
  public <T extends MetricDefinition> List<T> findByCategory(String category);
}
