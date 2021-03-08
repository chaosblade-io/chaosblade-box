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

package com.alibaba.chaosbox.collector.kubeapi;

import com.alibaba.chaosbox.collector.CollectorStrategy;
import com.alibaba.chaosbox.collector.CollectorType;
import com.alibaba.chaosbox.collector.PodCollector;
import com.alibaba.chaosbox.collector.model.Pod;
import com.alibaba.chaosbox.collector.model.Query;
import io.kubernetes.client.openapi.ApiCallback;
import io.kubernetes.client.openapi.ApiClient;
import io.kubernetes.client.openapi.ApiException;
import io.kubernetes.client.openapi.apis.CoreV1Api;
import io.kubernetes.client.openapi.models.V1PodList;
import io.kubernetes.client.util.Config;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

/**
 * @author yefei
 */
@Component
@CollectorStrategy(CollectorType.KUBE_API)
public class KubeApiPodCollector implements PodCollector, InitializingBean {

    private ApiClient client;

    @Override
    public void afterPropertiesSet() throws Exception {
        client = Config.defaultClient();
    }

    @Override
    public CompletableFuture<List<Pod>> collect(Query query) {
        CompletableFuture<List<Pod>> future = new CompletableFuture<>();
        CoreV1Api api = new CoreV1Api(client);
        try {
            api.listPodForAllNamespacesAsync(null, null, null, null,
                    null, null, null, null, null,
                    new ApiCallback<V1PodList>() {
                        @Override
                        public void onFailure(ApiException e, int statusCode, Map<String, List<String>> responseHeaders) {
                            future.completeExceptionally(e);
                        }

                        @Override
                        public void onSuccess(V1PodList result, int statusCode, Map<String, List<String>> responseHeaders) {
                            List<Pod> pods = result.getItems().stream()
                                    .filter(v1Pod -> Objects.equals(v1Pod.getSpec().getNodeName(), query.getNodeName()))
                                    .map(v1Pod ->
                                            Pod.builder()
                                                    .name(v1Pod.getMetadata().getName())
                                                    .namespace(v1Pod.getMetadata().getNamespace())
                                                    .ip(v1Pod.getStatus().getPodIP())
                                                    .build()
                                    ).collect(Collectors.toList());
                            future.complete(pods);
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
