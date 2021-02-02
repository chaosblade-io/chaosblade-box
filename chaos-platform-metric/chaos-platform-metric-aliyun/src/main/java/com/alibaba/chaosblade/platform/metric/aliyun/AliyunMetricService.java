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

package com.alibaba.chaosblade.platform.metric.aliyun;

import cn.hutool.core.date.DateField;
import cn.hutool.core.date.DateUtil;
import com.alibaba.chaosblade.platform.cmmon.utils.JsonUtils;
import com.alibaba.chaosblade.platform.metric.*;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.cms.model.v20190101.DescribeMetricListRequest;
import com.aliyuncs.cms.model.v20190101.DescribeMetricListResponse;
import com.aliyuncs.cms.model.v20190101.DescribeMonitoringAgentStatusesRequest;
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

    private String ak;

    private String sk;

    private IAcsClient client;

    @Override
    public CompletableFuture<MetricChartLineResponse> selectChartLine(MetricChartLineRequest metricChartLineRequest) {

        DescribeMonitoringAgentStatusesRequest request = new DescribeMonitoringAgentStatusesRequest();
        request.setInstanceIds(metricChartLineRequest.getInstance());

        MetricChartLineResponse metricChartLineResponse = MetricChartLineResponse.builder().build();
        DescribeMetricListRequest describeMetricListRequest = new DescribeMetricListRequest();
        describeMetricListRequest.setStartTime(DateUtil.date().offset(DateField.MINUTE, -10).toStringDefaultTimeZone());
        describeMetricListRequest.setEndTime(DateUtil.date().toStringDefaultTimeZone());
        describeMetricListRequest.setPeriod("60");
        describeMetricListRequest.setNamespace("acs_ecs_dashboard");
        describeMetricListRequest.setMetricName(metricChartLineRequest.getCategoryCode());

        CompletableFuture<MetricChartLineResponse> completableFuture = new CompletableFuture<>();
        DescribeMetricListResponse response = null;
        try {
            response = client.getAcsResponse(describeMetricListRequest);
        } catch (ClientException e) {
            completableFuture.completeExceptionally(e);
        }
        String datapoints = response.getDatapoints();
        List<Map<String, String>> list = JsonUtils.readValue(new TypeReference<List<Map<String, String>>>() {
        }, datapoints);

        List<MetricChartLine> lines = list.stream().map(map ->
                MetricChartLine.builder()
                        .time(DateUtil.date(Long.valueOf(map.get("timestamp"))))
                        .value(map.get("Average"))
                        .category(JsonUtils.writeValueAsString(map))
                        .build()
        ).collect(Collectors.toList());

        metricChartLineResponse.setMetricChartLines(lines);

        completableFuture.complete(metricChartLineResponse);
        return completableFuture;
    }

    @Override
    public void afterPropertiesSet() {
        DefaultProfile profile = DefaultProfile.getProfile("cn-hangzhou", ak, sk);
        client = new DefaultAcsClient(profile);
    }
}
