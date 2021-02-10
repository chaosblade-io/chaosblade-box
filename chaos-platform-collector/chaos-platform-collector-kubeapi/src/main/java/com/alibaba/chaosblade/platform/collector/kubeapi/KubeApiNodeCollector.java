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

package com.alibaba.chaosblade.platform.collector.kubeapi;

import com.alibaba.chaosblade.platform.collector.CollectorStrategy;
import com.alibaba.chaosblade.platform.collector.CollectorType;
import com.alibaba.chaosblade.platform.collector.NodeCollector;
import com.alibaba.chaosblade.platform.collector.model.Node;
import com.alibaba.chaosblade.platform.collector.model.Query;
import io.kubernetes.client.openapi.ApiCallback;
import io.kubernetes.client.openapi.ApiClient;
import io.kubernetes.client.openapi.ApiException;
import io.kubernetes.client.openapi.apis.CoreV1Api;
import io.kubernetes.client.openapi.models.V1NodeList;
import io.kubernetes.client.util.Config;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

/**
 * @author yefei
 */
@Component
@CollectorStrategy(CollectorType.KUBE_API)
public class KubeApiNodeCollector implements NodeCollector, InitializingBean {

    private ApiClient client;

    @Override
    public void afterPropertiesSet() throws Exception {
        client = Config.defaultClient();
    }

    @Override
    public CompletableFuture<List<Node>> collect(Query query) {
        CompletableFuture<List<Node>> future = new CompletableFuture<>();
        CoreV1Api api = new CoreV1Api(client);
        try {
            api.listNodeAsync(null,
                    null, null,
                    null, null, null,
                    null, null, null
                    , new ApiCallback<V1NodeList>() {
                        @Override
                        public void onFailure(ApiException e, int statusCode, Map<String, List<String>> responseHeaders) {
                            future.completeExceptionally(e);
                        }

                        @Override
                        public void onSuccess(V1NodeList result, int statusCode, Map<String, List<String>> responseHeaders) {
                            List<Node> nodes = result.getItems().stream().map(v1Node ->
                                    Node.builder().name(v1Node.getMetadata().getName())
                                            .ip(v1Node.getStatus().getAddresses().get(0).getAddress())
                                            .build()
                            ).collect(Collectors.toList());
                            future.complete(nodes);
                        }

                        @Override
                        public void onUploadProgress(long bytesWritten, long contentLength, boolean done) {

                        }

                        @Override
                        public void onDownloadProgress(long bytesRead, long contentLength, boolean done) {

                        }
                    });

        } catch (ApiException e) {
            future.completeExceptionally(e);
        }
        return future;
    }
}
