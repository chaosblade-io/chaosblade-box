package com.alibaba.chaosblade.box.service.infrastructure.service;

import com.alibaba.chaosblade.box.common.common.enums.AppFromType;
import com.alibaba.chaosblade.box.common.common.enums.DeviceType;
import com.alibaba.chaosblade.box.common.common.enums.InstallMode;
import com.alibaba.chaosblade.box.common.infrastructure.constant.ApplicationDimension;
import com.alibaba.chaosblade.box.common.infrastructure.constant.DeviceStatus;
import com.alibaba.chaosblade.box.dao.model.ApplicationDeviceDO;
import com.alibaba.chaosblade.box.dao.model.DeviceDO;
import com.alibaba.chaosblade.box.dao.repository.ApplicationDeviceRepository;
import com.alibaba.chaosblade.box.dao.repository.UserApplicationRepository;
import com.alibaba.chaosblade.box.service.model.cluster.DeviceRealDTO;
import com.alibaba.chaosblade.box.service.model.cluster.KubPodExtInfoDTO;
import com.alibaba.chaosblade.box.service.model.cluster.UserAppConfig;
import com.alibaba.chaosblade.box.service.model.cluster.UserTopologyConfig;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author: xinyuan.ymm
 * @create: 2020-04-24 5:09 PM
 */
@Service
public class ApplicationDeviceController {
    private static final Logger LOGGER = LoggerFactory.getLogger(ApplicationDeviceController.class);

    @Autowired
    private UserApplicationRepository userApplicationRepository;

    @Resource
    private ApplicationDeviceRepository applicationDeviceRepository;

    @Resource
    private UserTopologyConfigService userTopologyConfigService;

    /**
     * k8s模式POD应用注册
     *
     * @param userId
     * @param resources
     */
    public Map<String, String> insertOrUpdateByPod(String userId, String environment, List<DeviceRealDTO> resources) {
        if (CollectionUtils.isNotEmpty(resources)) {
            Map<String, String> podToAppNameMap = new HashMap<>(resources.size());
            try {
                UserTopologyConfig userTopologyConfig = userTopologyConfigService.queryConfigContentWithCache(userId,
                    environment);
                resources.stream().forEach(podInfo -> {
                    String appName = handlerPodAppInfo(userId, podInfo, userTopologyConfig.getUserAppConfig());
                    if (StringUtils.isNotBlank(appName)) {
                        podToAppNameMap.put(podInfo.getConfigurationId(), appName);
                    }
                });
            } catch (Exception e) {
                LOGGER.error("AppPodNameHandleFail", userId, e.getMessage(), e);
            }

            return podToAppNameMap;
        }

        return new HashMap<>(0);
    }

    private String handlerPodAppInfo(String userId, DeviceRealDTO podRealDTO, UserAppConfig userAppConfig) {
        KubPodExtInfoDTO kubPodExtInfoDTO = JSON.parseObject(podRealDTO.getExtInfo(), KubPodExtInfoDTO.class);
        if (kubPodExtInfoDTO == null || StringUtils.isBlank(kubPodExtInfoDTO.getLabels())) {
            return null;
        }

        String appName = null;
        String appGroupName = null;
        JSONObject jsonObjectByLabels = JSON.parseObject(kubPodExtInfoDTO.getLabels());

        //基于标签的App名称解析
        if (AppFromType.LABEL.name().equalsIgnoreCase(userAppConfig.getAppFromType())) {
            if (CollectionUtils.isNotEmpty(userAppConfig.getAppNameSet())) {
                //单独配置了App标签组，依次解析，前者优先
                for (String appNameKey : userAppConfig.getAppNameSet()) {
                    appName = jsonObjectByLabels.getString(appNameKey);
                    if (StringUtils.isNotBlank(appName)) {
                        break;
                    }
                }
            }

            if (CollectionUtils.isNotEmpty(userAppConfig.getAppGroupNameKeySet())) {
                //单独配置了AppGroup标签组，依次解析，前者优先
                for (String appGroupNameKey : userAppConfig.getAppGroupNameKeySet()) {
                    appGroupName = jsonObjectByLabels.getString(appGroupNameKey);
                    if (StringUtils.isNotBlank(appGroupName)) {
                        break;
                    }
                }
            }

            //未提取到应用信息，且不适用默认应用配置
            if (StringUtils.isBlank(appName)
                && userAppConfig.getUseDefaultAppChain() != null
                && !userAppConfig.getUseDefaultAppChain()) {
                return null;
            }
        }

        //走默认配置
        if (StringUtils.isBlank(appName)) {
            appName = defaultParseAppName(jsonObjectByLabels);
            appGroupName = defaultParseAppGroup(jsonObjectByLabels);
        }

        if (StringUtils.isBlank(appName)) {
            return null;
        }
        if (StringUtils.isBlank(appGroupName)) {
            appGroupName = appName + "-group";
        }

        //应用信息设置到设备对象中
        podRealDTO.setApplicationName(appName);

        persistencePodAppInfo(userId, appName, appGroupName, podRealDTO, kubPodExtInfoDTO);

        return appName;
    }

    /**
     * 持久化Pod应用信息
     *
     * @param userId
     * @param appName
     * @param appGroupName
     * @param podRealDTO
     * @param kubPodExtInfoDTO
     */
    private void persistencePodAppInfo(String userId, String appName, String appGroupName, DeviceRealDTO podRealDTO,
        KubPodExtInfoDTO kubPodExtInfoDTO) {
        if (podRealDTO.getStatus() != null && DeviceStatus.ONLINE.getStatus() == podRealDTO.getStatus()) {

            ApplicationDeviceDO applicationDeviceDO = new ApplicationDeviceDO();
            applicationDeviceDO.setUserId(userId);
            applicationDeviceDO.setClusterId(podRealDTO.getClusterId());
            applicationDeviceDO.setKubNamespace(kubPodExtInfoDTO.getKubNamespace());
            applicationDeviceDO.setNamespace(podRealDTO.getEnvironment());
            applicationDeviceDO.setDeviceName(podRealDTO.getDeviceName());
            applicationDeviceDO.setPrivateIp(podRealDTO.getPrivateIp());
            applicationDeviceDO.setDeviceType(DeviceType.POD.getType());
            applicationDeviceDO.setConnectTime(System.currentTimeMillis());
            applicationDeviceDO.setLastHealthPingTime(System.currentTimeMillis());
            applicationDeviceDO.setConfigurationId(podRealDTO.getConfigurationId());
            applicationDeviceDO.setHostConfigurationId(podRealDTO.getHostConfigurationId());
            applicationDeviceDO.setAppName(appName);
            applicationDeviceDO.setGroupName(appGroupName);
            applicationDeviceDO.setDimension(ApplicationDimension.POD.getValue());
            userApplicationRepository.registerOrUpdateDevice(applicationDeviceDO);
        }
    }

    /**
     * 主机模式, 应用注册
     *
     * @param deviceLists
     * @param appName
     * @param appGroupName
     */
    public void insertOrUpdateByHost(String appName, String appGroupName, List<DeviceDO> deviceLists) {
        if (CollectionUtils.isNotEmpty(deviceLists)) {
            try {
                for (DeviceDO deviceDO : deviceLists) {
                    if (StringUtils.isBlank(deviceDO.getUserId()) || StringUtils.isBlank(appName)) {
                        continue;
                    }
                    if (StringUtils.isBlank(appGroupName)) {
                        appGroupName = appName + "-group";
                    }

                    //清除历史设备归属的应用信息
                    delete(deviceDO.getUserId(), DeviceType.HOST, deviceDO.getConfigurationId());
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
                    chaosApplicationDeviceDO.setOsType(deviceDO.getOsType());
                    userApplicationRepository.registerOrUpdateDevice(chaosApplicationDeviceDO);
                }
            } catch (Exception e) {
                LOGGER.error("AppHostNameHandleFail", appName, e.getMessage(), e);
            }
        }
    }

    /**
     * k8s模式，Node应用注册
     *
     * @param appName
     * @param deviceLists
     */
    public void insertOrUpdateByNode(String appName, List<DeviceDO> deviceLists) {
        if (CollectionUtils.isNotEmpty(deviceLists)) {
            try {
                for (DeviceDO deviceDO : deviceLists) {
                    if (!InstallMode.isKubernetes(deviceDO.getInstallMode())) {
                        continue;
                    }

                    if (StringUtils.isBlank(deviceDO.getUserId()) || StringUtils.isBlank(appName)) {
                        continue;
                    }

                    String appGroupName = deviceDO.getClusterId();
                    if (StringUtils.isBlank(appGroupName)) {
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
                    chaosApplicationDeviceDO.setDimension(ApplicationDimension.NODE.getValue());
                    userApplicationRepository.registerOrUpdateDevice(chaosApplicationDeviceDO);
                }
            } catch (Exception e) {
                LOGGER.error("AppNodeNameHandleFail", appName, e.getMessage(), e);
            }
        }
    }

    /**
     * 基于PodLabel解析应用名称
     *
     * @param jsonObject
     * @return
     */
    public String defaultParseAppName(JSONObject jsonObject) {

        // Chaos app
        String appName = jsonObject.getString("chaos/app-instance");
        if (StringUtils.isNotBlank(appName)) {
            return appName;
        }

        //APP Center
        appName = jsonObject.getString("app-group-name");
        if (StringUtils.isNotBlank(appName)) {
            return appName;
        }

        //K8S Standard App Label
        appName = jsonObject.getString("app.kubernetes.io/name");
        if (StringUtils.isNotBlank(appName)) {
            return appName;
        }

        //k8s app
        appName = jsonObject.getString("k8s-app");
        if (StringUtils.isNotBlank(appName)) {
            return appName;
        }

        appName = jsonObject.getString("app");
        if (StringUtils.isNotBlank(appName)) {
            return appName;
        }

        return null;
    }

    /**
     * 解析应用分组，应用分组并不是一个全球标准的概念模型
     *
     * @param jsonObject
     * @return
     */
    private String defaultParseAppGroup(JSONObject jsonObject) {

        //Chaos APP
        String appGroupName = jsonObject.getString("chaos/app-group");
        if (StringUtils.isNotBlank(appGroupName)) {
            return appGroupName;
        }
        return null;
    }

    /**
     * 应用生命周期续租
     *
     * @param userId
     * @param resourceSet
     */
    public void lease(String userId, Set<String> resourceSet) {
        if (CollectionUtils.isNotEmpty(resourceSet)) {
            try {
                String resourceType = "app-lease";

                userApplicationRepository.batchUpdateDeviceHeartBeat(Lists.newArrayList(resourceSet));
            } catch (Exception e) {
                LOGGER.error("AppDeviceLeaseFail", userId, e.getMessage(), e);
            }
        }
    }

    /**
     * 删除应用
     *
     * @param userId
     * @param deviceType
     * @param configurationId
     */
    public void delete(String userId, DeviceType deviceType, String configurationId) {
        try {
            applicationDeviceRepository.deleteDevice(userId, deviceType, configurationId);
        } catch (Exception e) {
            LOGGER.error("AppDeviceDeleteFail", userId, e.getMessage(), e);
        }
    }

}
