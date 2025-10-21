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

package com.alibaba.chaosblade.box.dao.model;

import com.alibaba.chaosblade.box.common.experiment.task.ExperimentTaskContext;
import com.alibaba.chaosblade.box.dao.model.base.BaseTaskDO;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/** @author haibin */
@Data
@TableName(value = "t_chaos_experiment_task")
public class ExperimentTaskDO extends BaseTaskDO {

  /** 任务对应的演练ID */
  @TableField(value = "experiment_id")
  private String experimentId;

  /** 当前步骤的任务ID */
  @TableField(value = "activity_task_id")
  private String activityTaskId;

  /** 任务类型 */
  private Integer taskType;

  /** 演练任务的人员 */
  @TableField(value = "user_id")
  private String userId;

  @TableField(value = "outer_id")
  private String outerId;

  @TableField(value = "namespace")
  private String namespace;

  @TableField(value = "experiment_task_context")
  private ExperimentTaskContext experimentTaskContext;

  @TableField(value = "duration")
  private Long duration;

  @TableField(value = "name")
  private String name;

  @TableField(value = "feedback_status")
  private Integer feedBackStatus;

  /** 外部员工工号：由mk发起的演练执行，保存发起人工号 */
  @TableField(value = "outer_user_id")
  private String outerUserId;

  /** 是否删除 */
  @TableField(value = "is_delete")
  Boolean isDelete;
}
