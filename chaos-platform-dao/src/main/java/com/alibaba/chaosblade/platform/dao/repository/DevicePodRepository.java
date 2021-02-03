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

package com.alibaba.chaosblade.platform.dao.repository;

import cn.hutool.core.util.StrUtil;
import com.alibaba.chaosblade.platform.dao.QueryWrapperBuilder;
import com.alibaba.chaosblade.platform.dao.mapper.DevicePodMapper;
import com.alibaba.chaosblade.platform.dao.model.DevicePodDO;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * @author yefei
 */
@Repository
public class DevicePodRepository implements IRepository<Long, DevicePodDO> {

    @Autowired
    private DevicePodMapper devicePodMapper;

    public List<DevicePodDO> selectList(DevicePodDO devicePodDO) {
        QueryWrapper<DevicePodDO> queryWrapper = QueryWrapperBuilder.build();
        if (StrUtil.isNotBlank(devicePodDO.getNamespace())) {
            queryWrapper.lambda().like(DevicePodDO::getNamespace, devicePodDO.getNamespace());
        }
        if (StrUtil.isNotBlank(devicePodDO.getPodName())) {
            queryWrapper.lambda().like(DevicePodDO::getPodName, devicePodDO.getPodName());
        }
        if (StrUtil.isNotBlank(devicePodDO.getPodIp())) {
            queryWrapper.lambda().like(DevicePodDO::getPodIp, devicePodDO.getPodIp());
        }
        return devicePodMapper.selectList(queryWrapper);
    }

    public Optional<DevicePodDO> selectByNameAndNamespace(String namespace, String podName) {
        QueryWrapper<DevicePodDO> queryWrapper = QueryWrapperBuilder.build();
        queryWrapper.lambda().eq(DevicePodDO::getPodName, podName);
        queryWrapper.lambda().eq(DevicePodDO::getNamespace, namespace);
        return Optional.ofNullable(devicePodMapper.selectOne(queryWrapper));
    }

    @Override
    public Optional<DevicePodDO> selectById(Long aLong) {
        return Optional.ofNullable(devicePodMapper.selectById(aLong));
    }

    @Override
    public Long insert(DevicePodDO devicePodDO) {
        devicePodMapper.insert(devicePodDO);
        return devicePodDO.getId();
    }

    @Override
    public boolean updateByPrimaryKey(Long id, DevicePodDO devicePodDO) {
        devicePodDO.setId(id);
        return devicePodMapper.updateById(devicePodDO) == 1;
    }
}
