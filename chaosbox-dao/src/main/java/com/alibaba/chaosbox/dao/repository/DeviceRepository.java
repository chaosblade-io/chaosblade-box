/*
 * Copyright 1999-2021 Alibaba Group Holding Ltd.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.alibaba.chaosbox.dao.repository;

import cn.hutool.core.util.StrUtil;
import com.alibaba.chaosbox.common.enums.DeviceStatus;
import com.alibaba.chaosbox.common.enums.DeviceType;
import com.alibaba.chaosbox.dao.QueryWrapperBuilder;
import com.alibaba.chaosbox.dao.mapper.DeviceMapper;
import com.alibaba.chaosbox.dao.model.DeviceDO;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

/**
 * @author yefei
 */
@Repository
public class DeviceRepository implements IRepository<Long, DeviceDO> {

    @Autowired
    private DeviceMapper deviceMapper;

    /**
     *  deviceType hostName ip
     * @param deviceType
     * @param hostName
     * @return
     */
    public Optional<DeviceDO> selectOneByUnique(Byte deviceType, String hostName) {
        QueryWrapper<DeviceDO> queryWrapper = QueryWrapperBuilder.build();
        queryWrapper.lambda().eq(DeviceDO::getType, deviceType);
        queryWrapper.lambda().eq(DeviceDO::getHostname, hostName);
        return Optional.ofNullable(deviceMapper.selectOne(queryWrapper));
    }

    public List<DeviceDO> selectMachines(DeviceDO deviceDO) {
        QueryWrapper<DeviceDO> queryWrapper = newQueryWrapper(deviceDO);
        return deviceMapper.selectList(queryWrapper);
    }

    public long selectHostCount(DeviceDO deviceDO) {
        QueryWrapper<DeviceDO> queryWrapper = newQueryWrapper(deviceDO);
        return deviceMapper.selectCount(queryWrapper);
    }

    private QueryWrapper<DeviceDO> newQueryWrapper(DeviceDO deviceDO) {
        QueryWrapper<DeviceDO> queryWrapper = QueryWrapperBuilder.build();
        if (deviceDO.getType() != null) {
            queryWrapper.lambda().eq(DeviceDO::getType, deviceDO.getType());
        }
        if (StrUtil.isNotBlank(deviceDO.getHostname())) {
            queryWrapper.lambda().like(DeviceDO::getHostname, deviceDO.getHostname());
        }
        if (StrUtil.isNotBlank(deviceDO.getIp())) {
            queryWrapper.lambda().like(DeviceDO::getIp, deviceDO.getIp());
        }
        if (deviceDO.getIsExperimented() != null) {
            queryWrapper.lambda().eq(DeviceDO::getIsExperimented, deviceDO.getIsExperimented());
        }
        if (deviceDO.getStatus() != null) {
            queryWrapper.lambda().eq(DeviceDO::getStatus, deviceDO.getStatus());
        }
        return queryWrapper;
    }

    @Override
    public Optional<DeviceDO> selectById(Long aLong) {
        return Optional.ofNullable(deviceMapper.selectById(aLong));
    }

    @Override
    public Long insert(DeviceDO deviceDO) {
        deviceMapper.insert(deviceDO);
        return deviceDO.getId();
    }

    @Override
    public boolean updateByPrimaryKey(Long id, DeviceDO deviceDO) {
        deviceDO.setId(id);
        return deviceMapper.updateById(deviceDO) == 1;
    }

    public List<DeviceDO> selectBatchIds(Collection<? extends Serializable> idList) {
        return deviceMapper.selectBatchIds(idList);
    }

    public Integer selectHostCount() {
        QueryWrapper<DeviceDO> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(DeviceDO::getType, DeviceType.HOST.getCode());
        return deviceMapper.selectCount(queryWrapper);
    }

    public Integer selectHostOnlineCount() {
        QueryWrapper<DeviceDO> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(DeviceDO::getType, DeviceType.HOST.getCode());
        queryWrapper.lambda().eq(DeviceDO::getStatus, DeviceStatus.ONLINE.getStatus());
        return deviceMapper.selectCount(queryWrapper);
    }
}
