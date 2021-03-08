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

package com.alibaba.chaosbox.toolsmgr.api;

import lombok.Data;

/**
 * @author yefei
 */
@Data
public class Response<T> {

    private boolean success;

    private String message;

    private T result;

    public static <T> Response<T> ofSuccess(T t) {
        Response<T> response = new Response();
        response.setSuccess(true);
        response.setResult(t);
        return response;
    }

    public static <T> Response<T> ofFail(String message) {
        Response<T> response = new Response();
        response.setMessage(message);
        return response;
    }
}
