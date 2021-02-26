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

package com.alibaba.chaosblade.platform.scenario.api;

import cn.hutool.core.io.IoUtil;
import cn.hutool.core.io.resource.ResourceUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.http.HttpUtil;
import com.alibaba.chaosblade.platform.cmmon.enums.ChaosTools;
import com.alibaba.chaosblade.platform.cmmon.model.chaos.PluginSpecBean;
import com.alibaba.chaosblade.platform.cmmon.utils.SystemPropertiesUtils;
import com.alibaba.chaosblade.platform.scenario.api.model.ToolsVersion;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.introspector.Property;
import org.yaml.snakeyaml.introspector.PropertyUtils;
import org.yaml.snakeyaml.nodes.Tag;
import org.yaml.snakeyaml.representer.Representer;

import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;

/**
 * @author yefei
 */
@Slf4j
@Component
@Original({ChaosTools.LITMUS_CHAOS, ChaosTools.CHAOS_BLADE})
public class DefaultScenarioYamlProvider implements ScenarioYamlProvider, InitializingBean {

    private final static String VERSION_YAML = Constants.VERSION_YAML;

    protected Map<String, String> versionYaml = new ConcurrentHashMap<>();

    protected Map<String, String> specYaml = new ConcurrentHashMap<>();

    @Autowired
    private ApplicationContext applicationContext;

    @Value("${chaos.scene.market}")
    private String sceneMarket;

    @Override
    public void afterPropertiesSet() {
        Map<String, ScenarioParser> beansOfType = applicationContext.getBeansOfType(ScenarioParser.class);
        for (Map.Entry<String, ScenarioParser> entry : beansOfType.entrySet()) {
            generateSpec(entry.getValue().parse(ScenarioRequest.builder().build()));
        }
    }

    public void generateSpec(List<PluginSpecBean> pluginSpecBeanList) {

        Consumer<PluginSpecBean> pluginSpecBeanConsumer = pluginSpecBean -> {

            String path = pluginSpecBean.getKind() + File.separatorChar + pluginSpecBean.getVersion();

            Yaml yaml = new Yaml();
            String dump = yaml.dumpAs(pluginSpecBean, Tag.MAP, DumperOptions.FlowStyle.BLOCK);

            String specFileName = String.format("%s-%s-spec-%s.yaml",
                    pluginSpecBean.getKind(),
                    pluginSpecBean.getType(),
                    pluginSpecBean.getVersion()
            );
            specYaml.put(specFileName, dump);

            Representer representer = new Representer();
            representer.setPropertyUtils(new PropertyUtils() {
                @Override
                public Property getProperty(Class<? extends Object> type, String name) {
                    if (name.indexOf('-') > -1) {
                        name = name.replace('-', '_');
                    }
                    return super.getProperty(type, name);
                }
            });

            String versionYamlFilePath = path + File.separatorChar + VERSION_YAML;
            ToolsVersion toolsVersion = new Yaml(representer).loadAs(ResourceUtil.getStream(versionYamlFilePath), ToolsVersion.class);

            if (toolsVersion.getScenarioFiles() == null) {
                toolsVersion.setScenarioFiles(new ArrayList<>());
            }

            if (!toolsVersion.getScenarioFiles().contains(specFileName)) {
                toolsVersion.getScenarioFiles().add(specFileName);
                versionYaml.put(pluginSpecBean.getVersion(), yaml.dumpAs(toolsVersion, Tag.MAP, DumperOptions.FlowStyle.BLOCK));
            }
        };
        pluginSpecBeanList.forEach(pluginSpecBeanConsumer);
    }

    @Override
    public String versionYaml(ScenarioRequest scenarioRequest) {
        String path = String.format("%s/%s/%s",
                scenarioRequest.getChaosTools(),
                scenarioRequest.getVersion(),
                Constants.VERSION_YAML);

        if (StrUtil.isNotBlank(sceneMarket)) {
            HttpRequest request = HttpUtil.createGet(sceneMarket + path);
            HttpResponse execute = request.execute();
            return IoUtil.read(execute.bodyStream(), SystemPropertiesUtils.getPropertiesFileEncoding());
        }

        String yaml = versionYaml.get(scenarioRequest.getVersion());
        if (yaml == null) {

            InputStream inputStream = Thread.currentThread().getContextClassLoader().getResourceAsStream(path);
            if (inputStream == null) {
                return null;
            } else {
                return IoUtil.read(inputStream, SystemPropertiesUtils.getPropertiesFileEncoding());
            }
        } else {
            return yaml;
        }
    }

    @Override
    public String specYaml(ScenarioRequest scenarioRequest) {
        String path = String.format("%s/%s/%s",
                scenarioRequest.getChaosTools(),
                scenarioRequest.getVersion(),
                scenarioRequest.getSpec());

        if (StrUtil.isNotBlank(sceneMarket)) {
            HttpRequest request = HttpUtil.createGet(sceneMarket + path);
            HttpResponse execute = request.execute();
            return IoUtil.read(execute.bodyStream(), SystemPropertiesUtils.getPropertiesFileEncoding());
        }

        String yaml = specYaml.get(scenarioRequest.getSpec());
        if (yaml == null) {
            InputStream inputStream = Thread.currentThread().getContextClassLoader().getResourceAsStream(path);
            if (inputStream == null) {
                return null;
            } else {
                return IoUtil.read(inputStream, SystemPropertiesUtils.getPropertiesFileEncoding());
            }
        } else {
            return yaml;
        }
    }


    @Override
    public String configuration(ScenarioRequest scenarioRequest) {
        String path = String.format("%s", Constants.CONFIGURATION_YAML);

        if (StrUtil.isNotBlank(sceneMarket)) {
            HttpRequest request = HttpUtil.createGet(sceneMarket + path);
            HttpResponse execute = request.execute();
            return IoUtil.read(execute.bodyStream(), SystemPropertiesUtils.getPropertiesFileEncoding());
        } else {
            InputStream inputStream = Thread.currentThread().getContextClassLoader().getResourceAsStream(path);
            if (inputStream == null) {
                return null;
            } else {
                return IoUtil.read(inputStream, SystemPropertiesUtils.getPropertiesFileEncoding());
            }
        }
    }

    @Override
    public String overview(ScenarioRequest scenarioRequest) {
        String path = String.format("%s/%s",
                scenarioRequest.getChaosTools(),
                Constants.OVERVIEW_YAML);

        if (StrUtil.isNotBlank(sceneMarket)) {
            HttpRequest request = HttpUtil.createGet(sceneMarket + path);
            HttpResponse execute = request.execute();
            return IoUtil.read(execute.bodyStream(), SystemPropertiesUtils.getPropertiesFileEncoding());
        } else {
            InputStream inputStream = Thread.currentThread().getContextClassLoader().getResourceAsStream(path);
            if (inputStream == null) {
                return null;
            } else {
                return IoUtil.read(inputStream, SystemPropertiesUtils.getPropertiesFileEncoding());
            }
        }
    }
}
