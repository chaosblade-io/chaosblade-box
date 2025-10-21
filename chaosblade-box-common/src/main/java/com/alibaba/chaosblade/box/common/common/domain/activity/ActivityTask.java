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

package com.alibaba.chaosblade.box.common.common.domain.activity;

import com.alibaba.chaosblade.box.common.app.sdk.constants.PhaseType;
import com.alibaba.chaosblade.box.common.common.domain.experiment.ActivityRunParam;
import com.alibaba.chaosblade.box.common.common.enums.ResultEnum;
import com.alibaba.chaosblade.box.common.common.enums.StateEnum;
import com.alibaba.chaosblade.box.common.common.enums.UserCheckState;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
import lombok.Data;

/** @author haibin */
@Data
public class ActivityTask implements Serializable {

  private String experimentTaskId;

  /** 活动任务ID */
  private String activityTaskId;

  private String activityId;

  /** 开始时间 */
  private Date startTime;
  /** 结束时间 */
  private Date endTime;

  /** 运行状态 */
  private StateEnum state;

  /** 运行结果 */
  private ResultEnum runResult;

  /** 用户校验结果 */
  private UserCheckState userCheckState;

  /** 所处阶段 */
  private PhaseType phase;

  /** 错误信息 */
  private String errorMessage;

  /** 运行时候的参数和定义 */
  private ActivityRunParam runParam;

  /** 小程序的执行信息 */
  private List<MiniAppTaskSummary> apps;

  /** 小程序的名字 */
  private String miniAppName;

  /** 小程序的code */
  private String miniAppCode;

  /** activity顺序 */
  private Integer order;

  private String executableAppCode;

  private String flowId;

  /** 是否运行重试 */
  private Boolean retryable;
}
