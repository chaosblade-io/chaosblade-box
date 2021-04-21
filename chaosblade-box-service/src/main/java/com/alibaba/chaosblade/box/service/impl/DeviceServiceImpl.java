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
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DateField;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.EnumUtil;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.chaosblade.box.common.enums.AgentType;
import com.alibaba.chaosblade.box.common.enums.DeviceStatus;
import com.alibaba.chaosblade.box.common.enums.DeviceType;
import com.alibaba.chaosblade.box.common.enums.ProbesInstallModel;
import com.alibaba.chaosblade.box.common.exception.BizException;
import com.alibaba.chaosblade.box.common.exception.ExceptionMessageEnum;
import com.alibaba.chaosblade.box.common.utils.JsonUtils;
import com.alibaba.chaosblade.box.dao.QueryWrapperBuilder;
import com.alibaba.chaosblade.box.dao.mapper.DevicePodMapper;
import com.alibaba.chaosblade.box.dao.model.*;
import com.alibaba.chaosblade.box.dao.page.PageUtils;
import com.alibaba.chaosblade.box.dao.repository.*;
import com.alibaba.chaosblade.box.service.DeviceService;
import com.alibaba.chaosblade.box.service.model.device.*;
import com.alibaba.chaosblade.box.service.model.tools.ToolsResponse;
import com.alibaba.chaosblade.box.service.probes.ProbesInstallSuccessEvent;
import com.alibaba.chaosblade.box.service.probes.heartbeats.Heartbeats;
import com.fasterxml.jackson.core.type.TypeReference;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.alibaba.chaosblade.box.common.exception.ExceptionMessageEnum.DEVICE_NOT_FOUNT;

/**
 * @author yefei
 */
@Slf4j
@Service
public class DeviceServiceImpl implements DeviceService {

    @Autowired
    private DeviceRepository deviceRepository;

    @Autowired
    private DeviceNodeRepository deviceNodeRepository;

    @Autowired
    private DevicePodRepository devicePodRepository;

    @Autowired
    private ApplicationRepository applicationRepository;

    @Autowired
    private ApplicationGroupRepository applicationGroupRepository;

    @Autowired
    private ApplicationDeviceRepository applicationDeviceRepository;

    @Autowired
    private ProbesRepository probesRepository;

    @Autowired
    private ToolsRepository toolsRepository;

    @Autowired
    private DevicePodMapper devicePodMapper;

    @Autowired
    private Heartbeats heartbeats;

    @Autowired
    private ApplicationContext applicationContext;

    @Override
    @Transactional
    public void deviceRegister(DeviceRegisterRequest deviceRegisterRequest) {

        ProbesInstallModel installModel = EnumUtil.fromString(ProbesInstallModel.class, deviceRegisterRequest.getInstallMode().toUpperCase());
        switch (installModel) {
            case ANSIBLE:
            case SSH:
                Long appId = applicationRepository.selectOneByNamespaceAndAppName(
                        deviceRegisterRequest.getNamespace(),
                        deviceRegisterRequest.getAppInstance())

                        .map(ApplicationDO::getId)
                        .orElseGet(() -> {
                            ApplicationDO applicationDO = ApplicationDO.builder()
                                    .namespace(deviceRegisterRequest.getNamespace())
                                    .appName(deviceRegisterRequest.getAppInstance())
                                    .build();
                            applicationRepository.insert(applicationDO);
                            return applicationDO.getId();
                        });

                Long groupId = applicationGroupRepository.selectOneByAppIdAndGroupName(appId, deviceRegisterRequest.getAppGroup())
                        .map(ApplicationGroupDO::getId)
                        .orElseGet(() -> {
                            ApplicationGroupDO applicationGroupDO = ApplicationGroupDO.builder()
                                    .appId(appId)
                                    .appName(deviceRegisterRequest.getAppInstance())
                                    .groupName(deviceRegisterRequest.getAppGroup())
                                    .build();

                            applicationGroupRepository.insert(applicationGroupDO);
                            return applicationGroupDO.getId();
                        });

                DeviceDO deviceDO = DeviceDO.builder()
                        .ip(deviceRegisterRequest.getIp())
                        .hostname(deviceRegisterRequest.getHostName())
                        .version(deviceRegisterRequest.getVersion())
                        .cpuCore(deviceRegisterRequest.getCpuCore())
                        .status(DeviceStatus.ONLINE.getStatus())
                        .memorySize(deviceRegisterRequest.getMemorySize() == null ? null : deviceRegisterRequest.getMemorySize().intValue())
                        .lastOnlineTime(DateUtil.date())
                        .lastPingTime(DateUtil.date())
                        .connectTime(DateUtil.date())
                        .uptime(deviceRegisterRequest.getUptime())
                        .installMode(deviceRegisterRequest.getInstallMode())
                        .type(DeviceType.HOST.getCode())
                        .build();

                Long deviceId = deviceRepository.selectOneByUnique(DeviceType.HOST.getCode(), deviceRegisterRequest.getHostName())
                        .map(device -> {
                            deviceRepository.updateByPrimaryKey(device.getId(), deviceDO);
                            return device.getId();
                        }).orElseGet(() -> {
                            deviceRepository.insert(deviceDO);
                            applicationDeviceRepository.insert(ApplicationDeviceDO.builder()
                                    .namespace(deviceRegisterRequest.getNamespace())
                                    .appId(appId)
                                    .appName(deviceRegisterRequest.getAppInstance())
                                    .groupId(groupId)
                                    .groupName(deviceRegisterRequest.getAppGroup())
                                    .deviceId(deviceDO.getId())
                                    .build());
                            return deviceDO.getId();
                        });

                if (NumberUtil.isNumber(deviceRegisterRequest.getAgentId())){
                    long id = Long.parseLong(deviceRegisterRequest.getAgentId());
                    probesRepository.updateByPrimaryKey(id,
                            ProbesDO.builder()
                                    .deviceId(deviceId)
                                    .hostname(deviceRegisterRequest.getHostName())
                                    .status(DeviceStatus.ONLINE.getStatus())
                                    .version(deviceRegisterRequest.getAgentVersion())
                                    .build());
                } else {
                    ProbesDO probesDO = probesRepository.selectByDeviceId(deviceId).orElseGet(() -> {
                        ProbesDO probes = ProbesDO.builder()
                                .ip(deviceRegisterRequest.getIp())
                                .deviceId(deviceId)
                                .agentType(AgentType.HOST.getCode())
                                .status(DeviceStatus.ONLINE.getStatus())
                                .hostname(deviceRegisterRequest.getHostName())
                                .version(deviceRegisterRequest.getAgentVersion())
                                .lastOnlineTime(DateUtil.date())
                                .lastPingTime(DateUtil.date())
                                .installMode((byte) installModel.getCode())
                                .ip(deviceRegisterRequest.getIp())
                                .build();
                        probesRepository.insert(probes);
                        return probes;
                    });
                    heartbeats.addHeartbeats(probesDO);
                }
                applicationContext.publishEvent(new ProbesInstallSuccessEvent(deviceId));
                break;
            case K8S:
            case K8S_HELM:
                ProbesDO probesDO = probesRepository.selectByHost(deviceRegisterRequest.getIp())
                        .orElseGet(() -> {
                            ProbesDO probes = ProbesDO.builder()
                                    .ip(deviceRegisterRequest.getIp())
                                    .agentType(AgentType.KUBERNETES.getCode())
                                    .status(DeviceStatus.ONLINE.getStatus())
                                    .hostname(deviceRegisterRequest.getHostName())
                                    .version(deviceRegisterRequest.getAgentVersion())
                                    .installMode((byte) installModel.getCode())
                                    .ip(deviceRegisterRequest.getIp())
                                    .build();
                            probesRepository.insert(probes);
                            return probes;
                        });

                heartbeats.addHeartbeats(probesDO);
        }
    }

    @Override
    public List<DeviceResponse> getMachinesForHost(DeviceRequest deviceRequest) {
        if (deviceRequest.getProbeId() != null) {
            PageUtils.startPage(deviceRequest);
            ProbesDO probesDO = probesRepository.selectById(deviceRequest.getProbeId())
                    .orElseThrow(() -> new BizException(ExceptionMessageEnum.PROBES_NO_FOUND));
            return deviceRepository.selectById(probesDO.getDeviceId())
                    .map(deviceDO -> CollUtil.newArrayList(covert(deviceDO)))
                    .orElse(new ArrayList<>());
        }

        PageUtils.startPage(deviceRequest);

        List<DeviceDO> devices = deviceRepository.selectMachines(DeviceDO.builder()
                .ip(deviceRequest.getIp())
                .hostname(deviceRequest.getHostname())
                .isExperimented(deviceRequest.getChaosed())
                .status(deviceRequest.getStatus())
                .type(DeviceType.HOST.getCode())
                .build());

        if (CollectionUtil.isEmpty(devices)) {
            return Collections.emptyList();
        }

        return devices.stream().map(deviceDO -> covert(deviceDO)).collect(Collectors.toList());
    }

    public DeviceResponse covert(DeviceDO deviceDO) {
        DeviceResponse deviceResponse = new DeviceResponse();
        deviceResponse.setVersion(deviceDO.getVersion())
                .setDeviceId(deviceDO.getId())
                .setIp(deviceDO.getIp())
                .setHostname(deviceDO.getHostname())
                .setStatus(deviceDO.getStatus())
                .setChaosed(deviceDO.getIsExperimented())
                .setCreateTime(deviceDO.getGmtCreate())
                .setHeartbeatTime(deviceDO.getLastOnlineTime())
                .setChaosTime(deviceDO.getLastExperimentTime())
                .setTaskId(deviceDO.getLastTaskId())
                .setChaostools(toolsRepository.selectByDeviceId(deviceDO.getId()).stream().map(toolsDO ->
                        ToolsResponse.builder()
                                .name(toolsDO.getName())
                                .version(toolsDO.getVersion())
                                .build()
                ).collect(Collectors.toList()))
                .setTaskStatus(deviceDO.getLastTaskStatus());

        return deviceResponse;
    }

    @Override
    public KubernetesStatisticsResponse getKubernetesTotalStatistics() {
        return KubernetesStatisticsResponse.builder()
                .nodes(deviceRepository.selectHostCount(DeviceDO.builder()
                        .type(DeviceType.NODE.getCode())
                        .build()))
                .pods(deviceRepository.selectHostCount(DeviceDO.builder()
                        .type(DeviceType.POD.getCode())
                        .build()))
                .namespaces(devicePodMapper.selectList(QueryWrapperBuilder.<DevicePodDO>build()
                        .groupBy("namespace")).size())
                .containers(devicePodRepository.selectList(DevicePodDO.builder().build()).stream().flatMap(devicePodDO ->
                        Optional.ofNullable(devicePodDO.getContainers()).map(
                                containers -> {
                                    try {
                                        return Stream.of(JsonUtils.reader(ContainerBO[].class).readValue(containers));
                                    } catch (IOException e) {
                                        return Stream.empty();
                                    }
                                }
                        ).orElse(Stream.empty())
                ).count())
                .build();
    }

    @Override
    public List<DeviceNodeResponse> getMachinesForNode(DeviceNodeRequest deviceNodeRequest) {

        List<Long> deviceIds = deviceRepository.selectMachines(DeviceDO.builder().status(deviceNodeRequest.getStatus())
                    .type(DeviceType.NODE.getCode())
                    .build()).stream().map(DeviceDO::getId).collect(Collectors.toList());

        PageUtils.startPage(deviceNodeRequest);

        if (CollUtil.isEmpty(deviceIds)) {
            return Collections.emptyList();
        }

        List<DeviceNodeDO> deviceNodeDOS = deviceNodeRepository.selectList(DeviceNodeDO.builder()
                .clusterName(deviceNodeRequest.getClusterName())
                .nodeName(deviceNodeRequest.getNode())
                .build(), deviceIds);

        if (CollUtil.isEmpty(deviceNodeDOS)) {
            return Collections.emptyList();
        }

        Map<Long, DeviceDO> deviceDOMap = deviceRepository.selectBatchIds(deviceNodeDOS.stream().map(DeviceNodeDO::getDeviceId)
                .collect(Collectors.toList()))
                .stream().collect(Collectors.toMap(DeviceDO::getId, u -> u));

        return deviceNodeDOS.stream().map(deviceNodeDO ->
                {
                    DeviceNodeResponse deviceNodeResponse = new DeviceNodeResponse();

                    deviceNodeResponse.setClusterName(deviceNodeDO.getClusterName())
                            .setNodeName(deviceNodeDO.getNodeName())
                            .setNodeIp(deviceNodeDO.getNodeIp())
                            .setNodeVersion(deviceNodeDO.getNodeVersion())
                            .setDeviceId(deviceNodeDO.getDeviceId())
                            .setStatus(deviceDOMap.get(deviceNodeDO.getDeviceId()).getStatus())
                            .setChaosed(deviceDOMap.get(deviceNodeDO.getDeviceId()).getIsExperimented())
                            .setCreateTime(deviceDOMap.get(deviceNodeDO.getDeviceId()).getGmtCreate())
                            .setHeartbeatTime(deviceDOMap.get(deviceNodeDO.getDeviceId()).getLastOnlineTime())
                            .setChaosTime(deviceDOMap.get(deviceNodeDO.getDeviceId()).getLastExperimentTime())
                            .setTaskId(deviceDOMap.get(deviceNodeDO.getDeviceId()).getLastTaskId())
                            .setTaskStatus(deviceDOMap.get(deviceNodeDO.getDeviceId()).getLastTaskStatus());

                    return deviceNodeResponse;
                }
        ).collect(Collectors.toList());
    }

    @Override
    public List<DevicePodResponse> getMachinesForPod(DevicePodRequest devicePodRequest) {

        List<Long> deviceIds = deviceRepository.selectMachines(DeviceDO.builder().status(devicePodRequest.getStatus())
                .type(DeviceType.POD.getCode())
                .build()).stream().map(DeviceDO::getId).collect(Collectors.toList());

        List<DeviceNodeDO> deviceNodeDOS = deviceNodeRepository.selectList(DeviceNodeDO.builder()
                .clusterName(devicePodRequest.getClusterName())
                .nodeName(devicePodRequest.getNode())
                .build()
        );

        PageUtils.startPage(devicePodRequest);

        if (CollUtil.isEmpty(deviceIds) || CollUtil.isEmpty(deviceNodeDOS)) {
            return Collections.emptyList();
        }

        Map<Long, DeviceNodeDO> nodeMap = deviceNodeDOS.stream().collect(Collectors.toMap(DeviceNodeDO::getId, v -> v));
        List<DevicePodDO> devicePodDOS = devicePodRepository.selectList(DevicePodDO.builder()
                .namespace(devicePodRequest.getNamespace())
                .podName(devicePodRequest.getPod())
                .podIp(devicePodRequest.getIp())
                .build(), deviceIds, deviceNodeDOS.stream().map(DeviceNodeDO::getId).collect(Collectors.toList())
        );
        if (CollUtil.isEmpty(devicePodDOS)) {
            return Collections.emptyList();
        }

        Map<Long, DeviceDO> deviceDOMap = deviceRepository.selectBatchIds(devicePodDOS.stream().map(DevicePodDO::getDeviceId)
                .collect(Collectors.toList()))
                .stream().collect(Collectors.toMap(DeviceDO::getId, u -> u));

        return devicePodDOS.stream().map(devicePodDO ->
                {
                    DevicePodResponse devicePodResponse = new DevicePodResponse();

                    List<ContainerBO> containers;
                    if (StrUtil.isBlank(devicePodDO.getContainers())) {
                        containers = Collections.emptyList();
                    } else {
                        containers = JsonUtils.readValue(new TypeReference<List<ContainerBO>>() {
                        }, devicePodDO.getContainers());
                    }

                    devicePodResponse.setClusterName(nodeMap.get(devicePodDO.getNodeId()).getClusterName())
                            .setNodeName(nodeMap.get(devicePodDO.getNodeId()).getNodeName())
                            .setNodeIp(nodeMap.get(devicePodDO.getNodeId()).getNodeIp())
                            .setNodeVersion(nodeMap.get(devicePodDO.getNodeId()).getNodeVersion())
                            .setNamespace(devicePodDO.getNamespace())
                            .setContainers(containers)
                            .setPodName(devicePodDO.getPodName())
                            .setPodIp(devicePodDO.getPodIp())
                            .setDeviceId(devicePodDO.getDeviceId())
                            .setStatus(deviceDOMap.get(devicePodDO.getDeviceId()).getStatus())
                            .setChaosed(deviceDOMap.get(devicePodDO.getDeviceId()).getIsExperimented())
                            .setCreateTime(deviceDOMap.get(devicePodDO.getDeviceId()).getGmtCreate())
                            .setHeartbeatTime(deviceDOMap.get(devicePodDO.getDeviceId()).getLastOnlineTime())
                            .setChaosTime(deviceDOMap.get(devicePodDO.getDeviceId()).getLastExperimentTime())
                            .setTaskId(deviceDOMap.get(devicePodDO.getDeviceId()).getLastTaskId())
                            .setTaskStatus(deviceDOMap.get(devicePodDO.getDeviceId()).getLastTaskStatus());

                    return devicePodResponse;
                }
        ).collect(Collectors.toList());
    }

    @Override
    public DeviceResponse banMachine(DeviceRequest deviceRequest) {

        deviceRepository.updateByPrimaryKey(deviceRequest.getDeviceId(), DeviceDO.builder()
                .status(DeviceStatus.FORBIDDEN.getStatus())
                .build());

        return getMachinesById(deviceRequest);
    }

    @Override
    public DeviceResponse unbanMachine(DeviceRequest deviceRequest) {
        DeviceDO deviceDO = deviceRepository.selectById(deviceRequest.getDeviceId())
                .orElseThrow(() -> new BizException(DEVICE_NOT_FOUNT));

        if (DateUtil.date().offset(DateField.MINUTE, -1).after(deviceDO.getLastOnlineTime())) {
            deviceRepository.updateByPrimaryKey(deviceRequest.getDeviceId(), DeviceDO.builder()
                    .status(DeviceStatus.OFFLINE.getStatus())
                    .build());
        } else {
            deviceRepository.updateByPrimaryKey(deviceRequest.getDeviceId(), DeviceDO.builder()
                    .status(DeviceStatus.ONLINE.getStatus())
                    .build());
        }

        return getMachinesById(deviceRequest);
    }


    @Override
    public DeviceResponse getMachinesById(DeviceRequest deviceRequest) {
        DeviceDO deviceDO = deviceRepository.selectById(deviceRequest.getDeviceId()).orElseThrow(
                () -> new BizException(DEVICE_NOT_FOUNT)
        );

        List<ToolsDO> toolsDOS = toolsRepository.selectByDeviceId(deviceRequest.getDeviceId());
        List<ToolsResponse> tools = toolsDOS.stream().map(toolsDO ->
                ToolsResponse.builder()
                        .name(toolsDO.getName())
                        .version(toolsDO.getVersion())
                        .build()
        ).collect(Collectors.toList());

        DeviceResponse deviceResponse = new DeviceResponse();
        deviceResponse.setVersion(deviceDO.getVersion())
                .setDeviceId(deviceDO.getId())
                .setIp(deviceDO.getIp())
                .setHostname(deviceDO.getHostname())
                .setStatus(deviceDO.getStatus())
                .setChaosed(deviceDO.getIsExperimented())
                .setCreateTime(deviceDO.getGmtCreate())
                .setHeartbeatTime(deviceDO.getLastOnlineTime())
                .setChaosTime(deviceDO.getLastExperimentTime())
                .setTaskId(deviceDO.getLastTaskId())
                .setTaskStatus(deviceDO.getLastTaskStatus())
                .setOriginal(deviceRequest.getOriginal())
                .setChaostools(tools);
        return deviceResponse;
    }

    @Override
    public DeviceNodeResponse getNodeByDeviceId(Long id) {
        DeviceNodeDO deviceNodeDO = deviceNodeRepository.selectByDeviceId(id).orElseThrow(
                () -> new BizException(DEVICE_NOT_FOUNT)
        );

        DeviceDO deviceDO = deviceRepository.selectById(deviceNodeDO.getDeviceId()).orElseThrow(
                () -> new BizException(DEVICE_NOT_FOUNT)
        );
        DeviceNodeResponse deviceNodeResponse = new DeviceNodeResponse();

        deviceNodeResponse.setClusterName(deviceNodeDO.getClusterName())
                .setClusterId(deviceNodeDO.getClusterId())
                .setNodeName(deviceNodeDO.getNodeName())
                .setNodeIp(deviceNodeDO.getNodeIp())
                .setNodeVersion(deviceNodeDO.getNodeVersion())
                .setDeviceId(deviceNodeDO.getDeviceId())
                .setStatus(deviceDO.getStatus())
                .setChaosed(deviceDO.getIsExperimented())
                .setCreateTime(deviceDO.getGmtCreate())
                .setHeartbeatTime(deviceDO.getLastOnlineTime())
                .setChaosTime(deviceDO.getLastExperimentTime())
                .setTaskId(deviceDO.getLastTaskId())
                .setType(deviceDO.getType())
                .setTaskStatus(deviceDO.getLastTaskStatus());

        return deviceNodeResponse;
    }

    @Override
    public DevicePodResponse getPodByDeviceId(Long id) {

        DevicePodDO devicePodDO = devicePodRepository.selectByDeviceId(id).orElseThrow(
                () -> new BizException(DEVICE_NOT_FOUNT)
        );

        DeviceNodeDO deviceNodeDO = deviceNodeRepository.selectById(devicePodDO.getDeviceId()).orElseThrow(
                () -> new BizException(DEVICE_NOT_FOUNT)
        );

        DeviceDO deviceDO = deviceRepository.selectById(deviceNodeDO.getDeviceId()).orElseThrow(
                () -> new BizException(DEVICE_NOT_FOUNT)
        );
        DevicePodResponse devicePodResponse = new DevicePodResponse();

        List<ContainerBO> containers;
        if (StrUtil.isBlank(devicePodDO.getContainers())) {
            containers = Collections.emptyList();
        } else {
            containers = JsonUtils.readValue(new TypeReference<List<ContainerBO>>() {
            }, devicePodDO.getContainers());
        }

        devicePodResponse.setClusterName(deviceNodeDO.getClusterName())
                .setClusterId(deviceNodeDO.getClusterId())
                .setNodeName(deviceNodeDO.getNodeName())
                .setNodeIp(deviceNodeDO.getNodeIp())
                .setNodeVersion(deviceNodeDO.getNodeVersion())
                .setNamespace(devicePodDO.getNamespace())
                .setContainers(containers)
                .setPodName(devicePodDO.getPodName())
                .setPodIp(devicePodDO.getPodIp())
                .setDeviceId(devicePodDO.getDeviceId())
                .setStatus(deviceDO.getStatus())
                .setChaosed(deviceDO.getIsExperimented())
                .setCreateTime(deviceDO.getGmtCreate())
                .setHeartbeatTime(deviceDO.getLastOnlineTime())
                .setChaosTime(deviceDO.getLastExperimentTime())
                .setTaskId(deviceDO.getLastTaskId())
                .setTaskStatus(deviceDO.getLastTaskStatus());

        return devicePodResponse;
    }

    @Override
    public HostStatisticsResponse getHostTotalStatistics() {
        return HostStatisticsResponse.builder()
                .totals(deviceRepository.selectHostCount())
                .onlines(deviceRepository.selectHostOnlineCount())
                .build();
    }
}
