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

import java.util.concurrent.*;

/** @author haibin.lhb */
public class ActivityTaskThreadPoolExecutor extends ThreadPoolExecutor {

  private TaskQueue taskQueue;

  public ActivityTaskThreadPoolExecutor(
      int corePoolSize,
      int maximumPoolSize,
      long keepAliveTime,
      TimeUnit unit,
      TaskQueue taskQueue,
      BlockingQueue<Runnable> workQueue,
      ThreadFactory threadFactory,
      RejectedExecutionHandler handler) {
    super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue, threadFactory, handler);
    this.taskQueue = taskQueue;
  }

  @Override
  protected void beforeExecute(Thread t, Runnable r) {
    super.beforeExecute(t, r);
    ActivityTaskFutureTask activityTaskFutureTask = (ActivityTaskFutureTask) r;
    activityTaskFutureTask.setRunnableThread(t);
    TaskQueue.RunningItem runningItem = new TaskQueue.RunningItem(activityTaskFutureTask);
    runningItem.enter(taskQueue);
    taskQueue.removeWaitingItem(activityTaskFutureTask);
  }

  @Override
  protected void afterExecute(Runnable r, Throwable t) {
    super.afterExecute(r, t);
    ActivityTaskFutureTask activityTaskFutureTask = (ActivityTaskFutureTask) r;
    activityTaskFutureTask.setRunnableThread(null);
    taskQueue.removeRunningItem(activityTaskFutureTask);
  }

  @Override
  protected <T> RunnableFuture<T> newTaskFor(Callable<T> callable) {
    ActivityTaskFutureTask<T> activityTaskFutureTask = new ActivityTaskFutureTask<>(callable);
    TaskQueue.WaitingItem waitingItem = new TaskQueue.WaitingItem(activityTaskFutureTask);
    waitingItem.enter(taskQueue);
    return new ActivityTaskFutureTask<>(callable);
  }
}
