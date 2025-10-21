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

import com.alibaba.chaosblade.box.common.experiment.task.flow.util.HashMapSettings;
import java.util.HashMap;

/** @author haibin */
public class InvokeContext {

  public StepExecuteContext getStepExecuteContext() {
    return stepExecuteContext;
  }

  public void setStepExecuteContext(StepExecuteContext stepExecuteContext) {
    this.stepExecuteContext = stepExecuteContext;
  }

  private StepExecuteContext stepExecuteContext;

  public HashMapSettings getTempData() {
    return tempData;
  }

  /** 临时性的上下文,用来当做本次活动或者小程序调用时候的上下文用,不会作为整个演练的上下文保存 */
  private HashMapSettings tempData;

  public InvokeContext() {
    tempData = new HashMapSettings(new HashMap<>());
  }
}
