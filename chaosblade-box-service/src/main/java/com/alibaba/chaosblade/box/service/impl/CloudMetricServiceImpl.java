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

package com.alibaba.chaosblade.box.service.impl;

import com.alibaba.chaosblade.box.common.app.sdk.scope.Host;
import com.alibaba.chaosblade.box.common.common.domain.Response;
import com.alibaba.chaosblade.box.common.infrastructure.metric.ChaosMetricEntity;
import com.alibaba.chaosblade.box.common.infrastructure.metric.MetricRequest;
import com.alibaba.chaosblade.box.common.infrastructure.metric.UserDefinitionMetricRequest;
import com.alibaba.chaosblade.box.common.infrastructure.monitor.metric.miniapp.MetricDefinition;
import com.alibaba.chaosblade.box.dao.infrastructure.monitor.metric.MetricProvider;
import com.alibaba.chaosblade.box.dao.infrastructure.monitor.miniapp.BaseMetricServiceImpl;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/** @author haibin */
@Component(value = "cloudMetricService")
@Slf4j
public class CloudMetricServiceImpl extends BaseMetricServiceImpl {

  @Autowired private List<MetricProvider> metricProviders;

  @Override
  protected List<ChaosMetricEntity> getMetrics(
      MetricDefinition metricDefinition, MetricRequest metricRequest) {
    return metricProviders.stream()
        .flatMap(
            new Function<MetricProvider, Stream<ChaosMetricEntity>>() {
              @Override
              public Stream<ChaosMetricEntity> apply(MetricProvider metricProvider) {
                return Optional.ofNullable(metricProvider.provide(metricDefinition, metricRequest))
                    .orElse(new ArrayList<>()).stream();
              }
            })
        .collect(Collectors.toList());
  }

  @Override
  public Response<Map<String, List<ChaosMetricEntity>>> batchGetTargetsMetric(
      List<Host> list, MetricRequest metricRequest) {
    return null;
  }

  @Override
  public Response<List<ChaosMetricEntity>> getUserDefinitionMetric(
      UserDefinitionMetricRequest userDefinitionMetricRequest) {
    return null;
  }
}
