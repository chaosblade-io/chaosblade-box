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

package com.alibaba.chaosblade.box.dao.infrastructure.model.cluster;

import com.alibaba.chaosblade.box.dao.model.ApplicationDeviceDO;
import lombok.AllArgsConstructor;
import lombok.Getter;

/** @author haibin.lhb */
@AllArgsConstructor
@Getter
public class KubernetesDataQuery {

  public KubernetesDataQuery(
      String userId,
      String clusterId,
      String targetConfigurationId,
      String namespace,
      String kubNamespace,
      String podName) {
    this.userId = userId;
    this.clusterId = clusterId;
    this.targetConfigurationId = targetConfigurationId;
    this.namespace = namespace;
    this.kubNamespace = kubNamespace;
    this.podName = podName;
  }

  public KubernetesDataQuery(String userId, String clusterId) {
    this.userId = userId;
    this.clusterId = clusterId;
  }

  private String userId;

  private String clusterId;

  private String targetConfigurationId;

  /** 这里的namespace是chaos box的，不是kubNamespace */
  private String namespace;

  private String kubNamespace;

  private String podName;

  public static KubernetesDataQuery ofApplicationDeviceDO(ApplicationDeviceDO applicationDeviceDO) {
    return new KubernetesDataQuery(
        applicationDeviceDO.getUserId(),
        applicationDeviceDO.getClusterId(),
        applicationDeviceDO.getConfigurationId(),
        applicationDeviceDO.getNamespace(),
        applicationDeviceDO.getKubNamespace(),
        applicationDeviceDO.getDeviceName());
  }

  public boolean supportApiServer() {
    return kubNamespace != null && podName != null;
  }

  private String deploymentName;
}
