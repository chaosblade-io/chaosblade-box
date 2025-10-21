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

import lombok.Data;

/** @author haibin */
@Data
public class ExperimentGuardMetricDataItem {

  private Long timestamp;

  private Number value;

  /**
   * //TODO ,2019.12.19,haibin.lhb 本来考虑到机器会很多，如果按照机器维度来返回指定的话，前端不好展示，
   * 但是考虑到云上演练的机器数目其实不会很多，所以这里group的值还是用ip来展示， 后续可以再改成group来展示
   */
  private String group;
}
