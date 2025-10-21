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

package com.alibaba.chaosblade.box.dao.infrastructure.app.chaosblade;

import com.alibaba.chaosblade.box.common.experiment.task.flow.ChaosBladeAppResponse;
import com.alibaba.chaosblade.box.dao.infrastructure.experiment.task.flow.step.MiniAppInvokeContext;
import com.alibaba.chaosblade.box.dao.model.ChaosBladeExpUidDO;
import org.springframework.stereotype.Component;

/** @author haibin */
@Component
public class EmptyChaosBladeMiniAppInterceptor implements ChaosBladeMiniAppInterceptor {

  @Override
  public void preCreate(
      ChaosBladeExpUidDO chaosBladeExpUidDO,
      MiniAppInvokeContext miniAppInvokeContext,
      ChaosBladeAppResponse chaosAppResponse) {}

  @Override
  public void afterCreate(
      ChaosBladeExpUidDO chaosBladeExpUidDO,
      MiniAppInvokeContext miniAppInvokeContext,
      ChaosBladeAppResponse chaosAppResponse) {}

  @Override
  public void preUpdate(
      ChaosBladeExpUidDO chaosBladeExpUidDO,
      MiniAppInvokeContext miniAppInvokeContext,
      ChaosBladeAppResponse chaosAppResponse) {}
}
