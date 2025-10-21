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

import java.util.List;
import lombok.Data;
import lombok.Getter;

/** @author haibin */
@Data
public class ThreadPoolStats {

  private List<Stats> stats;

  public ThreadPoolStats(List<Stats> stats) {
    this.stats = stats;
  }

  @Getter
  public static class Stats {

    private final String name;
    private final int threads;
    private final int queue;
    private final int active;
    private final long rejected;
    private final int largest;
    private final long completed;

    public Stats(
        String name,
        int threads,
        int queue,
        int active,
        long rejected,
        int largest,
        long completed) {
      this.name = name;
      this.threads = threads;
      this.queue = queue;
      this.active = active;
      this.rejected = rejected;
      this.largest = largest;
      this.completed = completed;
    }
  }
}
