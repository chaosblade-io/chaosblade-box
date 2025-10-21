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

package com.alibaba.chaosblade.box.service.model.application;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

/** @author haibin */
@Data
public class UserApplicationSummary {

  @JSONField(name = "app_name")
  private String appName;

  private Boolean isDefault;

  @JSONField(name = "app_type")
  private Integer appType;

  @JSONField(name = "app_id")
  private String appId;

  @JSONField(name = "experiment_task_count")
  private Integer experimentTaskCount;

  @JSONField(name = "machine_count")
  private Integer machineCount;

  /** 场景数目 */
  @JSONField(name = "scene_function_count")
  private Integer sceneFunctionCount;
}
