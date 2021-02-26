/*
 * Copyright 1999-2021 Alibaba Group Holding Ltd.
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

package com.alibaba.chaosblade.platform.service.impl;

import com.alibaba.chaosblade.platform.cmmon.exception.BizException;
import com.alibaba.chaosblade.platform.cmmon.exception.ExceptionMessageEnum;
import com.alibaba.chaosblade.platform.cmmon.model.chaos.PluginSpecBean;
import com.alibaba.chaosblade.platform.dao.model.DeviceDO;
import com.alibaba.chaosblade.platform.dao.model.ToolsDO;
import com.alibaba.chaosblade.platform.dao.repository.DeviceRepository;
import com.alibaba.chaosblade.platform.dao.repository.ToolsRepository;
import com.alibaba.chaosblade.platform.scenario.api.ScenarioRequest;
import com.alibaba.chaosblade.platform.scenario.api.ScenarioYamlProviderStrategy;
import com.alibaba.chaosblade.platform.scenario.api.model.ToolsOverview;
import com.alibaba.chaosblade.platform.scenario.api.model.ToolsVersion;
import com.alibaba.chaosblade.platform.service.DeviceService;
import com.alibaba.chaosblade.platform.service.ToolsService;
import com.alibaba.chaosblade.platform.service.model.device.DeviceRequest;
import com.alibaba.chaosblade.platform.service.model.device.DeviceResponse;
import com.alibaba.chaosblade.platform.service.model.tools.ToolsRequest;
import com.alibaba.chaosblade.platform.service.model.tools.ToolsStatisticsResponse;
import com.alibaba.chaosblade.platform.toolsmgr.api.ChannelType;
import com.alibaba.chaosblade.platform.toolsmgr.api.ChaosToolsMgrStrategyContext;
import com.alibaba.chaosblade.platform.toolsmgr.api.Request;
import com.alibaba.chaosblade.platform.toolsmgr.api.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.introspector.Property;
import org.yaml.snakeyaml.introspector.PropertyUtils;
import org.yaml.snakeyaml.representer.Representer;

import static com.alibaba.chaosblade.platform.cmmon.exception.ExceptionMessageEnum.*;

/**
 * @author yefei
 */
@Service
public class ToolsServiceImpl implements ToolsService {

    @Autowired
    private ToolsRepository toolsRepository;

    @Autowired
    private DeviceRepository deviceRepository;

    @Autowired
    private DeviceService deviceService;

    @Autowired
    private ChaosToolsMgrStrategyContext chaosToolsMgrStrategyContext;

    @Autowired
    private ScenarioYamlProviderStrategy scenarioYamlProviderStrategy;

    @Override
    public ToolsStatisticsResponse getChaostoolsDeployedStatistics(ToolsRequest toolsRequest) {
        return ToolsStatisticsResponse.builder()
                .host(toolsRepository.selectCountOfHost())
                .kubernetes(toolsRepository.selectCountOfKubernetes())
                .build();
    }

    @Override
    public DeviceResponse deployChaostoolsToHost(ToolsRequest toolsRequest) {
        DeviceDO deviceDO = deviceRepository.selectById(toolsRequest.getMachineId()).orElseThrow(() -> new BizException("机器不存在"));
        ToolsDO toolsDO = toolsRepository.selectByNameAndVersion(
                toolsRequest.getMachineId(),
                toolsRequest.getName(),
                toolsRequest.getVersion());
        if (toolsDO != null) {
            throw new BizException(ExceptionMessageEnum.CHAOS_TOOLS_EXISTS, toolsRequest.getName() + ":" + toolsRequest.getVersion());
        }

        Response<String> deployTools = chaosToolsMgrStrategyContext.deployTools(Request.builder().host(deviceDO.getIp())
                .toolsName(toolsRequest.getName())
                .toolsVersion(toolsRequest.getVersion())
                .channel(ChannelType.ANSIBLE.name())
                .toolsUrl(toolsRequest.getUrl()).build());

        if (deployTools.isSuccess()) {
            toolsRepository.insert(ToolsDO.builder()
                    .deviceId(toolsRequest.getMachineId())
                    .deviceType(deviceDO.getType())
                    .name(toolsRequest.getName())
                    .version(toolsRequest.getVersion())
                    .url(toolsRequest.getUrl())
                    .build());
        } else {
            throw new BizException(CHAOS_TOOLS_INSTALL_FAIL, deployTools.getMessage());
        }
        return deviceService.getMachinesById(DeviceRequest.builder().deviceId(toolsRequest.getMachineId()).build());
    }

    @Override
    public DeviceResponse undeployChaostoolsForHost(ToolsRequest toolsRequest) {

        DeviceDO deviceDO = deviceRepository.selectById(toolsRequest.getMachineId()).orElseThrow(() -> new BizException("机器不存在"));

        Response<String> deployTools = chaosToolsMgrStrategyContext.unDeployTools(
                Request.builder().host(deviceDO.getIp())
                        .channel(ChannelType.ANSIBLE.name())
                        .toolsName(toolsRequest.getName()).build());

        if (deployTools.isSuccess()) {
            toolsRepository.deleteByMachineIdAndName(toolsRequest.getMachineId(), toolsRequest.getName());
        } else {
            throw new BizException(CHAOS_TOOLS_UNINSTALL_FAIL, deployTools.getMessage());
        }
        return deviceService.getMachinesById(DeviceRequest.builder().deviceId(toolsRequest.getMachineId()).build());
    }

    @Override
    public DeviceResponse upgradeChaostoolsToHost(ToolsRequest toolsRequest) {
        DeviceDO deviceDO = deviceRepository.selectById(toolsRequest.getMachineId()).orElseThrow(() -> new BizException("机器不存在"));

        Response<String> deployTools = chaosToolsMgrStrategyContext.deployTools(Request.builder().host(deviceDO.getIp())
                .toolsName(toolsRequest.getName())
                .toolsVersion(toolsRequest.getVersion())
                .channel(ChannelType.ANSIBLE.name())
                .toolsUrl(toolsRequest.getUrl()).build());

        if (deployTools.isSuccess()) {
            toolsRepository.deleteByMachineIdAndName(toolsRequest.getMachineId(), toolsRequest.getName());
            toolsRepository.insert(ToolsDO.builder()
                    .deviceId(toolsRequest.getMachineId())
                    .deviceType(deviceDO.getType())
                    .name(toolsRequest.getName())
                    .version(toolsRequest.getVersion())
                    .url(toolsRequest.getUrl())
                    .build());
        } else {
            throw new BizException(CHAOS_TOOLS_UPDATE_FAIL, deployTools.getMessage());
        }
        return deviceService.getMachinesById(DeviceRequest.builder().deviceId(toolsRequest.getMachineId()).build());
    }

    @Override
    public ToolsOverview toolsOverview(String toolsName) {
        Representer representer = new Representer();
        String overview = scenarioYamlProviderStrategy.overview(ScenarioRequest.builder()
                .chaosTools(toolsName)
                .build());
        representer.getPropertyUtils().setSkipMissingProperties(true);
        return new Yaml(representer).loadAs(overview, ToolsOverview.class);
    }

    @Override
    public ToolsVersion toolsVersion(String toolsName, String version) {

        Representer representer = new Representer();
        representer.setPropertyUtils(new PropertyUtils() {
            @Override
            public Property getProperty(Class<? extends Object> type, String name) {
                if (name.indexOf('-') > -1) {
                    name = name.replace('-', '_');
                }
                return super.getProperty(type, name);
            }
        });
        representer.getPropertyUtils().setSkipMissingProperties(true);

        String versionYaml = scenarioYamlProviderStrategy.versionYaml(ScenarioRequest.builder()
                .chaosTools(toolsName)
                .version(version)
                .build());

        return new Yaml(representer).loadAs(versionYaml, ToolsVersion.class);
    }

    @Override
    public PluginSpecBean toolsScene(String toolsName, String version, String sceneFileName) {
        Representer representer = new Representer();
        representer.getPropertyUtils().setSkipMissingProperties(true);

        String versionYaml = scenarioYamlProviderStrategy.specYaml(ScenarioRequest.builder()
                .chaosTools(toolsName)
                .version(version)
                .spec(sceneFileName)
                .build());
        return new Yaml(representer).loadAs(versionYaml, PluginSpecBean.class);
    }
}
