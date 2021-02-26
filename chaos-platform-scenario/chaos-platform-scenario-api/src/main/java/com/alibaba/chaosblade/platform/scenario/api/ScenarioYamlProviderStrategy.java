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
import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.http.HttpUtil;
import com.alibaba.chaosblade.platform.cmmon.enums.ChaosTools;
import com.alibaba.chaosblade.platform.cmmon.utils.SystemPropertiesUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.io.InputStream;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author yefei
 */
@Component
public class ScenarioYamlProviderStrategy implements ScenarioYamlProvider, InitializingBean {

    private Map<String, ScenarioYamlProvider> map = new ConcurrentHashMap<>();

    @Value("${chaos.scene.market}")
    private String sceneMarket;

    @Autowired
    private ApplicationContext applicationContext;

    @Override
    public void afterPropertiesSet() throws Exception {
        Map<String, ScenarioYamlProvider> beansOfType = applicationContext.getBeansOfType(ScenarioYamlProvider.class);
        for (Map.Entry<String, ScenarioYamlProvider> entry : beansOfType.entrySet()) {
            ScenarioYamlProvider value = entry.getValue();
            Original annotation = value.getClass().getAnnotation(Original.class);
            if (annotation != null) {
                for (ChaosTools chaosTools : annotation.value()) {
                    map.put(chaosTools.getName(), value);
                }
            }
        }
    }

    @Override
    public String versionYaml(ScenarioRequest scenarioRequest) {
        ScenarioYamlProvider scenarioYamlProvider = map.get(scenarioRequest.getChaosTools());
        return scenarioYamlProvider.versionYaml(scenarioRequest);
    }

    @Override
    public String specYaml(ScenarioRequest scenarioRequest) {
        ScenarioYamlProvider scenarioYamlProvider = map.get(scenarioRequest.getChaosTools());
        return scenarioYamlProvider.specYaml(scenarioRequest);
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
        ScenarioYamlProvider scenarioYamlProvider = map.get(scenarioRequest.getChaosTools());
        return scenarioYamlProvider.overview(scenarioRequest);
    }
}
