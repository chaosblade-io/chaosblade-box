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

package com.alibaba.chaosbox.metric.aliyun;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUtil;
import com.alibaba.chaosbox.common.DeviceMeta;
import com.alibaba.chaosbox.common.exception.BizException;
import com.alibaba.chaosbox.common.utils.JsonUtils;
import com.alibaba.chaosbox.common.utils.Preconditions;
import com.alibaba.chaosbox.metric.*;
import com.alibaba.chaosbox.metric.*;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.cms.model.v20190101.DescribeMetricListRequest;
import com.aliyuncs.cms.model.v20190101.DescribeMetricListResponse;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.profile.DefaultProfile;
import com.fasterxml.jackson.core.type.TypeReference;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

/**
 * @author yefei
 */
@Slf4j
@Service
@MetricStrategy(MetricSource.ALIYUN)
public class AliyunMetricService implements MetricService, InitializingBean {

    private final static String PARAM_REGION = "region";

    private String ak;

    private String sk;

    private IAcsClient client;

    @Override
    public CompletableFuture<List<MetricChartLineResponse>> selectChartLine(MetricChartLineRequest metricChartLineRequest) {
        CompletableFuture<List<MetricChartLineResponse>> completableFuture = new CompletableFuture<>();

        Map<String, String> map = metricChartLineRequest.getParams();
        Preconditions.checkNotNull(map, new BizException("Aliyun Metric need param"));
        Preconditions.checkNotNull(map.get(PARAM_REGION), new BizException("region param url"));

        List<MetricChartLineResponse> metricChartLineResponses = CollUtil.newArrayList();
        for (DeviceMeta device : metricChartLineRequest.getDevices()) {

            DescribeMetricListRequest describeMetricListRequest = new DescribeMetricListRequest();
            describeMetricListRequest.setDimensions("{\"instanceId\":\"" + device.getHostname() + "\"}");
            describeMetricListRequest.setStartTime(DateUtil.date(metricChartLineRequest.getStartTime()).toStringDefaultTimeZone());
            describeMetricListRequest.setEndTime(DateUtil.date(metricChartLineRequest.getEndTime()).toStringDefaultTimeZone());
            describeMetricListRequest.setPeriod("15");
            describeMetricListRequest.setNamespace("acs_ecs_dashboard");
            String[] split = metricChartLineRequest.getCategoryCode().split("[.]");
            describeMetricListRequest.setMetricName(split[2]);

            DescribeMetricListResponse response = null;
            try {
                DefaultProfile profile = DefaultProfile.getProfile(map.get(PARAM_REGION), ak, sk);
                response = client.getAcsResponse(describeMetricListRequest, profile);
            } catch (ClientException e) {
                completableFuture.completeExceptionally(e);
            }
            String datapoints = response.getDatapoints();
            List<Map<String, String>> list = JsonUtils.readValue(new TypeReference<List<Map<String, String>>>() {
            }, datapoints);

            {
                List<MetricChartLine> lines = list.stream().map(dot ->
                        MetricChartLine.builder()
                                .time(DateUtil.date(Long.parseLong(dot.get("timestamp"))))
                                .value(dot.get("Average"))
                                .build()
                ).collect(Collectors.toList());

                Metric metric = Metric.builder().instance(device.getHostname()).dimension("Average").build();
                MetricChartLineResponse metricChartLineResponse = MetricChartLineResponse.builder().build();
                metricChartLineResponse.setMetricChartLines(lines);
                metricChartLineResponse.setMetric(JsonUtils.writeValueAsString(metric));
                metricChartLineResponse.setDeviceMeta(device);
                metricChartLineResponses.add(metricChartLineResponse);
            }

            {
                List<MetricChartLine> lines = list.stream().map(dot ->
                        MetricChartLine.builder()
                                .time(DateUtil.date(Long.parseLong(dot.get("timestamp"))))
                                .value(dot.get("Minimum"))
                                .build()
                ).collect(Collectors.toList());

                Metric metric = Metric.builder().instance(device.getHostname()).dimension("Minimum").build();
                MetricChartLineResponse metricChartLineResponse = MetricChartLineResponse.builder().build();
                metricChartLineResponse.setMetricChartLines(lines);
                metricChartLineResponse.setMetric(JsonUtils.writeValueAsString(metric));
                metricChartLineResponse.setDeviceMeta(device);
                metricChartLineResponses.add(metricChartLineResponse);
            }

            {
                List<MetricChartLine> lines = list.stream().map(dot ->
                        MetricChartLine.builder()
                                .time(DateUtil.date(Long.parseLong(dot.get("timestamp"))))
                                .value(dot.get("Maximum"))
                                .build()
                ).collect(Collectors.toList());

                Metric metric = Metric.builder().instance(device.getHostname()).dimension("Maximum").build();
                MetricChartLineResponse metricChartLineResponse = MetricChartLineResponse.builder().build();
                metricChartLineResponse.setMetricChartLines(lines);
                metricChartLineResponse.setMetric(JsonUtils.writeValueAsString(metric));
                metricChartLineResponse.setDeviceMeta(device);
                metricChartLineResponses.add(metricChartLineResponse);
            }
        }

        completableFuture.complete(metricChartLineResponses);
        return completableFuture;
    }

    @Override
    public void afterPropertiesSet() {
        client = new DefaultAcsClient();
    }
}
