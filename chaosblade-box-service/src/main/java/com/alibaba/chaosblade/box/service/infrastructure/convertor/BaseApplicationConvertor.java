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

package com.alibaba.chaosblade.box.service.infrastructure.convertor;

import com.alibaba.chaosblade.box.dao.model.ApplicationRelationDO;
import com.alibaba.chaosblade.box.dao.repository.ActivityTaskRepository;
import com.alibaba.chaosblade.box.dao.repository.ApplicationDeviceRepository;
import com.alibaba.chaosblade.box.dao.repository.ApplicationRelationRepository;
import org.springframework.beans.factory.annotation.Autowired;

/** @author haibin */
public abstract class BaseApplicationConvertor<From, To> {

  @Autowired protected ApplicationRelationRepository applicationRelationRepository;

  @Autowired protected ApplicationDeviceRepository applicationDeviceRepository;

  @Autowired private ActivityTaskRepository activityTaskRepository;

  /**
   * 类型转换
   *
   * @param from
   * @return
   */
  public abstract To convert(From from);

  protected Integer getMachineCount(Long appId) {
    return applicationDeviceRepository.countByAppId(appId);
  }

  protected Integer getExperimentTaskCount(Long appId) {
    return applicationRelationRepository.countByAppIdAndRelationType(
        appId, ApplicationRelationDO.RELATION_EXPERIMENT_TASK);
  }

  protected Integer getSceneFunctionCount(Long appId) {
    return activityTaskRepository.countAppCodesCountByAppId(appId);
  }
}
