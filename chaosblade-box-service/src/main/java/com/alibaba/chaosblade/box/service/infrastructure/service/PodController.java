package com.alibaba.chaosblade.box.service.infrastructure.service;

import com.alibaba.chaosblade.box.common.common.enums.DeviceType;
import com.alibaba.chaosblade.box.common.infrastructure.constant.DeviceStatus;
import com.alibaba.chaosblade.box.service.model.cluster.CacheRefreshDTO;
import com.alibaba.chaosblade.box.service.model.cluster.DeviceRealDTO;
import com.alibaba.chaosblade.box.service.model.cluster.IdentifierModel;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * @author: xinyuan.ymm
 * @create: 2019-10-15 2:15 PM
 */
@Component
public class PodController {
    private static final Logger LOGGER = LoggerFactory.getLogger(PodController.class);

//    @Resource
//    private DeviceInfoRepository deviceInfoRepository;

    @Resource
    private ApplicationDeviceController applicationDeviceController;

    public String getResourceType() {
        return "Pod";
    }


    public void insertOrUpdate(String userId, String environment, List<DeviceRealDTO> objectList) {
        applicationDeviceController.insertOrUpdateByPod(userId, environment, objectList);
//        deviceInfoRepository.insert(objectList);
    }


    public int delete(String userId, String environment, IdentifierModel identifierModel) {
        try {
            applicationDeviceController.delete(userId, DeviceType.POD, identifierModel.getConfigurationId());
//            deviceInfoRepository.delete(userId, environment, DeviceType.POD.getType(),
//                identifierModel.getHostConfigurationId(), identifierModel.getConfigurationId());
        } catch (Exception e) {
            LOGGER.error("PodDeleteException", userId, identifierModel.getConfigurationId(), e);
        }

        return 0;
    }


    public void lease(String userId, String vpcId, String namespace, String clusterId, String clusterName,
        String hostConfigurationId,
        Set<String> resourceSet) {
        List<DeviceRealDTO> resourceLeaseDOS = new ArrayList<>(resourceSet.size());
        for (String cid : resourceSet) {
            if (StringUtils.isBlank(cid)) {
                continue;
            }

            DeviceRealDTO resourceLeaseDO = new DeviceRealDTO();
            resourceLeaseDO.setUserId(userId);
            resourceLeaseDO.setVpcId(vpcId);
            resourceLeaseDO.setEnvironment(namespace);
            resourceLeaseDO.setClusterId(clusterId);
            resourceLeaseDO.setClusterName(clusterName);
            resourceLeaseDO.setDeviceType(DeviceType.POD.getType());
            resourceLeaseDO.setConfigurationId(cid);
            resourceLeaseDO.setStatus(DeviceStatus.ONLINE.getStatus());
            resourceLeaseDO.setHostConfigurationId(hostConfigurationId);
            resourceLeaseDO.setLeaseTime(System.currentTimeMillis());
            resourceLeaseDOS.add(resourceLeaseDO);
        }

        if (CollectionUtils.isNotEmpty(resourceLeaseDOS)) {
            applicationDeviceController.lease(userId, resourceSet);
//            deviceInfoRepository.insert(resourceLeaseDOS);
        }
    }

    public void refresh(String userId, List<CacheRefreshDTO> cacheRefreshList) {
        return;
    }

}
