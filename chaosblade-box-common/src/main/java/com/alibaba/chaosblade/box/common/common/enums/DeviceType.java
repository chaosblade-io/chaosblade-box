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

package com.alibaba.chaosblade.box.common.common.enums;

import java.util.Arrays;

/**
 * @author: xinyuan
 * @create: 2018-03-27 下午5:30
 */
public enum DeviceType {
  HOST(0),
  CONTAINER(1),
  POD(2),
  HOST_POD(22);

  private int type;

  DeviceType(int type) {
    this.type = type;
  }

  public int getType() {
    return type;
  }

  public static DeviceType transByCode(int code) {
    return Arrays.stream(DeviceType.values()).filter(o -> o.equals(code)).findFirst().orElse(null);
  }
}
