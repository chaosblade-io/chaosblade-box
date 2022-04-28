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

    Map<String, String> queryAgentInstallCommandByMode(String var1, String var2, InstallMode var3, DeviceOsType var4, String var5, String var6);

    public String queryHelmAgentInstallPackageAddress();

    public Map<String, String> queryAgentUnInstallCommand(String userId, String namespace, String configurationId);

    public Boolean uninstallAgent(String user, SettingRequest settingRequest);

    public Response<String> installAgentForHost(String lincense, InstallAgentForHostRequest installAgentForHostRequest);

    public Boolean uninstallAgentForHost(String user, SettingRequest settingRequest);

    public Boolean pingAgent(DeviceDO deviceDO);

    public PluginDTO queryAgentPluginDetail(String var1, String var2, String var3);
}
