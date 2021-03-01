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

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.ArrayUtil;
import com.alibaba.chaosblade.platform.blade.kubeapi.crd.ChaosBlade;
import com.alibaba.chaosblade.platform.blade.kubeapi.crd.ExperimentStatus;
import com.alibaba.chaosblade.platform.blade.kubeapi.model.StatusResponseCommand;
import com.alibaba.chaosblade.platform.cmmon.utils.timer.HashedWheelTimer;
import com.alibaba.chaosblade.platform.cmmon.utils.timer.Timer;
import com.alibaba.chaosblade.platform.invoker.ChaosInvoker;
import com.alibaba.chaosblade.platform.invoker.RequestCommand;
import com.alibaba.chaosblade.platform.invoker.ResponseCommand;
import io.kubernetes.client.openapi.ApiCallback;
import io.kubernetes.client.openapi.ApiClient;
import io.kubernetes.client.openapi.ApiException;
import io.kubernetes.client.openapi.apis.CustomObjectsApi;
import io.kubernetes.client.util.Config;
import org.springframework.beans.factory.InitializingBean;

import java.util.Map;
import java.util.concurrent.CompletableFuture;

/**
 * @author yefei
 */
public abstract class AbstractChaosBladeChaosInvoker implements ChaosInvoker<RequestCommand, ResponseCommand>, InitializingBean {

    protected ApiClient client;

    protected Timer timer;

    @Override
    public void afterPropertiesSet() throws Exception {
        client = Config.defaultClient();

        timer = new HashedWheelTimer(r -> {
            Thread thread = new Thread(r);
            thread.setName("timer-check-chaoblade-status");
            return thread;
        });
    }

    protected CompletableFuture<StatusResponseCommand> checkStatus(RequestCommand requestCommand) {
        CustomObjectsApi apiInstance = new CustomObjectsApi(client);

        CompletableFuture<StatusResponseCommand> completableFuture = new CompletableFuture<>();
        try {
            apiInstance.getClusterCustomObjectAsync(
                    Constants.GROUP,
                    Constants.VERSION,
                    Constants.PLURAL,
                    requestCommand.getName(),
                    new ApiCallback() {
                        @Override
                        public void onFailure(ApiException e, int statusCode, Map responseHeaders) {
                            StatusResponseCommand responseCommand = new StatusResponseCommand();
                            responseCommand.setSuccess(false);
                            responseCommand.setCode(String.valueOf(statusCode));
                            responseCommand.setResult(e.getMessage());
                            responseCommand.setError(e.getResponseBody());
                            completableFuture.complete(responseCommand);
                        }

                        @Override
                        public void onSuccess(Object result, int statusCode, Map responseHeaders) {
                            ChaosBlade chaosBlade = BeanUtil.toBean(result, ChaosBlade.class);

                            StatusResponseCommand statusResponseCommand = new StatusResponseCommand();
                            statusResponseCommand.setCode(String.valueOf(statusCode));
                            if (ArrayUtil.isNotEmpty(chaosBlade.getStatus().getExpStatuses())) {
                                ExperimentStatus expStatus = chaosBlade.getStatus().getExpStatuses()[0];
                                statusResponseCommand.setState(expStatus.getState());
                                statusResponseCommand.setError(expStatus.getError());
                                statusResponseCommand.setSuccess(expStatus.isSuccess());
                            }

                            statusResponseCommand.setPhase(chaosBlade.getStatus().getPhase());
                            completableFuture.complete(statusResponseCommand);
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
            StatusResponseCommand responseCommand = new StatusResponseCommand();
            responseCommand.setSuccess(false);
            responseCommand.setResult(e.getMessage());
            responseCommand.setError(e.getResponseBody());
            responseCommand.setCode(String.valueOf(e.getCode()));
            completableFuture.complete(responseCommand);
        }
        return completableFuture;
    }
}
