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

import cn.hutool.core.util.StrUtil;
import com.alibaba.chaosblade.box.dao.QueryWrapperBuilder;
import com.alibaba.chaosblade.box.dao.model.ApplicationGroupDO;
import com.alibaba.chaosblade.box.dao.mapper.ApplicationGroupMapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * @author yefei
 */
@Repository
public class ApplicationGroupRepository implements IRepository<Long, ApplicationGroupDO> {

    @Autowired
    private ApplicationGroupMapper applicationGroupMapper;

    public Optional<ApplicationGroupDO> selectOneByAppIdAndGroupName(Long appId, String groupName) {
        QueryWrapper<ApplicationGroupDO> queryWrapper = QueryWrapperBuilder.build();
        queryWrapper.lambda().eq(ApplicationGroupDO::getAppId, appId);
        queryWrapper.lambda().eq(ApplicationGroupDO::getGroupName, groupName);
        return Optional.ofNullable(applicationGroupMapper.selectOne(queryWrapper));
    }

    public List<ApplicationGroupDO> selectList(ApplicationGroupDO applicationGroupDO) {
        QueryWrapper<ApplicationGroupDO> queryWrapper = new QueryWrapper();
        if (applicationGroupDO.getAppId() != null) {
            queryWrapper.lambda().eq(ApplicationGroupDO::getAppId, applicationGroupDO.getAppId());
        }
        if (StrUtil.isNotBlank(applicationGroupDO.getGroupName())) {
            queryWrapper.lambda().like(ApplicationGroupDO::getGroupName, applicationGroupDO.getGroupName());
        }
        return applicationGroupMapper.selectList(queryWrapper);
    }

    @Override
    public Optional<ApplicationGroupDO> selectById(Long aLong) {
        return Optional.empty();
    }

    @Override
    public Long insert(ApplicationGroupDO applicationGroupDO) {
        applicationGroupMapper.insert(applicationGroupDO);
        return applicationGroupDO.getId();
    }

    @Override
    public boolean updateByPrimaryKey(Long id, ApplicationGroupDO applicationGroupDO) {
        return false;
    }

    public long selectCount() {
        return applicationGroupMapper.selectCount(new QueryWrapper<>());
    }
}
