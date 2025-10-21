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

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executor;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.function.Function;
import lombok.Getter;

/** @author haibin */
public class ThreadPool {

  private volatile Map<String, ExecutorHolder> executors;

  public ThreadPool() {
    this.executors = new ConcurrentHashMap<>();
  }

  public synchronized void register(ThreadPoolConfig threadPoolConfig) {
    executors.putIfAbsent(
        threadPoolConfig.getThreadName(),
        new ExecutorHolder(ThreadPoolExecutors.newFixed(threadPoolConfig)));
  }

  public void register(String name) {
    ThreadPoolConfig threadPoolConfig = new ThreadPoolConfig(name);
    threadPoolConfig.setMaxThreadSize(2);
    threadPoolConfig.setCoreSize(1);
    register(threadPoolConfig);
  }

  public void shutDown() {
    for (ExecutorHolder executorHolder : executors.values()) {
      if (executorHolder.executor instanceof ThreadPoolExecutor) {
        ((ThreadPoolExecutor) executorHolder.getExecutor()).shutdown();
      }
    }
  }

  public Executor executor(String threadName, ThreadPoolConfig createIfNotExist) {
    return executors
        .computeIfAbsent(
            threadName,
            new Function<String, ExecutorHolder>() {
              @Override
              public ExecutorHolder apply(String s) {
                createIfNotExist.setThreadName(threadName);
                return new ExecutorHolder(ThreadPoolExecutors.newFixed(createIfNotExist));
              }
            })
        .getExecutor();
  }

  public Executor executor(String threadName) {
    return executors
        .computeIfAbsent(
            threadName,
            new Function<String, ExecutorHolder>() {
              @Override
              public ExecutorHolder apply(String s) {
                ThreadPoolConfig threadPoolConfig = new ThreadPoolConfig(threadName);
                threadPoolConfig.setMaxThreadSize(10);
                threadPoolConfig.setCoreSize(10);
                return new ExecutorHolder(ThreadPoolExecutors.newFixed(threadPoolConfig));
              }
            })
        .getExecutor();
  }

  static class ExecutorHolder {

    @Getter private Executor executor;

    public ExecutorHolder(Executor executor) {
      this.executor = executor;
    }
  }
}
