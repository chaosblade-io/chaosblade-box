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

import com.alibaba.chaosblade.platform.collector.Collector;
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
import org.springframework.beans.factory.annotation.Value;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

/**
 * @author yefei
 */
public abstract class AbstractCollector<T> implements Collector<T>, InitializingBean, DisposableBean {

    @Value("${chaos.collector.prometheus.api}")
    private String api;

    private CloseableHttpAsyncClient httpAsyncClient;

    public CompletableFuture<List<T>> collect(String query) {

        HttpPost httpPost = new HttpPost(api);
        List<NameValuePair> params = new ArrayList<>();
        params.add(new BasicNameValuePair("query", query));
        params.add(new BasicNameValuePair("start", String.valueOf(System.currentTimeMillis() / 1000)));
        params.add(new BasicNameValuePair("end", String.valueOf(System.currentTimeMillis() / 1000)));
        params.add(new BasicNameValuePair("step", "14"));

        try {
            httpPost.setEntity(new UrlEncodedFormEntity(params));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        CompletableFuture<List<T>> future = new CompletableFuture<>();
        httpAsyncClient.execute(httpPost, new FutureCallback<HttpResponse>() {
            @Override
            public void completed(HttpResponse httpResponse) {
                try {
                    HttpEntity entity = httpResponse.getEntity();
                    future.complete(pack(EntityUtils.toByteArray(entity)));
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
        httpAsyncClient.close();
    }

    @Override
    public void afterPropertiesSet() {
        RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(3000).setConnectTimeout(3000)
                .build();
        httpAsyncClient = HttpAsyncClients.custom().setDefaultRequestConfig(requestConfig).build();
        httpAsyncClient.start();
    }

    abstract List<T> pack(byte[] bytes);
}
