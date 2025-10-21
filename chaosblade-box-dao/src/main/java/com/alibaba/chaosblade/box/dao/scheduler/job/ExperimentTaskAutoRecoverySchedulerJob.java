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

package com.alibaba.chaosblade.box.dao.scheduler.job;

import com.alibaba.chaosblade.box.common.commands.CommandBus;
import com.alibaba.chaosblade.box.dao.command.task.ExperimentTaskFinishedCommand;
import com.alibaba.chaosblade.box.dao.model.ExperimentTaskDO;
import com.alibaba.chaosblade.box.dao.repository.ExperimentTaskRepository;
import com.alibaba.chaosblade.box.dao.scheduler.SchedulerConstant;
import com.alibaba.chaosblade.box.dao.scheduler.SchedulerJobService;
import com.alibaba.chaosblade.box.dao.scheduler.domain.SchedulerJobCreateRequest;
import com.alibaba.chaosblade.box.dao.scheduler.quartz.BaseJob;
import java.util.function.Consumer;
import java.util.function.Predicate;
import lombok.extern.slf4j.Slf4j;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/** @author haibin */
@Component
@Slf4j
@DisallowConcurrentExecution
public class ExperimentTaskAutoRecoverySchedulerJob extends BaseJob
    implements Job, InitializingBean {

  @Autowired private ExperimentTaskRepository experimentTaskRepository;

  @Autowired private CommandBus commandBus;

  @Autowired private SchedulerJobService schedulerJobService;

  // 最大三分钟
  private Integer MAX_STOPPING_TIME = 3 * 60 * 1000;

  @Override
  public void execute(JobExecutionContext context) throws JobExecutionException {
    // 找到所有正在运行中的演练
    experimentTaskRepository.findStoppingTasks().stream()
        .filter(
            new Predicate<ExperimentTaskDO>() {
              @Override
              public boolean test(ExperimentTaskDO experimentTaskDO) {
                return System.currentTimeMillis() - experimentTaskDO.getGmtModified().getTime()
                    > MAX_STOPPING_TIME;
              }
            })
        .forEach(
            new Consumer<ExperimentTaskDO>() {
              @Override
              public void accept(ExperimentTaskDO experimentTaskDO) {
                commandBus.syncRun(
                    ExperimentTaskFinishedCommand.class, experimentTaskDO.getTaskId());
                log.info(
                    "stop experiment task by system autoRecoverySchedulerJob,taskId:{}",
                    experimentTaskDO.getTaskId());
              }
            });
  }

  @Override
  public void afterPropertiesSet() throws Exception {
    String cronExpression = "0 0/1 * * * ?";
    SchedulerJobCreateRequest schedulerJobCreateRequest =
        new SchedulerJobCreateRequest(
            cronExpression,
            0,
            ExperimentTaskAutoRecoverySchedulerJob.class.getName(),
            SchedulerConstant.BUSINESS_TYPE_AUTO_RECOVERY,
            "-1",
            ExperimentTaskAutoRecoverySchedulerJob.class.getName());
    schedulerJobService.addSchedulerJob(schedulerJobCreateRequest);
  }
}
