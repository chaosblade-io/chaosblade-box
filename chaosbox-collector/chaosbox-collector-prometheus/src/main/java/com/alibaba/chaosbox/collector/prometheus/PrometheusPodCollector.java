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

package com.alibaba.chaosbox.collector.prometheus;

import com.alibaba.chaosbox.collector.prometheus.model.PrometheusPod;
import com.alibaba.chaosbox.common.model.PrometheusResponse;
import com.alibaba.chaosbox.common.utils.JsonUtils;
import com.alibaba.chaosbox.collector.CollectorStrategy;
import com.alibaba.chaosbox.collector.CollectorType;
import com.alibaba.chaosbox.collector.PodCollector;
import com.alibaba.chaosbox.collector.model.Pod;
import com.alibaba.chaosbox.collector.model.Query;
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
public class PrometheusPodCollector extends AbstractCollector<Pod> implements PodCollector {

    @Override
    public CompletableFuture<List<Pod>> collect(Query query) {
        return collect(String.format("kube_pod_info{node='%s'}", query.getNodeName()));
    }

    @Override
    List<Pod> pack(byte[] bytes) {

        PrometheusResponse<PrometheusPod> response = JsonUtils.readValue(new TypeReference<PrometheusResponse<PrometheusPod>>() {
        }, bytes);

        return response.getData().getResult().stream().map(result ->
                Pod.builder().name(result.getMetric().getName())
                        .node(result.getMetric().getNode())
                        .ip(result.getMetric().getIp())
                        .namespace(result.getMetric().getNamespace())
                        .build()
        ).collect(Collectors.toList());
    }
}
