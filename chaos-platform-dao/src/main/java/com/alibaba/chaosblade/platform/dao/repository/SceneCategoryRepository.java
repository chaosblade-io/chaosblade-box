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
import com.alibaba.chaosblade.platform.dao.mapper.SceneCategoryMapper;
import com.alibaba.chaosblade.platform.dao.model.SceneCategoryDO;
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
public class SceneCategoryRepository implements IRepository<Long, SceneCategoryDO> {

    @Autowired
    private SceneCategoryMapper categoryMapper;

    public List<SceneCategoryDO> selectList(SceneCategoryDO sceneCategoryDO) {
        QueryWrapper<SceneCategoryDO> queryWrapper = QueryWrapperBuilder.build();
        if (StrUtil.isNotBlank(sceneCategoryDO.getName())) {
            queryWrapper.lambda().like(SceneCategoryDO::getName, sceneCategoryDO.getName());
        }
        if (StrUtil.isNotBlank(sceneCategoryDO.getSupportScope())) {
            queryWrapper.lambda().like(SceneCategoryDO::getSupportScope, sceneCategoryDO.getSupportScope());
        }
        return categoryMapper.selectList(queryWrapper);
    }

    @Override
    public Optional<SceneCategoryDO> selectById(Long aLong) {
        return Optional.ofNullable(categoryMapper.selectById(aLong));
    }

    public List<SceneCategoryDO> selectByIds(Collection<Long> ids) {
        return categoryMapper.selectBatchIds(ids);
    }

    @Override
    public Long insert(SceneCategoryDO sceneCategoryDO) {
        categoryMapper.insert(sceneCategoryDO);
        return sceneCategoryDO.getId();
    }

    @Override
    public boolean updateByPrimaryKey(Long id, SceneCategoryDO sceneCategoryDO) {
        sceneCategoryDO.setId(id);
        return categoryMapper.updateById(sceneCategoryDO) == 1;
    }
}
