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

import cn.hutool.core.util.IdUtil;
import com.alibaba.chaosblade.platform.cmmon.constants.ChaosConstant;
import com.alibaba.chaosblade.platform.cmmon.enums.DeviceType;
import com.alibaba.chaosblade.platform.cmmon.utils.JsonUtils;
import com.alibaba.chaosblade.platform.invoker.*;
import com.alibaba.chaosblade.platform.litmus.kubeapi.crd.ChaosExperimentDefinitionEnv;
import com.alibaba.chaosblade.platform.litmus.kubeapi.crd.engine.*;
import io.kubernetes.client.openapi.ApiCallback;
import io.kubernetes.client.openapi.ApiClient;
import io.kubernetes.client.openapi.ApiException;
import io.kubernetes.client.openapi.apis.CustomObjectsApi;
import io.kubernetes.client.openapi.models.V1ObjectMeta;
import io.kubernetes.client.util.Config;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.CompletableFuture;

/**
 * @author yefei
 */
@ChaosInvokerStrategy(value = ChaosTools.LITMUS_CHAOS,
        deviceType = {
                DeviceType.NODE, DeviceType.POD
        },
        phase = ChaosConstant.PHASE_ATTACK)
@Component
public class LitmusAttackChaosInvoker implements ChaosInvoker<RequestCommand, ResponseCommand>, InitializingBean {

    private ApiClient client;

    @Override
    public CompletableFuture<ResponseCommand> invoke(RequestCommand requestCommand) {
        CustomObjectsApi apiInstance = new CustomObjectsApi(client);

        V1ObjectMeta v1ObjectMeta = new V1ObjectMeta();
        v1ObjectMeta.setName(IdUtil.fastSimpleUUID());
        ChaosEngine chaosEngine = ChaosEngine.builder()
                .apiVersion(Constants.API_VERSION)
                .kind(Constants.CHAOS_ENGINE)
                .metadata(v1ObjectMeta)
                .spec(ChaosEngineSpec.builder()
                        .appinfo(ChaosEngineSpecAppinfo.builder().build())
                        .chaosServiceAccount("")
                        .engineState("active")
                        .monitoring(false)
                        .annotationCheck(false)
                        .jobCleanUpPolicy("delete")
                        .experiments(new ChaosEngineSpecExperiment[]{
                                ChaosEngineSpecExperiment.builder()
                                        .name(requestCommand.getSceneCode())
                                        .spec(ChaosEngineSpecExperimentSpec.builder()
                                                .components(ChaosEngineSpecExperimentSpecComponents.builder()
                                                        .env(requestCommand.getArguments().keySet().stream()
                                                                .map(key -> ChaosExperimentDefinitionEnv.builder()
                                                                        .name(key)
                                                                        .value(requestCommand.getArguments().get(key))
                                                                        .build()
                                                                ).toArray(ChaosExperimentDefinitionEnv[]::new))
                                                        .build()
                                                )
                                                .build())
                                        .build()})
                        .build()).build();

        CompletableFuture<ResponseCommand> completableFuture = new CompletableFuture<>();
        try {
            apiInstance.createClusterCustomObjectAsync(
                    Constants.GROUP,
                    Constants.VERSION,
                    Constants.PLURAL,
                    JsonUtils.writeValueAsBytes(chaosEngine),
                    "ture",
                    null,
                    null,
                    new ApiCallback() {
                        @Override
                        public void onFailure(ApiException e, int statusCode, Map responseHeaders) {
                            ResponseCommand responseCommand = ResponseCommand.builder()
                                    .success(false)
                                    .code(String.valueOf(statusCode))
                                    .result(e.getMessage())
                                    .error(e.getResponseBody())
                                    .build();
                            completableFuture.complete(responseCommand);
                        }

                        @Override
                        public void onSuccess(Object result, int statusCode, Map responseHeaders) {
                            ResponseCommand responseCommand = ResponseCommand.builder()
                                    .code(String.valueOf(statusCode))
                                    .success(true)
                                    .result(v1ObjectMeta.getName())
                                    .build();
                            completableFuture.complete(responseCommand);
                        }

                        @Override
                        public void onUploadProgress(long bytesWritten, long contentLength, boolean done) {

                        }

                        @Override
                        public void onDownloadProgress(long bytesRead, long contentLength, boolean done) {

                        }
                    }
            );
        } catch (ApiException e) {
            ResponseCommand responseCommand = ResponseCommand.builder()
                    .success(false)
                    .code(String.valueOf(e.getCode()))
                    .result(e.getMessage())
                    .error(e.getResponseBody())
                    .build();
            completableFuture.complete(responseCommand);
        }
        return completableFuture;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        client = Config.defaultClient();
    }
}
