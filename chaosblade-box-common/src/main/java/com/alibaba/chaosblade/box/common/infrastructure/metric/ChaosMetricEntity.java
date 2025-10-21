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

package com.alibaba.chaosblade.box.common.infrastructure.metric;

import com.alibaba.chaosblade.box.common.app.sdk.scope.Host;
import lombok.Data;
import lombok.RequiredArgsConstructor;

/** @author haibin */
@RequiredArgsConstructor
@Data
public class ChaosMetricEntity {

  private String userId;
  private String namespace;

  public ChaosMetricEntity(
      String userId, String namespace, String metric, Host host, Long timestamp, Number value) {
    this.userId = userId;
    this.namespace = namespace;
    this.metric = metric;
    this.host = host;
    this.timestamp = timestamp;
    this.value = value;
  }

  public ChaosMetricEntity(Host host, Long timestamp, Number value, String unit) {
    this.host = host;
    this.timestamp = timestamp;
    this.value = value;
    this.unit = unit;
  }

  private String metric;
  private Host host;

  private Long timestamp;

  private Number value;

  private String unit;
}
