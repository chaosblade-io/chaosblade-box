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
import com.alibaba.chaosblade.box.service.ApplicationService;
import com.alibaba.chaosblade.box.dao.model.ApplicationDO;
import com.alibaba.chaosblade.box.dao.model.ApplicationDeviceDO;
import com.alibaba.chaosblade.box.dao.model.ApplicationGroupDO;
import com.alibaba.chaosblade.box.dao.model.DeviceDO;
import com.alibaba.chaosblade.box.dao.page.PageUtils;
import com.alibaba.chaosblade.box.dao.repository.ApplicationDeviceRepository;
import com.alibaba.chaosblade.box.dao.repository.ApplicationGroupRepository;
import com.alibaba.chaosblade.box.dao.repository.ApplicationRepository;
import com.alibaba.chaosblade.box.dao.repository.DeviceRepository;
import com.alibaba.chaosblade.box.service.model.application.*;
import com.alibaba.chaosblade.box.service.model.application.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author yefei
 */
@Slf4j
@Service
public class ApplicationServiceImpl implements ApplicationService {

    @Autowired
    private ApplicationRepository applicationRepository;

    @Autowired
    private ApplicationGroupRepository applicationGroupRepository;

    @Autowired
    private DeviceRepository deviceRepository;

    @Autowired
    private ApplicationDeviceRepository applicationDeviceRepository;

    @Override
    public void insertApplicationDevice(ApplicationDeviceRequest applicationDeviceRequest) {

    }

    @Override
    public List<ApplicationResponse> selectApplication(ApplicationRequest applicationRequest) {
        List<ApplicationDO> list = applicationRepository.selectList(ApplicationDO.builder()
                .appName(applicationRequest.getAppName())
                .build());
        if (CollectionUtil.isEmpty(list)) {
            return Collections.emptyList();
        } else {
            return list.stream().map(applicationDO -> ApplicationResponse.builder()
                    .appId(applicationDO.getId())
                    .groupId(applicationDO.getId())
                    .appName(applicationDO.getAppName())
                    .build())
                    .collect(Collectors.toList());
        }
    }

    @Override
    public List<ApplicationResponse> selectApplicationGroup(ApplicationRequest applicationRequest) {
        List<ApplicationGroupDO> list = applicationGroupRepository.selectList(ApplicationGroupDO.builder()
                .appId(applicationRequest.getAppId())
                .build());
        if (CollectionUtil.isEmpty(list)) {
            return Collections.emptyList();
        } else {
            return list.stream().map(groupDO -> ApplicationResponse.builder()
                    .appId(groupDO.getId())
                    .appName(groupDO.getAppName())
                    .groupName(groupDO.getGroupName())
                    .build())
                    .collect(Collectors.toList());
        }
    }

    @Override
    public List<ApplicationDeviceResponse> selectApplicationDevice(ApplicationRequest applicationRequest) {

        List<ApplicationDeviceDO> list = applicationDeviceRepository.selectList(ApplicationDeviceDO.builder()
                .groupId(applicationRequest.getGroupId())
                .build());
        if (CollectionUtil.isEmpty(list)) {
            return Collections.emptyList();
        } else {

            List<DeviceDO> deviceDOS =
                    deviceRepository.selectBatchIds(list.stream().map(ApplicationDeviceDO::getDeviceId).collect(Collectors.toList()));

            return deviceDOS.stream().map(deviceDO -> ApplicationDeviceResponse.builder()
                    .deviceIp(deviceDO.getIp())
                    .deviceName(deviceDO.getHostname())
                    .build())
                    .collect(Collectors.toList());
        }
    }

    @Override
    public ApplicationStatisticsResponse getApplicationTotalStatistics(boolean active) {

        return ApplicationStatisticsResponse.builder()
                .app(applicationRepository.selectCount())
                .appGroup(applicationGroupRepository.selectCount())
                .machine(deviceRepository.selectHostCount())
                .build();
    }

    @Override
    public List<ApplicationResponse> getMachinesForApplication(ApplicationRequest applicationRequest) {
        List<DeviceDO> deviceDOS = deviceRepository.selectMachines(DeviceDO.builder()
                .hostname(applicationRequest.getHostname())
                .ip(applicationRequest.getIp())
                .type(applicationRequest.getType())
                .build());
        Map<Long, DeviceDO> deviceDOMap = deviceDOS.stream().collect(Collectors.toMap(DeviceDO::getId, v -> v));

        PageUtils.startPage(applicationRequest);
        if (CollUtil.isEmpty(deviceDOMap)) {
            return Collections.emptyList();
        }

        List<ApplicationDeviceDO> applicationDeviceDOS = applicationDeviceRepository.selectByDeviceIds(deviceDOMap.keySet());
        if (CollectionUtil.isEmpty(applicationDeviceDOS)) {
            return Collections.emptyList();
        }
        return applicationDeviceDOS.stream().map(applicationDeviceDO -> {
            ApplicationResponse applicationResponse = new ApplicationResponse();
            applicationResponse.setAppName(applicationDeviceDO.getAppName())
                    .setGroupName(applicationDeviceDO.getGroupName())
                    .setHostname(deviceDOMap.get(applicationDeviceDO.getDeviceId()).getHostname())
                    .setDeviceId(deviceDOMap.get(applicationDeviceDO.getDeviceId()).getId())
                    .setIp(deviceDOMap.get(applicationDeviceDO.getDeviceId()).getIp())
                    .setHostname(deviceDOMap.get(applicationDeviceDO.getDeviceId()).getHostname())
                    .setStatus(deviceDOMap.get(applicationDeviceDO.getDeviceId()).getStatus())
                    .setChaosed(deviceDOMap.get(applicationDeviceDO.getDeviceId()).getIsExperimented())
                    .setCreateTime(deviceDOMap.get(applicationDeviceDO.getDeviceId()).getGmtCreate())
                    .setHeartbeatTime(deviceDOMap.get(applicationDeviceDO.getDeviceId()).getLastOnlineTime())
                    .setChaosTime(deviceDOMap.get(applicationDeviceDO.getDeviceId()).getLastExperimentTime())
                    .setTaskId(deviceDOMap.get(applicationDeviceDO.getDeviceId()).getLastTaskId())
                    .setTaskStatus(deviceDOMap.get(applicationDeviceDO.getDeviceId()).getLastTaskStatus());
            return applicationResponse;
        }).collect(Collectors.toList());
    }
}
