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

package com.alibaba.chaosblade.platform.http.model.reuest;

import cn.hutool.core.util.StrUtil;
import com.alibaba.chaosblade.platform.invoker.RequestCommand;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.HashMap;
import java.util.Map;

/**
 * @author yefei
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class HttpChannelRequest extends RequestCommand {

    private static final String FROM_HEADER = "FR";
    private static final String CLIENT = "C";
    private static final String SERVER = "S";
    protected final Map<String, String> headers;
    protected final Map<String, String> params;

    protected String host;

    protected int port;

    protected String requestURL;

    public HttpChannelRequest() {
        this(false);
    }

    public HttpChannelRequest(boolean isClient) {
        this.headers = new HashMap();
        this.params = new HashMap();
        if (isClient) {
            this.headers.put(FROM_HEADER, CLIENT);
        } else {
            this.headers.put(FROM_HEADER, SERVER);
        }
    }

    public Map<String, String> getHeaders() {
        return this.headers;
    }

    public Map<String, String> getParams() {
        return this.params;
    }

    public String getHeader(String key) {
        return this.headers.get(key);
    }

    public String getParam(String key) {
        return this.params.get(key);
    }

    public void removeHeader(String key) {
        this.headers.remove(key);
    }

    public void removeParam(String key) {
        this.params.remove(key);
    }

    public boolean fromClient() {
        return CLIENT.equals(this.getHeader(FROM_HEADER));
    }

    public boolean fromServer() {
        return SERVER.equals(this.getHeader(FROM_HEADER));
    }

    public RequestCommand addHeader(String key, String value) {
        if (StrUtil.isBlank(key)) {
            throw new IllegalArgumentException("Parameter key cannot be empty");
        } else {
            this.headers.put(key, value);
            return this;
        }
    }

    public RequestCommand addParam(String key, String value) {
        if (StrUtil.isBlank(key)) {
            throw new IllegalArgumentException("Parameter key cannot be empty");
        } else {
            this.params.put(key, value);
            return this;
        }
    }

}
