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

/** @author haibin */
public interface FlowEngine {

  public void init() throws Exception;

  /**
   * run specified activity in a experiment
   *
   * @param experimentExecuteContext experiment you want to run
   * @return attack result
   */
  public ActivityExecuteResult runActivity(
      Activity activity, ExperimentExecuteContext experimentExecuteContext)
      throws ActivityExecuteFailedException;

  /**
   * flow engine context
   *
   * @return
   */
  FlowEngineContext getFlowEngineContext();
}
