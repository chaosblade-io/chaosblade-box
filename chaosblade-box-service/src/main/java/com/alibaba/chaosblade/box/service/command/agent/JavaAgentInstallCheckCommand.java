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

package com.alibaba.chaosblade.box.service.command.agent;

import com.alibaba.chaosblade.box.common.app.sdk.scope.Host;
import com.alibaba.chaosblade.box.common.commands.SpringBeanCommand;
import com.alibaba.chaosblade.box.dao.model.ChaosBladeExpUidDO;
import com.alibaba.chaosblade.box.dao.model.ExperimentMiniAppTaskDO;
import com.alibaba.chaosblade.box.dao.repository.ChaosBladeExpUidRepository;
import com.alibaba.chaosblade.box.service.command.task.JavaAgentInstallCallback;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/** @author haibin */
@Component
public class JavaAgentInstallCheckCommand
    extends SpringBeanCommand<ExperimentMiniAppTaskDO, Boolean> {

  @Autowired private ChaosBladeExpUidRepository chaosBladeExpUidRepository;

  @Autowired private JavaAgentInstallCallback javaAgentInstallCallback;

  @Override
  public Boolean execute(ExperimentMiniAppTaskDO experimentMiniAppTaskDO) {
    Host host = new Host();
    host.setIp(experimentMiniAppTaskDO.getHostIp());
    host.setDeviceConfigurationId(experimentMiniAppTaskDO.getDeviceConfigurationId());
    ChaosBladeExpUidDO chaosBladeExpUidDO =
        chaosBladeExpUidRepository.findByActivityTargetTaskId(experimentMiniAppTaskDO.getTaskId());
    if (chaosBladeExpUidDO == null) {
      return false;
    }
    if (chaosBladeExpUidDO.getExpired()) {
      return false;
    }
    javaAgentInstallCallback.execute(experimentMiniAppTaskDO, chaosBladeExpUidDO, null);
    return null;
  }
}
