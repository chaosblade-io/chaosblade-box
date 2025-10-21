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

package com.alibaba.chaosblade.box.service.command.scope;

import com.alibaba.chaosblade.box.common.commands.SpringBeanCommand;
import com.alibaba.chaosblade.box.common.common.domain.Response;
import com.alibaba.chaosblade.box.common.common.enums.CommonErrorCode;
import com.alibaba.chaosblade.box.common.common.enums.ScopeTypeEnum;
import com.alibaba.chaosblade.box.dao.infrastructure.utils.CloudHostUtil;
import com.alibaba.chaosblade.box.dao.model.DeviceDO;
import com.alibaba.chaosblade.box.dao.repository.DeviceRepository;
import com.alibaba.chaosblade.box.dao.repository.ExperimentHostRelationRepository;
import com.alibaba.chaosblade.box.service.model.scope.ScopeInfo;
import com.alibaba.chaosblade.box.service.model.scope.ScopeInfoQueryRequest;
import com.alibaba.chaosblade.box.service.model.scope.ScopeRunningInfo;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/** @author haibin */
@Component
public class ScopeInfoQueryCommand
    extends SpringBeanCommand<ScopeInfoQueryRequest, Response<ScopeInfo>> {

  @Autowired private DeviceRepository deviceRepository;

  @Autowired private ExperimentHostRelationRepository experimentHostRelationRepository;

  @Autowired private List<ScopeInfoQueryInterceptor> scopeInfoQueryInterceptorList;

  @Override
  public Response<ScopeInfo> execute(ScopeInfoQueryRequest scopeInfoQueryRequest) {
    String configId = scopeInfoQueryRequest.getConfigurationId();
    DeviceDO deviceDO = deviceRepository.findByConfigurationId(configId);
    if (deviceDO == null) {
      return Response.failedWith(CommonErrorCode.B_HOST_NOT_FOUND);
    }
    ScopeInfo scopeInfo = new ScopeInfo();
    fillCommonInfo(scopeInfo, deviceDO);
    fillRunningInfo(scopeInfo, deviceDO);
    for (ScopeInfoQueryInterceptor scopeInfoQueryInterceptor : scopeInfoQueryInterceptorList) {
      scopeInfoQueryInterceptor.doQuery(scopeInfo, deviceDO);
    }
    return Response.okWithData(scopeInfo);
  }

  private void fillRunningInfo(ScopeInfo scopeInfo, DeviceDO deviceDO) {
    ScopeRunningInfo scopeRunningInfo = new ScopeRunningInfo();
    scopeRunningInfo.setTotal(
        experimentHostRelationRepository.countExperimentTaskByHost(deviceDO.getConfigurationId()));
    scopeInfo.setRunningInfo(scopeRunningInfo);
  }

  private void fillCommonInfo(ScopeInfo scopeInfo, DeviceDO deviceDO) {
    scopeInfo.setAgentStatus(deviceDO.getStatus());
    scopeInfo.setConfigurationId(deviceDO.getConfigurationId());
    scopeInfo.setPrivateIp(deviceDO.getPrivateIp());
    scopeInfo.setPublicIp(deviceDO.getPublicIp());
    scopeInfo.setScope(
        CloudHostUtil.isK8sInstallMode(deviceDO.getInstallMode())
            ? ScopeTypeEnum.K8s.getValue()
            : ScopeTypeEnum.HOST.getValue());
    scopeInfo.setLastHealthPingTime(deviceDO.getLastHealthPingTime());
    scopeInfo.setHostname(deviceDO.getDeviceName());
    scopeInfo.setCollectTime(deviceDO.getConnectTime());
    scopeInfo.setOsVersion(deviceDO.getOsVersion());
    scopeInfo.setAgentVersion(deviceDO.getVersion());
    scopeInfo.setDeviceId(deviceDO.getDeviceId());
  }
}
