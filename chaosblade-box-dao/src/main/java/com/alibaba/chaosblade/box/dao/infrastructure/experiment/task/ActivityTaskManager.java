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

package com.alibaba.chaosblade.box.dao.infrastructure.experiment.task;

import com.alibaba.chaosblade.box.common.common.domain.Response;
import com.alibaba.chaosblade.box.common.common.domain.activity.ActivityTask;
import com.alibaba.chaosblade.box.dao.model.ActivityTaskDO;
import java.util.List;

/** @author haibin */
public interface ActivityTaskManager {

  /**
   * 查询活动任务
   *
   * @param activityTaskId 活动任务ID
   * @return
   */
  public Response<ActivityTask> queryFullActivityTaskInfo(String activityTaskId);

  /**
   * 查询节点任务概况
   *
   * @param activityTaskDO
   * @return
   */
  public Response<ActivityTask> querySimpleActivityTaskInfo(ActivityTaskDO activityTaskDO);

  /**
   * 放弃activity任务
   *
   * @param activityTaskDO 活动任务ID
   * @param rejectReason 放弃的理由
   */
  public void rejectActivityTask(ActivityTaskDO activityTaskDO, String rejectReason);

  /**
   * 放弃演练的所有任务
   *
   * @param experimentTaskId 实验任务ID
   * @param rejectReason 放弃的理由
   * @return 放弃的活动任务数目
   */
  public long rejectActivityTasksByExperimentTaskId(String experimentTaskId, String rejectReason);

  /**
   * 创建节点任务
   *
   * @param activityTasksCreateRequest
   * @return
   */
  public List<ActivityTaskDO> createActivityTasks(
      ActivityTasksCreateRequest activityTasksCreateRequest);
}
