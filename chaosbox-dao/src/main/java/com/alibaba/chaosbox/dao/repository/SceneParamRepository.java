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
import com.alibaba.chaosbox.dao.mapper.SceneParamMapper;
import com.alibaba.chaosbox.dao.model.SceneParamDO;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
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
public class SceneParamRepository extends ServiceImpl<SceneParamMapper, SceneParamDO> implements IRepository<Long, SceneParamDO> {

    @Autowired
    private SceneParamMapper sceneParamMapper;

    public void insertBatch(Collection<SceneParamDO> collection) {
        saveBatch(collection);
    }

    @Override
    public Optional<SceneParamDO> selectById(Long aLong) {
        return Optional.ofNullable(sceneParamMapper.selectById(aLong));
    }

    @Override
    public Long insert(SceneParamDO sceneParamDO) {
        sceneParamMapper.insert(sceneParamDO);
        return sceneParamDO.getId();
    }

    @Override
    public boolean updateByPrimaryKey(Long id, SceneParamDO sceneParamDO) {
        sceneParamDO.setId(id);
        return sceneParamMapper.updateById(sceneParamDO) == 1;
    }

    public List<SceneParamDO> selectList(SceneParamDO sceneParamDO) {
        QueryWrapper<SceneParamDO> queryWrapper = QueryWrapperBuilder.build();
        if (sceneParamDO.getSceneId() != null) {
            queryWrapper.lambda().eq(SceneParamDO::getSceneId, sceneParamDO.getSceneId());
        }
        return sceneParamMapper.selectList(queryWrapper);
    }
}
