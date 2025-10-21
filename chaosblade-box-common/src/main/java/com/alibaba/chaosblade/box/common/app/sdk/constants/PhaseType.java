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

package com.alibaba.chaosblade.box.common.app.sdk.constants;

import lombok.Getter;

/** @author sunju */
public enum PhaseType {

  /** 阶段未定义 */
  UNKNOWN(-1, -1),

  /** 准备阶段 */
  PREPARE(0, 1),

  ATTACK(1, 2),

  CHECK(2, 4),

  RECOVER(3, 8);

  @Getter private Integer type;

  @Getter private Integer compareFlag;

  PhaseType(int type, Integer compareFlag) {
    this.type = type;
    this.compareFlag = compareFlag;
  }

  public static PhaseType ofType(Integer type) {
    for (PhaseType phaseType : PhaseType.values()) {
      if (phaseType.type.equals(type)) {
        return phaseType;
      }
    }
    return UNKNOWN;
  }

  public static boolean isSupport(Integer source, Integer toSupport) {
    return (source & toSupport) == toSupport;
  }
}
