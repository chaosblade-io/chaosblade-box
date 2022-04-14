package com.alibaba.chaosblade.box.service.infrastructure.convertor;

import com.alibaba.chaosblade.box.common.common.enums.DeviceType;
import com.alibaba.chaosblade.box.dao.model.ApplicationDeviceDO;
import com.alibaba.chaosblade.box.dao.model.ApplicationDeviceTagDO;
import com.alibaba.chaosblade.box.dao.model.DeviceDO;
import com.alibaba.chaosblade.box.dao.repository.ApplicationDeviceTagRepository;
import com.alibaba.chaosblade.box.dao.repository.DeviceRepository;
import com.alibaba.chaosblade.box.service.model.experiment.ExperimentScope;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

/**
 * @author haibin
 *
 *
 */
@Slf4j
@Component
public class ApplicationDeviceToExperimentScopeConvertor
    extends BaseApplicationConvertor<ApplicationDeviceDO, ExperimentScope> {
    @Autowired
    private DeviceRepository deviceRepository;

    @Autowired
    private ApplicationDeviceTagRepository applicationDeviceTagRepository;

    @Override
    public ExperimentScope convert(ApplicationDeviceDO applicationDeviceDO) {
        ExperimentScope experimentScope = new ExperimentScope();
        experimentScope.setDeviceName(applicationDeviceDO.getDeviceName());
        experimentScope.setConfigurationId(applicationDeviceDO.getConfigurationId());
        experimentScope.setPrivateIp(applicationDeviceDO.getPrivateIp());
        experimentScope.setGroups(Lists.newArrayList(applicationDeviceDO.getGroupName()));
        experimentScope.setHostConfigurationId(applicationDeviceDO.getHostConfigurationId());
        experimentScope.setDeviceType(applicationDeviceDO.getDeviceType());
        DeviceDO deviceDO = deviceRepository.findByConfigurationId(applicationDeviceDO.getHostConfigurationId());
        if (null == deviceDO) {
            log.info("[ApplicationDeviceToExperimentScopeConverter] error device HostConfigurationId: {},ip:{}",
                applicationDeviceDO.getHostConfigurationId(), applicationDeviceDO.getPrivateIp());
            experimentScope.setAgentStatus(2);
            return experimentScope;
        }
        experimentScope.setAgentStatus(deviceDO.getStatus());
        experimentScope.setAgentVersion(deviceDO.getVersion());
        if (DeviceType.POD.getType() == applicationDeviceDO.getDeviceType()) {
            experimentScope.setHostName(deviceDO.getHostname());
            experimentScope.setClusterName(deviceDO.getClusterName());
            experimentScope.setDeviceId(deviceDO.getDeviceId());
        } else {
            experimentScope.setPublicIp(deviceDO.getPublicIp());
            experimentScope.setHostName(applicationDeviceDO.getDeviceName());
        }
        experimentScope.setConnectTime(applicationDeviceDO.getConnectTime());
        experimentScope.setDeviceTags(
                applicationDeviceTagRepository.getTagsByAppIdAndConfigurationIdAndUserId(
                        applicationDeviceDO.getAppId(),applicationDeviceDO.getConfigurationId(),applicationDeviceDO.getUserId())
                        .stream().map(ApplicationDeviceTagDO::getTagName).distinct().collect(Collectors.toList()));
        return experimentScope;
    }
}
