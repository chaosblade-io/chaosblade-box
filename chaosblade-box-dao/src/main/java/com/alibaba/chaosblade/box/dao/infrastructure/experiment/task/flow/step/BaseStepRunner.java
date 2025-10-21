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

import com.alibaba.chaosblade.box.common.experiment.task.flow.BaseRunRequest;
import com.alibaba.chaosblade.box.common.experiment.task.flow.step.StepExecuteResult;
import com.alibaba.chaosblade.box.dao.infrastructure.experiment.task.flow.FlowEngineContext;

/** @author haibin */
public abstract class BaseStepRunner<Re extends BaseRunRequest, Rs extends StepExecuteResult> {
  protected FlowEngineContext flowEngineContext;

  public BaseStepRunner(FlowEngineContext flowEngineContext) {
    this.flowEngineContext = flowEngineContext;
  }

  public Rs run(Re re) {
    StepExecuteContext stepExecuteContext = buildContext(re);
    return innerRun(stepExecuteContext);
  }

  protected abstract StepExecuteContext buildContext(Re re);

  protected abstract Rs innerRun(StepExecuteContext context);
}
