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

package com.alibaba.chaosblade.box.collector.kubeapi;

import cn.hutool.core.collection.CollUtil;
import com.alibaba.chaosblade.box.collector.CollectorStrategy;
import com.alibaba.chaosblade.box.collector.CollectorType;
import com.alibaba.chaosblade.box.collector.ContainerCollector;
import com.alibaba.chaosblade.box.collector.model.Container;
import com.alibaba.chaosblade.box.collector.model.Query;
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
public class KubeApiContainerCollector implements ContainerCollector, InitializingBean {

    private ApiClient client;

    @Override
    public void afterPropertiesSet() throws Exception {
        client = Config.defaultClient();
    }

    @Override
    public CompletableFuture<List<Container>> collect(Query query) {
        CompletableFuture<List<Container>> future = new CompletableFuture<>();
        CoreV1Api api = new CoreV1Api(client);
        try {
            api.listPodForAllNamespacesAsync(null, null, String.format("metadata.name=%s", query.getPodName()), null,
                    null, null, null, null, null,
                    new ApiCallback<V1PodList>() {
                        @Override
                        public void onFailure(ApiException e, int statusCode, Map<String, List<String>> responseHeaders) {
                            future.completeExceptionally(e);
                        }

                        @Override
                        public void onSuccess(V1PodList result, int statusCode, Map<String, List<String>> responseHeaders) {
                            List<Container> containers = result.getItems().stream()
                                    .filter(v1Pod -> Objects.equals(v1Pod.getMetadata().getName(), query.getPodName())
                                            && CollUtil.isNotEmpty(v1Pod.getStatus().getContainerStatuses()))
                                    .flatMap(v1Pod ->
                                            v1Pod.getStatus().getContainerStatuses().stream()
                                                    .map(v1ContainerStatus ->
                                                            Container.builder().pod(v1Pod.getMetadata().getName())
                                                                    .name(v1ContainerStatus.getName())
                                                                    .containerId(v1ContainerStatus.getContainerID())
                                                                    .build()
                                                    )
                                    ).collect(Collectors.toList());
                            future.complete(containers);
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
