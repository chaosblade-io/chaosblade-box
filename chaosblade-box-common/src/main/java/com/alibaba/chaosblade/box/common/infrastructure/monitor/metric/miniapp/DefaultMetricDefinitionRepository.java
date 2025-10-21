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

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/** @author haibin */
public class DefaultMetricDefinitionRepository implements MetricDefinitionRepository {

  private static String filePath = "metric.json";

  private static List<MetricDefinition> internalMetricDefinitions = new ArrayList<>();

  private static Map<String, List<MetricDefinition>> categoryToMetrics = new HashMap<>();

  static {
    InputStream inputStream =
        Thread.currentThread().getContextClassLoader().getResourceAsStream(filePath);
    try {
      Type type = new TypeReference<List<MetricDefinition>>() {}.getType();
      internalMetricDefinitions = JSON.parseObject(inputStream, type);
      categoryToMetrics =
          internalMetricDefinitions.stream()
              .collect(Collectors.groupingBy(MetricDefinition::getCategory));
    } catch (Throwable throwable) {
    }
  }

  @Override
  public Optional<MetricDefinition> findByMetric(String metricKey) {
    return internalMetricDefinitions.stream()
        .filter(
            new Predicate<MetricDefinition>() {
              @Override
              public boolean test(MetricDefinition internalMetricDefinition) {
                return internalMetricDefinition.getKey().equals(metricKey);
              }
            })
        .findFirst();
  }

  @Override
  public List<MetricDefinition> findByCategory(String category) {
    return categoryToMetrics.getOrDefault(category, new ArrayList<>());
  }
}
