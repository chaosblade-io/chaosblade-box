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

package com.alibaba.chaosblade.platform.blade.kubeapi;

import cn.hutool.core.util.IdUtil;
import com.alibaba.chaosblade.platform.blade.kubeapi.crd.ChaosBlade;
import com.alibaba.chaosblade.platform.blade.kubeapi.crd.ChaosBladeSpec;
import com.alibaba.chaosblade.platform.blade.kubeapi.crd.ExperimentSpec;
import com.alibaba.chaosblade.platform.blade.kubeapi.crd.FlagSpec;
import com.alibaba.chaosblade.platform.cmmon.constants.ChaosConstant;
import com.alibaba.chaosblade.platform.cmmon.enums.DeviceType;
import com.alibaba.chaosblade.platform.cmmon.utils.JsonUtils;
import com.alibaba.chaosblade.platform.cmmon.utils.SceneCodeParseUtil;
import com.alibaba.chaosblade.platform.invoker.ChaosInvoker;
import com.alibaba.chaosblade.platform.invoker.ChaosInvokerStrategy;
import com.alibaba.chaosblade.platform.invoker.RequestCommand;
import com.alibaba.chaosblade.platform.invoker.ResponseCommand;
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
 * @create 2021-02-19 10:03
 */
@ChaosInvokerStrategy(deviceType = {DeviceType.NODE, DeviceType.POD}, phase = ChaosConstant.PHASE_ATTACK)
@Component
public class KubeApiAttackChaosInvoker implements ChaosInvoker<RequestCommand, ResponseCommand>, InitializingBean {

    private ApiClient client;

    @Override
    public CompletableFuture<ResponseCommand> invoke(RequestCommand requestCommand) {
        CustomObjectsApi apiInstance = new CustomObjectsApi(client);

        V1ObjectMeta v1ObjectMeta = new V1ObjectMeta();
        v1ObjectMeta.setName(IdUtil.fastSimpleUUID());
        ChaosBlade chaosBladeRequest = ChaosBlade.builder()
                .apiVersion(Constants.API_VERSION)
                .kind(Constants.KIND)
                .metadata(v1ObjectMeta)
                .spec(ChaosBladeSpec.builder()
                        .experiments(new ExperimentSpec[]{
                                ExperimentSpec.builder()
                                        .scope(requestCommand.getScope())
                                        .target(SceneCodeParseUtil.getTarget(requestCommand.getSceneCode()).split("-")[1])
                                        .action(SceneCodeParseUtil.getAction(requestCommand.getSceneCode()))
                                        .matchers(
                                                requestCommand.getArguments().keySet().stream()
                                                        .map(key -> FlagSpec.builder()
                                                                .name(key)
                                                                .value(new String[]{requestCommand.getArguments().get(key)})
                                                                .build()
                                                        ).toArray(FlagSpec[]::new))
                                        .build()
                        }).build()).build();

        CompletableFuture<ResponseCommand> completableFuture = new CompletableFuture<>();
        try {
            apiInstance.createClusterCustomObjectAsync(
                    Constants.GROUP,
                    Constants.VERSION,
                    Constants.PLURAL,
                    JsonUtils.writeValueAsBytes(chaosBladeRequest),
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
