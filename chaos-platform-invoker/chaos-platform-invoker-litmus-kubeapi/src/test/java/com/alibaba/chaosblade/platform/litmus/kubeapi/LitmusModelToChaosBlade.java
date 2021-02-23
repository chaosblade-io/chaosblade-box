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

package com.alibaba.chaosblade.platform.litmus.kubeapi;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.http.HttpUtil;
import com.alibaba.chaosblade.platform.cmmon.model.chaos.ActionSpecBean;
import com.alibaba.chaosblade.platform.cmmon.model.chaos.FlagSpecBean;
import com.alibaba.chaosblade.platform.cmmon.model.chaos.ModelSpecBean;
import com.alibaba.chaosblade.platform.cmmon.model.chaos.PluginSpecBean;
import com.alibaba.chaosblade.platform.cmmon.utils.SystemPropertiesUtils;
import com.alibaba.chaosblade.platform.litmus.kubeapi.crd.experiment.ChaosExperiment;
import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.nodes.Tag;
import org.yaml.snakeyaml.representer.Representer;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author yefei
 */
public class LitmusModelToChaosBlade {

    public static void main(String[] args) throws Exception {

        HttpRequest request = HttpUtil.createGet("https://hub.litmuschaos.io/api/chaos/1.13.0?file=charts/generic/experiments.yaml");
        HttpResponse execute = request.execute();
        String s = new String(execute.bodyBytes());

        List<ChaosExperiment> list = new ArrayList<>();
        for (String s1 : StrUtil.split(s, "---")) {
            if (StrUtil.isBlank(s1)) {
                continue;
            }
            s1 = s1.trim();
            Representer representer = new Representer();
            representer.getPropertyUtils().setSkipMissingProperties(true);
            Yaml yaml = new Yaml(representer);
            ChaosExperiment chaosExperiment = yaml.loadAs(s1, ChaosExperiment.class);
            list.add(chaosExperiment);
        }

        List<ModelSpecBean> modelSpecBeans = list.stream().map(chaosExperiment -> {
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

                    return ModelSpecBean.builder()
                            .longDesc(chaosExperiment.getDescription().getMessage())
                            .target(target)
                            .actions(CollUtil.newArrayList(ActionSpecBean.builder()
                                    .action(action).flags(
                                            Arrays.stream(chaosExperiment.getSpec().getDefinition().getEnv())
                                                    .map(flag ->
                                                            FlagSpecBean.builder()
                                                                    .name(flag.getName())
                                                                    .defaultValue(flag.getValue())
                                                                    .build()
                                                    ).collect(Collectors.toList())

                                    ).build()))
                            .scope(scope)
                            .build();
                }
        ).collect(Collectors.toList());

        PluginSpecBean pluginSpecBean = PluginSpecBean.builder()
                .kind("litmuschaos")
                .version("1.13.0")
                .items(modelSpecBeans).build();


        String dump = new Yaml().dumpAs(pluginSpecBean, Tag.MAP, DumperOptions.FlowStyle.BLOCK);
        FileUtil.writeBytes(dump.getBytes(SystemPropertiesUtils.getPropertiesFileEncoding()), new File("/temp/litmuschaos-spec.yaml"));

    }
}
