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

package com.alibaba.chaosblade.box.dao.infrastructure.experiment.task.async;

import com.alibaba.chaosblade.box.common.app.sdk.scope.Host;
import com.alibaba.chaosblade.box.dao.model.DeviceDO;
import com.alibaba.chaosblade.box.dao.repository.DeviceRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/** @author sunpeng */
@Slf4j
@Component
public class K8sAsyncCallBackHostInterceptor implements AsyncCallBackHostInterceptor {

  @Autowired private DeviceRepository deviceRepository;

  @Override
  public void fillHostInfo(Host host) {
    DeviceDO deviceDO = deviceRepository.findByConfigurationId(host.getDeviceConfigurationId());
    log.info("[K8sAsyncCallBackHostInterceptor] targetIp:{}", host.getTargetIp());
    if (null != deviceDO) {
      log.info("[K8sAsyncCallBackHostInterceptor] privateIp:{}", deviceDO.getPrivateIp());
      host.setIp(deviceDO.getPrivateIp());
      host.setTargetIp(deviceDO.getPrivateIp());
    }
  }
}
