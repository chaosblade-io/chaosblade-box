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

package com.alibaba.chaosblade.box.dao.infrastructure.strategy;

import com.alibaba.chaosblade.box.common.app.sdk.scope.Host;
import com.alibaba.chaosblade.box.common.config.DefaultChaosProperties;
import com.alibaba.chaosblade.box.common.experiment.task.flow.ActivityTaskExecutionAttributes;
import com.alibaba.chaosblade.box.dao.infrastructure.experiment.task.flow.step.ActivityInvokeContext;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

/** @author haibin */
@Component
@Order(1000)
public class DynamicBatchActivityTargetRunnableStrategy
    extends BatchActivityTargetRunnableStrategy {

  @Autowired private DefaultChaosProperties chaosProperties;

  @Override
  public boolean support(ActivityInvokeContext activityInvokeContext) {
    return ActivityTaskExecutionAttributes.ATTRIBUTE_VALUE_ACTIVITY_RUNNABLE_STRATEGY_BATCH.equals(
            activityInvokeContext
                .getContextData()
                .getAsString(
                    ActivityTaskExecutionAttributes.ATTRIBUTE_KEY_ACTIVITY_RUNNABLE_STRATEGY))
        && !activityInvokeContext
            .getContextData()
            .containsKey(
                ActivityTaskExecutionAttributes.ATTRIBUTE_KEY_ACTIVITY_RUNNABLE_BATCH_SIZE);
  }

  @Override
  protected int calcBatchSize(ActivityInvokeContext activityInvokeContext) {
    List<Host> hostList = activityInvokeContext.getActivity().getScope();
    return Math.min(hostList.size(), chaosProperties.getMiniAppMaxBatchSize());
  }
}
