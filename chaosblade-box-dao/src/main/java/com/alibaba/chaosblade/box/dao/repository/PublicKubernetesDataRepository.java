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

package com.alibaba.chaosblade.box.dao.repository;

import com.alibaba.chaosblade.box.dao.infrastructure.model.cluster.KubernetesDataQuery;
import com.alibaba.chaosblade.box.dao.infrastructure.model.cluster.KubernetesPod;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/** @author haibin.lhb */
@Component
@Slf4j
public class PublicKubernetesDataRepository implements KubernetesDataRepository {

  @Override
  public Optional<KubernetesPod> findPodInfo(KubernetesDataQuery kubernetesDataQuery) {
    KubernetesPod kubernetesPod = getKubernetesPod(kubernetesDataQuery);
    if (kubernetesPod != null) {
      kubernetesPod.setUserId(kubernetesDataQuery.getUserId());
      kubernetesPod.setNamespace(kubernetesDataQuery.getNamespace());
      kubernetesPod.setTargetConfigurationId(kubernetesDataQuery.getTargetConfigurationId());
      kubernetesPod.setClusterId(kubernetesDataQuery.getClusterId());
    }
    return Optional.ofNullable(kubernetesPod);
  }

  private KubernetesPod getKubernetesPod(KubernetesDataQuery kubernetesDataQuery) {
    return null;
  }
}
