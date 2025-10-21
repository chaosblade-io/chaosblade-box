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

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/** @author haibin.lhb */
@Component
@Slf4j
public class LogPrintQueueListener extends QueueListener {

  @Override
  public void onEnterWaiting(TaskQueue.WaitingItem waitingItem) {
    log.debug("EnterWaitingQueue:" + waitingItem.getId());
  }

  @Override
  public void onLeaveWaiting(TaskQueue.WaitingItem waitingItem) {
    log.debug("LeaveWaitingQueue:" + waitingItem.getId());
  }

  @Override
  public void onEnterRunning(TaskQueue.RunningItem runningItem) {
    log.debug("EnterRunningQueue:" + runningItem.getId());
  }

  @Override
  public void onLeaveRunning(TaskQueue.RunningItem runningItem) {
    log.debug(
        "LeaveRunningQueue:"
            + runningItem.getId()
            + ",duration:"
            + runningItem.getRunningDuration());
  }

  @Override
  public void onLeaveBlocking(TaskQueue.BlockingItem blockingItem) {
    super.onLeaveBlocking(blockingItem);
  }
}
