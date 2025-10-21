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

/** @author haibin */
public enum ExperimentTaskTypeEnum implements IEnum<Integer> {

  /** 手动演练，手动演练就是用户来控制演练周期以及里面的步骤执行 */
  MANUAL(0),

  /** 自动演练，就是演练流程自动化运行 */
  AUTO(1);

  private int value;

  ExperimentTaskTypeEnum(int i) {
    this.value = i;
  }

  @Override
  public Integer getValue() {
    return this.value;
  }
}
