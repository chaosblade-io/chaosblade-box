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

import com.alibaba.chaosblade.box.common.common.domain.task.BaseExperimentTask;
import com.alibaba.fastjson.annotation.JSONField;
import java.io.Serializable;
import java.util.List;
import lombok.Data;

/** @author haibin */
@Data
public class ApplicationBasicInfo implements Serializable {

  @JSONField(name = "app_name")
  private String appName;

  @JSONField(name = "app_groups")
  private List<String> appGroups;

  @JSONField(name = "experiment_task_count")
  private Integer experimentTaskCount;

  @JSONField(name = "machine_count")
  private Integer machineCount;

  @JSONField(name = "task")
  private BaseExperimentTask task;

  @JSONField(name = "app_type")
  private Integer appType;

  @JSONField(name = "app_id")
  private String appId;
}
