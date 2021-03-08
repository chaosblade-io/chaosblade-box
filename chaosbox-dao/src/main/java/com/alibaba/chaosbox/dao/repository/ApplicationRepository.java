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
import com.alibaba.chaosbox.dao.QueryWrapperBuilder;
import com.alibaba.chaosbox.dao.mapper.ApplicationMapper;
import com.alibaba.chaosbox.dao.model.ApplicationDO;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * @author yefei
 */
@Repository
public class ApplicationRepository implements IRepository<Long, ApplicationDO> {

    @Autowired
    private ApplicationMapper applicationMapper;

    public Optional<ApplicationDO> selectOneByNamespaceAndAppName(String namespace, String appName) {
        QueryWrapper<ApplicationDO> queryWrapper = QueryWrapperBuilder.build();
        queryWrapper.lambda().eq(ApplicationDO::getNamespace, namespace);
        queryWrapper.lambda().eq(ApplicationDO::getAppName, appName);
        return Optional.ofNullable(applicationMapper.selectOne(queryWrapper));
    }

    public List<ApplicationDO> selectList(ApplicationDO applicationDO) {
        QueryWrapper<ApplicationDO> queryWrapper = QueryWrapperBuilder.build();
        if (StrUtil.isNotBlank(applicationDO.getNamespace())) {
            queryWrapper.lambda().like(ApplicationDO::getNamespace, applicationDO.getNamespace());
        }
        if (StrUtil.isNotBlank(applicationDO.getAppName())) {
            queryWrapper.lambda().like(ApplicationDO::getAppName, applicationDO.getAppName());
        }
        return applicationMapper.selectList(queryWrapper);
    }

    @Override
    public Optional<ApplicationDO> selectById(Long aLong) {
        return Optional.empty();
    }

    @Override
    public Long insert(ApplicationDO applicationDO) {
        applicationMapper.insert(applicationDO);
        return applicationDO.getId();
    }

    @Override
    public boolean updateByPrimaryKey(Long id, ApplicationDO applicationDO) {
        return false;
    }

    public long selectCount() {
        return applicationMapper.selectCount(new QueryWrapper<>());
    }
}
