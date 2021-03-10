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

package com.alibaba.chaosblade.box.dao.repository;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.chaosblade.box.dao.QueryWrapperBuilder;
import com.alibaba.chaosblade.box.dao.mapper.DeviceNodeMapper;
import com.alibaba.chaosblade.box.dao.model.DeviceNodeDO;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * @author yefei
 */
@Repository
public class DeviceNodeRepository implements IRepository<Long, DeviceNodeDO> {

    @Autowired
    private DeviceNodeMapper deviceNodeMapper;

    @Override
    public Optional<DeviceNodeDO> selectById(Long aLong) {
        return Optional.ofNullable(deviceNodeMapper.selectById(aLong));
    }

    public Optional<DeviceNodeDO> selectByNodeName(String nodeName) {
        QueryWrapper<DeviceNodeDO> queryWrapper = QueryWrapperBuilder.build();
        queryWrapper.lambda().eq(DeviceNodeDO::getNodeName, nodeName);
        return Optional.ofNullable(deviceNodeMapper.selectOne(queryWrapper));
    }

    @Override
    public Long insert(DeviceNodeDO deviceNodeDO) {
        deviceNodeMapper.insert(deviceNodeDO);
        return deviceNodeDO.getId();
    }

    @Override
    public boolean updateByPrimaryKey(Long id, DeviceNodeDO deviceNodeDO) {
        deviceNodeDO.setId(id);
        return deviceNodeMapper.updateById(deviceNodeDO) == 1;
    }

    public List<DeviceNodeDO> selectList(DeviceNodeDO deviceNodeDO, List<Long> deviceIds) {
        QueryWrapper<DeviceNodeDO> queryWrapper = QueryWrapperBuilder.build();
        if (StrUtil.isNotBlank(deviceNodeDO.getClusterName())) {
            queryWrapper.lambda().like(DeviceNodeDO::getClusterName, deviceNodeDO.getClusterName());
        }
        if (StrUtil.isNotBlank(deviceNodeDO.getNodeName())) {
            queryWrapper.lambda().like(DeviceNodeDO::getNodeName, deviceNodeDO.getNodeName());
        }
        if (CollUtil.isNotEmpty(deviceIds)) {
            queryWrapper.lambda().in(DeviceNodeDO::getDeviceId, deviceIds);
        }
        return deviceNodeMapper.selectList(queryWrapper);
    }


    public List<DeviceNodeDO> selectList(DeviceNodeDO deviceNodeDO) {
        QueryWrapper<DeviceNodeDO> queryWrapper = QueryWrapperBuilder.build();
        if (StrUtil.isNotBlank(deviceNodeDO.getClusterName())) {
            queryWrapper.lambda().like(DeviceNodeDO::getClusterName, deviceNodeDO.getClusterName());
        }
        if (StrUtil.isNotBlank(deviceNodeDO.getNodeName())) {
            queryWrapper.lambda().like(DeviceNodeDO::getNodeName, deviceNodeDO.getNodeName());
        }
        return deviceNodeMapper.selectList(queryWrapper);
    }
}
