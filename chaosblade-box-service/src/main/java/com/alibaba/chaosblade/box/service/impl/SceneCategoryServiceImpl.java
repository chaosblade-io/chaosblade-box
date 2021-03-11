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

package com.alibaba.chaosblade.box.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.CollectionUtil;
import com.alibaba.chaosblade.box.service.SceneCategoryService;
import com.alibaba.chaosblade.box.common.utils.JsonUtils;
import com.alibaba.chaosblade.box.dao.mapper.SceneCategoryMapper;
import com.alibaba.chaosblade.box.dao.model.SceneCategoryDO;
import com.alibaba.chaosblade.box.dao.repository.SceneCategoryRepository;
import com.alibaba.chaosblade.box.service.model.scene.categroy.SceneCategoryRequest;
import com.alibaba.chaosblade.box.service.model.scene.categroy.SceneCategoryResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static com.alibaba.chaosblade.box.common.constants.ChaosConstant.LANGUAGE_EN;

/**
 * @author yefei
 */
@Service
public class SceneCategoryServiceImpl implements SceneCategoryService {

    @Autowired
    private SceneCategoryMapper sceneCategoryMapper;

    @Autowired
    private SceneCategoryRepository sceneCategoryRepository;

    @Override
    public void importSceneCategory(List<SceneCategoryResponse> sceneCategoryResponse) {
        insert(sceneCategoryResponse);
    }

    private void insert(List<SceneCategoryResponse> sceneCategoryResponse) {
        for (SceneCategoryResponse categoryResponse : sceneCategoryResponse) {
            sceneCategoryMapper.insert(SceneCategoryDO.builder()
                    .parentId(categoryResponse.getParentId())
                    .name(categoryResponse.getName())
                    .level(categoryResponse.getLevel())
                    .supportScope(JsonUtils.writeValueAsString(categoryResponse.getSupportScopeTypes()))
                    .build());

            if (CollUtil.isNotEmpty(categoryResponse.getChildren())) {
                insert(categoryResponse.getChildren());
            }
        }
    }


    @Override
    public List<SceneCategoryResponse> selectSceneCategory(SceneCategoryRequest sceneCategoryRequest) {
        List<SceneCategoryDO> sceneCategoryDOS = sceneCategoryRepository.selectList(SceneCategoryDO.builder()
                .supportScope(sceneCategoryRequest.getScopeType())
                .build());

        if (CollectionUtil.isEmpty(sceneCategoryDOS)) {
            return Collections.emptyList();
        }
        String language = LocaleContextHolder.getLocale().getLanguage();

        List<SceneCategoryResponse> all = sceneCategoryDOS.stream().map(sceneCategoryDO -> SceneCategoryResponse.builder()
                .categoryId(sceneCategoryDO.getId())
                .parentId(sceneCategoryDO.getParentId())
                .name(LANGUAGE_EN.equals(language) ? sceneCategoryDO.getCategoryCode() : sceneCategoryDO.getName())
                .level(sceneCategoryDO.getLevel())
                .supportScopeTypes(JsonUtils.readValue(List.class, sceneCategoryDO.getSupportScope()))
                .build()
        ).collect(Collectors.toList());

        List<SceneCategoryResponse> parents = all.stream().filter(sceneCategoryDO -> sceneCategoryDO.getParentId() == null)
                .collect(Collectors.toList());

        fill(all, parents);

        return parents;
    }

    private void fill(List<SceneCategoryResponse> list, List<SceneCategoryResponse> parents) {

        List<SceneCategoryResponse> newParents = CollUtil.newArrayList();
        for (SceneCategoryResponse parent : parents) {
            for (SceneCategoryResponse sceneCategoryResponse : list) {
                if (parent.getCategoryId().equals(sceneCategoryResponse.getParentId())) {
                    List<SceneCategoryResponse> children = parent.getChildren();
                    if (children == null) {
                        children = new ArrayList<>();
                        parent.setChildren(children);
                    }
                    children.add(sceneCategoryResponse);
                    newParents.add(sceneCategoryResponse);
                }
            }
        }

        if (CollUtil.isNotEmpty(parents)) {
            fill(list, newParents);
        }
    }
}
