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

import com.alibaba.chaosblade.box.common.common.annotation.KubernetesExtensionPoint;
import com.alibaba.chaosblade.box.dao.infrastructure.utils.CloudHostUtil;
import com.alibaba.chaosblade.box.dao.model.DeviceDO;
import com.alibaba.chaosblade.box.dao.repository.ApplicationDeviceRepository;
import com.alibaba.chaosblade.box.service.model.cluster.ClusterInfo;
import com.alibaba.chaosblade.box.service.model.scope.ScopeInfo;
import org.springframework.beans.factory.annotation.Autowired;

/** @author haibin.lhb */
@KubernetesExtensionPoint
public class ClusterScopeInfoQueryInterceptor implements ScopeInfoQueryInterceptor {

  @Autowired private ApplicationDeviceRepository applicationDeviceRepository;

  @Override
  public void doQuery(ScopeInfo nodeScopeInfo, DeviceDO nodeDeviceDO) {
    if (!CloudHostUtil.isK8sInstallMode(nodeDeviceDO.getInstallMode())) {
      return;
    }
    ClusterInfo clusterInfo = new ClusterInfo();
    clusterInfo.setClusterName(nodeDeviceDO.getClusterName());
    nodeScopeInfo.setClusterInfo(clusterInfo);
    clusterInfo.setPodCount(
        applicationDeviceRepository.countDeviceByClusterNode(nodeDeviceDO.getConfigurationId()));
  }
}
