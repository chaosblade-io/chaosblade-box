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

import com.alibaba.chaosbox.dao.QueryWrapperBuilder;
import com.alibaba.chaosbox.dao.mapper.ToolsMapper;
import com.alibaba.chaosbox.dao.model.ToolsDO;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * @author yefei
 */
@Repository
public class ToolsRepository implements IRepository<Long, ToolsDO> {

    @Autowired
    private ToolsMapper toolsMapper;

    @Override
    public Optional<ToolsDO> selectById(Long aLong) {
        return Optional.ofNullable(toolsMapper.selectById(aLong));
    }

    @Override
    public Long insert(ToolsDO toolsDO) {
        toolsMapper.insert(toolsDO);
        return toolsDO.getId();
    }

    @Override
    public boolean updateByPrimaryKey(Long id, ToolsDO toolsDO) {
        toolsDO.setId(id);
        return toolsMapper.updateById(toolsDO) == 1;
    }

    public List<ToolsDO> selectByDeviceId(Long deviceId) {
        QueryWrapper<ToolsDO> queryWrapper = QueryWrapperBuilder.build();
        queryWrapper.lambda().eq(ToolsDO::getDeviceId, deviceId);
        return toolsMapper.selectList(queryWrapper);
    }

    public Integer selectCountOfHost() {
        QueryWrapper<ToolsDO> queryWrapper = QueryWrapperBuilder.build();
        queryWrapper.lambda().eq(ToolsDO::getDeviceType, 0);
        return toolsMapper.selectCount(queryWrapper);
    }

    public Integer selectCountOfKubernetes() {
        QueryWrapper<ToolsDO> queryWrapper = QueryWrapperBuilder.build();
        queryWrapper.lambda().ne(ToolsDO::getDeviceType, 0);
        return toolsMapper.selectCount(queryWrapper);
    }

    public void deleteByMachineIdAndName(Long machineId, String name) {
        QueryWrapper<ToolsDO> queryWrapper = QueryWrapperBuilder.build();
        queryWrapper.lambda().eq(ToolsDO::getDeviceId, machineId);
        queryWrapper.lambda().eq(ToolsDO::getName, name);
        toolsMapper.delete(queryWrapper);
    }

    public ToolsDO selectByNameAndVersion(Long deviceId, String name, String version) {
        QueryWrapper<ToolsDO> queryWrapper = QueryWrapperBuilder.build();
        queryWrapper.lambda().eq(ToolsDO::getDeviceId, deviceId);
        queryWrapper.lambda().eq(ToolsDO::getName, name);
        queryWrapper.lambda().eq(ToolsDO::getVersion, version);
        return toolsMapper.selectOne(queryWrapper);
    }
}

