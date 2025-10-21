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
import java.util.concurrent.atomic.AtomicInteger;

/** @author haibin */
public class ThreadPoolExecutors {

  public static DefaultThreadPoolExecutor newFixed(ThreadPoolConfig threadPoolConfig) {
    ThreadFactory threadFactory =
        threadPoolConfig.getThreadFactory() == null
            ? ThreadPoolExecutors.defaultThreadFactory(threadPoolConfig.getThreadName())
            : threadPoolConfig.getThreadFactory();
    BlockingQueue<Runnable> queue;
    if (threadPoolConfig.getQueueCapacity() < 0) {
      queue = new LinkedTransferQueue<>();
    } else {
      queue =
          new SizeBlockingQueue<>(
              new LinkedTransferQueue<Runnable>(), threadPoolConfig.getQueueCapacity());
    }
    return new DefaultThreadPoolExecutor(
        threadPoolConfig.getCoreSize(),
        threadPoolConfig.getMaxThreadSize(),
        1,
        TimeUnit.SECONDS,
        queue,
        threadFactory,
        threadPoolConfig.getRunnableDecorator());
  }

  public static ScheduledExecutorService newSingleThreadScheduledExecutor(String name) {
    return new ScheduledThreadPoolExecutor(1, defaultThreadFactory(name));
  }

  public static ThreadFactory defaultThreadFactory(String name) {
    return new MyThreadFactory(name);
  }

  static class MyThreadFactory implements ThreadFactory {

    final AtomicInteger atomicInteger = new AtomicInteger(1);

    final String name;

    public MyThreadFactory(String name) {
      this.name = name;
    }

    /**
     * Constructs a new {@code Thread}. Implementations may also initialize activityOrder,
     * nameSuffix, daemon runState, {@code ThreadGroup}, etc.
     *
     * @param r a runnable to be executed by new thread instance
     * @return constructed thread, or {@code null} if the request to create a thread is rejected
     */
    @Override
    public Thread newThread(Runnable r) {
      return new Thread(r, name + "[No:]" + atomicInteger.getAndIncrement() + "]");
    }
  }
}
