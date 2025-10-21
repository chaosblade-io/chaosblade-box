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

package com.alibaba.chaosblade.box.common.common.domain;

import com.alibaba.chaosblade.box.common.common.enums.IEnum;

/**
 * 强弱依赖状态
 *
 * @author haibin.lhb
 */
public class StrongAndWeakDepConstant {

  /** 强弱依赖状态 */
  public static enum StrongAndWeakStatus implements IEnum<Integer> {
    /** 强依赖 */
    Strong(1),

    /** 弱依赖 */
    Weak(2),

    /** 未知 */
    UNKNOWN(0);

    private Integer value;

    StrongAndWeakStatus(Integer value) {
      this.value = value;
    }

    @Override
    public Integer getValue() {
      return value;
    }
  }
}
