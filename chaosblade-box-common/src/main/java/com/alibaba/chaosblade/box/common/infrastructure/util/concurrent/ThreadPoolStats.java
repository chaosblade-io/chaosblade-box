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
