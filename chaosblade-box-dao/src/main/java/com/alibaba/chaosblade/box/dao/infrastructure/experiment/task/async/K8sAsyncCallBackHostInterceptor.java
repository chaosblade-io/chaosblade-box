package com.alibaba.chaosblade.box.dao.infrastructure.experiment.task.async;

import com.alibaba.chaosblade.box.common.app.sdk.scope.Host;
import com.alibaba.chaosblade.box.dao.model.DeviceDO;
import com.alibaba.chaosblade.box.dao.repository.DeviceRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author sunpeng
 *
 *
 */
@Slf4j
@Component
public class K8sAsyncCallBackHostInterceptor implements AsyncCallBackHostInterceptor {

    @Autowired
    private DeviceRepository deviceRepository;

    @Override
    public void fillHostInfo(Host host) {
        DeviceDO deviceDO = deviceRepository.findByConfigurationId(host.getDeviceConfigurationId());
        log.info("[K8sAsyncCallBackHostInterceptor] targetIp:{}", host.getTargetIp());
        if(null != deviceDO) {
            log.info("[K8sAsyncCallBackHostInterceptor] privateIp:{}", deviceDO.getPrivateIp());
            host.setIp(deviceDO.getPrivateIp());
            host.setTargetIp(deviceDO.getPrivateIp());
        }
    }

}
