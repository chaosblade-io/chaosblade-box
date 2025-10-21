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

import com.alibaba.chaosblade.box.common.experiment.task.flow.exception.ActivityExecuteFailedException;
import com.alibaba.chaosblade.box.common.experiment.task.flow.step.activity.ActivityExecuteResult;
import com.alibaba.chaosblade.box.dao.infrastructure.experiment.task.flow.activity.ExperimentExecuteContext;
import com.alibaba.chaosblade.box.dao.infrastructure.experiment.task.flow.step.activity.Activity;

/**
 * main entrance for chaos,all operations for faultInjection are here
 *
 * @author haibin
 */
public class ChaosFlowEngine implements FlowEngine {

  private FlowEngineContext flowEngineContext;

  /**
   * init a chaos
   *
   * @param flowEngineConfig
   */
  public ChaosFlowEngine(FlowEngineConfig flowEngineConfig) {
    this.flowEngineContext = new FlowEngineContext(flowEngineConfig);
  }

  @Override
  public void init() throws Exception {
    flowEngineContext.init();
  }

  @Override
  public ActivityExecuteResult runActivity(
      Activity activity, ExperimentExecuteContext experimentExecuteContext)
      throws ActivityExecuteFailedException {
    return this.flowEngineContext
        .getActivityProcessor()
        .runActivity(activity, experimentExecuteContext);
  }

  @Override
  public FlowEngineContext getFlowEngineContext() {
    return this.flowEngineContext;
  }
}
