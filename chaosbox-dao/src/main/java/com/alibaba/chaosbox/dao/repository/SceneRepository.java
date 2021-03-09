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
import com.alibaba.chaosbox.dao.mapper.SceneMapper;
import com.alibaba.chaosbox.dao.model.SceneDO;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

/**
 * @author yefei
 */
@Component
public class SceneRepository extends ServiceImpl<SceneMapper, SceneDO> implements IRepository<Long, SceneDO> {

    @Autowired
    private SceneMapper sceneMapper;

    public List<SceneDO> selectList(SceneDO sceneDO) {
        QueryWrapper<SceneDO> queryWrapper = QueryWrapperBuilder.build();
        if (StrUtil.isNotBlank(sceneDO.getCategories())) {
            queryWrapper.lambda().like(SceneDO::getCategories, sceneDO.getCategories());
        }
        if (StrUtil.isNotBlank(sceneDO.getSceneName())) {
            queryWrapper.lambda().like(SceneDO::getSceneName, sceneDO.getSceneName());
        }
        if (StrUtil.isNotBlank(sceneDO.getVersion())) {
            queryWrapper.lambda().eq(SceneDO::getVersion, sceneDO.getVersion());
        }
        if (StrUtil.isNotBlank(sceneDO.getOriginal())) {
            queryWrapper.lambda().eq(SceneDO::getOriginal, sceneDO.getOriginal());
        }
        if (StrUtil.isNotBlank(sceneDO.getSupportScope())) {
            queryWrapper.lambda().like(SceneDO::getSupportScope, sceneDO.getSupportScope());
        }
        if (StrUtil.isNotBlank(sceneDO.getSceneCode())) {
            queryWrapper.lambda().like(SceneDO::getSceneCode, sceneDO.getSceneCode());
        }
        if (sceneDO.getStatus() != null) {
            queryWrapper.lambda().eq(SceneDO::getStatus, sceneDO.getStatus());
        }
        return sceneMapper.selectList(queryWrapper);
    }

    public Optional<SceneDO> selectByCodeAndVersion(String code, String version) {
        QueryWrapper<SceneDO> queryWrapper = QueryWrapperBuilder.build();
        queryWrapper.lambda().eq(SceneDO::getVersion, version);
        queryWrapper.lambda().like(SceneDO::getSceneCode, code);
        return Optional.ofNullable(sceneMapper.selectOne(queryWrapper));
    }

    public void insertBatch(Collection<SceneDO> collection) {
        saveBatch(collection);
    }

    @Override
    public Optional<SceneDO> selectById(Long aLong) {
        return Optional.ofNullable(sceneMapper.selectById(aLong));
    }

    @Override
    public Long insert(SceneDO sceneDO) {
        sceneMapper.insert(sceneDO);
        return sceneDO.getId();
    }

    @Override
    public boolean updateByPrimaryKey(Long id, SceneDO sceneDO) {
        sceneDO.setId(id);
        return sceneMapper.updateById(sceneDO) == 1;
    }

    public void incrementUseCount(Long sceneId) {
        UpdateWrapper<SceneDO> updateWrapper = new UpdateWrapper<>();
        updateWrapper.lambda().eq(SceneDO::getId, sceneId)
                .setSql(true, "use_count = ifnull(use_count, 0) + 1");
        sceneMapper.update(null, updateWrapper);
    }
}
