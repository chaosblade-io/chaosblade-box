package com.alibaba.chaosblade.box.service.infrastructure.convertor;

import com.alibaba.chaosblade.box.common.common.enums.ScopeTypeEnum;
import com.alibaba.chaosblade.box.dao.model.ApplicationDeviceDO;
import com.alibaba.chaosblade.box.dao.model.DeviceDO;
import com.alibaba.chaosblade.box.dao.repository.DeviceRepository;
import com.alibaba.chaosblade.box.dao.infrastructure.utils.CloudHostUtil;
import com.alibaba.chaosblade.box.service.model.device.CloudDevice;
import com.alibaba.chaosblade.box.service.model.device.DeviceRole;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author haibin
 *
 *
 */
@Component
public class ApplicationDeviceConvertor extends BaseApplicationConvertor<ApplicationDeviceDO, CloudDevice> {

    @Autowired
    private DeviceRepository deviceRepository;

    @Override
    public CloudDevice convert(ApplicationDeviceDO applicationDeviceDO) {
        DeviceDO deviceDO = deviceRepository.findByConfigurationId(applicationDeviceDO.getHostConfigurationId());
        if (deviceDO == null) {
            return null;
        }
        CloudDevice cloudDevice = CloudDevice.from(deviceDO);
        cloudDevice.setVpcId(deviceDO.getVpcId());
        cloudDevice.setPrivateIp(applicationDeviceDO.getPrivateIp());
        cloudDevice.setDeviceId(deviceDO.getDeviceId());
        cloudDevice.setDeviceName(applicationDeviceDO.getDeviceName());
        cloudDevice.setDeviceConfigurationId(applicationDeviceDO.getHostConfigurationId());
        cloudDevice.setK8s(CloudHostUtil.isK8sInstallMode(deviceDO.getInstallMode()));
        cloudDevice.setMaster(DeviceRole.master.name().equals(deviceDO.getDeviceRole()));
        cloudDevice.setDeviceType(deviceDO.getDeviceType());
        cloudDevice.setClusterName(deviceDO.getClusterName());
        cloudDevice.setScopeType(cloudDevice.isK8s() ? ScopeTypeEnum.K8s.getValue() : ScopeTypeEnum.HOST.getValue());
        cloudDevice.setAppConfigurationId(applicationDeviceDO.getConfigurationId());
        cloudDevice.setNodeGroup(applicationDeviceDO.getGroupName());
        cloudDevice.setAppId(String.valueOf(applicationDeviceDO.getAppId()));
        cloudDevice.setApp(applicationDeviceDO.getAppName());
        cloudDevice.setIp(applicationDeviceDO.getPrivateIp());
        cloudDevice.setAllow(true);
        cloudDevice.setKubNamespace(applicationDeviceDO.getKubNamespace());
        cloudDevice.setClusterId(deviceDO.getClusterId());
        cloudDevice.setOsType(applicationDeviceDO.getOsType());
        return cloudDevice;
    }
}
