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

import com.alibaba.chaosblade.platform.cmmon.ansible.AnsibleResponse;
import com.alibaba.chaosblade.platform.cmmon.ansible.AnsibleUtil;
import com.alibaba.chaosblade.platform.cmmon.exception.BizException;
import com.alibaba.chaosblade.platform.cmmon.exception.ExceptionMessageEnum;
import com.alibaba.chaosblade.platform.dao.model.DeviceDO;
import com.alibaba.chaosblade.platform.dao.model.ToolsDO;
import com.alibaba.chaosblade.platform.dao.repository.DeviceRepository;
import com.alibaba.chaosblade.platform.dao.repository.ToolsRepository;
import com.alibaba.chaosblade.platform.service.DeviceService;
import com.alibaba.chaosblade.platform.service.ToolsService;
import com.alibaba.chaosblade.platform.service.model.device.DeviceRequest;
import com.alibaba.chaosblade.platform.service.model.device.DeviceResponse;
import com.alibaba.chaosblade.platform.service.model.tools.ToolsRequest;
import com.alibaba.chaosblade.platform.service.model.tools.ToolsStatisticsResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

        AnsibleResponse response = AnsibleUtil.deployTools(deviceDO.getIp(),
                toolsRequest.getName(),
                toolsRequest.getVersion(),
                toolsRequest.getUrl());

        if (response.getChanged()) {
            toolsRepository.insert(ToolsDO.builder()
                    .deviceId(toolsRequest.getMachineId())
                    .deviceType(deviceDO.getType())
                    .name(toolsRequest.getName())
                    .version(toolsRequest.getVersion())
                    .url(toolsRequest.getUrl())
                    .build());
        } else {
            throw new BizException(CHAOS_TOOLS_INSTALL_FAIL, response.getMsg());
        }
        return deviceService.getMachinesById(DeviceRequest.builder().deviceId(toolsRequest.getMachineId()).build());
    }

    @Override
    public DeviceResponse undeployChaostoolsForHost(ToolsRequest toolsRequest) {

        DeviceDO deviceDO = deviceRepository.selectById(toolsRequest.getMachineId()).orElseThrow(() -> new BizException("机器不存在"));
        AnsibleResponse response = AnsibleUtil.unDeployTools(deviceDO.getIp(), toolsRequest.getName());
        if (response.getChanged()) {
            toolsRepository.deleteByMachineIdAndName(toolsRequest.getMachineId(), toolsRequest.getName());
        } else {
            throw new BizException(CHAOS_TOOLS_UNINSTALL_FAIL, response.getMsg());
        }
        return deviceService.getMachinesById(DeviceRequest.builder().deviceId(toolsRequest.getMachineId()).build());
    }

    @Override
    public DeviceResponse upgradeChaostoolsToHost(ToolsRequest toolsRequest) {
        DeviceDO deviceDO = deviceRepository.selectById(toolsRequest.getMachineId()).orElseThrow(() -> new BizException("机器不存在"));
        AnsibleResponse response = AnsibleUtil.deployTools(deviceDO.getIp(),
                toolsRequest.getName(),
                toolsRequest.getVersion(),
                toolsRequest.getUrl());
        if (response.getChanged()) {
            toolsRepository.deleteByMachineIdAndName(toolsRequest.getMachineId(), toolsRequest.getName());
            toolsRepository.insert(ToolsDO.builder()
                    .deviceId(toolsRequest.getMachineId())
                    .deviceType(deviceDO.getType())
                    .name(toolsRequest.getName())
                    .version(toolsRequest.getVersion())
                    .url(toolsRequest.getUrl())
                    .build());
        } else {
            throw new BizException(CHAOS_TOOLS_UPDATE_FAIL, response.getMsg());
        }
        return deviceService.getMachinesById(DeviceRequest.builder().deviceId(toolsRequest.getMachineId()).build());
    }
}
