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

package com.alibaba.chaosblade.box.common.commands.interceptor;

import com.alibaba.chaosblade.box.common.commands.Command;
import com.alibaba.chaosblade.box.common.commands.InvocationCommand;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

/** @author haibin.lhb */
@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class MetricCommandInterceptor implements CommandInterceptor {

  @Override
  public void onStarted(Command<?> command) {}

  @Override
  public void onReturn(InvocationCommand command, Object result) {
    // ChaosApplicationMetric.getCommandInvocationCompass().record(command.getCost(TimeUnit.MILLISECONDS),
    //    ChaosApplicationMetric.SUCCESS);
  }

  @Override
  public void onError(InvocationCommand command, Throwable throwable) {
    // ChaosApplicationMetric.getCommandInvocationCompass().record(command.getCost(TimeUnit.MILLISECONDS),
    //    ChaosApplicationMetric.ERROR);
  }
}
