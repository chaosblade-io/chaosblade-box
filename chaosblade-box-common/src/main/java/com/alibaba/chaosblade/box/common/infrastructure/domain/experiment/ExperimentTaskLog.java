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
import java.util.Date;
import java.util.Map;
import java.util.Set;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

/** @author jiumu */
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ExperimentTaskLog {

  /** 演练名字 */
  String experimentName;

  /** 演练ID */
  String experimentId;

  /** 演练任务Id */
  String taskId;

  /** 演练开始时间 */
  Date startTime;

  /** 结束事件 */
  Date endTime;

  /** 演练执行人 */
  String userName;

  /** 结果 */
  ResultEnum result;

  /** 演练日志 */
  Map<Long, String> logs;

  /** 演练中涉及的aone应用名列表 */
  Set<String> aoneApps;
}
