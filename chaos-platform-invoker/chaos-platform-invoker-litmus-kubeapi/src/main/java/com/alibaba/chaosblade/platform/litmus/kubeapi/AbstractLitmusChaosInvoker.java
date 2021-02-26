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

import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.http.HttpUtil;
import com.alibaba.chaosblade.platform.cmmon.constants.ChaosConstant;
import com.alibaba.chaosblade.platform.invoker.ChaosInvoker;
import com.alibaba.chaosblade.platform.cmmon.enums.ChaosTools;
import com.alibaba.chaosblade.platform.invoker.RequestCommand;
import com.alibaba.chaosblade.platform.invoker.ResponseCommand;
import com.alibaba.chaosblade.platform.scenario.litmus.model.ChaosExperiment;
import io.kubernetes.client.openapi.ApiClient;
import io.kubernetes.client.openapi.ApiException;
import io.kubernetes.client.openapi.apis.CustomObjectsApi;
import io.kubernetes.client.util.Config;
import org.springframework.beans.factory.InitializingBean;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.representer.Representer;

import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author yefei
 */
public abstract class AbstractLitmusChaosInvoker implements ChaosInvoker<RequestCommand, ResponseCommand>, InitializingBean {

    protected final static String SA_SUFFIX = "-sa";

    protected ApiClient client;

    protected final Map<String, ChaosExperiment> experimentMap = new ConcurrentHashMap<>();

    @Override
    public void afterPropertiesSet() throws Exception {
        HttpRequest request = HttpUtil.createGet("https://hub.litmuschaos.io/api/chaos/1.13.0?file=charts/generic/experiments.yaml");
        HttpResponse execute = request.execute();
        String s = new String(execute.bodyBytes());

        for (String s1 : StrUtil.split(s, "---")) {
            if (StrUtil.isBlank(s1)) {
                continue;
            }
            s1 = s1.trim();
            Representer representer = new Representer();
            representer.getPropertyUtils().setSkipMissingProperties(true);
            Yaml yaml = new Yaml(representer);
            ChaosExperiment chaosExperiment = yaml.loadAs(s1, ChaosExperiment.class);

            // pod-delete
            // k8s-pod-delete
            // node-cpu-hog
            // kafka-broker-disk-failure // todo
            String name = chaosExperiment.getMetadata().getName();

            String[] split = StrUtil.split(name, "-");
            String target;
            String action;
            if (split.length == 2) {
                target = split[0] + "-" + split[0];
                action = split[1];
            } else if (split.length == 3) {
                target = split[0] + "-" + split[1];
                action = split[2];
            } else {
                continue;
            }

            String sceneCode = ChaosTools.LITMUS_CHAOS.getName() + ChaosConstant.DOT
                    + target + ChaosConstant.DOT +  action;

            experimentMap.put(sceneCode, chaosExperiment);
        }
        client = Config.defaultClient();
    }

    protected CompletableFuture<ResponseCommand> postExperiment(RequestCommand requestCommand) {
        CompletableFuture<ResponseCommand> future = new CompletableFuture<>();
        try {
            CustomObjectsApi customObjectsApi = new CustomObjectsApi(client);
            customObjectsApi.deleteNamespacedCustomObject(
                    Constants.GROUP,
                    Constants.VERSION,
                    requestCommand.getNamespace(),
                    Constants.EXPERIMENT_PLURAL,
                    requestCommand.getSceneCode(),
                    10,
                    false,
                    null,
                    null,
                    null
            );

        } catch (ApiException e) {
            if (e.getCode() == 409) {
                // ignore
            } else {
                future.complete(ResponseCommand.builder()
                        .code(String.valueOf(e.getCode()))
                        .error(e.getMessage())
                        .result(e.getResponseBody())
                        .success(false).build());
            }
        }
        future.complete(ResponseCommand.builder().success(true).build());
        return future;
    }

}
