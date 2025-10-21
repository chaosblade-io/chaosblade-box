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

package com.alibaba.chaosblade.box.common.infrastructure.util.concurrent;

import java.util.concurrent.*;

/** @author haibin */
public class DefaultThreadPoolExecutor extends ThreadPoolExecutor {

  private volatile ShutdownListener shutdownListener;

  private final Object monitor = new Object();

  private RunnableDecorator runnableDecorator;

  public DefaultThreadPoolExecutor(
      int corePoolSize,
      int maximumPoolSize,
      long keepAliveTime,
      TimeUnit unit,
      BlockingQueue<Runnable> workQueue) {
    super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue);
  }

  public DefaultThreadPoolExecutor(
      int corePoolSize,
      int maximumPoolSize,
      long keepAliveTime,
      TimeUnit unit,
      BlockingQueue<Runnable> workQueue,
      ThreadFactory threadFactory,
      RunnableDecorator runnableDecorator) {
    super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue, threadFactory);
    this.runnableDecorator = runnableDecorator;
  }

  public DefaultThreadPoolExecutor(
      int corePoolSize,
      int maximumPoolSize,
      long keepAliveTime,
      TimeUnit unit,
      BlockingQueue<Runnable> workQueue,
      RejectedExecutionHandler handler) {
    super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue, handler);
  }

  public DefaultThreadPoolExecutor(
      int corePoolSize,
      int maximumPoolSize,
      long keepAliveTime,
      TimeUnit unit,
      BlockingQueue<Runnable> workQueue,
      ThreadFactory threadFactory,
      RejectedExecutionHandler handler) {
    super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue, threadFactory, handler);
  }

  public void shutdown(ShutdownListener shutdownListener) {
    synchronized (monitor) {
      if (this.shutdownListener != null) {
        throw new IllegalStateException("Shutdown was already called on this thread pool");
      }
      if (isTerminated()) {
        shutdownListener.onTerminated();
      } else {
        this.shutdownListener = shutdownListener;
      }
    }
  }

  @Override
  protected synchronized void terminated() {
    super.terminated();
    synchronized (monitor) {
      if (shutdownListener != null) {
        try {
          shutdownListener.onTerminated();
        } finally {
          shutdownListener = null;
        }
      }
    }
  }

  public interface ShutdownListener {
    void onTerminated();
  }

  @Override
  public void execute(Runnable command) {
    try {
      if (this.runnableDecorator != null) {
        super.execute(runnableDecorator.decorate(command));
      } else {
        super.execute(command);
      }
    } catch (Throwable throwable) {
      if (command instanceof AbstractRunnable) {
        try {
          ((AbstractRunnable) command).onRejection(throwable);
        } finally {
          ((AbstractRunnable) command).onAfter();
        }
      } else {
        throw throwable;
      }
    }
  }
}
