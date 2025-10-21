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

package com.alibaba.chaosblade.box.dao.infrastructure.service;

import com.alibaba.chaosblade.box.common.app.sdk.scope.Host;
import com.alibaba.chaosblade.box.common.common.domain.Response;
import com.alibaba.chaosblade.box.common.infrastructure.metric.ChaosMetricEntity;
import com.alibaba.chaosblade.box.common.infrastructure.metric.MetricRequest;
import com.alibaba.chaosblade.box.common.infrastructure.metric.UserDefinitionMetricRequest;
import java.util.List;
import java.util.Map;

/** @author haibin */
public interface MetricService {

  public Response<List<ChaosMetricEntity>> getTargetMetric(MetricRequest metricRequest);

  public Response<Map<String, List<ChaosMetricEntity>>> batchGetTargetsMetric(
      List<Host> hosts, MetricRequest metricRequest);

  /**
   * 根据metric AppCode来获取下来指标 比如 chaosapp.metric.cpu,那么cpu就是category
   *
   * @param metricAppCode
   * @return
   */
  public Response<List<String>> getMetricKeysByAppCode(String metricAppCode);

  public Response<List<ChaosMetricEntity>> getUserDefinitionMetric(
      UserDefinitionMetricRequest userDefinitionMetricRequest);
}
