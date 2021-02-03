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

package com.alibaba.chaosblade.platform.http;

import cn.hutool.core.util.StrUtil;
import com.alibaba.chaosblade.platform.cmmon.enums.DeviceType;
import com.alibaba.chaosblade.platform.cmmon.utils.JsonUtils;
import com.alibaba.chaosblade.platform.cmmon.utils.Reflects;
import com.alibaba.chaosblade.platform.invoker.ChaosInvoker;
import com.alibaba.chaosblade.platform.invoker.ChaosInvokerStrategy;
import com.alibaba.chaosblade.platform.invoker.ResponseCommand;
import com.alibaba.chaosblade.platform.http.constant.Header;
import com.alibaba.chaosblade.platform.http.model.reuest.ChaosBladeRequest;
import com.alibaba.chaosblade.platform.http.model.reuest.HttpChannelRequest;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.concurrent.FutureCallback;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.nio.client.CloseableHttpAsyncClient;
import org.apache.http.impl.nio.client.HttpAsyncClients;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.concurrent.CompletableFuture;

import static com.alibaba.chaosblade.platform.http.constant.Header.CMD;

/**
 * @author yefei
 */
@Slf4j
@Component
@ChaosInvokerStrategy(deviceType = DeviceType.HOST)
public class ChaosBladeHttpInvoker implements ChaosInvoker<HttpChannelRequest, ResponseCommand>, InitializingBean, DisposableBean {

    public static final String CHAOSBLADE_HANDLER = "/chaosblade";

    private CloseableHttpAsyncClient httpclient;

    @Override
    public void destroy() throws Exception {
        httpclient.close();
    }

    @Override
    public void afterPropertiesSet() {
        RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(3000).setConnectTimeout(3000).build();
        httpclient = HttpAsyncClients.custom().setDefaultRequestConfig(requestConfig).build();
        httpclient.start();
    }

    @Override
    public CompletableFuture<ResponseCommand> invoke(HttpChannelRequest requestCommand) {
        requestCommand.addParam(Header.TIMESTAMP_KEY, String.valueOf(System.nanoTime() / 1000));
        if (requestCommand instanceof ChaosBladeRequest) {
            requestCommand.addParam(CMD, ((ChaosBladeRequest) requestCommand).buildCommand());
        }

        String domain = requestCommand.getHost() + ":" + requestCommand.getPort();

        HttpPost httpPost;
        if (StrUtil.isBlank(requestCommand.getRequestURL())) {
            httpPost = new HttpPost("http://" + domain + CHAOSBLADE_HANDLER);
        } else {
            httpPost = new HttpPost("http://" + domain + requestCommand.getRequestURL());
        }

        httpPost.setHeader("Accept", "application/json;charset=UTF-8");
        httpPost.setHeader("Content-Type", "application/x-www-form-urlencoded");

        StringEntity stringEntity = new StringEntity(JsonUtils.writeValueAsString(requestCommand), Charset.defaultCharset());
        httpPost.setEntity(stringEntity);

        if (requestCommand.getTimeout() != null) {
            httpPost.setConfig(RequestConfig.custom().setSocketTimeout(requestCommand.getTimeout().intValue()).build());
        }

        CompletableFuture<ResponseCommand> completableFuture = new CompletableFuture<>();
        Class<?> classGeneric = Reflects.getInterfaceGeneric(this, 0, 1);
        httpclient.execute(httpPost, new FutureCallback<HttpResponse>() {
            @Override
            public void completed(HttpResponse httpResponse) {
                HttpEntity entity = httpResponse.getEntity();
                try {
                    ResponseCommand v = (ResponseCommand) JsonUtils.readValue(classGeneric,  EntityUtils.toByteArray(entity));
                    completableFuture.complete(v);
                } catch (IOException e) {
                    completableFuture.completeExceptionally(e);
                }
            }

            @Override
            public void failed(Exception e) {
                completableFuture.completeExceptionally(e);
            }

            @Override
            public void cancelled() {
                completableFuture.completeExceptionally(new InterruptedException());
            }
        });
        return completableFuture;
    }
}
