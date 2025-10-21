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

package com.alibaba.chaosblade.box.common.infrastructure.constant;

/**
 * 应用的分组维度
 *
 * @author: xinyuan
 * @create: 2020-06-30 4:12 PM
 */
public enum ApplicationDimension {
  HOST(0),
  NODE(1),
  POD(2),
  PROCESS(3);

  private Integer value;

  ApplicationDimension(Integer value) {
    this.value = value;
  }

  public Integer getValue() {
    return value;
  }
}
