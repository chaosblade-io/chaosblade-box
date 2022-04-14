package com.alibaba.chaosblade.box.dao.repository;

import com.alibaba.chaosblade.box.common.infrastructure.constant.DeviceStatus;
import com.alibaba.chaosblade.box.dao.mapper.DeviceMapper;
import com.alibaba.chaosblade.box.dao.model.DeviceDO;
import com.alibaba.chaosblade.box.dao.query.CloudDeviceQuery;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.google.common.base.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

/**
 * @author haibin.lhb
 *
 * 
 */
@Component
public class PrivateCloudDeviceRepository {

    private DeviceRepository deviceRepository;
    private DeviceMapper deviceMapper;

    public List<DeviceDO> getAliveDevices(CloudDeviceQuery query) {
        QueryWrapper<DeviceDO> queryWrapper = new QueryWrapper<DeviceDO>()
            .eq(!Strings.isNullOrEmpty(query.getUserId()), "user_id", query.getUserId())
            .eq(!Strings.isNullOrEmpty(query.getNamespace()), "namespace", query.getNamespace())
            .eq("device_type", 0)
            .eq("status", 2)
            .isNotNull("version");
        return deviceMapper.selectList(queryWrapper);
    }

    public PrivateCloudDeviceRepository(@Autowired DeviceRepository deviceRepository) {
        this.deviceRepository = deviceRepository;
        this.deviceMapper = this.deviceRepository.getDeviceMapper();
    }

    public DeviceRepository getCommon() {
        return deviceRepository;
    }

    public List<DeviceDO> findByUserIdAndDeviceTypeAndConfigurationId(String userId, int deviceType, String cid) {
        QueryWrapper<DeviceDO> queryWrapper = new QueryWrapper<DeviceDO>();
        queryWrapper.eq("user_id", userId)
            .eq("configuration_id", cid)
            .eq("device_type", deviceType);
        return deviceMapper.selectList(queryWrapper);
    }

    public int insert(DeviceDO deviceDO) {
        return deviceMapper.insert(deviceDO);
    }

    public boolean updateDeviceById(DeviceDO deviceDO) {
        QueryWrapper<DeviceDO> queryWrapper = new QueryWrapper<DeviceDO>();
        queryWrapper.eq("id", deviceDO.getId());
        return getCommon().getDeviceMapper().update(deviceDO, queryWrapper) > 0;
    }

    public boolean updateHeartBeatTimeByDeviceDO(String configurationId, String version, String chaosVersion,
        Date heartBeatTime, String ip) {
        QueryWrapper<DeviceDO> queryWrapper = new QueryWrapper<DeviceDO>();
        queryWrapper.eq("configuration_id", configurationId);
        DeviceDO deviceDO = new DeviceDO();
        deviceDO.setVersion(version);
        deviceDO.setChaosVersion(chaosVersion);
        deviceDO.setLastHealthPingTime(heartBeatTime.getTime());
        deviceDO.setLastOnlineTime(heartBeatTime.getTime());
        deviceDO.setStatus(DeviceStatus.ONLINE.getStatus());
        deviceDO.setEnable(true);
        deviceDO.setGmtModified(new Date());
        deviceDO.setPublicIp(ip);
        return deviceRepository.getDeviceMapper().update(deviceDO, queryWrapper) > 0;
    }
}
