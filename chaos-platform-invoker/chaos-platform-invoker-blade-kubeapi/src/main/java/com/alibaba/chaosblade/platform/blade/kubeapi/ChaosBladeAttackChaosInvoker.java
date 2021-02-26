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
import cn.hutool.core.util.StrUtil;
import com.alibaba.chaosblade.platform.blade.kubeapi.crd.ChaosBlade;
import com.alibaba.chaosblade.platform.blade.kubeapi.crd.ChaosBladeSpec;
import com.alibaba.chaosblade.platform.blade.kubeapi.crd.ExperimentSpec;
import com.alibaba.chaosblade.platform.blade.kubeapi.crd.FlagSpec;
import com.alibaba.chaosblade.platform.blade.kubeapi.model.StatusResponseCommand;
import com.alibaba.chaosblade.platform.cmmon.TaskLogRecord;
import com.alibaba.chaosblade.platform.cmmon.constants.ChaosConstant;
import com.alibaba.chaosblade.platform.cmmon.enums.DeviceType;
import com.alibaba.chaosblade.platform.cmmon.utils.JsonUtils;
import com.alibaba.chaosblade.platform.cmmon.utils.SceneCodeParseUtil;
import com.alibaba.chaosblade.platform.cmmon.utils.timer.HashedWheelTimer;
import com.alibaba.chaosblade.platform.invoker.ChaosInvokerStrategy;
import com.alibaba.chaosblade.platform.invoker.RequestCommand;
import com.alibaba.chaosblade.platform.invoker.ResponseCommand;
import io.kubernetes.client.openapi.ApiCallback;
import io.kubernetes.client.openapi.ApiException;
import io.kubernetes.client.openapi.apis.CustomObjectsApi;
import io.kubernetes.client.openapi.models.V1ObjectMeta;
import io.kubernetes.client.util.Config;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

/**
 * @author yefei
 */
@Slf4j
@ChaosInvokerStrategy(deviceType = {DeviceType.NODE, DeviceType.POD}, phase = ChaosConstant.PHASE_ATTACK)
@Component
public class ChaosBladeAttackChaosInvoker extends AbstractChaosBladeChaosInvoker {

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

        final CompletableFuture<ResponseCommand> completableFuture = new CompletableFuture<>();
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
                            checkStatus(completableFuture, v1ObjectMeta.getName());
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

        timer = new HashedWheelTimer(r -> {
            Thread thread = new Thread(r);
            thread.setName("timer-check-chaoblade-status");
            return thread;
        });
    }

    private void checkStatus(CompletableFuture<ResponseCommand> future, String name) {

        timer.newTimeout(timeout -> {
                    RequestCommand requestCommand = new RequestCommand();
                    requestCommand.setName(name);

                    CompletableFuture<StatusResponseCommand> completableFuture = checkStatus(requestCommand);

                    completableFuture.handle((statusResponseCommand, e) -> {
                        if (e != null) {
                            future.completeExceptionally(e);
                        } else {
                            statusResponseCommand.setName(name);
                            statusResponseCommand.setResult(name);

                            log.info("子任务运行中，检查 CRD 状态，NAME: {}, PHASE: {},  是否成功: {}, 失败原因: {}",
                                    requestCommand.getName(),
                                    statusResponseCommand.getPhase(),
                                    statusResponseCommand.isSuccess(),
                                    statusResponseCommand.getError());

                            String error = statusResponseCommand.getError();

                            if (StrUtil.isNotEmpty(error)) {
                                future.complete(statusResponseCommand);
                            } else {
                                if ("Running".equals(statusResponseCommand.getPhase())) {
                                    future.complete(statusResponseCommand);
                                } else {
                                    checkStatus(future, name);
                                }
                            }
                        }
                        return null;
                    });
                }, 3, TimeUnit.SECONDS);
    }

}
