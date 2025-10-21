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

/** @author haibin */
public class InvokeContextFactory {

  public static <T extends InvokeContext> T create(
      StepExecuteContext stepExecuteContext, Class<T> tClass) {
    try {
      T t = tClass.newInstance();
      t.setStepExecuteContext(stepExecuteContext);
      return t;
    } catch (Throwable e) {
      throw new RuntimeException(
          "create invokeContext failed,must have no argument Constructor context class:" + tClass);
    }
  }
}
