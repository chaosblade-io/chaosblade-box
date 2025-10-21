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
 * 反馈相关的状态集合
 *
 * @author haibin.lhb
 */
public class FeedBackConstant {

  /** 反馈阶段 */
  public static enum FeedbackStatus implements IEnum<Integer> {

    /** 等待反馈 */
    FEEDBACK_STATUS_WAITING(0),

    /** 已经反馈 */
    FEEDBACK_STATUS_FINISHED(1);

    private Integer value;

    FeedbackStatus(int value) {
      this.value = value;
    }

    @Override
    public Integer getValue() {
      return value;
    }
  }

  /** 反馈是否符合预期 */
  public static enum ExpectationStatus implements IEnum<Integer> {
    /** 符合预期 */
    EXPECTATION_STATUS_YES(1),
    /** 不符合预期 */
    EXPECTATION_STATUS_NO(0);

    private Integer value;

    ExpectationStatus(int value) {
      this.value = value;
    }

    @Override
    public Integer getValue() {
      return value;
    }
  }

  /** 反馈是否符合业务 */
  public static enum BusinessImpactStatus implements IEnum<Integer> {
    /** 不影响业务 */
    BUSINESS_IMPACT_STATUS_NO(0),

    /** 影响业务 */
    BUSINESS_IMPACT_STATUS_YES(1);

    private Integer value;

    BusinessImpactStatus(int value) {
      this.value = value;
    }

    @Override
    public Integer getValue() {
      return value;
    }
  }
}
