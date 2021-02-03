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

package com.alibaba.chaosblade.platform.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.chaosblade.platform.cmmon.constants.ChaosConstant;
import com.alibaba.chaosblade.platform.cmmon.enums.SceneStatus;
import com.alibaba.chaosblade.platform.cmmon.exception.BizException;
import com.alibaba.chaosblade.platform.cmmon.exception.ExceptionMessageEnum;
import com.alibaba.chaosblade.platform.cmmon.utils.JsonUtils;
import com.alibaba.chaosblade.platform.dao.model.SceneCategoryDO;
import com.alibaba.chaosblade.platform.dao.model.SceneDO;
import com.alibaba.chaosblade.platform.dao.model.SceneParamDO;
import com.alibaba.chaosblade.platform.dao.page.PageUtils;
import com.alibaba.chaosblade.platform.dao.repository.SceneCategoryRepository;
import com.alibaba.chaosblade.platform.dao.repository.SceneParamRepository;
import com.alibaba.chaosblade.platform.dao.repository.SceneRepository;
import com.alibaba.chaosblade.platform.service.SceneParamService;
import com.alibaba.chaosblade.platform.service.SceneService;
import com.alibaba.chaosblade.platform.service.model.scene.*;
import com.alibaba.chaosblade.platform.service.model.scene.Action;
import com.alibaba.chaosblade.platform.service.model.scene.Scene;
import com.alibaba.chaosblade.platform.service.model.scene.SceneImportRequest;
import com.alibaba.chaosblade.platform.service.model.scene.SceneImportResponse;
import com.fasterxml.jackson.core.type.TypeReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.yaml.snakeyaml.Yaml;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author yefei
 */
@Service
public class SceneServiceImpl implements SceneService {

    @Autowired
    private SceneRepository sceneRepository;

    @Autowired
    private SceneParamRepository sceneParamRepository;

    @Autowired
    private SceneCategoryRepository sceneCategoryRepository;

    @Autowired
    private SceneParamService sceneParamService;

    @Override
    @Transactional
    public SceneImportResponse importScenarios(SceneImportRequest sceneImportRequest) {
        String version = sceneImportRequest.getVersion();
        String source = sceneImportRequest.getName();

        long count = 0L;

        for (Scene scenario : sceneImportRequest.getScenarios()) {
            if (CollUtil.isEmpty(scenario.getActions())) {
                continue;
            }

            Long prepareId = null;
            if (scenario.getPrepare() != null) {
                Prepare prepare = scenario.getPrepare();
                String sconeCode = StrUtil.builder(source,
                        ChaosConstant.DOT,
                        ChaosConstant.PHASE_PREPARE.toLowerCase(),
                        ChaosConstant.DOT,
                        prepare.getType()).toString();

                prepareId = sceneRepository.selectByCodeAndVersion(sconeCode, version).map(SceneDO::getId)
                        .orElseGet(() -> {
                            SceneDO sceneDO = SceneDO.builder()
                                    .sceneName(sconeCode)
                                    .sceneCode(sconeCode)
                                    .version(version)
                                    .original(source)
                                    .build();
                            sceneRepository.insert(sceneDO);
                            return sceneDO.getId();
                        });

                if (CollUtil.isNotEmpty(prepare.getFlags())) {
                    Long finalPrepareId = prepareId;
                    prepare.getFlags().forEach(flag -> {

                        sceneParamRepository.insert(SceneParamDO.builder()
                                .sceneId(finalPrepareId)
                                .alias(flag.getName())
                                .paramName(flag.getName())
                                .description(flag.getDesc())
                                .isRequired(flag.isRequired())
                                .build());
                    });
                }
            }

            for (Action action : scenario.getActions()) {
                String sconeCode;
                if ("host".equals(scenario.getScope())) {
                    sconeCode = StrUtil.builder(source, ChaosConstant.DOT, scenario.getTarget(), ChaosConstant.DOT, action.getName()).toString();
                } else {
                    sconeCode = StrUtil.builder(source, ChaosConstant.DOT,
                            scenario.getScope(),
                            "-",
                            scenario.getTarget(),
                            ChaosConstant.DOT, action.getName()).toString();
                }

                if (sceneRepository.selectByCodeAndVersion(sconeCode, version).isPresent()) {
                    throw new BizException(ExceptionMessageEnum.SCENE_EXISTS, sconeCode + ":" + version);
                }

                SceneDO sceneDO = SceneDO.builder()
                        .sceneName(sconeCode)
                        .sceneCode(sconeCode)
                        .description(action.getDesc())
                        .preSceneId(prepareId)
                        .version(version)
                        .original(source)
                        .build();
                sceneRepository.insert(sceneDO);
                count++;

                if (CollUtil.isNotEmpty(action.getMatchers())) {
                    action.getMatchers().forEach(matcher -> {

                        sceneParamRepository.insert(SceneParamDO.builder()
                                .sceneId(sceneDO.getId())
                                .alias(matcher.getName())
                                .paramName(matcher.getName())
                                .description(matcher.getDesc())
                                .isRequired(matcher.isRequired())
                                .build());
                    });
                }
                if (CollUtil.isNotEmpty(action.getFlags())) {
                    action.getFlags().forEach(flag -> {

                        sceneParamRepository.insert(SceneParamDO.builder()
                                .sceneId(sceneDO.getId())
                                .alias(flag.getName())
                                .paramName(flag.getName())
                                .description(flag.getDesc())
                                .isRequired(flag.isRequired())
                                .build());
                    });
                }
            }
        }

        return SceneImportResponse.builder().scenarioCount(count).build();
    }

    @Override
    @Transactional
    public void inputScene(InputStream inputStream) {
        Yaml yaml = new Yaml();
        PluginSpecBean pluginSpecBean = yaml.loadAs(inputStream, PluginSpecBean.class);
        Map<String, SceneDO> sceneDOMap = new HashMap<>();
        Map<String, List<SceneParamDO>> map = new HashMap<>();

        List<SceneCategoryDO> sceneCategories = sceneCategoryRepository.selectList(SceneCategoryDO.builder().build());
        sceneCategories.stream().map(sceneCategory -> Optional.ofNullable(null)
                .map(codes -> {
                    try {
                        return JsonUtils.reader(List.class).readValue("null");
                    } catch (IOException e) {
                        return Collections.emptyList();
                    }
                })
                .orElse(Collections.emptyList()).stream())
                .collect(Collectors.toList());

        for (ModelSpecBean item : pluginSpecBean.getItems()) {
            for (ActionSpecBean action : item.getActions()) {
                String sceneCode = ChaosConstant.CHAOS_PREFIX + item.getTarget() + ChaosConstant.DOT + action.getAction();
                sceneDOMap.put(sceneCode, SceneDO.builder()
                        .sceneName(item.getShortDesc())
                        .sceneCode(sceneCode)
                        .description(item.getLongDesc())
                        .version(pluginSpecBean.getVersion())
                        .build());

                List<SceneParamDO> sceneParamDOS;
                if (map.containsKey(sceneCode)) {
                    sceneParamDOS = map.get(sceneCode);
                } else {
                    sceneParamDOS = new ArrayList<>();
                    map.put(sceneCode, sceneParamDOS);
                }
                for (FlagSpecBean flag : action.getFlags()) {
                    sceneParamDOS.add(SceneParamDO.builder()
                            .alias(flag.getName())
                            .paramName(flag.getName())
                            .description(flag.getDesc())
                            .build());
                }
                for (MatcherSpecBean matcher : action.getMatchers()) {
                    sceneParamDOS.add(SceneParamDO.builder()
                            .alias(matcher.getName())
                            .paramName(matcher.getName())
                            .description(matcher.getDesc())
                            .isRequired(matcher.isRequired())
                            .build());
                }
            }
        }

        if (CollectionUtil.isNotEmpty(sceneDOMap)) {
            sceneRepository.insertBatch(sceneDOMap.values());
        }
        for (Map.Entry<String, SceneDO> entry : sceneDOMap.entrySet()) {
            String sceneCode = entry.getKey();
            List<SceneParamDO> sceneParamDOS = map.get(sceneCode);
            for (SceneParamDO sceneParamDO : sceneParamDOS) {
                sceneParamDO.setSceneId(entry.getValue().getId());
            }
            sceneParamRepository.saveBatch(sceneParamDOS);
        }
    }

    @Override
    public List<SceneResponse> getScenariosPageable(SceneRequest sceneRequest) {
        PageUtils.startPage(sceneRequest);
        List<SceneDO> sceneDOS = sceneRepository.selectList(SceneDO.builder()
                .sceneCode(sceneRequest.getCode())
                .categories(sceneRequest.getCategoryId())
                .sceneName(sceneRequest.getName())
                .version(sceneRequest.getVersion())
                .status(sceneRequest.getStatus())
                .supportScope(sceneRequest.getScopeType())
                .build());
        if (CollectionUtil.isEmpty(sceneDOS)) {
            return Collections.emptyList();
        }
        return sceneDOS.stream().map(scene -> SceneResponse.builder()
                .scenarioId(scene.getId())
                .name(scene.getSceneName())
                .code(scene.getSceneCode())
                .description(scene.getDescription())
                .status(scene.getStatus())
                .version(scene.getVersion())
                .supportScopeTypes(Optional.ofNullable(scene.getSupportScope()).map(s -> JsonUtils.readValue(List.class, s)).orElse(Collections.emptyList()))
                .createTime(scene.getGmtCreate())
                .modifyTime(scene.getGmtModified())
                .original(scene.getOriginal())
                .categories(
                        Optional.ofNullable(scene.getCategories()).map(s ->
                                sceneCategoryRepository.selectByIds(JsonUtils.readValue(new TypeReference<List<Long>>() {
                                }, s)).stream().map(sceneCategoryDO ->
                                        SceneCategoryResponse.builder()
                                                .categoryId(sceneCategoryDO.getId())
                                                .categoryName(sceneCategoryDO.getName())
                                                .build()
                                ).collect(Collectors.toList())
                        ).orElse(Collections.emptyList())
                )
                .parameters(
                        sceneParamService.selectSceneParamBySceneId(scene.getId())
                )
                .count(Optional.ofNullable(scene.getUseCount()).orElse(0))
                .build())
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public SceneResponse scenesUpdate(SceneRequest sceneRequest) {

        SceneDO sceneDO = SceneDO.builder()
                .sceneName(sceneRequest.getName())
                .description(sceneRequest.getName())
                .supportScope(sceneRequest.getSupportScopeTypes())
                .categories(sceneRequest.getCategoryIds())
                .build();
        sceneRepository.updateByPrimaryKey(sceneRequest.getScenarioId(), sceneDO);

        List<SceneParamRequest> parameters = sceneRequest.getParameters();
        if (CollUtil.isNotEmpty(parameters)) {
            for (SceneParamRequest parameter : parameters) {
                sceneParamRepository.updateByPrimaryKey(parameter.getParameterId(),
                        SceneParamDO.builder()
                                .paramName(parameter.getParamName())
                                .defaultValue(parameter.getDescription())
                                .component(parameter.getComponent())
                                .build()
                );
            }
        }
        return getScenarioById(sceneRequest);
    }

    @Override
    public SceneResponse getScenarioById(SceneRequest sceneRequest) {
        Optional<SceneDO> sceneDO = sceneRepository.selectById(sceneRequest.getScenarioId());

        return sceneDO.map(scene ->
                SceneResponse.builder()
                        .scenarioId(scene.getId())
                        .original(scene.getOriginal())
                        .name(scene.getSceneName())
                        .code(scene.getSceneCode())
                        .description(scene.getDescription())
                        .status(scene.getStatus())
                        .version(scene.getVersion())
                        .supportScopeTypes(Optional.ofNullable(scene.getSupportScope()).map(s -> JsonUtils.readValue(List.class, s)).orElse(Collections.emptyList()))
                        .createTime(scene.getGmtCreate())
                        .modifyTime(scene.getGmtModified())
                        .categories(
                                Optional.ofNullable(scene.getCategories()).map(s ->
                                        sceneCategoryRepository.selectByIds(JsonUtils.readValue(new TypeReference<List<Long>>() {
                                        }, s)).stream().map(sceneCategoryDO ->
                                                SceneCategoryResponse.builder()
                                                        .categoryId(sceneCategoryDO.getId())
                                                        .categoryName(sceneCategoryDO.getName())
                                                        .build()
                                        ).collect(Collectors.toList())
                                ).orElse(Collections.emptyList())
                        )
                        .requiredJavaAgent(scene.getRequiredJavaAgent())
                        .parameters(
                                sceneParamService.selectSceneParamBySceneId(scene.getId())
                        )
                        .count(Optional.ofNullable(scene.getUseCount()).orElse(0))
                        .preScenarioId(scene.getPreSceneId())
                        .build()
        ).orElseThrow(() -> new BizException(ExceptionMessageEnum.SCENE_IS_NULL));
    }

    @Override
    public SceneResponse banScenario(SceneRequest sceneRequest) {
        sceneRepository.updateByPrimaryKey(sceneRequest.getScenarioId(),
                SceneDO.builder().status(SceneStatus.SOLD_OUT.getCode()).build()
        );
        return getScenarioById(sceneRequest);
    }

    @Override
    public SceneResponse unbanScenario(SceneRequest sceneRequest) {
        sceneRepository.updateByPrimaryKey(sceneRequest.getScenarioId(),
                SceneDO.builder().status(SceneStatus.ACTIVE.getCode()).build()
        );
        return getScenarioById(sceneRequest);
    }
}
