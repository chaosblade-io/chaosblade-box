package com.alibaba.chaosblade.box.service.impl;

import com.alibaba.chaosblade.box.common.app.sdk.scope.Host;
import com.alibaba.chaosblade.box.common.common.constant.ChaosConstant;
import com.alibaba.chaosblade.box.common.common.domain.response.Response;
import com.alibaba.chaosblade.box.common.common.enums.DeviceOsType;
import com.alibaba.chaosblade.box.common.common.enums.DeviceType;
import com.alibaba.chaosblade.box.common.common.enums.InstallMode;
import com.alibaba.chaosblade.box.common.experiment.task.flow.util.PluginType;
import com.alibaba.chaosblade.box.common.infrastructure.constant.DeviceStatus;
import com.alibaba.chaosblade.box.common.sdk.AgentForChaos;
import com.alibaba.chaosblade.box.dao.infrastructure.app.chaosblade.ChaosBladeInvoker;
import com.alibaba.chaosblade.box.dao.infrastructure.utils.ProxyHelper;
import com.alibaba.chaosblade.box.dao.model.DeviceDO;
import com.alibaba.chaosblade.box.dao.model.UserDo;
import com.alibaba.chaosblade.box.dao.repository.DeviceRepository;
import com.alibaba.chaosblade.box.dao.repository.UserRepository;
import com.alibaba.chaosblade.box.service.SettingService;
import com.alibaba.chaosblade.box.service.model.agent.InstallAgentForHostRequest;
import com.alibaba.chaosblade.box.service.model.agent.PluginDTO;
import com.alibaba.chaosblade.box.service.model.agent.SettingRequest;
import com.alibaba.chaosblade.box.toolsmgr.ChaosToolsMgr;
import com.alibaba.chaosblade.box.toolsmgr.model.MgrRequest;
import com.google.common.base.Preconditions;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Service("settingService")
public class SettingServiceImpl implements SettingService {

    public static final String COMMAND_FILE_DOWNLOAD = "command_file_download";
    public static final String COMMAND_INSTALL = "command_install";
    public static final String COMMAND_UNINSTALL = "command_uninstall";

    @Value("${regionId.default}")
    private String regionId;

    @Value("${chaos.agent.url}")
    private String agentCtl;

    @Value("${chaos.agent.helm:}")
    private String agentHelm;

    @Value("${chaos.agent.version:}")
    private String chaosAgentVersion;

    @Value("${chaos.agent.repository:}")
    private String chaosAgentRepository;


    @Value("${chaos.server.domain:}")
    private String boxServerDomain;

    @Resource
    private DeviceRepository deviceRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AgentForChaos agentForChaos;

    @Autowired
    private ChaosToolsMgr chaosToolsMgr;

    @Autowired
    private ChaosBladeInvoker chaosBladeInvoker;

    @Override
    public Map<String, String> queryAgentInstallCommandByMode(String userId, String namespace, InstallMode mode,
                                                              DeviceOsType osType, String helmVersion, String lang) {
        Map<String, String> result = new HashMap<>();

        if (InstallMode.host.name().equalsIgnoreCase(mode.name())) {
            StringBuilder sb = buildBasicAgentInstallCommandForHost(userId, namespace, lang);
            result.put(COMMAND_INSTALL, sb.toString());
            return result;
        } else if (InstallMode.isKubernetes(mode.name())) {
            StringBuilder sb = buildAgentInstallCommandByHelmVersion(userId, namespace, helmVersion, lang);
            result.put(COMMAND_INSTALL, sb.toString());
            return result;
        }
        return null;
    }

    private StringBuilder buildBasicAgentInstallCommandForHost(String userId, String namespace, String lang) {
        Preconditions.checkArgument(StringUtils.isNotBlank(userId));
        Preconditions.checkArgument(StringUtils.isNotBlank(namespace));
        UserDo userDo = userRepository.getById(userId);
        if (userDo == null){
            return new StringBuilder();
        }
        String licenseKey = userDo.getLicense();

        StringBuilder sb = new StringBuilder("wget ");
        sb.append(agentCtl);
        sb.append(" -O chaos.tar.gz");

        sb.append(" && tar -zxvf chaos.tar.gz -C /opt/ ");

        sb.append("&& sudo sh /opt/chaos/chaosctl.sh install");

        sb.append(" -k ");
        sb.append(licenseKey);

        if (lang.equals(ChaosConstant.LANGUAGE_EN)) {
            sb.append(" -p ");
            sb.append(" [application name] ");
            sb.append(" -g ");
            sb.append(" [application group] ");

            sb.append(" -P ");
            sb.append(" [agent port] ");
        } else {
            sb.append(" -p ");
            sb.append(" [应用名] ");
            sb.append(" -g ");
            sb.append(" [应用分组] ");

            sb.append(" -P ");
            sb.append(" [agent端口号] ");
        }


        sb.append(" -t ");
        if (boxServerDomain.isEmpty()) {
            sb.append(" [chaosblade-box ip:port] ");
        } else {
            sb.append(boxServerDomain);
        }

        return sb;
    }

    private StringBuilder buildAgentInstallCommandByHelmVersion(String userId, String namespace, String helmVersion, String lang) {
        UserDo userDo = userRepository.getById(userId);
        if (userDo == null){
            return new StringBuilder();
        }
        String license = userDo.getLicense();

        StringBuilder sb = new StringBuilder("helm install ");
        String imageDomain = "";
        if (StringUtils.isNotBlank(helmVersion)) {
            switch (helmVersion) {
                case "v2":
                    sb.append(" --set");
                    sb.append(" env.name=");
                    sb.append(namespace);
                    sb.append(" --set license=");
                    sb.append(license);

                    if (lang.equals(ChaosConstant.LANGUAGE_EN)) {
                        sb.append(
                                " --set controller.cluster_id={replace with cluster id, value No special requirements} --set controller"
                                        + ".cluster_name={replace with cluster name, value No special requirements}");
                        sb.append(" --set transport.endpoint={replace with ip:port of box}");
                    } else {
                        sb.append(
                                " --set controller.cluster_id={替换为集群id，取值无特殊要求} --set controller"
                                        + ".cluster_name={替换为集群名字，取值无特殊要求}");
                        sb.append(" --set transport.endpoint={替换为box的 ip:port}");
                    }


                    if (StringUtils.isNotBlank(chaosAgentRepository)) {
                        sb.append(" --set images.chaos.repository=").append(chaosAgentRepository);
                        sb.append(" --set images.chaos.version=").append(chaosAgentVersion);
                    }
                    sb.append(" --namespace chaosblade --name agent chaos.tgz");
                    break;
                case "v3":
                    sb.append(" agent chaos.tgz");
                    sb.append(" --namespace chaosblade --set env.name=");
                    sb.append(namespace);
                    sb.append(",license=");
                    sb.append(license);
                    if (StringUtils.isNotBlank(chaosAgentRepository)) {
                        sb.append(",images.chaos.repository=").append(chaosAgentRepository);
                        sb.append(",images.chaos.version=").append(chaosAgentVersion);
                    }

                    if (lang.equals(ChaosConstant.LANGUAGE_EN)) {
                        sb.append(",transport.endpoint={replace with ip:port of box}");

                        sb.append(",controller.cluster_id={replace with cluster id, value No special requirements},controller.cluster_name={replace with cluster name, value No special requirements}");
                    } else {
                        sb.append(",transport.endpoint={替换为box的 ip:port}");

                        sb.append(",controller.cluster_id={替换为集群id，取值无特殊要求},controller.cluster_name={替换为集群名字，取值无特殊要求}");
                    }
                    break;
                default:
                    return buildAgentInstallCommandByHelm(userId, namespace, license);
            }
        } else {
            return buildAgentInstallCommandByHelm(userId, namespace, license);
        }
        return sb;
    }

    private StringBuilder buildAgentInstallCommandByHelm(String userId, String namespace, String license) {
        StringBuilder sb = new StringBuilder("helm install");
        sb.append(" --set");
        sb.append(" env.name=");
        sb.append(namespace);
        Preconditions.checkArgument(StringUtils.isNotBlank(userId));
        Preconditions.checkArgument(StringUtils.isNotBlank(namespace));
        sb.append(" --set");
        sb.append(" license=");
        sb.append(license);
        sb.append(" --namespace chaosblade");
        sb.append(" --name agent");
        sb.append(" chaos");
        sb.append(".tgz");

        return sb;
    }

    @Override
    public String queryHelmAgentInstallPackageAddress() {
        StringBuilder sb = new StringBuilder("wget ");
        sb.append(agentHelm);
        sb.append(" -O chaos");
        sb.append(".tgz");
        return sb.toString();
    }

    @Override
    public Map<String, String> queryAgentUnInstallCommand(String userId, String namespace, String configurationId) {
        if (StringUtils.isBlank(configurationId)) {
            return queryAgentUnInstallCommand(null, null);
        }
        DeviceDO deviceDO = deviceRepository.findByConfigurationIdAndUserId(userId, configurationId);
        if (deviceDO == null) {
            return queryAgentUnInstallCommand(null, null);
        }
        String installMode = deviceDO.getInstallMode();
        installMode = StringUtils.isNotBlank(installMode) ? installMode : InstallMode.host.name();
        Map<String, String> result = new HashMap<>();
        result.put(COMMAND_UNINSTALL,
                buildAgentUnInstallCommand(deviceDO.getVersion(), installMode,
                        DeviceOsType.getByTypeDefaultLinux(deviceDO.getOsType())));
        return result;
    }



    private String queryHostAgentUnInstallCommand() {
        StringBuilder sb = new StringBuilder("/opt/chaos/chaosctl.sh uninstall");
        return sb.toString();
    }

    private String buildAgentUnInstallCommand(String version, String installMode, DeviceOsType osType) {
        //host
        if (InstallMode.host.name().equalsIgnoreCase(installMode)) {
            return queryAgentUnInstallCommand(version, osType).get(COMMAND_UNINSTALL);
        }

        //k8s
        return buildAgentUnInstallCommandByHelm();
    }

    private Map<String, String> queryAgentUnInstallCommand(String version, DeviceOsType osType) {
        Map<String, String> result = new HashMap<>();

        result.put(COMMAND_UNINSTALL, queryHostAgentUnInstallCommand());


        return result;
    }

    private String buildAgentUnInstallCommandByHelm() {
        StringBuilder sb = new StringBuilder("helm delete agent -n chaosblade");
        return sb.toString();
    }


    @Override
    public Boolean uninstallAgent(String userId, SettingRequest settingRequest) {
        DeviceDO deviceDO = deviceRepository.findByConfigurationIdAndUserIdNamespace(userId,
                settingRequest.getConfigurationId(), settingRequest.getNamespace());
        if (DeviceStatus.getEnableInstallStatus().contains(deviceDO.getStatus())) {
            return true;
        }

        String hostDeviceIp = ProxyHelper.getProxyIp(deviceDO.getDeviceType(), deviceDO.getPrivateIp(),
                deviceDO.getParentIp());
        Response<String> response = agentForChaos.uninstallAgent(hostDeviceIp, Integer.toString(deviceDO.getPort()),"uninstall");
        return response.isSuccess();
    }

    @Override
    public Response<String> installAgentForHost(String lincense, InstallAgentForHostRequest installAgentForHostRequest) {

        DeviceDO deviceDO = deviceRepository.findActiveByPrivateIp(installAgentForHostRequest.getInstanceId());
        if (null != deviceDO) {
            return Response.ofSuccess("have installed");
        }

        MgrRequest mgrRequest = MgrRequest.builder().
                appGroupName(installAgentForHostRequest.getAppGroupName()).
                appName(installAgentForHostRequest.getAppName()).
                instanceIp(installAgentForHostRequest.getInstanceId()).
                instancePort(installAgentForHostRequest.getSshPort()).
                instanceUser(installAgentForHostRequest.getSshUser()).
                instancePassword(installAgentForHostRequest.getSshPassword()).
                needPassword(true).
                license(lincense).
                agentCtl(agentCtl).
                chaosboxEndpoint(boxServerDomain).
                build();
        // set community license


        Response<String> testResponse = chaosToolsMgr.testConnection(mgrRequest);
        if (!testResponse.isSuccess()){
            return testResponse;
        }

        // -k license -p 应用名 -g 应用分组名 -t chaos-server的ip:port
        String command = String.format("wget %s -O chaos.tar.gz " +
                        "&& tar -zxvf chaos.tar.gz -C /opt/ " +
                        "&& sudo sh /opt/chaos/chaosctl.sh install -k %s -p %s -g %s -t %s",
                mgrRequest.getAgentCtl(), mgrRequest.getLicense(), mgrRequest.getAppName(), mgrRequest.getAppGroupName(), mgrRequest.getChaosboxEndpoint());

        mgrRequest.setCommand(command);
        return chaosToolsMgr.deployAndInstallAgent(mgrRequest);
    }

    @Override
    public Boolean uninstallAgentForHost(String userId, SettingRequest settingRequest) {
        // 1. check host need uninstall or not
        DeviceDO deviceDO = deviceRepository.findByConfigurationId(settingRequest.getConfigurationId());
        if (deviceDO == null || !deviceDO.isAlive()) {
            return true;
        }

        Host host = new Host();
        host.setIp(deviceDO.getPrivateIp());
        host.setTargetIp(deviceDO.getPrivateIp());
        host.setPort(deviceDO.getPort());
        Response<String> uninstallResponse = chaosBladeInvoker.uninstallAgent(host);
        return uninstallResponse.isSuccess();

    }

    @Override
    public Boolean pingAgent(DeviceDO deviceDO) {
        // 1. just ping agent which is alive
        if (deviceDO == null || !deviceDO.isAlive()) {
            return true;
        }

        Host host = new Host();
        host.setIp(deviceDO.getPrivateIp());
        host.setTargetIp(deviceDO.getPrivateIp());
        host.setPort(deviceDO.getPort());
        Response<String> pingResponse = chaosBladeInvoker.pingAgent(host);
        return pingResponse.isSuccess();
    }

    @Override
    public PluginDTO queryAgentPluginDetail(String userId, String namespace, String deviceId) {
        DeviceDO deviceDO = deviceRepository.getDeviceByDeviceId(userId, namespace, deviceId, DeviceType.HOST.getType());
        if (deviceDO != null) {
            Date deviceOfflineTime = DateUtils.addMinutes(new Date(), -1);

            return convertHostDeviceToPlugin(deviceDO, PluginType.CHAOS_AGENT.name(),
                    deviceOfflineTime);
        }

        return null;
    }

    private PluginDTO convertHostDeviceToPlugin(DeviceDO deviceDO, String pluginType, Date deviceOfflineTime) {
        PluginDTO pluginDTO = new PluginDTO();
        pluginDTO.setInstanceId(deviceDO.getDeviceId());
        pluginDTO.setInstanceName(deviceDO.getDeviceName());
        pluginDTO.setPrivateIp(deviceDO.getPrivateIp());
        pluginDTO.setPublicIp(deviceDO.getPublicIp());
        pluginDTO.setPluginStatus(deviceDO.getStatus());
        pluginDTO.setConfigurationId(deviceDO.getConfigurationId());
        pluginDTO.setPluginType(pluginType);
        pluginDTO.setEnable(deviceDO.getEnable());
        pluginDTO.setVersion(deviceDO.getVersion());
        pluginDTO.setCreateTime(deviceDO.getGmtCreate().getTime());
        pluginDTO.setConnectTime(deviceDO.getConnectTime());
        pluginDTO.setInstallMode(simplifyInstallMode(deviceDO.getInstallMode(), deviceDO.getOsType()));
        pluginDTO.setCanAutoInstall(Boolean.TRUE);

        if (deviceDO.getLastHealthPingTime() != null && deviceDO.getLastHealthPingTime() < deviceOfflineTime
                .getTime()) {
            pluginDTO.setPluginStatus(DeviceStatus.OFFLINE.getStatus());
        }
        return pluginDTO;
    }
    private String simplifyInstallMode(String installMode, Integer osType) {
        if (StringUtils.isNotBlank(installMode)) {
            if (InstallMode.isKubernetes(installMode)) {
                return "Kubernetes";
            }

            if (DeviceOsType.linux.getType() == osType) {
                return "Linux Host";
            }
            return "Host";
        }

        return null;
    }
}
