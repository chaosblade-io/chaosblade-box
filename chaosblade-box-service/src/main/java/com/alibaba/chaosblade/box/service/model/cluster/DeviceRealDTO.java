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

import lombok.Data;

@Data
public class DeviceRealDTO {
  private String userId;
  private String environment;
  private String vpcId;
  private String provider;
  private String clusterId;
  private String clusterName;
  private String clusterNamespace;
  private String zoneId;
  private String version;
  private String chaosVersion;
  private String publicIp;
  private String privateIp;
  private String parentIp;
  private String deviceId;
  private Integer deviceType;
  private String deviceName;
  private String deviceRole;
  private String serialNumber;
  private String osVersion;
  private String hostname;
  private String spec;
  private Integer cpu;
  private Integer mem;
  private String configurationId;
  private String hostConfigurationId;
  private String podConfigurationId;
  private String hostInstanceId;
  private String parentConfigurationId;
  private String parentDeviceName;
  private Integer parentDeviceType;
  private String parentDeviceSpace;
  private String applicationName;
  private Integer status;
  private String state;
  private Boolean enable;
  private String installMode;
  private String ports;
  private String deviceCreateTime;
  private String uptime;
  private Long connectTime;
  private String commandId;
  private String requestId;
  private Long commandTime;
  private String requestResult;
  private String reason;
  private String reasonCode;
  private String md5;
  private String daemonSetConfigurationId;
  private String replicaSetConfigurationId;
  private String deploymentConfigurationId;
  private String serviceConfigurationId;
  private String extInfo;
  private Long leaseTime;
}
