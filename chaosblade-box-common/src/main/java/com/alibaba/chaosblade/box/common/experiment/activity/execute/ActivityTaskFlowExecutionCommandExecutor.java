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

import com.alibaba.chaosblade.box.common.commands.Command;
import com.alibaba.chaosblade.box.common.commands.CommandExecutorConfig;
import com.alibaba.chaosblade.box.common.commands.CommandExecutorInfo;
import com.alibaba.chaosblade.box.common.common.IdentityCommand;
import java.util.concurrent.*;
import org.springframework.context.ApplicationContext;

/** @author haibin */
public class ActivityTaskFlowExecutionCommandExecutor extends DefaultCommandExecutor {

  public TaskQueue getTaskQueue() {
    return taskQueue;
  }

  /** 任务队列，用来管理已经提交的任务状态 */
  private TaskQueue taskQueue;

  public ActivityTaskFlowExecutionCommandExecutor(
      String name,
      ApplicationContext applicationContext,
      CommandExecutorConfig commandExecutorConfig) {
    super(name, applicationContext, commandExecutorConfig);
  }

  /**
   * 初始化线程池的Queue
   *
   * @return
   */
  @Override
  protected BlockingQueue<Runnable> initQueue() {
    return new LinkedBlockingQueue<>();
  }

  @Override
  protected ExecutorService initExecutor(
      CommandExecutorConfig commandExecutorConfig, BlockingQueue<Runnable> blockingQueue) {
    taskQueue = new TaskQueue();
    taskQueue.init();
    return new ActivityTaskThreadPoolExecutor(
        commandExecutorConfig.getCoreSize(),
        commandExecutorConfig.getMaxPoolSize(),
        5,
        TimeUnit.SECONDS,
        taskQueue,
        blockingQueue,
        r -> new Thread(r, "command-executor-" + name),
        (r, executor) -> {});
  }

  @Override
  public <Response> Future<Response> asyncRun(Command<Response> command) {
    if (!(command instanceof IdentityCommand)) {
      throw new IllegalArgumentException("only support IdentityCommand");
    }
    return this.executor.submit(new ActivityTaskCallable<>(command));
  }

  @Override
  public <Response> Response syncRun(Command<Response> command) {
    return super.syncRun(command);
  }

  @Override
  public CommandExecutorInfo getCommandExecutorInfo() {
    CommandExecutorInfo commandExecutorInfo = super.getCommandExecutorInfo();
    TaskQueue.QueueStats queueStats = taskQueue.getQueueStats();
    commandExecutorInfo.setBlockingCount(queueStats.getBlocking());
    commandExecutorInfo.setHangCount(queueStats.getHang());
    commandExecutorInfo.setTaskQueueFinishedCount(queueStats.getFinished());
    return commandExecutorInfo;
  }

  public void forceExitCommand(String experimentTaskId) {
    TaskQueue.QueueItem queueItem = taskQueue.findRunningItemById(experimentTaskId);
    if (queueItem == null) {
      return;
    }
    if (queueItem instanceof TaskQueue.WaitingItem) {
      // 等待状态的暂时不做什么处理
    } else {
      queueItem.close();
    }
  }
}
