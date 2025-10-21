/*
 * Copyright 2025 The ChaosBlade Authors
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

package com.alibaba.chaosblade.box.common.common.domain;

import lombok.Data;

/** @author haibin */
@Data
public class Response<T> {

  private T result;

  private boolean success;

  /** error errorCode */
  private ChaosError error;

  /** 获取traceId */
  private String traceId;

  public static <T> Response<T> okWithData(T t) {
    return withData(t, true);
  }

  public static Response<Void> ok() {
    Response<Void> response = new Response<>();
    response.setSuccess(true);
    return response;
  }

  public static <T> Response<T> copy(Response<T> source) {
    Response<T> r = new Response<>();
    r.setResult(source.getResult());
    r.setError(source.getError());
    r.setSuccess(source.success);
    r.setTraceId(source.traceId);
    return r;
  }

  public static <T> Response<T> failedWith(ChaosError error) {
    Response<T> r = new Response<>();
    r.setSuccess(false);
    r.setError(error);
    return r;
  }

  public static <T> Response<T> failedWith(IErrorCode iErrorCode) {
    Response<T> r = new Response<>();
    r.setSuccess(false);
    r.setError(new ChaosError(iErrorCode));
    return r;
  }

  public static <T> Response<T> failedWith(IErrorCode iErrorCode, String message) {
    Response<T> r = new Response<>();
    r.setSuccess(false);
    r.setError(new ChaosError(iErrorCode, message));
    return r;
  }

  public static <T> Response<T> withData(T t, boolean success) {
    Response<T> r = new Response<>();
    r.setResult(t);
    r.setSuccess(success);
    return r;
  }

  public void error(ChaosError error) {
    this.setSuccess(false);
    this.setError(error);
  }
}
