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

package com.alibaba.chaosblade.box.dao.infrastructure.experiment.task.miniapp.chain;

import com.alibaba.chaosblade.box.common.app.sdk.ChaosAppResponse;
import com.alibaba.chaosblade.box.common.infrastructure.OnceInvokeReference;
import com.alibaba.chaosblade.box.common.infrastructure.chain.ChainContext;
import com.alibaba.chaosblade.box.dao.infrastructure.app.ChaosAppResponseReference;
import com.alibaba.chaosblade.box.dao.infrastructure.experiment.task.flow.step.MiniAppInvokeContext;
import lombok.Getter;
import lombok.Setter;

/** @author haibin */
public class ActivityTargetRetryInvokeContext implements ChainContext {

  @Getter private ChaosAppResponse preAppResponse;

  @Getter
  private OnceInvokeReference<MiniAppInvokeContext, ChaosAppResponseReference> invokeReference;

  @Getter private MiniAppInvokeContext miniAppInvokeContext;

  @Setter @Getter private ChaosAppResponse curAppResponse;

  public ActivityTargetRetryInvokeContext(
      OnceInvokeReference<MiniAppInvokeContext, ChaosAppResponseReference> onceInvokeReference,
      MiniAppInvokeContext miniAppInvokeContext,
      ChaosAppResponse preAppResponse) {
    this.invokeReference = onceInvokeReference;
    this.miniAppInvokeContext = miniAppInvokeContext;
    this.preAppResponse = preAppResponse;
    this.curAppResponse = preAppResponse;
  }
}
