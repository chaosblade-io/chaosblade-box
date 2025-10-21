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

package com.alibaba.chaosblade.box.service.model.scope;

import com.alibaba.chaosblade.box.dao.infrastructure.app.AppInfo;
import com.alibaba.chaosblade.box.service.model.cluster.ClusterInfo;
import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

/** @author haibin */
@Data
public class ScopeInfo {

  @JSONField(name = "private_ip")
  private String privateIp;

  @JSONField(name = "public_ip")
  private String publicIp;

  @JSONField(name = "agent_status")
  private Integer agentStatus;

  @JSONField(name = "last_ping_time")
  private Long lastHealthPingTime;

  @JSONField(name = "collect_time")
  private Long collectTime;

  @JSONField(name = "configuration_id")
  private String configurationId;

  @JSONField(name = "scope_type")
  private Integer scope;

  @JSONField(name = "ext_info")
  private ScopeExtInfo extInfo;

  @JSONField(name = "cluster_info")
  private ClusterInfo clusterInfo;

  @JSONField(name = "hostname")
  private String hostname;

  @JSONField(name = "running_info")
  private ScopeRunningInfo runningInfo;

  @JSONField(name = "agent_version")
  private String agentVersion;

  @JSONField(name = "os_version")
  private String osVersion;

  @JSONField(name = "app_info")
  private AppInfo appInfo;

  @JSONField(name = "device_id")
  private String deviceId;
}
