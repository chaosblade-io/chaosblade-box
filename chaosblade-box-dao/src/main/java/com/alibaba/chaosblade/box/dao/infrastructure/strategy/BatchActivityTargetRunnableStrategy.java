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

import com.alibaba.chaosblade.box.common.app.sdk.ChaosAppResponse;
import com.alibaba.chaosblade.box.common.app.sdk.scope.Host;
import com.alibaba.chaosblade.box.common.experiment.task.flow.ActivityTaskExecutionAttributes;
import com.alibaba.chaosblade.box.common.experiment.task.flow.exception.ExceptionHelper;
import com.alibaba.chaosblade.box.common.experiment.task.flow.util.concurrent.InvokeTracer;
import com.alibaba.chaosblade.box.common.experiment.task.flow.util.concurrent.TraceCallable;
import com.alibaba.chaosblade.box.common.infrastructure.util.concurrent.ExecutorAction;
import com.alibaba.chaosblade.box.common.infrastructure.util.concurrent.ExecutorUtil;
import com.alibaba.chaosblade.box.dao.infrastructure.app.MiniAppInvokerFactory;
import com.alibaba.chaosblade.box.dao.infrastructure.experiment.task.flow.step.ActivityInvokeContext;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.function.Function;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 并发跑
 *
 * @author haibin
 */
@Component
@Slf4j
public class BatchActivityTargetRunnableStrategy extends ActivityTargetsRunnableStrategy {

  @Autowired private InvokeTracer invokeTracer;

  @Override
  protected List<ChaosAppResponse> internalRun(
      ActivityInvokeContext activityInvokeContext, List<Host> invokeHosts) {
    int batchSize = calcBatchSize(activityInvokeContext);
    return ExecutorUtil.execute(
        new ExecutorAction<ChaosAppResponse>() {
          @Override
          public List<Callable<ChaosAppResponse>> getCallables() {
            return invokeHosts.stream()
                .map(
                    (Function<Host, Callable<ChaosAppResponse>>)
                        host ->
                            new TraceCallable<>(
                                invokeTracer,
                                new ActivityTargetCallable(host, activityInvokeContext)))
                .collect(Collectors.toList());
          }

          public ChaosAppResponse errorHandle(Callable<ChaosAppResponse> callable, Throwable e) {
            TraceCallable<ChaosAppResponse> traceCallable =
                (TraceCallable<ChaosAppResponse>) callable;
            ActivityTargetCallable activityTargetCallable =
                (ActivityTargetCallable) traceCallable.getCallable();
            ChaosAppResponse chaosAppResponse = new ChaosAppResponse(false);
            chaosAppResponse.setScope(activityTargetCallable.host);
            chaosAppResponse.setErrorMessage(ExceptionHelper.detailedMessage(e));
            return chaosAppResponse;
          }
        },
        batchSize);
  }

  @Override
  public boolean support(ActivityInvokeContext activityInvokeContext) {
    return ActivityTaskExecutionAttributes.ATTRIBUTE_VALUE_ACTIVITY_RUNNABLE_STRATEGY_BATCH.equals(
            activityInvokeContext
                .getContextData()
                .getAsString(
                    ActivityTaskExecutionAttributes.ATTRIBUTE_KEY_ACTIVITY_RUNNABLE_STRATEGY))
        && activityInvokeContext
            .getContextData()
            .containsKey(
                ActivityTaskExecutionAttributes.ATTRIBUTE_KEY_ACTIVITY_RUNNABLE_BATCH_SIZE);
  }

  protected int calcBatchSize(ActivityInvokeContext activityInvokeContext) {
    return activityInvokeContext.getContextData().getMiniAppBatchCount();
  }

  private static class ActivityTargetCallable implements Callable<ChaosAppResponse> {

    private Host host;

    private ActivityInvokeContext activityInvokeContext;

    public ActivityTargetCallable(Host host, ActivityInvokeContext activityInvokeContext) {
      this.host = host;
      this.activityInvokeContext = activityInvokeContext;
    }

    @Override
    public ChaosAppResponse call() throws Exception {
      return MiniAppInvokerFactory.createInvoker(this.activityInvokeContext, host).invoke();
    }
  }
}
