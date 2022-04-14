package com.alibaba.chaosblade.box.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.alibaba.chaosblade.box.common.common.ChaosAgentVersion;
import com.alibaba.chaosblade.box.common.common.domain.response.Response;
import com.alibaba.chaosblade.box.common.common.enums.CommonErrorCode;
import com.alibaba.chaosblade.box.common.common.enums.InstallMode;
import com.alibaba.chaosblade.box.common.infrastructure.domain.chaostools.ChaosToolsRequest;
import com.alibaba.chaosblade.box.common.infrastructure.domain.chaostools.ToolsOverview;
import com.alibaba.chaosblade.box.common.infrastructure.domain.chaostools.scenario.ChaosTools;
import com.alibaba.chaosblade.box.common.infrastructure.domain.chaostools.scenario.DefaultScenarioYamlProvider;
import com.alibaba.chaosblade.box.common.infrastructure.domain.chaostools.scenario.ScenarioRequest;
import com.alibaba.chaosblade.box.common.infrastructure.exception.ChaosException;
import com.alibaba.chaosblade.box.common.sdk.LitmusChaosForCloud;
import com.alibaba.chaosblade.box.dao.mapper.ChaosToolsMapper;
import com.alibaba.chaosblade.box.dao.model.ChaosToolsDO;
import com.alibaba.chaosblade.box.dao.model.DeviceDO;
import com.alibaba.chaosblade.box.dao.model.UserDo;
import com.alibaba.chaosblade.box.dao.repository.ChaosToolsRepository;
import com.alibaba.chaosblade.box.dao.repository.DeviceRepository;
import com.alibaba.chaosblade.box.dao.repository.LicenseRepository;
import com.alibaba.chaosblade.box.service.ChaosToolsService;
import com.alibaba.chaosblade.box.dao.infrastructure.utils.PluginTypeUtil;
import com.alibaba.chaosblade.box.dao.infrastructure.utils.ProxyHelper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.representer.Representer;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class ChaosToolsServiceImpl implements ChaosToolsService {

    @Autowired
    private DefaultScenarioYamlProvider scenarioYamlProvider;

    @Autowired
    private ChaosToolsMapper chaosToolsMapper;

    @Autowired
    private ChaosToolsRepository chaosToolsRepository;

    @Autowired
    private LitmusChaosForCloud litmusChaosForCloud;

    @Autowired
    private LicenseRepository licenseRepository;

    @Autowired
    private DeviceRepository deviceRepository;

    private Map<String, ChaosAgentVersion> versionCache = new HashMap<>();

    @Override
    public List<ToolsOverview> toolsOverviewList(ChaosToolsRequest chaosToolsRequest) {

        return Arrays.stream(ChaosTools.values()).map(chaosTools -> {
            String configuration = scenarioYamlProvider.overview(ScenarioRequest.builder().chaosTools(chaosTools.getName()).build());
            Representer representer = new Representer();
            representer.getPropertyUtils().setSkipMissingProperties(true);
            Yaml yaml = new Yaml(representer);
            ToolsOverview toolsOverview = yaml.loadAs(configuration, ToolsOverview.class);
            if (InstallMode.host.equals(InstallMode.valueOf(chaosToolsRequest.getInstallMode()))
                    && !"chaosblade".equals(toolsOverview.getName())) {
                return null;
            }
            if ("chaosblade".equals(toolsOverview.getName())) {
                toolsOverview.setInstalled(true);
                toolsOverview.setUnInstalled(false);
            } else {
                toolsOverview.setUnInstalled(true);
            }
            Optional<ChaosToolsDO> chaosToolsDO;
            if ("host".equals(chaosToolsRequest.getInstallMode())) {
                chaosToolsDO = chaosToolsRepository.selectByConfigurationId(chaosToolsRequest.getOperateId());
            } else {
                chaosToolsDO = chaosToolsRepository.selectByClusterId(chaosToolsRequest.getOperateId());
            }
            if (chaosToolsDO.isPresent()) {
                toolsOverview.setInstalled(true);
            }
            return toolsOverview;
        }).filter(Objects::nonNull).collect(Collectors.toList());
    }

    @Override
    public com.alibaba.chaosblade.box.common.common.domain.Response<Boolean> installChaosTools(ChaosToolsRequest chaosToolsRequest) {

        if("host".equals(chaosToolsRequest.getInstallMode())) {
            return com.alibaba.chaosblade.box.common.common.domain.Response.failedWith(CommonErrorCode.B_CHAOS_TOOLS_INSTALL_NOT_SUPPORT_HOST);
        }
        UserDo userDo = licenseRepository.getUserLicense(chaosToolsRequest.getUserId()).orElseThrow(
                () -> new ChaosException(CommonErrorCode.P_ARGUMENT_ILLEGAL, "无效的集群"));

        IPage<DeviceDO> devices = deviceRepository.getDevicesByClusterId(chaosToolsRequest.getOperateId(), 1, 1);
        if (CollUtil.isEmpty(devices.getRecords())) {
            throw new ChaosException(CommonErrorCode.P_ARGUMENT_ILLEGAL, "无效的集群");
        }
        String agentVersion = devices.getRecords().get(0).getVersion();
        ChaosAgentVersion chaosAgentVersion = versionCache.get(agentVersion);
        if (chaosAgentVersion == null) {
            chaosAgentVersion = new ChaosAgentVersion(agentVersion);
            versionCache.put(agentVersion, chaosAgentVersion);
        }

        DeviceDO deviceDO = devices.getRecords().get(0);
        String proxyTag = ProxyHelper.buildProxyTag(PluginTypeUtil.getPluginTypeByDeviceType(deviceDO.getDeviceType()), deviceDO.getPrivateIp(), null);

        Response<String> response = litmusChaosForCloud.installLitmusOperatorForCloud(
                deviceDO.getPrivateIp(), String.valueOf(deviceDO.getDeviceType()), userDo.getLicense(), userDo.getSecretKey(),
                deviceDO.getVpcId(), proxyTag, chaosToolsRequest.getVersion()
        );

        if (!response.isSuccess()) {
            throw new ChaosException(CommonErrorCode.B_CHAOS_TOOLS_INSTALL_ERROR, response.getError());
        } else {
            chaosToolsRepository.add(ChaosToolsDO.builder()
                    .clusterId(chaosToolsRequest.getOperateId())
                    .deviceType(chaosToolsRequest.getDeviceType())
                    .name(chaosToolsRequest.getName())
                    .url(chaosToolsRequest.getUrl())
                    .version(chaosToolsRequest.getVersion())
                    .build());
        }
        return com.alibaba.chaosblade.box.common.common.domain.Response.okWithData(true);
    }

    @Override
    public com.alibaba.chaosblade.box.common.common.domain.Response<Boolean> uninstallChaosTools(ChaosToolsRequest chaosToolsRequest) {

        UserDo userDo = licenseRepository.getUserLicense(chaosToolsRequest.getUserId()).orElseThrow(
                () -> new ChaosException(CommonErrorCode.P_ARGUMENT_ILLEGAL, "无效的集群"));

        IPage<DeviceDO> devices = deviceRepository.getDevicesByClusterId(chaosToolsRequest.getOperateId(), 1, 1);
        if (CollUtil.isEmpty(devices.getRecords())) {
            throw new ChaosException(CommonErrorCode.P_ARGUMENT_ILLEGAL, "无效的集群");
        }

        DeviceDO deviceDO = devices.getRecords().get(0);
        String proxyTag = ProxyHelper.buildProxyTag(PluginTypeUtil.getPluginTypeByDeviceType(deviceDO.getDeviceType()), deviceDO.getPrivateIp(), null);

        Response<String> response = litmusChaosForCloud.uninstallLitmusOperatorForCloud(
                deviceDO.getPrivateIp(), String.valueOf(deviceDO.getDeviceType()), userDo.getLicense(), userDo.getSecretKey(),
                deviceDO.getVpcId(), proxyTag);

        if (!response.isSuccess()) {
            throw new ChaosException(CommonErrorCode.B_CHAOS_TOOLS_UNINSTALL_ERROR, response.getError());
        } else {
            Optional<ChaosToolsDO> chaosToolsDO = chaosToolsRepository.selectByClusterId(chaosToolsRequest.getOperateId());
            chaosToolsDO.ifPresent(toolsDO -> chaosToolsMapper.deleteById(toolsDO.getId()));
        }
        return com.alibaba.chaosblade.box.common.common.domain.Response.okWithData(true);
    }

}
