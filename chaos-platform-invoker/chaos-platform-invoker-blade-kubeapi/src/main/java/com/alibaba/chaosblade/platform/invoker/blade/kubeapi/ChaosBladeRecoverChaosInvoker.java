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

package com.alibaba.chaosblade.platform.invoker.blade.kubeapi;

import cn.hutool.core.util.StrUtil;
import com.alibaba.chaosblade.platform.invoker.blade.kubeapi.model.StatusResponseCommand;
import com.alibaba.chaosblade.platform.cmmon.constants.ChaosConstant;
import com.alibaba.chaosblade.platform.cmmon.enums.DeviceType;
import com.alibaba.chaosblade.platform.cmmon.exception.BizException;
import com.alibaba.chaosblade.platform.invoker.ChaosInvokerStrategy;
import com.alibaba.chaosblade.platform.invoker.RequestCommand;
import com.alibaba.chaosblade.platform.invoker.ResponseCommand;
import io.kubernetes.client.openapi.ApiCallback;
import io.kubernetes.client.openapi.ApiException;
import io.kubernetes.client.openapi.apis.CustomObjectsApi;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

/**
 * @author yefei
 */
@Slf4j
@ChaosInvokerStrategy(deviceType = {DeviceType.NODE, DeviceType.POD}, phase = ChaosConstant.PHASE_RECOVER)
@Component
public class ChaosBladeRecoverChaosInvoker extends AbstractChaosBladeChaosInvoker {

    @Override
    public CompletableFuture<ResponseCommand> invoke(RequestCommand requestCommand) {
        CustomObjectsApi apiInstance = new CustomObjectsApi(client);

        CompletableFuture<ResponseCommand> completableFuture = new CompletableFuture<>();
        try {
            apiInstance.deleteClusterCustomObjectAsync(
                    Constants.GROUP,
                    Constants.VERSION,
                    Constants.PLURAL,
                    requestCommand.getName(),
                    10,
                    false,
                    null,
                    null,
                    null,
                    new ApiCallback() {
                        @Override
                        public void onFailure(ApiException e, int statusCode, Map responseHeaders) {
                            if (statusCode == 404) {
                                ResponseCommand responseCommand = ResponseCommand.builder().success(true)
                                        .result(requestCommand.getName()).build();
                                completableFuture.complete(responseCommand);
                            } else {
                                ResponseCommand responseCommand = ResponseCommand.builder()
                                        .success(false)
                                        .code(String.valueOf(statusCode))
                                        .result(e.getMessage())
                                        .error(e.getResponseBody())
                                        .build();
                                completableFuture.complete(responseCommand);
                            }
                        }

                        @Override
                        public void onSuccess(Object result, int statusCode, Map responseHeaders) {
                            checkStatus(completableFuture, requestCommand.getName());
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


    private void checkStatus(CompletableFuture<ResponseCommand> future, String name) {

        timer.newTimeout(timeout -> {
                    RequestCommand requestCommand = new RequestCommand();
                    requestCommand.setName(name);

                    CompletableFuture<StatusResponseCommand> completableFuture = checkStatus(requestCommand);

                    completableFuture.handle((statusResponseCommand, e) -> {
                        if (e != null) {
                            future.completeExceptionally(e);
                        } else {
                            log.info("子任务运行中，检查 CRD 状态，NAME: {}, PHASE: {},  是否成功: {}, 失败原因: {}",
                                    requestCommand.getName(),
                                    statusResponseCommand.getPhase(),
                                    statusResponseCommand.isSuccess(),
                                    statusResponseCommand.getError());

                            String error = statusResponseCommand.getError();

                            if ("404".equals(statusResponseCommand.getCode())) {
                                statusResponseCommand.setSuccess(true);
                                future.complete(statusResponseCommand);
                                return null;
                            }
                            if (StrUtil.isNotEmpty(error)) {
                                future.completeExceptionally(new BizException(error));
                                return null;
                            }
                            if ("Destroyed".equals(statusResponseCommand.getPhase())) {
                                future.complete(statusResponseCommand);
                            } else {
                                checkStatus(future, name);
                            }

                        }
                        return null;
                    });
                },
                3000,
                TimeUnit.MILLISECONDS);
    }



}
