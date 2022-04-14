package com.alibaba.chaosblade.box.service.infrastructure;


import com.alibaba.chaosblade.box.common.common.enums.DeviceType;
import com.alibaba.chaosblade.box.common.infrastructure.constant.ApplicationDimension;
import com.alibaba.chaosblade.box.common.infrastructure.domain.namespace.Namespace;
import com.alibaba.chaosblade.box.common.sdk.util.StringUtil;
import com.alibaba.chaosblade.box.dao.model.ApplicationDeviceDO;
import com.alibaba.chaosblade.box.dao.model.DeviceDO;
import com.alibaba.chaosblade.box.dao.repository.UserApplicationRepository;
import com.alibaba.chaosblade.box.dao.repository.UserRepository;
import com.alibaba.chaosblade.box.service.NamespaceService;
import com.google.common.base.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author haibin.lhb
 * 
 *
 */
@Component
public class UserApplicationRegister {

    @Autowired
    private UserApplicationRepository userApplicationRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private NamespaceService namespaceService;

    public Long registerApplicationByHost(DeviceDO deviceDO, String appName, String appGroupName) {
        ApplicationDeviceDO
            chaosApplicationDeviceDO = buildApplicationDeviceDO(deviceDO, appName, appGroupName);
        return userApplicationRepository.registerOrUpdateDevice(chaosApplicationDeviceDO).getAppId();
    }

    private ApplicationDeviceDO buildApplicationDeviceDO(DeviceDO deviceDO, String appName, String appGroupName) {
        if (Strings.isNullOrEmpty(appGroupName)) {
            appGroupName = appName + "-group";
        }
        ApplicationDeviceDO chaosApplicationDeviceDO = new ApplicationDeviceDO();
        chaosApplicationDeviceDO.setUserId(deviceDO.getUserId());
        chaosApplicationDeviceDO.setClusterId(deviceDO.getClusterId());
        chaosApplicationDeviceDO.setNamespace(deviceDO.getNamespace());
        chaosApplicationDeviceDO.setDeviceName(deviceDO.getDeviceName());
        chaosApplicationDeviceDO.setPrivateIp(deviceDO.getPrivateIp());
        chaosApplicationDeviceDO.setDeviceType(DeviceType.HOST.getType());
        chaosApplicationDeviceDO.setConnectTime(System.currentTimeMillis());
        chaosApplicationDeviceDO.setLastHealthPingTime(System.currentTimeMillis());
        chaosApplicationDeviceDO.setConfigurationId(deviceDO.getConfigurationId());
        chaosApplicationDeviceDO.setHostConfigurationId(deviceDO.getHostConfigurationId());
        chaosApplicationDeviceDO.setAppName(appName);
        chaosApplicationDeviceDO.setGroupName(appGroupName);
        chaosApplicationDeviceDO.setDimension(ApplicationDimension.HOST.getValue());
        return chaosApplicationDeviceDO;
    }

    public String getUserId(String oldUserId, String licenseKey) {
        if (StringUtil.isBlank(licenseKey)) {
            return "";
        }
        String userId = userRepository.getUserIdByLicense(licenseKey);
        if (userId.equals("")) {
            return oldUserId;
        }
        return userId;
    }

    /**
     * 检查用户的空间是否存在，如果为默认空间且不存在，则创建
     * @param userId
     * @param namespace
     * @return
     */
    public boolean checkNamespace(String userId, String namespace) {
        Namespace namespaceDetail = namespaceService.queryNamespace(userId, namespace);
        if (namespaceDetail != null && namespace.equals(namespaceDetail.getName())) {
            return true;
        }

        if (namespaceService.getDefaultNamespace().equalsIgnoreCase(namespace)){
            namespaceService.initDefaultNamespace(userId);
            return true;
        }

        return false;
    }
}
