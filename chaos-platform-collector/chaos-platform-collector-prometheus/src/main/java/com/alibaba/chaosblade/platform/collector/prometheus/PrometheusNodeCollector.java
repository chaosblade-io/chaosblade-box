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

package com.alibaba.chaosblade.platform.collector.prometheus;

import com.alibaba.chaosblade.platform.cmmon.utils.JsonUtils;
import com.alibaba.chaosblade.platform.collector.CollectorStrategy;
import com.alibaba.chaosblade.platform.collector.CollectorType;
import com.alibaba.chaosblade.platform.collector.NodeCollector;
import com.alibaba.chaosblade.platform.collector.model.Node;
import com.alibaba.chaosblade.platform.collector.model.Query;
import com.alibaba.chaosblade.platform.collector.prometheus.model.PrometheusNode;
import com.alibaba.chaosblade.platform.cmmon.model.PrometheusResponse;
import com.fasterxml.jackson.core.type.TypeReference;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

/**
 * @author yefei
 */
@Component
@CollectorStrategy(CollectorType.PROMETHEUS)
public class PrometheusNodeCollector extends AbstractCollector<Node> implements NodeCollector {

    @Override
    public CompletableFuture<List<Node>> collect(Query query) {
        return collect("kube_node_info");
    }

    @Override
    List<Node> pack(byte[] bytes) {

        PrometheusResponse<PrometheusNode> response = JsonUtils.readValue(new TypeReference<PrometheusResponse<PrometheusNode>>() {
        }, bytes);

        return response.getData().getResult().stream().map(result ->
                Node.builder().name(result.getMetric().getName())
                        .namespace(result.getMetric().getNamespace())
                        .build()
        ).collect(Collectors.toList());
    }
}
