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

package com.alibaba.chaosblade.box.common.experiment.activity.execute;

import com.alibaba.chaosblade.box.common.infrastructure.ChaosApplicationContext;
import com.alibaba.chaosblade.box.common.infrastructure.ChaosRequestContextHolder;
import java.util.concurrent.Callable;
import java.util.concurrent.FutureTask;
import org.jetbrains.annotations.NotNull;

/** @author haibin.lhb */
public class ActivityTaskFutureTask<T> extends FutureTask<T> {

  final Object rpcContext = null;

  final ChaosApplicationContext chaosApplicationContext;

  final ActivityTaskCallable<T> activityTaskCallable;

  public void setRunnableThread(Thread runnableThread) {
    this.runnableThread = runnableThread;
  }

  private Thread runnableThread;

  public ActivityTaskFutureTask(@NotNull Callable<T> callable) {
    super(callable);
    //        rpcContext = EagleEye.currentRpcContext();
    activityTaskCallable = (ActivityTaskCallable<T>) callable;
    chaosApplicationContext = ChaosRequestContextHolder.getApplicationContext().orElse(null);
  }

  public String getId() {
    return activityTaskCallable.getId();
  }

  @Override
  public void run() {
    //        EagleEye.setRpcContext(rpcContext);
    ChaosRequestContextHolder.setApplicationContext(chaosApplicationContext);
    try {
      super.run();
    } finally {
      //            EagleEye.clearRpcContext();
      ChaosRequestContextHolder.resetApplicationContextContext();
    }
  }

  @Override
  public boolean cancel(boolean mayInterruptIfRunning) {
    if (runnableThread != null && runnableThread == Thread.currentThread()) {
      this.runnableThread.interrupt();
    }
    return super.cancel(mayInterruptIfRunning);
  }
}
