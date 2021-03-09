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

package com.alibaba.chaosbox.metric.prometheus;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.chaosbox.common.exception.BizException;
import com.alibaba.chaosbox.common.utils.JsonUtils;
import com.alibaba.chaosbox.common.utils.Preconditions;
import com.alibaba.chaosbox.metric.*;
import com.alibaba.chaosbox.metric.*;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.concurrent.FutureCallback;
import org.apache.http.impl.nio.client.CloseableHttpAsyncClient;
import org.apache.http.impl.nio.client.HttpAsyncClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.CompletableFuture;

/**
 * @author yefei
 */
@Component
@MetricStrategy(MetricSource.PROMETHEUS)
public class PrometheusService implements MetricService, InitializingBean, DisposableBean {

    private final static String PARAM_URL = "url";

    private final static String PARAM_QUERY = "query";

    private final static String PARAM_STEP = "step";

    private final static String PARAM_RULE = "rule";

    private CloseableHttpAsyncClient client;

    @Override
    public CompletableFuture<List<MetricChartLineResponse>> selectChartLine(MetricChartLineRequest metricChartLineRequest) {
        CompletableFuture<List<MetricChartLineResponse>> future = new CompletableFuture<>();

        Map<String, String> map = metricChartLineRequest.getParams();
        Preconditions.checkNotNull(map, new BizException("Prometheus need param"));

        String paramUrl = map.get(PARAM_URL);
        Preconditions.checkNotNull(paramUrl, new BizException("Prometheus need param url"));

        String query = map.get(PARAM_QUERY);
        Preconditions.checkNotNull(query, new BizException("Prometheus need param query"));

        String step = map.get(PARAM_STEP);
        if (step == null) {
            step = "14";
        }

        HttpPost httpPost = new HttpPost(paramUrl);
        List<NameValuePair> params = new ArrayList<>();
        params.add(new BasicNameValuePair("query", query));
        params.add(new BasicNameValuePair("start", String.valueOf(metricChartLineRequest.getStartTime().getTime() / 1000)));
        params.add(new BasicNameValuePair("end", String.valueOf(metricChartLineRequest.getEndTime().getTime() / 1000)));
        params.add(new BasicNameValuePair("step", step));

        try {
            httpPost.setEntity(new UrlEncodedFormEntity(params));
        } catch (UnsupportedEncodingException e) {
            future.completeExceptionally(e);
        }

        client.execute(httpPost, new FutureCallback<HttpResponse>() {
            @Override
            public void completed(HttpResponse httpResponse) {
                try {
                    HttpEntity entity = httpResponse.getEntity();

                    ByteArrayInputStream inputStream = new ByteArrayInputStream(EntityUtils.toByteArray(entity));
                    JsonNode jsonNode = JsonUtils.reader().readTree(inputStream);

                    ArrayNode result = (ArrayNode) jsonNode.get("data").get("result");

                    String rule = map.get(PARAM_RULE);
                    Map<String, String> ipToInstance = new HashMap<>();
                    if (StrUtil.isNotBlank(rule)) {
                        ArrayNode arrayNode = (ArrayNode) JsonUtils.reader().readTree(rule);
                        for (JsonNode node : arrayNode) {
                            String field = node.fieldNames().next();
                            ipToInstance.put(field, node.get(field).asText());
                        }
                    }

                    List<MetricChartLineResponse> metricChartLineResponses = CollUtil.newArrayList();
                    if (CollUtil.isNotEmpty(result)) {
                        for (JsonNode node : result) {

                            String metric = node.get("metric").toString();
                            MetricChartLineResponse metricChartLineResponse = MetricChartLineResponse.builder().metric(metric)
                                    .build();
                            // values
                            ArrayNode values = (ArrayNode) node.get("values");
                            List<MetricChartLine> metricChartLines = new ArrayList<>();
                            for (JsonNode dot : values) {
                                String date = dot.get(0).asText();
                                String value = dot.get(1).asText();
                                metricChartLines.add(MetricChartLine.builder()
                                        .time(DateUtil.date(new BigDecimal(date).longValue() * 1000))
                                        .value(value)
                                        .build());
                            }

                            metricChartLineRequest.getDevices().stream().filter(deviceMeta -> {
                                if (StrUtil.contains(metric, deviceMeta.getIp()) ||
                                        StrUtil.contains(metric, deviceMeta.getHostname()) ||
                                        StrUtil.contains(metric, deviceMeta.getNamespace()) ||
                                        StrUtil.contains(metric, deviceMeta.getNodeName()) ||
                                        StrUtil.contains(metric, deviceMeta.getPodName()) ||
                                        StrUtil.contains(metric, deviceMeta.getContainerName())
                                ) {
                                    return true;
                                }
                                String value = ipToInstance.get(deviceMeta.getIp());
                                if (value != null) {
                                    return metric.contains(value);
                                }
                                return false;
                            }).findFirst().ifPresent(deviceMeta -> {
                                metricChartLineResponse.setMetricChartLines(metricChartLines);
                                metricChartLineResponse.setDeviceMeta(deviceMeta);
                                metricChartLineResponses.add(metricChartLineResponse);
                            });
                        }
                        future.complete(metricChartLineResponses);
                    } else {
                        future.complete(Collections.emptyList());
                    }
                } catch (IOException e) {
                    future.completeExceptionally(e);
                }
            }

            @Override
            public void failed(Exception e) {
                future.completeExceptionally(e);
            }

            @Override
            public void cancelled() {
                future.completeExceptionally(new InterruptedException());
            }
        });
        return future;
    }

    @Override
    public void destroy() throws Exception {
        client.close();
    }

    @Override
    public void afterPropertiesSet() {
        RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(3000).setConnectTimeout(3000).build();
        client = HttpAsyncClients.custom().setDefaultRequestConfig(requestConfig).build();
        client.start();
    }
}
