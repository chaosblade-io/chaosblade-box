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

package com.alibaba.chaosbox.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import com.alibaba.chaosbox.common.utils.JsonUtils;
import com.alibaba.chaosbox.dao.model.SceneParamDO;
import com.alibaba.chaosbox.dao.repository.SceneParamRepository;
import com.alibaba.chaosbox.service.SceneParamService;
import com.alibaba.chaosbox.service.model.scene.param.Component;
import com.alibaba.chaosbox.service.model.scene.param.SceneParamRequest;
import com.alibaba.chaosbox.service.model.scene.param.SceneParamResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author yefei
 */
@Service
public class SceneParamServiceImpl implements SceneParamService {

    @Autowired
    private SceneParamRepository sceneParamRepository;

    @Override
    public List<SceneParamResponse> selectSceneParam(SceneParamRequest sceneParamRequest) {

        List<SceneParamDO> sceneParamDOS = sceneParamRepository.selectList(SceneParamDO.builder()
                .sceneId(sceneParamRequest.getSceneId())
                .build());

        if (CollectionUtil.isEmpty(sceneParamDOS)) {
            return Collections.emptyList();
        }
        return sceneParamDOS.stream().map(sceneParamDO -> SceneParamResponse.builder()
                .parameterId(sceneParamDO.getSceneId())
                .alias(sceneParamDO.getAlias())
                .paramName(sceneParamDO.getParamName())
                .defaultValue(sceneParamDO.getDefaultValue())
                .description(sceneParamDO.getDescription())
                .build()
        ).collect(Collectors.toList());
    }

    @Override
    public List<SceneParamResponse> selectSceneParamBySceneId(Long sceneId) {

        List<SceneParamDO> sceneParamDOS = sceneParamRepository.selectList(SceneParamDO.builder()
                .sceneId(sceneId)
                .build());

        if (CollectionUtil.isEmpty(sceneParamDOS)) {
            return Collections.emptyList();
        }
        return sceneParamDOS.stream().map(sceneParamDO -> SceneParamResponse.builder()
                .parameterId(sceneParamDO.getSceneId())
                .alias(sceneParamDO.getAlias())
                .paramName(sceneParamDO.getParamName())
                .name(sceneParamDO.getParamName())
                .defaultValue(sceneParamDO.getDefaultValue())
                .description(sceneParamDO.getDescription())
                .required(sceneParamDO.getIsRequired())
                .component(Optional.ofNullable(sceneParamDO.getComponent())
                        .map(component -> JsonUtils.readValue(Component.class, component))
                        .orElse(null))
                .build()
        ).collect(Collectors.toList());
    }
}
