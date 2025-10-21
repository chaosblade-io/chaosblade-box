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

package com.alibaba.chaosblade.box.dao.scheduler.quartz;

import com.alibaba.chaosblade.box.dao.model.SchedulerJobDO;
import com.alibaba.chaosblade.box.dao.repository.SchedulerJobRepository;
import com.alibaba.chaosblade.box.dao.scheduler.SchedulerConstant;
import org.quartz.JobExecutionContext;
import org.springframework.beans.factory.annotation.Autowired;

/** @author haibin */
public abstract class BaseJob {

  @Autowired protected SchedulerJobRepository schedulerJobRepository;

  public BaseJob() {
    ChaosQuartzClassLoadHelper.addJobClass(this.getClass());
  }

  public SchedulerJobDO reloadSchedulerJobDO(JobExecutionContext jobExecutionContext) {
    String jobId = null;
    if (jobExecutionContext
        .getMergedJobDataMap()
        .containsKey(SchedulerConstant.SCHEDULER_PARAM_KET_JOB_ID)) {
      jobId =
          jobExecutionContext
              .getMergedJobDataMap()
              .getString(SchedulerConstant.SCHEDULER_PARAM_KET_JOB_ID);
    } else if (jobExecutionContext
        .getMergedJobDataMap()
        .containsKey(SchedulerConstant.SCHEDULER_PARAM_KET_JOB)) {
      jobId =
          ((SchedulerJobDO)
                  jobExecutionContext
                      .getMergedJobDataMap()
                      .get(SchedulerConstant.SCHEDULER_PARAM_KET_JOB))
              .getJobId();
    }
    if (jobId == null) {
      return null;
    }
    return schedulerJobRepository.findById(jobId).orElse(null);
  }
}
