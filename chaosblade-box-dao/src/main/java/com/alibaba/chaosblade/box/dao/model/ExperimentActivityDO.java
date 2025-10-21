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

import com.alibaba.chaosblade.box.common.app.sdk.constants.PhaseType;
import com.alibaba.chaosblade.box.common.common.domain.definition.ExperimentActivityDefinition;
import com.alibaba.chaosblade.box.dao.model.base.BaseDO;
import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

/**
 * 演练活动的对象结构
 *
 * @author haibin
 */
@Data
@TableName("t_chaos_experiment_activity")
public class ExperimentActivityDO extends BaseDO {

  public static Integer PRIORITY_COMMON = 1;
  public static Integer PRIORITY_LOW = 0;
  public static Integer PRIORITY_HIGH = 2;
  public static Integer USER_CHECK_REQUIRED = 1;
  public static Integer USER_CHECK_NOT_REQUIRED = 0;

  @TableId(type = IdType.ID_WORKER_STR, value = "activity_id")
  String activityId;

  /** 演练ID */
  @TableField(value = "experiment_id")
  String experimentId;

  /** 所属的阶段 {@link com.alibaba.chaosblade.box.common.app.sdk.constants.PhaseType} */
  @TableField(value = "phase")
  PhaseType phase;

  /** 顺序 */
  @TableField(value = "activity_order")
  private Integer activityOrder;

  /** 获取小程序Code */
  @TableField(value = "app_code")
  private String appCode;

  /** 优先级 */
  @TableField(value = "activity_priority")
  private Integer activityPriority;

  /** activity的定义 */
  @TableField(value = "activity_definition")
  private ExperimentActivityDefinition activityDefinition;

  /** 加一个乐观锁 */
  @Version
  @TableField(value = "version")
  private Integer version;

  @TableField(value = "activity_task_id")
  private String activityTaskId;

  /** 人工确认标记 */
  @TableField(value = "user_check_flag")
  private Integer userCheck;

  @TableField(value = "is_deleted")
  private Boolean isDeleted;

  @TableField(value = "activity_name")
  private String activityName;

  /** 对应的攻击activityId */
  @TableField(value = "attack_activity_id")
  private String attackActivityId;

  /** 如果是裂变小程序，executableAppCode为父code,否则就等于小程序的code */
  @TableField(value = "executable_app_code")
  private String executableAppCode;

  @TableField(value = "flow_id")
  private String flowId;

  public boolean needUserCheck() {
    return USER_CHECK_REQUIRED.equals(this.userCheck);
  }
}
