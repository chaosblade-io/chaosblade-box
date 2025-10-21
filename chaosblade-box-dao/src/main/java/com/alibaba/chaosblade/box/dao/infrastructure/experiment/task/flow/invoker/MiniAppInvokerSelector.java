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

package com.alibaba.chaosblade.box.dao.infrastructure.experiment.task.flow.invoker;

import com.alibaba.chaosblade.box.dao.infrastructure.experiment.task.flow.step.MiniAppInvokeContext;
import java.util.List;
import java.util.Objects;
import java.util.function.Predicate;

/** @author haibin */
public class MiniAppInvokerSelector {

  private static MiniAppInvoker Default = new DefaultChaosAppInvoker();

  private List<MiniAppInvoker> miniAppInvokers;

  public MiniAppInvokerSelector(List<MiniAppInvoker> miniAppInvokers) {
    this.miniAppInvokers = Objects.requireNonNull(miniAppInvokers);
  }

  public MiniAppInvoker select(MiniAppInvokeContext miniAppInvokeContext) {
    return this.miniAppInvokers.stream()
        .filter(
            new Predicate<MiniAppInvoker>() {
              @Override
              public boolean test(MiniAppInvoker miniAppInvoker) {
                return miniAppInvoker.support(miniAppInvokeContext);
              }
            })
        .findFirst()
        .orElse(Default);
  }
}
