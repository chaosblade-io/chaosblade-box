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

import com.alibaba.chaosblade.box.common.experiment.activity.execute.ActivityTaskFlowExecutionCommandExecutor;
import com.alibaba.chaosblade.box.common.experiment.activity.execute.QueueListener;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/** @author haibin.lhb */
@Component
public class TaskQueueCommandExecutorPreProcessor implements CommandExecutorPreProcessor {

  @Autowired private List<QueueListener> queueListenerList;

  @Override
  public void preProcess(CommandExecutor commandExecutor) {
    if (commandExecutor instanceof ActivityTaskFlowExecutionCommandExecutor) {
      for (QueueListener queueListener : queueListenerList) {
        ((ActivityTaskFlowExecutionCommandExecutor) commandExecutor)
            .getTaskQueue()
            .addQueueListener(queueListener);
      }
    }
  }
}
