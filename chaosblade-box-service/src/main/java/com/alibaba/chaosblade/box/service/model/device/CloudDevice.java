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

package com.alibaba.chaosblade.box.service.model.device;

import com.alibaba.chaosblade.box.common.app.sdk.scope.Host;
import com.alibaba.chaosblade.box.common.common.enums.ScopeTypeEnum;
import com.alibaba.chaosblade.box.dao.infrastructure.utils.CloudHostUtil;
import com.alibaba.chaosblade.box.dao.model.DeviceDO;
import com.google.common.base.Strings;
import lombok.Getter;
import lombok.Setter;

/** @author sunju */
@Getter
@Setter
public class CloudDevice extends Host {

  /** 机器禁用原因 */
  private String authMessage;

  public CloudDevice(String ip, int port) {
    super(ip, port);
  }

  public static CloudDevice from(DeviceDO device) {
    CloudDevice cloudDevice = new CloudDevice(device.getPrivateIp(), device.getPort());
    cloudDevice.setVpcId(device.getVpcId());
    cloudDevice.setPrivateIp(device.getPrivateIp());
    cloudDevice.setDeviceId(device.getDeviceId());
    cloudDevice.setDeviceName(device.getDeviceName());
    cloudDevice.setDeviceConfigurationId(device.getConfigurationId());
    cloudDevice.setK8s(CloudHostUtil.isK8sInstallMode(device.getInstallMode()));
    cloudDevice.setMaster(DeviceRole.master.name().equals(device.getDeviceRole()));
    cloudDevice.setDeviceType(device.getDeviceType());
    cloudDevice.setAllow(true);
    if (!Strings.isNullOrEmpty(device.getClusterName())) {
      cloudDevice.setClusterName(device.getClusterName());
    } else {
      cloudDevice.setClusterName(device.getDeviceName());
    }
    cloudDevice.setClusterId(device.getClusterId());
    cloudDevice.setScopeType(
        cloudDevice.isK8s() ? ScopeTypeEnum.K8s.getValue() : ScopeTypeEnum.HOST.getValue());
    return cloudDevice;
  }
}
