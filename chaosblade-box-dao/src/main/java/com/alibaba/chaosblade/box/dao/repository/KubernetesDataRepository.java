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

/** @author haibin.lhb */
public interface KubernetesDataRepository {

  /**
   * 查询pod信息,条件满足下会优先通过apiServer去查询,然后在用chaos来兜底，为了保证精准度
   *
   * @param kubernetesDataQuery
   * @return
   */
  public Optional<KubernetesPod> findPodInfo(KubernetesDataQuery kubernetesDataQuery);
}
