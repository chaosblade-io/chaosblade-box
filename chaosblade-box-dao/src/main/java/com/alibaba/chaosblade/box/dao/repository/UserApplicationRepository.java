package com.alibaba.chaosblade.box.dao.repository;

import com.alibaba.chaosblade.box.common.common.enums.AppType;
import com.alibaba.chaosblade.box.common.common.enums.DeviceType;
import com.alibaba.chaosblade.box.common.infrastructure.constant.ApplicationDimension;
import com.alibaba.chaosblade.box.common.infrastructure.constant.DeviceStatus;
import com.alibaba.chaosblade.box.dao.mapper.ApplicationDeviceMapper;
import com.alibaba.chaosblade.box.dao.mapper.ApplicationGroupMapper;
import com.alibaba.chaosblade.box.dao.mapper.ApplicationMapper;
import com.alibaba.chaosblade.box.dao.model.ApplicationDO;
import com.alibaba.chaosblade.box.dao.model.ApplicationDeviceDO;
import com.alibaba.chaosblade.box.dao.model.ApplicationGroupDO;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.google.common.base.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;
import java.util.Objects;

/**
 * @author haibin.lhb
 *
 *
 */
@Component
public class UserApplicationRepository {

    @Autowired
    private ApplicationMapper applicationMapper;

    @Autowired
    private ApplicationDeviceMapper applicationDeviceMapper;

    @Autowired
    private ApplicationGroupMapper applicationGroupMapper;

    public int batchUpdateDeviceHeartBeat(List<String> configurationIds) {
        if (configurationIds.isEmpty()) {
            return 0;
        }

        for (String configurationId : configurationIds) {
            updateDeviceHeartBeat(configurationId);
        }
        return configurationIds.size();
    }

    /**
     * 更新设备心跳
     *
     * @param configurationId
     * @return
     */
    public boolean updateDeviceHeartBeat(String configurationId) {
        QueryWrapper<ApplicationDeviceDO> applicationDeviceDOQueryWrapper = new QueryWrapper<>();
        applicationDeviceDOQueryWrapper.eq("configuration_id", configurationId);
        ApplicationDeviceDO updateObject = new ApplicationDeviceDO();
        updateObject.setLastHealthPingTime(System.currentTimeMillis());
        updateObject.setGmtModified(new Date());
        return applicationDeviceMapper.update(updateObject, applicationDeviceDOQueryWrapper) > 0;
    }

    /**
     * 注册或者更新应用设备
     *
     * @param requestDeviceDO
     * @return
     */
    public ApplicationDeviceDO registerOrUpdateDevice(ApplicationDeviceDO requestDeviceDO) {
        DeviceType deviceType = judgeDeviceType(requestDeviceDO);
        if (deviceType == null) { return null; }
        if (requestDeviceDO.getAppName() == null) {
            return null;
        }
        //删除老的记录
        deleteDevice(requestDeviceDO.getUserId(), ApplicationDimension.HOST, requestDeviceDO.getConfigurationId());
        //处理应用信息
        ApplicationDO chaosApplicationRequestDO = buildApplicationDOFromDeviceDO(deviceType, requestDeviceDO);
        ApplicationDO chaosApplicationDO = registerApplication(
            chaosApplicationRequestDO);
        //处理分组信息
        registerGroup(buildGroupFromDeviceDO(chaosApplicationDO, requestDeviceDO));
        //处理设备信息
        return registerDevice(requestDeviceDO, deviceType, chaosApplicationRequestDO,
            chaosApplicationDO);
    }

    private ApplicationDeviceDO registerDevice(ApplicationDeviceDO requestDeviceDO, DeviceType deviceType,
                                               ApplicationDO chaosApplicationRequestDO, ApplicationDO chaosApplicationDO) {
        ApplicationDeviceDO chaosApplicationDeviceDO = findDeviceByDeviceType(deviceType, requestDeviceDO);
        if (chaosApplicationDeviceDO == null) {
            chaosApplicationDeviceDO = requestDeviceDO;
            chaosApplicationDeviceDO.setAppId(chaosApplicationDO.getId());
            chaosApplicationDeviceDO.setAppName(chaosApplicationDO.getAppName());
            chaosApplicationDeviceDO.setStatus(DeviceStatus.ONLINE.getStatus());
            chaosApplicationDeviceDO.setDimension(requestDeviceDO.getDimension());
            chaosApplicationDeviceDO.setIsDeleted(false);
            chaosApplicationDeviceDO.setGmtCreate(new Date());
            chaosApplicationDeviceDO.setGmtModified(new Date());
            applicationDeviceMapper.insert(requestDeviceDO);
        } else {
            requestDeviceDO.setId(chaosApplicationDeviceDO.getId());
            requestDeviceDO.setAppId(chaosApplicationRequestDO.getId());
            requestDeviceDO.setAppName(chaosApplicationRequestDO.getAppName());
            requestDeviceDO.setStatus(DeviceStatus.ONLINE.getStatus());
            requestDeviceDO.setIsDeleted(false);
            requestDeviceDO.setGmtModified(new Date());
            applicationDeviceMapper.updateById(requestDeviceDO);
        }
        return chaosApplicationDeviceDO;
    }

    /**
     * 根据设备类型查询设备
     *
     * @param deviceType
     * @param requestDeviceDO
     * @return
     */
    private ApplicationDeviceDO findDeviceByDeviceType(DeviceType deviceType,
                                                       ApplicationDeviceDO requestDeviceDO) {
        ApplicationDeviceDO query = new ApplicationDeviceDO();
        query.setUserId(requestDeviceDO.getUserId());
        query.setNamespace(requestDeviceDO.getNamespace());
        query.setDeviceType(deviceType.getType());
        query.setConfigurationId(requestDeviceDO.getConfigurationId());
        query.setDimension(requestDeviceDO.getDimension());
        return applicationDeviceMapper.selectOne(new QueryWrapper<>(query));
    }

    /**
     * 从device对象中转换device
     *
     * @param chaosApplicationDeviceDO
     * @return
     */
    private ApplicationGroupDO buildGroupFromDeviceDO(
        ApplicationDO chaosApplicationDO,
        ApplicationDeviceDO chaosApplicationDeviceDO) {
        ApplicationGroupDO chaosApplicationGroupDO = new ApplicationGroupDO();
        chaosApplicationGroupDO.setAppName(chaosApplicationDO.getAppName());
        chaosApplicationGroupDO.setAppId(chaosApplicationDO.getId());
        chaosApplicationGroupDO.setName(chaosApplicationDeviceDO.getGroupName());
        return chaosApplicationGroupDO;
    }

    /**
     * 从device对象中转换application
     *
     * @param deviceType
     * @param requestChaosApplicationDeviceDO
     * @return
     */
    private ApplicationDO buildApplicationDOFromDeviceDO(DeviceType deviceType,
                                                         ApplicationDeviceDO requestChaosApplicationDeviceDO) {
        ApplicationDO chaosApplicationDO = new ApplicationDO();
        chaosApplicationDO.setNamespace(requestChaosApplicationDeviceDO.getNamespace());
        chaosApplicationDO.setUserId(requestChaosApplicationDeviceDO.getUserId());
        chaosApplicationDO.setAppName(requestChaosApplicationDeviceDO.getAppName());
        chaosApplicationDO.setAppType(AppType.judeAppTypeByDeviceType(deviceType).getType());
        chaosApplicationDO.setDimension(requestChaosApplicationDeviceDO.getDimension());
        chaosApplicationDO.setDisabled(false);
        return chaosApplicationDO;
    }

    private DeviceType judgeDeviceType(ApplicationDeviceDO chaosApplicationDeviceDO) {
        for (DeviceType deviceType : DeviceType.values()) {
            if (Objects.equals(deviceType.getType(), chaosApplicationDeviceDO.getDeviceType())) {
                return deviceType;
            }
        }
        return null;
    }

    private void registerGroup(ApplicationGroupDO requestApplicationGroupDO) {
        if (Strings.isNullOrEmpty(requestApplicationGroupDO.getName())) {
            return;
        }
        ApplicationGroupDO query = new ApplicationGroupDO();
        query.setAppName(requestApplicationGroupDO.getAppName());
        query.setName(requestApplicationGroupDO.getName());
        query.setAppId(requestApplicationGroupDO.getAppId());
        ApplicationGroupDO chaosApplicationGroupDO = applicationGroupMapper.selectOne(
            new QueryWrapper<>(query));
        if (chaosApplicationGroupDO != null) {
            requestApplicationGroupDO.setId(chaosApplicationGroupDO.getId());
            requestApplicationGroupDO.setGmtModified(new Date());
            applicationGroupMapper.updateById(requestApplicationGroupDO);
        } else {
            requestApplicationGroupDO.setGmtCreate(new Date());
            requestApplicationGroupDO.setGmtModified(new Date());
            applicationGroupMapper.insert(requestApplicationGroupDO);
        }
    }

    private ApplicationDO registerApplication(ApplicationDO requestApplicationDO) {
        ApplicationDO query = new ApplicationDO();
        query.setAppName(requestApplicationDO.getAppName());
        query.setUserId(requestApplicationDO.getUserId());
        query.setNamespace(requestApplicationDO.getNamespace());
        query.setDimension(requestApplicationDO.getDimension());
        ApplicationDO chaosApplicationDO = applicationMapper.selectOne(new QueryWrapper<>(query));
        if (chaosApplicationDO != null) {
            requestApplicationDO.setId(chaosApplicationDO.getId());
            chaosApplicationDO.setGmtModified(new Date());
            applicationMapper.updateById(requestApplicationDO);
        } else {
            chaosApplicationDO = requestApplicationDO;
            chaosApplicationDO.setDisabled(false);
            chaosApplicationDO.setGmtCreate(new Date());
            chaosApplicationDO.setGmtModified(new Date());
            applicationMapper.insert(chaosApplicationDO);
        }
        return chaosApplicationDO;
    }

    /**
     * 删除设备
     *
     * @param userId
     * @param configurationId
     * @return
     */
    public boolean deleteDevice(String userId, ApplicationDimension dimension, String configurationId) {
        ApplicationDeviceDO applicationDeviceDO = new ApplicationDeviceDO();
        applicationDeviceDO.setUserId(userId);
        applicationDeviceDO.setDimension(dimension.getValue());
        applicationDeviceDO.setConfigurationId(configurationId);
        applicationDeviceDO.setIsDeleted(false);
        applicationDeviceDO.setGmtModified(new Date());
        ApplicationDeviceDO updateObject = new ApplicationDeviceDO();
        updateObject.setIsDeleted(true);
        return applicationDeviceMapper.update(updateObject, new QueryWrapper<>(applicationDeviceDO)) > 0;
    }

}
