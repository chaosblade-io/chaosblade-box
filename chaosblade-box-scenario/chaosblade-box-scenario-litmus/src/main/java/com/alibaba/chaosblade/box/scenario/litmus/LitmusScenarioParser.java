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

package com.alibaba.chaosblade.box.scenario.litmus;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.io.IoUtil;
import cn.hutool.core.io.resource.ResourceUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.http.HttpUtil;
import com.alibaba.chaosblade.box.common.constants.ChaosConstant;
import com.alibaba.chaosblade.box.common.enums.ChaosTools;
import com.alibaba.chaosblade.box.common.model.chaos.ActionSpecBean;
import com.alibaba.chaosblade.box.common.model.chaos.FlagSpecBean;
import com.alibaba.chaosblade.box.common.model.chaos.ModelSpecBean;
import com.alibaba.chaosblade.box.common.model.chaos.PluginSpecBean;
import com.alibaba.chaosblade.box.common.utils.SystemPropertiesUtils;
import com.alibaba.chaosblade.box.scenario.api.Original;
import com.alibaba.chaosblade.box.scenario.api.ScenarioParser;
import com.alibaba.chaosblade.box.scenario.api.ScenarioRequest;
import com.alibaba.chaosblade.box.scenario.api.model.ScenarioOriginal;
import com.alibaba.chaosblade.box.scenario.litmus.model.ChaosExperiment;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.representer.Representer;

import java.io.InputStream;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author yefei
 */
@Slf4j
@Original(ChaosTools.LITMUS_CHAOS)
@Component
@ConfigurationProperties(prefix = "chaos.scene.originals")
public class LitmusScenarioParser implements ScenarioParser {

    private final static List<FlagSpecBean> GLOBAL_FLAG = CollUtil.newArrayList(
            FlagSpecBean.builder()
                    .name("appns")
                    .defaultValue("default")
                    .build(),
            FlagSpecBean.builder()
                    .name("applabel")
                    .build(),
            FlagSpecBean.builder()
                    .name("appkind")
                    .build()
    );

    public void setLitmus(List<ScenarioOriginal> litmus) {
        this.litmus = litmus;
    }

    private List<ScenarioOriginal> litmus;

    @Override
    public List<PluginSpecBean> parse(ScenarioRequest scenarioRequest) {

        return litmus.stream().map(
                litmus -> {
                    log.info("parse scenario yaml, name: {}, url: {}", litmus.getName(), litmus.getUrl());
                    HttpRequest request = HttpUtil.createGet(litmus.getUrl());
                    HttpResponse execute = request.execute();
                    String s = new String(execute.bodyBytes());

                    List<ModelSpecBean> modelSpecBeans = Arrays.stream(StrUtil.split(s, "---"))
                            .filter(StrUtil::isNotBlank)
                            .map(experiment -> {
                                experiment = experiment.trim();
                                Representer representer = new Representer();
                                representer.getPropertyUtils().setSkipMissingProperties(true);
                                Yaml yaml = new Yaml(representer);
                                ChaosExperiment chaosExperiment = yaml.loadAs(experiment, ChaosExperiment.class);

                                String[] split = StrUtil.split(chaosExperiment.getMetadata().getName(), "-");
                                String scope = split[0];
                                String target;
                                String action;
                                if (split.length == 2) {
                                    target = split[0];
                                    action = split[1];
                                } else if (split.length == 3) {
                                    target = split[1];
                                    action = split[2];
                                } else {
                                    return null;
                                }

                                InputStream stream = ResourceUtil.getStream(String.format("litmuschaos/%s/category.yaml", litmus.getVersion()));


                                String sceneCode = StrUtil.builder(scenarioRequest.getOriginal(), ChaosConstant.DOT,
                                        scope,
                                        "-",
                                        target,
                                        ChaosConstant.DOT, action).toString();


                                Map<String, List<String>> categories = yaml.load(IoUtil.read(stream, SystemPropertiesUtils.getPropertiesFileEncoding()));

                                return ModelSpecBean.builder()
                                        .longDesc(chaosExperiment.getDescription().getMessage())
                                        .target(target)
                                        .actions(CollUtil.newArrayList(ActionSpecBean.builder()
                                                .action(action)
                                                .categories(categories.get(sceneCode) == null ? null : categories.get(sceneCode).toArray(new String[0]))
                                                .flags(Stream.of(GLOBAL_FLAG.stream(),
                                                                Arrays.stream(chaosExperiment.getSpec().getDefinition().getEnv())
                                                                        .map(flag ->
                                                                                FlagSpecBean.builder()
                                                                                        .name(flag.getName())
                                                                                        .defaultValue(flag.getValue())
                                                                                        .build()
                                                                        )).flatMap(x -> x).collect(Collectors.toList()))
                                                .build()))
                                        .scope(scope)
                                        .build();
                            }).collect(Collectors.toList());

                    return PluginSpecBean.builder()
                            .kind(ChaosTools.LITMUS_CHAOS.getName())
                            .type(litmus.getName())
                            .version(litmus.getVersion())
                            .items(modelSpecBeans).build();
                }
        ).collect(Collectors.toList());
    }

}
