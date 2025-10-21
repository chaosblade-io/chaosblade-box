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

package com.alibaba.chaosblade.box.common.experiment.task.flow.step;

import com.alibaba.chaosblade.box.common.experiment.task.flow.util.NamedType;

/**
 * all nodes in experiment we call them "step",so step has two types, one type is phase,like
 * 'prepare,attack,check,recovery',it can't attack directly, Another type is called activity,it
 * represents the steps that can be performed, There are many ways to implement activities，like
 * PluginActivity,ComponentActivity and so on
 *
 * @author haibin
 */
public interface Step {

  enum StepType implements NamedType {

    /** phase step */
    PHASE,
    /** activity step */
    ACTIVITY
  }

  /**
   * unique id for step
   *
   * @return
   */
  public String getId();

  /**
   * stepType,phase or activity
   *
   * @return stepType {@link StepType}
   */
  public StepType getStepType();
}
