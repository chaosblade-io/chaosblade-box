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
public class ChaosFlowConfig implements FlowEngineConfig {

  public void setChaosBladeListener(ChaosBladeListener chaosBladeListener) {
    this.chaosBladeListener = chaosBladeListener;
  }

  public void setChaosBladeInvoker(ChaosBladeInvoker chaosBladeInvoker) {
    this.chaosBladeInvoker = chaosBladeInvoker;
  }

  public void setLitmusChaosInvoker(LitmusChaosInvoker litmusChaosInvoker) {
    this.litmusChaosInvoker = litmusChaosInvoker;
  }

  private ChaosBladeInvoker chaosBladeInvoker;

  private LitmusChaosInvoker litmusChaosInvoker;

  protected ChaosBladeListener chaosBladeListener;

  private LitmusChaosListener litmusChaosListener;

  public void setMiniAppInvokers(List<MiniAppInvoker> miniAppInvokers) {
    this.miniAppInvokers = miniAppInvokers;
  }

  private List<MiniAppInvoker> miniAppInvokers;

  public void setActivityTargetsRunnableStrategies(
      List<ActivityTargetsRunnableStrategy> activityTargetsRunnableStrategies) {
    this.activityTargetsRunnableStrategies = activityTargetsRunnableStrategies;
  }

  private List<ActivityTargetsRunnableStrategy> activityTargetsRunnableStrategies;

  public void setChaosAppLoaderConfig(ChaosAppLoaderConfig chaosAppLoaderConfig) {
    this.chaosAppLoaderConfig = chaosAppLoaderConfig;
  }

  private ChaosAppLoaderConfig chaosAppLoaderConfig;

  @Override
  public ChaosBladeListener getChaosBladeListener() {
    return this.chaosBladeListener;
  }

  @Override
  public LitmusChaosListener getLitmusChaosListener() {
    return litmusChaosListener;
  }

  @Override
  public ChaosAppLoaderConfig getChaosAppLoaderConfig() {
    return this.chaosAppLoaderConfig;
  }

  @Override
  public List<ActivityTargetsRunnableStrategy> getActivityTargetsRunnableStrategies() {
    return this.activityTargetsRunnableStrategies;
  }

  @Override
  public List<MiniAppInvoker> getMiniAppInvokers() {
    return this.miniAppInvokers;
  }

  @Override
  public ChaosBladeInvoker getChaosBladeInvoker() {
    return this.chaosBladeInvoker;
  }

  @Override
  public LitmusChaosInvoker getLitmusChaosInvoker() {
    return litmusChaosInvoker;
  }

  public void setLitmusChaosListener(LitmusChaosListener litmusChaosListener) {
    this.litmusChaosListener = litmusChaosListener;
  }
}
