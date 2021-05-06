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
import cn.hutool.core.io.IoUtil;
import cn.hutool.core.util.EnumUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.chaosblade.box.common.constants.ChaosConstant;
import com.alibaba.chaosblade.box.common.enums.ChaosTools;
import com.alibaba.chaosblade.box.common.enums.ExperimentDimension;
import com.alibaba.chaosblade.box.common.enums.SceneStatus;
import com.alibaba.chaosblade.box.common.exception.BizException;
import com.alibaba.chaosblade.box.common.exception.ExceptionMessageEnum;
import com.alibaba.chaosblade.box.common.model.chaos.PluginSpecBean;
import com.alibaba.chaosblade.box.common.utils.JsonUtils;
import com.alibaba.chaosblade.box.dao.model.SceneCategoryDO;
import com.alibaba.chaosblade.box.dao.model.SceneDO;
import com.alibaba.chaosblade.box.dao.model.SceneParamDO;
import com.alibaba.chaosblade.box.dao.page.PageUtils;
import com.alibaba.chaosblade.box.dao.repository.SceneCategoryRepository;
import com.alibaba.chaosblade.box.dao.repository.SceneParamRepository;
import com.alibaba.chaosblade.box.dao.repository.SceneRepository;
import com.alibaba.chaosblade.box.scenario.api.model.ToolsOverview;
import com.alibaba.chaosblade.box.scenario.api.model.ToolsVersion;
import com.alibaba.chaosblade.box.service.SceneParamService;
import com.alibaba.chaosblade.box.service.SceneService;
import com.alibaba.chaosblade.box.service.ToolsService;
import com.alibaba.chaosblade.box.service.model.device.KubernetesDevice;
import com.alibaba.chaosblade.box.service.model.scene.*;
import com.alibaba.chaosblade.box.service.model.scene.param.Component;
import com.alibaba.chaosblade.box.service.model.scene.param.SceneParamRequest;
import com.alibaba.chaosblade.box.service.model.scene.param.SceneParamResponse;
import com.fasterxml.jackson.core.type.TypeReference;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.representer.Representer;

import java.nio.charset.Charset;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author yefei
 */
@Slf4j
@Service
@DependsOn("sceneCategoryLoader")
public class SceneServiceImpl implements SceneService, InitializingBean {

    @Autowired
    private SceneRepository sceneRepository;

    @Autowired
    private SceneParamRepository sceneParamRepository;

    @Autowired
    private SceneCategoryRepository sceneCategoryRepository;

    @Autowired
    private SceneParamService sceneParamService;

    @Autowired
    private ToolsService toolsService;

    @Value("${chaos.scene.import}")
    private boolean autoImport;

    @Override
    public void afterPropertiesSet() {
        if (autoImport) {
            for (ChaosTools value : ChaosTools.values()) {
                covert(value.getName());
            }
        }
    }

    private List<Scene> pluginSpecBeanToSpec(PluginSpecBean pluginSpecBean) {
        return pluginSpecBean.getItems().stream().map(
                item -> Scene.builder().actions(
                        item.getActions().stream().map(
                                actionSpecBean -> Action.builder()
                                        .name(actionSpecBean.getAction())
                                        .desc(actionSpecBean.getLongDesc())
                                        .flags(actionSpecBean.getFlags() == null ? CollUtil.newArrayList() : actionSpecBean.getFlags().stream().map(
                                                flagSpecBean -> Flag.builder()
                                                        .name(flagSpecBean.getName())
                                                        .desc(flagSpecBean.getDesc())
                                                        .defaultValue(flagSpecBean.isNoArgs() ? "true" : null)
                                                        .required(flagSpecBean.isRequired())
                                                        .build()
                                        ).collect(Collectors.toList()))
                                        .matchers(actionSpecBean.getMatchers() == null ? CollUtil.newArrayList() : actionSpecBean.getMatchers().stream().map(
                                                matcherSpecBean -> Matcher.builder()
                                                        .name(matcherSpecBean.getName())
                                                        .desc(matcherSpecBean.getDesc())
                                                        .defaultValue(matcherSpecBean.isNoArgs() ? "true" : null)
                                                        .required(matcherSpecBean.isRequired())
                                                        .build()
                                        ).collect(Collectors.toList()))
                                        .categories(CollUtil.newArrayList(actionSpecBean.getCategories()))
                                        .build()
                        ).collect(Collectors.toList())
                ).prepare(
                        item.getPrepare() == null ? null :
                                Prepare.builder().required(item.getPrepare().isRequired())
                                        .flags(item.getPrepare().getFlags().stream().map(
                                                flagSpecBean -> Flag.builder()
                                                        .name(flagSpecBean.getName())
                                                        .desc(flagSpecBean.getDesc())
                                                        .defaultValue(flagSpecBean.isNoArgs() ? "true" : null)
                                                        .required(flagSpecBean.isRequired())
                                                        .build()
                                        ).collect(Collectors.toList()))
                                        .type(item.getPrepare().getType())
                                        .build()
                ).target(item.getTarget())
                        .scope(item.getScope())
                        .build()
        ).collect(Collectors.toList());
    }


    private void covert(String origin) {
        ToolsOverview toolsOverview = toolsService.toolsOverview(origin);
        ToolsVersion toolsVersion = toolsService.toolsVersion(toolsOverview.getName(), toolsOverview.getLatest());
        List<String> scenarioFiles = toolsVersion.getScenarioFiles();
        for (String scenarioFile : scenarioFiles) {
            PluginSpecBean pluginSpecBean = toolsService.toolsScene(toolsOverview.getName(), toolsOverview.getLatest(), scenarioFile);
            List<Scene> scenes = pluginSpecBeanToSpec(pluginSpecBean);
            try {
                importScenarios(SceneImportRequest.builder()
                        .name(toolsOverview.getName())
                        .version(toolsOverview.getLatest())
                        .active(true)
                        .scenarios(scenes).build());
            } catch (BizException e) {
                // ignore
            }
        }
    }

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

                sceneRepository.selectByCodeAndVersion(sconeCode, version).map(SceneDO::getId)
                        .orElseGet(() -> {
                            SceneDO sceneDO = SceneDO.builder()
                                    .sceneName(sconeCode)
                                    .sceneCode(sconeCode)
                                    .version(version)
                                    .original(source)
                                    .build();
                            sceneRepository.insert(sceneDO);

                            if (CollUtil.isNotEmpty(prepare.getFlags())) {
                                prepare.getFlags().forEach(flag ->
                                        sceneParamRepository.insert(SceneParamDO.builder()
                                                .sceneId(sceneDO.getId())
                                                .alias(flag.getName())
                                                .paramName(flag.getName())
                                                .description(flag.getDesc())
                                                .isRequired(flag.isRequired())
                                                .build()));
                            }
                            return sceneDO.getId();
                        });
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
                        .supportScope("[\"" + scenario.getScope() + "\"]")
                        .original(source)
                        .build();

                if (sceneImportRequest.isActive()) {
                    sceneDO.setStatus(SceneStatus.ACTIVE.getCode());
                }
                if (CollUtil.isNotEmpty(action.getCategories())) {
                    List<Long> categories = sceneCategoryRepository.selectListByCodes(action.getCategories()).stream()
                            .map(SceneCategoryDO::getId).collect(Collectors.toList());
                    sceneDO.setCategories(JsonUtils.writeValueAsString(categories));
                }
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
                        sceneParamService.selectSceneParamBySceneId(scene.getId()).stream().map(
                                sceneParamResponse -> sceneParamCover(sceneParamResponse, sceneRequest)
                        ).collect(Collectors.toList())
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
                                .component(JsonUtils.writeValueAsString(parameter.getComponent()))
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
                        .supportScopeTypes(Optional.ofNullable(scene.getSupportScope()).map(s -> JsonUtils.readValue(List.class, s))
                                .orElse(Collections.emptyList()))
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
                                sceneParamService.selectSceneParamBySceneId(scene.getId()).stream().map(
                                        sceneParamResponse -> sceneParamCover(sceneParamResponse, sceneRequest)
                                ).collect(Collectors.toList())
                        )
                        .count(Optional.ofNullable(scene.getUseCount()).orElse(0))
                        .preScenarioId(scene.getPreSceneId())
                        .build()
        ).orElseThrow(() -> new BizException(ExceptionMessageEnum.SCENE_IS_NULL));
    }

    // todo
    private SceneParamResponse sceneParamCover(SceneParamResponse sceneParamResponse, SceneRequest sceneRequest) {
        if (StrUtil.isBlank(sceneRequest.getDimension())) {
            return sceneParamResponse;
        }
        ExperimentDimension dimension = EnumUtil.fromString(ExperimentDimension.class, sceneRequest.getDimension().toUpperCase());
        switch (dimension) {
            case CONTAINER:
                if ("container-names".equals(sceneParamResponse.getName())) {
                    sceneParamResponse.setValue(sceneRequest.getMachines().stream().map(KubernetesDevice::getContainerName).distinct()
                            .collect(Collectors.joining(",")));
                    if (sceneParamResponse.getComponent() == null) {
                        sceneParamResponse.setComponent(Component.builder().editable(false).build());
                    } else {
                        sceneParamResponse.getComponent().setEditable(false);
                    }
                }
                if ("TARGET_CONTAINER".equals(sceneParamResponse.getName())) {
                    sceneParamResponse.setValue(sceneRequest.getMachines().stream().map(KubernetesDevice::getContainerName).distinct()
                            .collect(Collectors.joining(",")));
                    if (sceneParamResponse.getComponent() == null) {
                        sceneParamResponse.setComponent(Component.builder().editable(false).build());
                    } else {
                        sceneParamResponse.getComponent().setEditable(false);
                    }
                }
            case POD:
                if ("namespace".equals(sceneParamResponse.getName())) {
                    sceneParamResponse.setValue(sceneRequest.getMachines().stream().map(KubernetesDevice::getNamespace).findFirst().get());
                    if (sceneParamResponse.getComponent() == null) {
                        sceneParamResponse.setComponent(Component.builder().editable(false).build());
                    } else {
                        sceneParamResponse.getComponent().setEditable(false);
                    }
                }
                if ("appns".equals(sceneParamResponse.getName())) {
                    sceneParamResponse.setValue(sceneRequest.getMachines().stream().map(KubernetesDevice::getNamespace).findFirst().get());
                    if (sceneParamResponse.getComponent() == null) {
                        sceneParamResponse.setComponent(Component.builder().editable(false).build());
                    } else {
                        sceneParamResponse.getComponent().setEditable(false);
                    }
                }
                if ("names".equals(sceneParamResponse.getName())) {
                    sceneParamResponse.setValue(sceneRequest.getMachines().stream().map(KubernetesDevice::getPodName)
                            .collect(Collectors.joining(",")));
                    if (sceneParamResponse.getComponent() == null) {
                        sceneParamResponse.setComponent(Component.builder().editable(false).build());
                    } else {
                        sceneParamResponse.getComponent().setEditable(false);
                    }
                }
                if ("TARGET_PODS".equals(sceneParamResponse.getName())) {
                    sceneParamResponse.setValue(sceneRequest.getMachines().stream().map(KubernetesDevice::getPodName)
                            .collect(Collectors.joining(",")));
                    if (sceneParamResponse.getComponent() == null) {
                        sceneParamResponse.setComponent(Component.builder().editable(false).build());
                    } else {
                        sceneParamResponse.getComponent().setEditable(false);
                    }
                }
                break;
            case NODE:
                if ("names".equals(sceneParamResponse.getName())) {
                    sceneParamResponse.setValue(sceneRequest.getMachines().stream().map(KubernetesDevice::getNodeName)
                            .collect(Collectors.joining(",")));
                    if (sceneParamResponse.getComponent() == null) {
                        sceneParamResponse.setComponent(Component.builder().editable(false).build());
                    } else {
                        sceneParamResponse.getComponent().setEditable(false);
                    }
                }
                if ("TARGET_NODES".equals(sceneParamResponse.getName())) {
                    sceneParamResponse.setValue(sceneRequest.getMachines().stream().map(KubernetesDevice::getNodeName)
                            .collect(Collectors.joining(",")));
                    if (sceneParamResponse.getComponent() == null) {
                        sceneParamResponse.setComponent(Component.builder().editable(false).build());
                    } else {
                        sceneParamResponse.getComponent().setEditable(false);
                    }
                }
                break;
        }
        return sceneParamResponse;
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

    @Override
    public SceneImportResponse uploadScenarios(SceneImportRequest sceneImportRequest) throws Exception {
        long count = 0;
        for (MultipartFile file : sceneImportRequest.getScenarioFiles()) {
            String name = file.getOriginalFilename();
            String[] split = name.split("-");
            if (split.length != 4 || !name.endsWith(".yaml")) {
                throw new BizException(ExceptionMessageEnum.SCENE_FILE_NAME_PARSE_FAIL);
            }
            Representer representer = new Representer();
            representer.getPropertyUtils().setSkipMissingProperties(true);
            Yaml yaml = new Yaml(representer);
            PluginSpecBean pluginSpecBean = yaml.loadAs(IoUtil.read(file.getInputStream(), Charset.defaultCharset()), PluginSpecBean.class);

            List<Scene> scenes = pluginSpecBeanToSpec(pluginSpecBean);

            SceneImportResponse sceneImportResponse = importScenarios(SceneImportRequest.builder()
                    .name(split[0])
                    .version(split[3].replace(".yaml", ""))
                    .active(true)
                    .scenarios(scenes).build());
            count = count + sceneImportResponse.getScenarioCount();
        }
        return SceneImportResponse.builder().scenarioCount(count).build();
    }
}
