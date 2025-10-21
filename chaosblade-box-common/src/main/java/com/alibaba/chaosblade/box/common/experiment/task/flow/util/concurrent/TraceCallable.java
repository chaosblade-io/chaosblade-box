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

package com.alibaba.chaosblade.box.common.experiment.task.flow.util.concurrent;

import java.util.Map;
import java.util.concurrent.Callable;
import lombok.Getter;

/** @author haibin */
@Getter
public class TraceCallable<V> implements Callable<V> {

  public InvokeTracer invokeTracer;

  private Callable<V> callable;

  private Map<String, String> context;

  public TraceCallable(InvokeTracer invokeTracer, Callable<V> callable) {
    this.invokeTracer = invokeTracer;
    this.callable = callable;
    this.context = invokeTracer.getCopy();
  }

  @Override
  public V call() throws Exception {
    invokeTracer.putContext(this.context);
    return this.callable.call();
  }
}
