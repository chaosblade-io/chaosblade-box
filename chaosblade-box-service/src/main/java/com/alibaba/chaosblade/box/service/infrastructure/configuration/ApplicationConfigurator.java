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

package com.alibaba.chaosblade.box.service.infrastructure.configuration;

import com.alibaba.chaosblade.box.dao.model.ApplicationConfigurationDO;
import com.alibaba.chaosblade.box.dao.repository.ApplicationConfigurationRepository;
import com.alibaba.chaosblade.box.service.model.application.ApplicationConfigurationUpdateRequest;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/** @author haibin.lhb */
@Component
public class ApplicationConfigurator {

  @Autowired private ApplicationConfigurationProvider applicationConfigurationProvider;

  @Autowired private ApplicationConfigurationRepository applicationConfigurationRepository;

  public List<ApplicationConfiguration> provide(Long appId) {
    return applicationConfigurationProvider.provideApplicationConfigurations(appId);
  }

  public boolean updateOrCreateIfNotExistConfiguration(
      ApplicationConfigurationUpdateRequest applicationConfigurationUpdateRequest) {
    ApplicationConfigurationDO applicationConfigurationDO =
        applicationConfigurationRepository.findByAliasAndAppId(
            applicationConfigurationUpdateRequest.getAlias(),
            applicationConfigurationUpdateRequest.getAppId());
    if (applicationConfigurationDO == null) {
      applicationConfigurationDO = new ApplicationConfigurationDO();
      applicationConfigurationDO.setValue(applicationConfigurationUpdateRequest.getValue());
      applicationConfigurationDO.setAlias(applicationConfigurationUpdateRequest.getAlias());
      applicationConfigurationDO.setAppId(
          Long.valueOf(applicationConfigurationUpdateRequest.getAppId()));
      applicationConfigurationDO.setScope(applicationConfigurationUpdateRequest.getScope());
      applicationConfigurationDO.setOverride(applicationConfigurationUpdateRequest.getOverride());
      applicationConfigurationDO.setName(applicationConfigurationUpdateRequest.getName());
      return applicationConfigurationRepository.add(applicationConfigurationDO);
    }
    applicationConfigurationDO.setScope(applicationConfigurationUpdateRequest.getScope());
    applicationConfigurationDO.setValue(applicationConfigurationUpdateRequest.getValue());
    applicationConfigurationDO.setOverride(applicationConfigurationUpdateRequest.getOverride());
    return applicationConfigurationRepository.update(applicationConfigurationDO);
  }
}
