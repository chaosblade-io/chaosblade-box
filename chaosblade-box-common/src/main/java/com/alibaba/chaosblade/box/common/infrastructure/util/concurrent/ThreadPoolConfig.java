package com.alibaba.chaosblade.box.common.infrastructure.util.concurrent;

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
