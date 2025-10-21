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

package com.alibaba.chaosblade.box.common.common.domain.feedback;

import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * 演练反馈内容
 *
 * @author haibin.lhb
 */
@Data
public class ExperimentTaskFeedback implements Serializable {

  private String feedbackId;

  /** 反馈额外信息 */
  private ExtraFeedbackComponent extra;

  /** 反馈时间 */
  private Date feedbackTime;

  /** 用户备注 */
  private String memo;

  /** 是否符合预期 */
  private Integer expectationStatus;

  /** 业务影响程度 */
  private Integer businessStatus;
}
