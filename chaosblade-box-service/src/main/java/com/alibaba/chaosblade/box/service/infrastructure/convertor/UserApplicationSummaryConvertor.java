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

import com.alibaba.chaosblade.box.dao.model.ApplicationDO;
import com.alibaba.chaosblade.box.service.model.application.UserApplicationSummary;
import org.springframework.stereotype.Component;

/** @author haibin */
@Component
public class UserApplicationSummaryConvertor
    extends BaseApplicationConvertor<ApplicationDO, UserApplicationSummary> {

  @Override
  public UserApplicationSummary convert(ApplicationDO applicationDO) {
    UserApplicationSummary userApplicationSummary = new UserApplicationSummary();
    userApplicationSummary.setAppId(String.valueOf(applicationDO.getId()));
    userApplicationSummary.setAppType(applicationDO.getAppType());
    userApplicationSummary.setAppName(applicationDO.getAppName());
    userApplicationSummary.setIsDefault(false);
    userApplicationSummary.setMachineCount(getMachineCount(applicationDO.getId()));
    userApplicationSummary.setExperimentTaskCount(getExperimentTaskCount(applicationDO.getId()));
    return userApplicationSummary;
  }
}
