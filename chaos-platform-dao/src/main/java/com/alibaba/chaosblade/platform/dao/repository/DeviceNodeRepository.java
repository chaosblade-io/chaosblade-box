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
import com.alibaba.chaosblade.platform.dao.mapper.DeviceNodeMapper;
import com.alibaba.chaosblade.platform.dao.model.DeviceNodeDO;
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
        return Optional.empty();
    }

    @Override
    public Long insert(DeviceNodeDO deviceNodeDO) {
        return 0L;
    }

    @Override
    public boolean updateByPrimaryKey(Long id, DeviceNodeDO deviceNodeDO) {
        return false;
    }

    public long selectCount() {
        return deviceNodeMapper.selectCount(new QueryWrapper<>());
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
