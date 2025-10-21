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

package com.alibaba.chaosblade.box.dao.infrastructure.experiment.task.flow.step;

import com.alibaba.chaosblade.box.common.app.sdk.ChaosAppContext;
import com.alibaba.chaosblade.box.common.app.sdk.constants.PhaseType;
import com.alibaba.chaosblade.box.dao.infrastructure.experiment.task.flow.FlowEngineContext;
import com.alibaba.chaosblade.box.dao.infrastructure.experiment.task.flow.activity.ExperimentExecuteContext;
import lombok.Getter;

/** @author haibin */
public class StepExecuteContext {

  /** 用户数据 */
  public StepExecuteContext(
      FlowEngineContext flowEngineContext, ExperimentExecuteContext experimentExecuteContext) {
    this.flowEngineContext = flowEngineContext;
    this.request = experimentExecuteContext;
  }

  @Getter private ExperimentExecuteContext request;

  public PhaseType getPhase() {
    return getChaosAppContext().getPhase();
  }

  @Getter private transient FlowEngineContext flowEngineContext;

  public ChaosAppContext getChaosAppContext() {
    return chaosAppContext;
  }

  public void setChaosAppContext(ChaosAppContext chaosAppContext) {
    this.chaosAppContext = chaosAppContext;
  }

  private ChaosAppContext chaosAppContext;

  public String getActivityTaskId() {
    return request.getActivityTaskId();
  }
}
