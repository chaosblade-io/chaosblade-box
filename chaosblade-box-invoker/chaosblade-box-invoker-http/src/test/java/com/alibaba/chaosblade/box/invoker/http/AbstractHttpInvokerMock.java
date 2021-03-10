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

package com.alibaba.chaosblade.box.invoker.http;

import com.alibaba.testable.core.annotation.MockConstructor;
import com.alibaba.testable.core.annotation.MockDiagnose;
import com.alibaba.testable.core.annotation.MockMethod;
import com.alibaba.testable.core.model.LogLevel;
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

import java.io.UnsupportedEncodingException;
import java.util.concurrent.Future;

/**
 * @author yefei
 */
@MockDiagnose(LogLevel.ENABLE)
public class AbstractHttpInvokerMock {

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
        StringEntity entity = new StringEntity("{\"Success\":true}");
        response.setEntity(entity);
        callback.completed(response);
        return null;
    }
}
