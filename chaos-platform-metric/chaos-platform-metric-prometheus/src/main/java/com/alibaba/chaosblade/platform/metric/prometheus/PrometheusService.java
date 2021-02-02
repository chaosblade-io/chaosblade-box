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

package com.alibaba.chaosblade.platform.metric.prometheus;

import cn.hutool.core.date.DateUtil;
import com.alibaba.chaosblade.platform.cmmon.utils.JsonUtils;
import com.alibaba.chaosblade.platform.metric.*;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
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
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

/**
 * @author yefei
 */
@Component
@MetricStrategy(MetricSource.PROMETHEUS)
public class PrometheusService implements MetricService, InitializingBean, DisposableBean {

    private final static String PARAM_URL = "url";

    private final static String PARAM_QUERY = "query";

    private CloseableHttpAsyncClient client;

    @Override
    public CompletableFuture<MetricChartLineResponse> selectChartLine(MetricChartLineRequest metricChartLineRequest) {
        CompletableFuture<MetricChartLineResponse> future = new CompletableFuture<>();

        Map<String, String> map = metricChartLineRequest.getParams();
        if (map == null) {
            future.completeExceptionally(new IllegalArgumentException("Prometheus need param"));
        }

        String paramUrl = map.get(PARAM_URL);
        if (paramUrl == null) {
            future.completeExceptionally(new IllegalArgumentException("Prometheus need param url"));
        }
        String query = map.get(PARAM_QUERY);
        if (query == null) {
            future.completeExceptionally(new IllegalArgumentException("Prometheus need param query"));
        }

        HttpPost httpPost = new HttpPost(paramUrl);
        List<NameValuePair> params = new ArrayList<>();

        params.add(new BasicNameValuePair("query", query));
        params.add(new BasicNameValuePair("start", String.valueOf(metricChartLineRequest.getStartTime().getTime() / 1000)));
        params.add(new BasicNameValuePair("end", String.valueOf(metricChartLineRequest.getEndTime().getTime() / 1000)));
        params.add(new BasicNameValuePair("step", "14"));

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

                    ArrayNode arrayNode = (ArrayNode) jsonNode.get("data").get("result").get(0).get("values");

                    List<MetricChartLine> metricChartLines = new ArrayList<>();
                    for (JsonNode node : arrayNode) {
                        String date = node.get(0).asText();
                        String value = node.get(1).asText();
                        metricChartLines.add(
                                MetricChartLine.builder()
                                        .time(DateUtil.date(new BigDecimal(date).longValue() * 1000))
                                        .value(value)
                                        .category(metricChartLineRequest.getCategoryCode())
                                        .build());
                    }

                    MetricChartLineResponse response = MetricChartLineResponse.builder()
                            .metricChartLines(metricChartLines)
                            .build();

                    future.complete(response);
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
        client = HttpAsyncClients.createDefault();
        client.start();
    }
}
