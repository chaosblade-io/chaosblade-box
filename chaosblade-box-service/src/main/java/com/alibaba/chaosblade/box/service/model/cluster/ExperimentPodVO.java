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

package com.alibaba.chaosblade.box.service.model.cluster;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/** @author haibin.lhb */
@Data
@EqualsAndHashCode
@ToString
public class ExperimentPodVO {

  @JSONField(name = "pod_ip")
  private String podIp;

  @JSONField(name = "pod_name")
  private String podName;

  @JSONField(name = "kub_namespace")
  private String kubNamespace;

  @JSONField(name = "app_name")
  private String appName;

  @JSONField(name = "app_id")
  private Long appId;
  /** 是否被演练 */
  @JSONField(name = "is_experiment")
  private Boolean isExperimented;

  @JSONField(name = "last_heart_time")
  private Long heartTime;
}
