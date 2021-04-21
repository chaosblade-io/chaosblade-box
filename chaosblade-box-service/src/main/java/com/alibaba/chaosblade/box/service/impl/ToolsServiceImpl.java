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

package com.alibaba.chaosblade.box.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.chaosblade.box.common.enums.ChaosTools;
import com.alibaba.chaosblade.box.common.enums.ChaosToolsStatus;
import com.alibaba.chaosblade.box.common.enums.DeviceType;
import com.alibaba.chaosblade.box.common.exception.BizException;
import com.alibaba.chaosblade.box.common.exception.ExceptionMessageEnum;
import com.alibaba.chaosblade.box.common.model.chaos.PluginSpecBean;
import com.alibaba.chaosblade.box.dao.QueryWrapperBuilder;
import com.alibaba.chaosblade.box.dao.mapper.ClusterMapper;
import com.alibaba.chaosblade.box.dao.mapper.DeviceMapper;
import com.alibaba.chaosblade.box.dao.model.ClusterDO;
import com.alibaba.chaosblade.box.dao.model.DeviceDO;
import com.alibaba.chaosblade.box.dao.model.ToolsDO;
import com.alibaba.chaosblade.box.dao.page.PageUtils;
import com.alibaba.chaosblade.box.dao.repository.ClusterRepository;
import com.alibaba.chaosblade.box.dao.repository.DeviceRepository;
import com.alibaba.chaosblade.box.dao.repository.ToolsRepository;
import com.alibaba.chaosblade.box.scenario.api.ScenarioRequest;
import com.alibaba.chaosblade.box.scenario.api.ScenarioYamlProvider;
import com.alibaba.chaosblade.box.scenario.api.model.ToolsOverview;
import com.alibaba.chaosblade.box.scenario.api.model.ToolsVersion;
import com.alibaba.chaosblade.box.service.ClusterService;
import com.alibaba.chaosblade.box.service.DeviceService;
import com.alibaba.chaosblade.box.service.ToolsService;
import com.alibaba.chaosblade.box.service.model.cluster.ClusterBO;
import com.alibaba.chaosblade.box.service.model.device.DeviceRequest;
import com.alibaba.chaosblade.box.service.model.device.DeviceResponse;
import com.alibaba.chaosblade.box.service.model.tools.ToolsRequest;
import com.alibaba.chaosblade.box.service.model.tools.ToolsResponse;
import com.alibaba.chaosblade.box.service.model.tools.ToolsStatisticsResponse;
import com.alibaba.chaosblade.box.toolsmgr.api.ChannelType;
import com.alibaba.chaosblade.box.toolsmgr.api.ChaosToolsMgrStrategyContext;
import com.alibaba.chaosblade.box.toolsmgr.api.Request;
import com.alibaba.chaosblade.box.toolsmgr.api.Response;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.introspector.Property;
import org.yaml.snakeyaml.introspector.PropertyUtils;
import org.yaml.snakeyaml.representer.Representer;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.alibaba.chaosblade.box.common.exception.ExceptionMessageEnum.*;

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
    private ScenarioYamlProvider scenarioYamlProvider;

    @Autowired
    private ClusterMapper clusterMapper;

    @Autowired
    private DeviceMapper deviceMapper;

    @Override
    public ToolsStatisticsResponse getChaostoolsDeployedStatistics(ToolsRequest toolsRequest) {
        return ToolsStatisticsResponse.builder()
                .host(toolsRepository.selectCountOfHost())
                .kubernetes(toolsRepository.selectCountOfKubernetes())
                .build();
    }

    @Override
    public DeviceResponse deployChaostoolsToHost(ToolsRequest toolsRequest) {
        DeviceDO deviceDO = deviceRepository.selectById(toolsRequest.getMachineId()).orElseThrow(() -> new BizException(DEVICE_NOT_FOUNT));
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

        DeviceDO deviceDO = deviceRepository.selectById(toolsRequest.getMachineId()).orElseThrow(() -> new BizException(DEVICE_NOT_FOUNT));

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
        DeviceDO deviceDO = deviceRepository.selectById(toolsRequest.getMachineId()).orElseThrow(() -> new BizException(DEVICE_NOT_FOUNT));

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
        String overview = scenarioYamlProvider.overview(ScenarioRequest.builder()
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

        String versionYaml = scenarioYamlProvider.versionYaml(ScenarioRequest.builder()
                .chaosTools(toolsName)
                .version(version)
                .build());

        return new Yaml(representer).loadAs(versionYaml, ToolsVersion.class);
    }

    @Override
    public PluginSpecBean toolsScene(String toolsName, String version, String sceneFileName) {
        Representer representer = new Representer();
        representer.getPropertyUtils().setSkipMissingProperties(true);

        String versionYaml = scenarioYamlProvider.specYaml(ScenarioRequest.builder()
                .chaosTools(toolsName)
                .version(version)
                .spec(sceneFileName)
                .build());
        return new Yaml(representer).loadAs(versionYaml, PluginSpecBean.class);
    }

    @Override
    public List<ToolsResponse> getChaostoolsPageable(ToolsRequest toolsRequest) {
        if (toolsRequest.getDeviceType() == null) {
            return Collections.emptyList();
        }

        List<ToolsDO> toolsDOS = toolsRepository.selectList(ToolsDO.builder()
                .name(toolsRequest.getName())
                .version(toolsRequest.getVersion())
                .deviceType(toolsRequest.getDeviceType())
                .build());

        Map<Long, ToolsDO> map = toolsDOS.stream().collect(Collectors.toMap(ToolsDO::getDeviceId, u -> u));

        if (DeviceType.HOST.getCode().equals(toolsRequest.getDeviceType())) {
            PageUtils.startPage(toolsRequest);
            QueryWrapper<DeviceDO> queryWrapper = QueryWrapperBuilder.build();
            if (StrUtil.isNotBlank(toolsRequest.getHostname())) {
                queryWrapper.lambda().like(DeviceDO::getHostname, toolsRequest.getHostname());
            }
            if (ChaosToolsStatus.INSTALLED.getCode().equals(toolsRequest.getStatus())) {
                if (CollUtil.isEmpty(toolsDOS)) {
                    return Collections.emptyList();
                }
                queryWrapper.lambda().in(DeviceDO::getId, map.keySet());
            }
            if (ChaosToolsStatus.UNINSTALLED.getCode().equals(toolsRequest.getStatus()) && CollUtil.isNotEmpty(toolsDOS)) {
                queryWrapper.lambda().notIn(DeviceDO::getId, map.keySet());
            }
            queryWrapper.lambda().eq(DeviceDO::getType, toolsRequest.getDeviceType());
            List<DeviceDO> deviceDOS = deviceMapper.selectList(queryWrapper);

            return deviceDOS.stream().map(deviceDO -> {
                ToolsResponse toolsResponse = ToolsResponse.builder().build();
                toolsResponse.setHostname(deviceDO.getHostname());
                toolsResponse.setIp(deviceDO.getIp());

                toolsResponse.setName(Optional.ofNullable(map.get(deviceDO.getId()))
                        .map(ToolsDO::getName).orElse("-"));
                toolsResponse.setVersion(Optional.ofNullable(map.get(deviceDO.getId()))
                        .map(ToolsDO::getVersion).orElse("-"));

                toolsResponse.setStatus(map.get(deviceDO.getId()) == null ?
                        ChaosToolsStatus.UNINSTALLED.getCode() : ChaosToolsStatus.INSTALLED.getCode());

                toolsResponse.setDeviceId(deviceDO.getId());
                return toolsResponse;
            }).collect(Collectors.toList());

        } else if (DeviceType.CLUSTER.getCode().equals(toolsRequest.getDeviceType())) {
            PageUtils.startPage(toolsRequest);
            QueryWrapper<ClusterDO> queryWrapper = QueryWrapperBuilder.build();
            if (StrUtil.isNotBlank(toolsRequest.getClusterName())) {
                queryWrapper.lambda().like(ClusterDO::getClusterName, toolsRequest.getClusterName());
            }
            if (ChaosToolsStatus.INSTALLED.getCode().equals(toolsRequest.getStatus())) {
                if (CollUtil.isEmpty(toolsDOS)) {
                    return Collections.emptyList();
                }
                queryWrapper.lambda().in(ClusterDO::getId, map.keySet());
            }
            if (ChaosToolsStatus.UNINSTALLED.getCode().equals(toolsRequest.getStatus()) && CollUtil.isNotEmpty(toolsDOS)) {
                queryWrapper.lambda().notIn(ClusterDO::getId, map.keySet());
            }

            List<ClusterDO> clusters = clusterMapper.selectList(queryWrapper);

            return clusters.stream().map(cluster -> {
                ToolsResponse toolsResponse = ToolsResponse.builder().build();
                toolsResponse.setClusterName(cluster.getClusterName());
                toolsResponse.setName(Optional.ofNullable(map.get(cluster.getId()))
                        .map(ToolsDO::getName).orElse("-"));
                toolsResponse.setVersion(Optional.ofNullable(map.get(cluster.getId()))
                        .map(ToolsDO::getVersion).orElse("-"));

                toolsResponse.setStatus(map.get(cluster.getId()) == null ?
                        ChaosToolsStatus.UNINSTALLED.getCode() : ChaosToolsStatus.INSTALLED.getCode());
                toolsResponse.setDeviceId(cluster.getId());
                return toolsResponse;
            }).collect(Collectors.toList());
        } else {
            return Collections.emptyList();
        }
    }

}
