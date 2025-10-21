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

package com.alibaba.chaosblade.box.dao.infrastructure.experiment.task.flow;

import com.alibaba.chaosblade.box.common.experiment.task.flow.step.OnceInvoke;
import com.alibaba.chaosblade.box.dao.infrastructure.experiment.task.flow.step.InvokeContext;
import com.alibaba.chaosblade.box.dao.infrastructure.experiment.task.interceptor.InvokeInterceptor;
import java.util.List;

/** @author haibin */
public class InterceptorInvoker<T extends InvokeInterceptor<Re, Rs>, Re extends InvokeContext, Rs> {

  private InvokeInterceptorExecutionChain<T, Re, Rs> invokeInterceptorExecutionChain;

  public InterceptorInvoker(List<T> invokeInterceptors) {
    invokeInterceptorExecutionChain = new InvokeInterceptorExecutionChain<>();
    invokeInterceptorExecutionChain.addInterceptors(invokeInterceptors);
  }

  public Rs invoke(OnceInvoke<Re, Rs> onceInvoke, Re re, Rs defaultResult) {
    Throwable throwable = null;
    try {
      if (invokeInterceptorExecutionChain.applyPreHandle(re, defaultResult)) {
        this.invokeInterceptorExecutionChain.applyPostHandle(onceInvoke, re, defaultResult);
      }
    } catch (Exception exception) {
      throwable = exception;
    }
    this.invokeInterceptorExecutionChain.applyAfterHandle(re, defaultResult, throwable);
    return defaultResult;
  }
}
