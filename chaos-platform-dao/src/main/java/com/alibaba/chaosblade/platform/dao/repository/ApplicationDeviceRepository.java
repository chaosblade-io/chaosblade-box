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
import com.alibaba.chaosblade.platform.dao.mapper.ApplicationDeviceMapper;
import com.alibaba.chaosblade.platform.dao.model.ApplicationDeviceDO;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

/**
 * @author yefei
 */
@Repository
public class ApplicationDeviceRepository implements IRepository<Long, ApplicationDeviceDO> {

    @Autowired
    private ApplicationDeviceMapper applicationDeviceMapper;

    public List<ApplicationDeviceDO> selectList(ApplicationDeviceDO applicationDeviceDO) {
        QueryWrapper<ApplicationDeviceDO> queryWrapper = QueryWrapperBuilder.build();
        if (StrUtil.isNotBlank(applicationDeviceDO.getAppName())) {
            queryWrapper.lambda().like(ApplicationDeviceDO::getAppName, applicationDeviceDO.getGroupName());
        }
        if (StrUtil.isNotBlank(applicationDeviceDO.getGroupName())) {
            queryWrapper.lambda().like(ApplicationDeviceDO::getGroupName, applicationDeviceDO.getGroupName());
        }
        if (applicationDeviceDO.getGroupId() != null) {
            queryWrapper.lambda().eq(ApplicationDeviceDO::getGroupId, applicationDeviceDO.getGroupId());
        }
        return applicationDeviceMapper.selectList(queryWrapper);
    }

    @Override
    public Optional<ApplicationDeviceDO> selectById(Long aLong) {
        return Optional.empty();
    }

    @Override
    public Long insert(ApplicationDeviceDO applicationDeviceDO) {
        applicationDeviceMapper.insert(applicationDeviceDO);
        return applicationDeviceDO.getId();
    }

    @Override
    public boolean updateByPrimaryKey(Long id, ApplicationDeviceDO applicationDeviceDO) {
        return false;
    }

    public List<ApplicationDeviceDO> selectByDeviceIds(Collection<Long> deviceIds) {
        QueryWrapper<ApplicationDeviceDO> queryWrapper = QueryWrapperBuilder.build();
        queryWrapper.lambda().in(ApplicationDeviceDO::getDeviceId, deviceIds);
        return applicationDeviceMapper.selectList(queryWrapper);
    }
}
