package com.alibaba.chaosblade.box.service.command.scope;

import cn.hutool.core.util.StrUtil;
import com.alibaba.chaosblade.box.common.common.constant.PrivateCloudConstant;
import com.alibaba.chaosblade.box.common.common.enums.DeviceType;
import com.alibaba.chaosblade.box.common.common.enums.InstallMode;
import com.alibaba.chaosblade.box.common.infrastructure.IChaosDomain;
import com.alibaba.chaosblade.box.common.infrastructure.constant.DeviceStatus;
import com.alibaba.chaosblade.box.common.infrastructure.util.ClockUtil;
import com.alibaba.chaosblade.box.common.infrastructure.util.ChaosPreconditions;
import com.alibaba.chaosblade.box.dao.model.ApplicationDeviceDO;
import com.alibaba.chaosblade.box.dao.model.DeviceDO;
import com.alibaba.chaosblade.box.dao.repository.ApplicationDeviceRepository;
import com.alibaba.chaosblade.box.dao.repository.PrivateCloudDeviceRepository;
import com.alibaba.chaosblade.box.service.model.agent.HeartBeatCallbackRequest;
import com.alibaba.chaosblade.box.service.model.agent.RegisteredCallbackRequest;
import com.google.common.base.Preconditions;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @author haibin.lhb
 *
 * 
 */
public class PrivateScope implements IChaosDomain {

    private static Logger logger = LoggerFactory.getLogger("Scope");

    @Autowired
    private PrivateCloudDeviceRepository privateCloudDeviceRepository;

    private String configurationId;

    @Autowired
    private ApplicationDeviceRepository applicationDeviceRepository;

    public PrivateScope() {

    }

    public PrivateScope(String configurationId) {
        this.configurationId = configurationId;
    }

    public DeviceDO register(RegisteredCallbackRequest registeredCallbackRequest) {
        String configurationId = generatorDeviceConfigurationId(
            registeredCallbackRequest.getUserId(),
            registeredCallbackRequest.getNamespace(), PrivateCloudConstant.GLOBAL_VPC_ID,
            registeredCallbackRequest.getIp(),
            registeredCallbackRequest.getDeviceType(), registeredCallbackRequest.getDeviceId());
        DeviceDO oldDeviceDO = saveDevice(
            registeredCallbackRequest, configurationId);
        this.configurationId = oldDeviceDO.getConfigurationId();
        return oldDeviceDO;
    }

    private DeviceDO saveDevice(RegisteredCallbackRequest registeredCallbackRequest, String configurationId) {
        DeviceDO oldDeviceDO = constructDeviceDO(
            registeredCallbackRequest, configurationId);
        if (oldDeviceDO.getId() == null) {
            ChaosPreconditions.checkPersistence(privateCloudDeviceRepository.getCommon().add(oldDeviceDO),
                "add device failed");
        } else {
            ChaosPreconditions.checkPersistence(privateCloudDeviceRepository.getCommon().update(oldDeviceDO),
                "add device failed");
        }
        return oldDeviceDO;
    }

    private DeviceDO constructDeviceDO(RegisteredCallbackRequest registeredCallbackRequest, String configurationId) {
        DeviceDO oldDeviceDO
            = privateCloudDeviceRepository.getCommon().findByConfigurationId(configurationId);
        if (oldDeviceDO == null) {
            oldDeviceDO = new DeviceDO();
        }

        String installMode = registeredCallbackRequest.getAgentMode();
        installMode = StringUtils.isNotBlank(installMode) ? installMode : InstallMode.host.name();

        oldDeviceDO.setInstallMode(installMode);
        oldDeviceDO.setUserId(registeredCallbackRequest.getUserId());
        oldDeviceDO.setConfigurationId(configurationId);
        oldDeviceDO.setNamespace(registeredCallbackRequest.getNamespace());
        oldDeviceDO.setVpcId(PrivateCloudConstant.GLOBAL_VPC_ID);
        oldDeviceDO.setStatus(DeviceStatus.ONLINE.getStatus());
        oldDeviceDO.setPrivateIp(registeredCallbackRequest.getIp());
        String instanceId = registeredCallbackRequest.getInstanceId();
        oldDeviceDO.setHostname(getLimitLengthField(instanceId, 127));
        oldDeviceDO.setDeviceId(getLimitLengthField(instanceId, 127));
        oldDeviceDO.setDeviceName(getLimitLengthField(instanceId, 510));
        oldDeviceDO.setUptime(getLimitLengthField(registeredCallbackRequest.getUpTime(), 127));
        oldDeviceDO.setOsVersion(registeredCallbackRequest.getOsVersion());
        oldDeviceDO.setConnectTime(ClockUtil.now().getTime());
        oldDeviceDO.setVersion(registeredCallbackRequest.getVersion());
        oldDeviceDO.setLastHealthPingTime(ClockUtil.now().getTime());
        oldDeviceDO.setCpu(registeredCallbackRequest.getCpuNum());
        oldDeviceDO.setMem(getMemSize(registeredCallbackRequest.getMemSize()));
        oldDeviceDO.setDeviceType(DeviceType.HOST.getType());
        oldDeviceDO.setUserId(registeredCallbackRequest.getUserId());
        oldDeviceDO.setVpcId(PrivateCloudConstant.GLOBAL_VPC_ID);
        oldDeviceDO.setMd5(DigestUtils.md5Hex(oldDeviceDO.toString()));
        oldDeviceDO.setHostConfigurationId(configurationId);
        oldDeviceDO.setPort(registeredCallbackRequest.getPort());
        oldDeviceDO.setClusterId(registeredCallbackRequest.getClusterId());
        oldDeviceDO.setClusterName(registeredCallbackRequest.getClusterName());
        oldDeviceDO.setProvider(registeredCallbackRequest.getStartupMode());
        oldDeviceDO.setRequestId(registeredCallbackRequest.getRequestId());
        return oldDeviceDO;
    }

    private String getLimitLengthField(String content, int maxLength) {
        if (StrUtil.isNotBlank(content) && content.length() > maxLength) {
            return content.substring(0, maxLength);
        }
        return content;
    }

    private Integer getMemSize(String memSize) {
        try {
            if (memSize != null) {
                return new BigDecimal(memSize).divide(new BigDecimal(1024)).intValue();
            }
        } catch (Exception e) {
            logger.warn("key: {}, time: {}, message: {}", "getMemSizeException", System.currentTimeMillis(),e.getMessage(), e);
        }
        return null;
    }

    public Boolean updateHeartBeatTime(HeartBeatCallbackRequest heartBeatCallbackRequest) {
        this.configurationId = heartBeatCallbackRequest.getConfigurationId();
        Date heartBeatTime = ClockUtil.now();
        boolean success = privateCloudDeviceRepository.updateHeartBeatTimeByDeviceDO(this.configurationId,
            heartBeatCallbackRequest.getVersion(), heartBeatCallbackRequest.getChaosVersion(),
            heartBeatTime, heartBeatCallbackRequest.getIp());
        if (success) {
            ApplicationDeviceDO applicationDeviceDO = new ApplicationDeviceDO();
            applicationDeviceDO.setLastHealthPingTime(heartBeatTime.getTime());
            applicationDeviceDO.setStatus(DeviceStatus.ONLINE.getStatus());
            applicationDeviceDO.setGmtModified(new Date());
            applicationDeviceDO.setConfigurationId(this.configurationId);
            applicationDeviceDO.setPublicIp(heartBeatCallbackRequest.getIp());
            return applicationDeviceRepository.update(applicationDeviceDO);
        }
        return false;
    }

    public static String generatorDeviceConfigurationId(String userId, String namespace, String vpcId, String hostIp, Integer deviceType, String deviceId) {
        Preconditions.checkArgument(StrUtil.isNotBlank(userId));
        Preconditions.checkArgument(StrUtil.isNotBlank(vpcId));
        Preconditions.checkArgument(StrUtil.isNotBlank(deviceId));
        Preconditions.checkNotNull(deviceType);
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append(userId);
        stringBuffer.append('|').append(namespace);
        stringBuffer.append('|').append(vpcId);
        stringBuffer.append('|').append(hostIp);
        stringBuffer.append('|').append(deviceType);
        stringBuffer.append('|').append(deviceId);
        return DigestUtils.md5Hex(stringBuffer.toString());
    }

    public static String generatorKubernetesConfigurationId(String userId, String namespace, String vpcId, String uid) {
        Preconditions.checkArgument(StringUtils.isNotBlank(userId));
        Preconditions.checkArgument(StringUtils.isNotBlank(vpcId));
        Preconditions.checkArgument(StringUtils.isNotBlank(uid));
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append(userId).append('|').append(namespace).append('|').append(vpcId);
        stringBuffer.append(DeviceType.POD.getType()).append(uid);
        return DigestUtils.md5Hex(stringBuffer.toString());
    }
}
