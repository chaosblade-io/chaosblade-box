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
public enum UserCheckState implements IEnum<Integer> {

  /** 等待用户确认 */
  USER_CHECK_STATE_WAITING(0),

  /** 用户确认通过 */
  USER_CHECK_STATE_PASSED(1),

  /** 用户确认失败 */
  USER_CHECK_STATE_FAILED(2);

  private Integer type;

  UserCheckState(Integer type) {
    this.type = type;
  }

  @Override
  public Integer getValue() {
    return this.type;
  }
}
