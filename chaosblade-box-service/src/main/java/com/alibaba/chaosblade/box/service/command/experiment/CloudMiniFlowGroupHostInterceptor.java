package com.alibaba.chaosblade.box.service.command.experiment;

import com.alibaba.chaosblade.box.common.app.sdk.scope.Host;
import com.alibaba.chaosblade.box.common.infrastructure.util.CollectionUtil;
import com.alibaba.chaosblade.box.dao.model.ApplicationDeviceDO;
import com.alibaba.chaosblade.box.dao.model.DeviceDO;
import com.alibaba.chaosblade.box.dao.repository.ApplicationDeviceRepository;
import com.alibaba.chaosblade.box.dao.repository.DeviceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author sunpeng
 *
 *
 */
@Component
public class CloudMiniFlowGroupHostInterceptor implements MiniFlowGroupHostInterceptor {

    @Autowired
    private DeviceRepository deviceRepository;

    @Autowired
    private ApplicationDeviceRepository applicationDeviceRepository;

    @Override
    public void check(Host host) {
        DeviceDO deviceDO = deviceRepository.findByConfigurationId(host.getDeviceConfigurationId());
        if (null == deviceDO) {
            host.setInvalid(true);
        } else {
            host.setInvalid(false);
        }
    }

    @Override
    public List<String> batchCheck(List<String> list, Set<String> appId, Set<String> groupName) {
        if(CollectionUtil.isNullOrEmpty(list)){
            return new ArrayList<>();
        }
        String app = appId.iterator().next();
        //校验应用机器中是否还存在
        List<ApplicationDeviceDO> applicationDevices = applicationDeviceRepository.findByAppInfoAndConfigurationIds(app,groupName,list);
        if(CollectionUtil.isNullOrEmpty(applicationDevices)){
            return new ArrayList<>();
        }
        //校验device中是否存在
        List<DeviceDO> deviceDOList = deviceRepository.findByConfigurationIds(
                applicationDevices.stream().map(ApplicationDeviceDO::getHostConfigurationId).collect(Collectors.toList()));
        if (CollectionUtil.isNullOrEmpty(deviceDOList)) {
            return new ArrayList<>();
        }
        return deviceDOList.stream().map(DeviceDO::getConfigurationId).collect(Collectors.toList());
    }
}
