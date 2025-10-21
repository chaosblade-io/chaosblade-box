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

package com.alibaba.chaosblade.box.dao.infrastructure.experiment.task.flow.invoker;

import com.alibaba.chaosblade.box.common.app.sdk.ChaosAppResponse;
import com.alibaba.chaosblade.box.dao.infrastructure.app.ChaosAppResponseReference;
import com.alibaba.chaosblade.box.dao.infrastructure.experiment.task.flow.step.MiniAppInvokeContext;

/** @author haibin */
public class DefaultChaosAppInvoker implements MiniAppInvoker {

  @Override
  public boolean support(MiniAppInvokeContext miniAppInvokeContext) {
    return true;
  }

  @Override
  public ChaosAppResponseReference invoke(MiniAppInvokeContext miniAppInvokeContext) {
    ChaosAppResponse chaosAppResponse = ChaosAppInvoker.invokeFromMiniApp(miniAppInvokeContext);
    ChaosAppResponseReference chaosAppResponseReference = new ChaosAppResponseReference();
    chaosAppResponseReference.setChaosAppResponse(chaosAppResponse);
    return chaosAppResponseReference;
  }
}
