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

package com.alibaba.chaosblade.box.common.experiment.task.flow.util.concurrent;

import java.util.concurrent.ThreadFactory;
import lombok.Data;
import lombok.Getter;

/** @author haibin */
@Data
public class ThreadPoolConfig {

  /** thread pool alias */
  @Getter private String threadName;

  /** decorate thread runnable */
  private RunnableDecorator runnableDecorator;

  /** thread pool taskQueue */
  private int queueCapacity = 256;

  private int coreSize = 1;

  private int maxThreadSize = 3;

  private ThreadFactory threadFactory;

  public ThreadPoolConfig(String threadName) {
    this.threadName = threadName;
  }
}
