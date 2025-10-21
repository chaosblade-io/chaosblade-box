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

import com.alibaba.chaosblade.box.dao.infrastructure.app.ChaosAppLoaderConfig;
import com.alibaba.chaosblade.box.dao.infrastructure.app.chaosblade.ChaosBladeInvoker;
import com.alibaba.chaosblade.box.dao.infrastructure.app.chaosblade.ChaosBladeListener;
import com.alibaba.chaosblade.box.dao.infrastructure.app.litmuschaos.LitmusChaosInvoker;
import com.alibaba.chaosblade.box.dao.infrastructure.app.litmuschaos.LitmusChaosListener;
import com.alibaba.chaosblade.box.dao.infrastructure.experiment.task.flow.invoker.MiniAppInvoker;
import com.alibaba.chaosblade.box.dao.infrastructure.strategy.ActivityTargetsRunnableStrategy;
import java.util.List;

/** @author haibin */
public interface FlowEngineConfig {

  /**
   * get chaosblade listener
   *
   * @return listener
   */
  ChaosBladeListener getChaosBladeListener();

  /** @return */
  LitmusChaosListener getLitmusChaosListener();

  /**
   * get chaos apploader config
   *
   * @return
   */
  ChaosAppLoaderConfig getChaosAppLoaderConfig();

  List<ActivityTargetsRunnableStrategy> getActivityTargetsRunnableStrategies();

  List<MiniAppInvoker> getMiniAppInvokers();

  ChaosBladeInvoker getChaosBladeInvoker();

  LitmusChaosInvoker getLitmusChaosInvoker();
}
