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

/**
 * 演练的状态,
 *
 * <p>FROZEN->READY
 *
 * @author haibin
 */
public enum ExperimentStateEnum implements IEnum<Integer> {

  /** 不可见 */
  INVISIBLE(-2),

  DRAFT(-1),
  /** 准备好可以使用 */
  READY(0),
  /** 当前演练有一个任务正在运行当中 */
  RUNNING(1),

  FINISHED(5);

  Integer state;

  ExperimentStateEnum(int state) {
    this.state = state;
  }

  @Override
  public Integer getValue() {
    return this.state;
  }

  public static Integer getValue(ExperimentStateEnum state) {
    if (null == state) {
      return null;
    }
    return state.getValue();
  }
}
