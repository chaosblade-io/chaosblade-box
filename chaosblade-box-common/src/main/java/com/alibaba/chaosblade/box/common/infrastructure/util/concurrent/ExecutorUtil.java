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

import com.google.common.collect.Lists;
import java.util.List;
import java.util.concurrent.*;
import java.util.function.Consumer;
import lombok.extern.slf4j.Slf4j;

/** Created by haibin on 16/7/19. */
@Slf4j
public class ExecutorUtil {

  public static int DefaultAwaitTime = 5;

  public static <T> List<T> execute(
      ExecutorAction<T> executorAction, int size, long time, TimeUnit timeUnit)
      throws InterruptedException {
    size = size < 0 ? Runtime.getRuntime().availableProcessors() : size;
    ExecutorService executorService = Executors.newWorkStealingPool(size);
    try {
      List<T> result = execute(executorService, executorAction);
      executorService.shutdown();
      executorService.awaitTermination(time, timeUnit);
      return result;
    } finally {
      if (!executorService.isShutdown()) {
        executorService.shutdownNow();
      }
    }
  }

  public static <T> List<T> execute(ExecutorAction<T> executorAction, int size) {
    try {
      return execute(executorAction, size, DefaultAwaitTime, TimeUnit.MINUTES);
    } catch (InterruptedException e) {
      return Lists.newArrayList();
    }
  }

  public static <T> List<T> execute(
      ExecutorService executorService, ExecutorAction<T> executorAction) {
    List<Callable<T>> callables = executorAction.getCallables();
    List<T> result = Lists.newCopyOnWriteArrayList();
    try {
      executorService
          .invokeAll(callables)
          .forEach(
              new Consumer<Future<T>>() {
                @Override
                public void accept(Future<T> tFuture) {
                  try {
                    T future = tFuture.get();
                    if (future != null) {
                      result.add(tFuture.get());
                    }
                  } catch (Exception e) {
                    log.error("execute error", e);
                  }
                }
              });
    } catch (Exception e) {
      log.error("Interrupted while", e);
      return Lists.newArrayList();
    }
    return result;
  }

  public static void stop(ExecutorService executorService) throws InterruptedException {
    try {
      executorService.shutdown();
      executorService.awaitTermination(5, TimeUnit.SECONDS);
    } finally {
      if (!executorService.isTerminated()) {
        executorService.shutdownNow();
      }
    }
  }
}
