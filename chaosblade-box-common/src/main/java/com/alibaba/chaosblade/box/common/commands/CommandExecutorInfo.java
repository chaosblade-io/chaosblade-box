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

package com.alibaba.chaosblade.box.common.commands;

import lombok.Data;

/** @author haibin */
@Data
public class CommandExecutorInfo {

  private String name;
  private long completedTask;
  private int queueSize;
  private int activeCount;
  private int hangCount;
  private int blockingCount;

  private int taskQueueFinishedCount;

  public CommandExecutorInfo(String name) {
    this.name = name;
  }

  private int maxPoolSize;

  private int corePoolSize;

  public void setCompletedTask(long completedTask) {
    this.completedTask = completedTask;
  }

  public long getCompletedTask() {
    return completedTask;
  }

  public void setQueueSize(int queueSize) {

    this.queueSize = queueSize;
  }

  public int getQueueSize() {
    return queueSize;
  }

  public void setActiveCount(int activeCount) {
    this.activeCount = activeCount;
  }

  public int getActiveCount() {
    return activeCount;
  }
}
