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

package com.alibaba.chaosblade.box.common.infrastructure.domain.experiment;

import com.alibaba.chaosblade.box.common.common.enums.ResultEnum;
import com.alibaba.chaosblade.box.common.common.enums.StateEnum;
import java.io.Serializable;
import java.util.Date;
import java.util.Set;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

/** @author jiumu */
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ExperimentTaskSimple implements Serializable {

  /** 演练名字 */
  String experimentName;

  /** 演练ID */
  String experimentId;

  /** 演练任务Id */
  String taskId;

  /** 演练开始时间 */
  Date startTime;

  /** 结束时间 */
  Date endTime;

  /** 运行状态 */
  StateEnum state;

  /** 结果 */
  ResultEnum result;

  /** 任务类型 */
  String type;

  /** 演练错误信息 */
  String message;

  /** 演练中涉及的aone应用名列表 */
  Set<String> aoneApps;

  /** 演练中涉及的主机地址 */
  Set<String> hostIps;

  /** 演练中涉及的小程序类型信息 */
  Set<String> appDescs;
}
