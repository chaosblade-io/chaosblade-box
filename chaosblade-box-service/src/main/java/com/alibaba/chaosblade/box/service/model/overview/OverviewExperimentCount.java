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

package com.alibaba.chaosblade.box.service.model.overview;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

/** @author sunpeng */
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class OverviewExperimentCount {

  long total;
  long running;
  long finished;
  long exception;
  long failure;
  /** 执行过的演练 */
  long active;

  /** 成功的演练 */
  long success;

  /** 未进行过演练的数目 */
  long idle;
}
