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

package com.alibaba.chaosblade.box.invoker.litmus.kubeapi;

import cn.hutool.core.util.StrUtil;
import com.alibaba.chaosblade.box.invoker.ChaosInvoker;
import com.alibaba.chaosblade.box.invoker.RequestCommand;
import com.alibaba.chaosblade.box.invoker.ResponseCommand;
import io.kubernetes.client.openapi.ApiClient;
import io.kubernetes.client.openapi.ApiException;
import io.kubernetes.client.openapi.apis.CustomObjectsApi;
import io.kubernetes.client.util.Config;
import org.springframework.beans.factory.InitializingBean;

import java.io.ByteArrayInputStream;
import java.util.concurrent.CompletableFuture;

/**
 * @author yefei
 */
public abstract class AbstractLitmusChaosInvoker implements ChaosInvoker<RequestCommand, ResponseCommand>, InitializingBean {

    protected final static String SA_SUFFIX = "-sa";

    protected ApiClient client;

    @Override
    public void afterPropertiesSet() throws Exception {
        client = Config.defaultClient();
    }

    protected ApiClient getClient(RequestCommand requestCommand) throws Exception {
        if (StrUtil.isBlank(requestCommand.getConfig())) {
            return client;
        } else {
            ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(requestCommand.getConfig().getBytes());
            return Config.fromConfig(byteArrayInputStream);
        }
    }

    protected CompletableFuture<ResponseCommand> postExperiment(RequestCommand requestCommand) {
        CompletableFuture<ResponseCommand> future = new CompletableFuture<>();
        try {
            CustomObjectsApi customObjectsApi = new CustomObjectsApi(getClient(requestCommand));
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
        } catch (Exception e) {
            future.completeExceptionally(e);
        }
        future.complete(ResponseCommand.builder().success(true).build());
        return future;
    }

}
