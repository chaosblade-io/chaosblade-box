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

package com.alibaba.chaosblade.box.common.experiment.request;

import com.alibaba.chaosblade.box.common.common.enums.ResultEnum;
import java.util.Date;
import java.util.Set;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

/** @author jiumu */
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ExperimentTaskRequest {

  String experimentId;

  Date startTime;

  Date endTime;

  /** 按应用进行统计 */
  Set<String> aoneApps;

  /** 按类型进行统计 */
  Set<String> appDescs;

  /** 按结果进行统计 */
  Set<ResultEnum> resultEnums;
}
