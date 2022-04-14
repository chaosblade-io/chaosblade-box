package com.alibaba.chaosblade.box.dao.infrastructure.experiment.task.interceptor;

import com.alibaba.chaosblade.box.common.app.sdk.ChaosAppResponse;
import com.alibaba.chaosblade.box.common.app.sdk.scope.Host;
import com.alibaba.chaosblade.box.common.common.enums.CommonErrorCode;
import com.alibaba.chaosblade.box.common.experiment.task.flow.interceptor.InterceptorDesc;
import com.alibaba.chaosblade.box.common.infrastructure.exception.ChaosException;
import com.alibaba.chaosblade.box.common.infrastructure.util.WaitUtil;
import com.alibaba.chaosblade.box.dao.infrastructure.experiment.task.flow.step.MiniAppInvokeContext;
import com.alibaba.chaosblade.box.dao.infrastructure.utils.CloudHostUtil;
import com.alibaba.chaosblade.box.dao.model.ApplicationDeviceDO;
import com.alibaba.chaosblade.box.dao.model.DeviceDO;
import com.alibaba.chaosblade.box.dao.repository.ApplicationDeviceRepository;
import com.alibaba.chaosblade.box.dao.repository.DeviceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Objects;
import java.util.concurrent.TimeUnit;

/**
 * @author haibin.lhb
 *
 */
@Component
@InterceptorDesc("Check Whether host application changed")
public class ApplicationHostMiniAppInvokeInterceptor extends BaseMiniAppInvokeInterceptor {

    @Autowired
    private ApplicationDeviceRepository applicationDeviceRepository;

    @Autowired
    private DeviceRepository deviceRepository;

    /**
     * 需要在执行之前再校验一下机器是否发生了变化，防止同一台机器变成了其他业务的机器地址
     *
     * @param miniAppInvokeContext
     * @param  chaosAppResponse
     * @return
     */
    @Override
    protected boolean preHandle(MiniAppInvokeContext miniAppInvokeContext, ChaosAppResponse chaosAppResponse) {
        Host host = miniAppInvokeContext.getHost();
        if (host == null) {return true;}
        if (host.isAppScope()) {
            ApplicationDeviceDO applicationDeviceDO = applicationDeviceRepository.findByAppIdAndAppConfigurationId(
                host.getLongAppId(),
                host.getAppConfigurationId()).orElse(null);
            if (applicationDeviceDO == null) {
                 chaosAppResponse.withErrorCode(CommonErrorCode.B_APPLICATION_HOST_NOT_FOUND);
                return false;
            }
            if (!isAppOrNodeGroupNotChanged(host, applicationDeviceDO)) {
                 chaosAppResponse.withErrorCode(CommonErrorCode.B_HOST_APP_OR_GROUP_CHANGED);
                return false;
            }
            host.setDeviceConfigurationId(applicationDeviceDO.getHostConfigurationId());
            DeviceDO deviceDO = checkHostStatus(host.getDeviceConfigurationId());
            host.setDeviceConfigurationId(deviceDO.getConfigurationId());
            String ip = (deviceDO.getPublicIp() == null || deviceDO.getPublicIp().isEmpty()) ? deviceDO.getPrivateIp() : deviceDO.getPublicIp();
            host.setTargetIp(ip);
        }
        miniAppInvokeContext.setHost(host);
        return true;
    }

    private DeviceDO checkHostStatus(String hostConfigurationId) {
        DeviceDO deviceDO = WaitUtil.waitFor(() -> deviceRepository.findByConfigurationId(hostConfigurationId),
            deviceDO1 -> deviceDO1 != null && deviceDO1.isAlive(), 2, TimeUnit.SECONDS);
        if (deviceDO == null) {
            throw new ChaosException(CommonErrorCode.B_K8S_AGENT_NOT_ALIVE);
        }
        if (!deviceDO.isAlive()) {
            if (CloudHostUtil.isK8sInstallMode(deviceDO.getInstallMode())) {
                throw new ChaosException(CommonErrorCode.B_K8S_AGENT_NOT_ALIVE);
            } else {
                throw new ChaosException(CommonErrorCode.B_HOST_NOT_ALIVE);
            }
        }
        return deviceDO;
    }

    @Override
    public Integer getOrder() {
        return Integer.MAX_VALUE - 1;
    }

    private boolean isAppOrNodeGroupNotChanged(Host host, ApplicationDeviceDO applicationDeviceDO) {
        return Objects.equals(host.getApp(), applicationDeviceDO.getAppName()) && Objects.equals(host.getNodeGroup(),
            applicationDeviceDO.getGroupName());
    }

}
