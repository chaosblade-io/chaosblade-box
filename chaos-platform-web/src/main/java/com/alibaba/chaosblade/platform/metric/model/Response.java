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

package com.alibaba.chaosblade.platform.metric.model;

import lombok.Data;

import java.io.Serializable;

/**
 * @param <T>
 * @author yefei
 */
@Data
public class Response<T> implements Serializable {

    private static final Integer SUCCESS_CODE = 2000;

    private static final Integer SYSTEM_ERROR_CODE = 6000;

    private Integer code = SUCCESS_CODE;

    private String message;

    private boolean success;

    private T data;

    private String requestId;

    public static <T> Response<T> ofSuccess(T t) {
        Response<T> response = new Response();
        response.setSuccess(true);
        response.setData(t);
        return response;
    }

    public static Response<?> ofFail(String message) {
        return ofFail(message, null);
    }

    public static Response<?> ofFail(String message, Integer code) {
        Response<?> response = new Response();
        if (code == null) {
            response.setCode(SYSTEM_ERROR_CODE);
        } else {
            response.setCode(code);
        }
        response.setMessage(message);
        return response;
    }
}
