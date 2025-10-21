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

package com.alibaba.chaosblade.box.service;

import com.alibaba.chaosblade.box.common.common.domain.response.Response;
import com.alibaba.chaosblade.box.common.common.enums.DeviceOsType;
import com.alibaba.chaosblade.box.common.common.enums.InstallMode;
import com.alibaba.chaosblade.box.dao.model.DeviceDO;
import com.alibaba.chaosblade.box.service.model.agent.InstallAgentForHostRequest;
import com.alibaba.chaosblade.box.service.model.agent.PluginDTO;
import com.alibaba.chaosblade.box.service.model.agent.SettingRequest;
import java.util.Map;

public interface SettingService {

  Map<String, String> queryAgentInstallCommandByMode(
      String var1, String var2, InstallMode var3, DeviceOsType var4, String var5, String var6);

  public String queryHelmAgentInstallPackageAddress();

  public Map<String, String> queryAgentUnInstallCommand(
      String userId, String namespace, String configurationId);

  public Boolean uninstallAgent(String user, SettingRequest settingRequest);

  public Response<String> installAgentForHost(
      String lincense, InstallAgentForHostRequest installAgentForHostRequest);

  public Boolean uninstallAgentForHost(String user, SettingRequest settingRequest);

  public Boolean pingAgent(DeviceDO deviceDO);

  public PluginDTO queryAgentPluginDetail(String var1, String var2, String var3);
}
