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
public enum StateEnum implements IEnum<Integer> {

  /** 准备运行 */
  READY(0),
  /** 运行中 */
  RUNNING(1),

  /** 暂停 */
  SUSPEND(2),
  /** 停止当中 */
  STOPPING(3),

  /** 已经结束 */
  FINISHED(4);

  private Integer value;

  StateEnum(Integer value) {
    this.value = value;
  }

  @Override
  public Integer getValue() {
    return this.value;
  }
}
