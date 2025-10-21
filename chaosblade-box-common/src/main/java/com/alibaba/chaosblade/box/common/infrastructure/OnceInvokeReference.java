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

package com.alibaba.chaosblade.box.common.infrastructure;

import com.alibaba.chaosblade.box.common.experiment.task.flow.step.OnceInvoke;
import java.util.concurrent.atomic.AtomicBoolean;

/** @author haibin */
public class OnceInvokeReference<Re, Rs> implements OnceInvoke<Re, Rs> {

  private AtomicBoolean invoked = new AtomicBoolean(false);

  private OnceInvoke<Re, Rs> onceInvoke;

  public OnceInvokeReference(OnceInvoke<Re, Rs> onceInvoke) {
    this.onceInvoke = onceInvoke;
  }

  public boolean isInvoked() {
    return this.invoked.get();
  }

  @Override
  public Rs invoke(Re re) {
    if (this.invoked.get()) {
      throw new IllegalStateException("the method only invoke once");
    }
    this.invoked.set(true);
    return this.onceInvoke.invoke(re);
  }
}
