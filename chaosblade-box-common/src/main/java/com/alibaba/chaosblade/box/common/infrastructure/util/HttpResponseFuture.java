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

package com.alibaba.chaosblade.box.common.infrastructure.util;

import java.util.function.Consumer;
import java.util.function.Function;
import lombok.Setter;

/** @author haibin */
@Setter
public class HttpResponseFuture<T> {

  private T response;

  private Throwable throwable;

  public T get() {
    return response;
  }

  public <R> R apply(Function<T, R> function) {
    if (response != null) {
      return function.apply(response);
    }
    return null;
  }

  public HttpResponseFuture<T> onThrowable(Consumer<Throwable> throwableConsumer) {
    if (throwable != null) {
      throwableConsumer.accept(throwable);
    }
    return this;
  }

  public HttpResponseFuture<T> onResponse(Consumer<T> responseConsumer) {
    if (response != null) {
      responseConsumer.accept(response);
    }
    return this;
  }
}
