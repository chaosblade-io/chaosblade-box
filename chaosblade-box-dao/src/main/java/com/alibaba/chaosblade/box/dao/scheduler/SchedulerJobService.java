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

package com.alibaba.chaosblade.box.dao.scheduler;

import com.alibaba.chaosblade.box.dao.scheduler.domain.SchedulerJobCreateRequest;
import com.alibaba.chaosblade.box.dao.scheduler.domain.SchedulerJobUpdateRequest;
import java.util.Map;

/** @author haibin */
public interface SchedulerJobService {

  /** 增加一次定时任务 */
  public String addSchedulerJob(SchedulerJobCreateRequest schedulerJobCreateRequest);

  /**
   * 启动一次定时任务
   *
   * @return
   */
  public boolean enableSchedulerJob(String jobId);

  /**
   * 停止定时任务
   *
   * @param jobId
   * @return
   */
  public String disableSchedulerJob(String jobId);

  /**
   * 根据业务ID来停止定时任务
   *
   * @param businessType 业务类型
   * @param businessId 业务ID
   * @return jobId is exist
   */
  public String disableSchedulerJobByBusinessId(Integer businessType, String businessId);

  boolean triggerSchedulerJob(String taskId, Map<String, String> params);

  /**
   * 删除schedulerTaskid
   *
   * @param jobId
   * @return
   */
  public boolean deleteSchedulerJob(String jobId);

  /**
   * 更新定时任务
   *
   * @param schedulerJobUpdateRequest
   * @return
   */
  public boolean updateSchedulerJob(SchedulerJobUpdateRequest schedulerJobUpdateRequest);

  String stopSchedulerJob(String jobId);
}
