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
import com.alibaba.chaosbox.metric.MetricChartLineRequest;
import com.alibaba.testable.core.annotation.MockConstructor;
import com.alibaba.testable.core.annotation.MockDiagnose;
import com.alibaba.testable.core.annotation.MockMethod;
import com.alibaba.testable.core.model.LogLevel;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.HttpVersion;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.concurrent.FutureCallback;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.DefaultHttpResponseFactory;
import org.apache.http.impl.nio.client.CloseableHttpAsyncClient;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpCoreContext;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.concurrent.Future;

/**
 * @author yefei
 */
@Slf4j
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = PrometheusServiceTest.Config.class)
public class PrometheusServiceTest {

    @Configuration
    @ComponentScan("com.alibaba.chaosbox.metric.prometheus")
    public static class Config {
    }

    @Autowired
    private PrometheusService prometheusService;

    @MockDiagnose(LogLevel.ENABLE)
    public static class Mock {

        @MockConstructor
        public HttpPost createHttpPost(String uri) {
            return new HttpPost("http://127.0.0.1/api");
        }

        @MockMethod
        public Future<HttpResponse> execute(
                CloseableHttpAsyncClient self,
                final HttpUriRequest request,
                final FutureCallback<HttpResponse> callback) throws UnsupportedEncodingException {
            DefaultHttpResponseFactory factory = DefaultHttpResponseFactory.INSTANCE;
            final BasicHttpContext localContext = new BasicHttpContext();
            final HttpCoreContext context = HttpCoreContext.adapt(localContext);
            HttpResponse response = factory.newHttpResponse(HttpVersion.HTTP_1_1,
                    HttpStatus.SC_CONTINUE, context);
            StringEntity entity = new StringEntity("{\"status\":\"success\",\"data\":{\"resultType\":\"matrix\",\"result\":[{\"metric\":{\"__name__\":\"node_memory_Buffers_bytes\",\"endpoint\":\"metrics\",\"instance\":\"172.19.128.194:9100\",\"job\":\"node-exporter\",\"namespace\":\"default\",\"pod\":\"chaosbox-prometheus-node-exporter-45825\",\"service\":\"chaosbox-prometheus-node-exporter\"},\"values\":[[1614840609.379,\"187949056\"],[1614840623.379,\"187949056\"]]}]}}");
            response.setEntity(entity);
            callback.completed(response);
            return null;
        }
    }

    @Test
    public void testAttack() throws Exception {
        MetricChartLineRequest metricChartLineRequest = MetricChartLineRequest.builder().build();
        metricChartLineRequest.setStartTime(DateUtil.date());
        metricChartLineRequest.setEndTime(DateUtil.date());
        metricChartLineRequest.setDevices(CollUtil.newArrayList());
        HashMap<String, String> params = new HashMap<>();
        params.put("url", "abc");
        params.put("query", "node_memory_Buffers_bytes");
        metricChartLineRequest.setParams(params);

        prometheusService.selectChartLine(metricChartLineRequest);
    }
}
