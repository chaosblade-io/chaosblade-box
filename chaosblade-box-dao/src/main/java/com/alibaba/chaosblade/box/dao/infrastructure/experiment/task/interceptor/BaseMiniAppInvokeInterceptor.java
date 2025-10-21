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

package com.alibaba.chaosblade.box.dao.infrastructure.experiment.task.interceptor;

import com.alibaba.chaosblade.box.common.app.sdk.ChaosAppResponse;
import com.alibaba.chaosblade.box.common.experiment.task.flow.step.OnceInvoke;
import com.alibaba.chaosblade.box.dao.infrastructure.app.ChaosAppResponseReference;
import com.alibaba.chaosblade.box.dao.infrastructure.experiment.task.flow.step.MiniAppInvokeContext;

/** @author haibin */
public abstract class BaseMiniAppInvokeInterceptor
    implements InvokeInterceptor<MiniAppInvokeContext, ChaosAppResponseReference> {
  @Override
  public Integer getOrder() {
    return 0;
  }

  @Override
  public boolean preHandle(
      MiniAppInvokeContext miniAppInvokeContext,
      ChaosAppResponseReference chaosAppResponseReference) {
    return preHandle(miniAppInvokeContext, chaosAppResponseReference.getChaosAppResponse());
  }

  protected boolean preHandle(
      MiniAppInvokeContext miniAppInvokeContext, ChaosAppResponse chaosAppResponse) {
    return true;
  }

  protected void afterHandle(
      MiniAppInvokeContext miniAppInvokeContext,
      ChaosAppResponse chaosAppResponse,
      Throwable throwable) {}

  @Override
  public void afterHandle(
      MiniAppInvokeContext miniAppInvokeContext,
      ChaosAppResponseReference chaosAppResponseReference,
      Throwable throwable) {
    afterHandle(miniAppInvokeContext, chaosAppResponseReference.getChaosAppResponse(), throwable);
  }

  @Override
  public void postHandle(
      OnceInvoke<MiniAppInvokeContext, ChaosAppResponseReference> onceInvoke,
      MiniAppInvokeContext miniAppInvokeContext,
      ChaosAppResponseReference chaosAppResponseReference) {}
}
