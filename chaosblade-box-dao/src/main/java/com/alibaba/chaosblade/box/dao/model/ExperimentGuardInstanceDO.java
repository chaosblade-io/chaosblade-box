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

import com.alibaba.chaosblade.box.common.infrastructure.domain.experiment.guard.ExperimentGuardArgument;
import com.alibaba.chaosblade.box.common.infrastructure.domain.experiment.guard.ExperimentGuardMonitorMetricResultEntity;
import com.alibaba.chaosblade.box.common.infrastructure.domain.experiment.guard.GuardRunState;
import com.alibaba.chaosblade.box.dao.model.base.BaseDO;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/** @author haibin */
@TableName(value = "t_chaos_experiment_guard_instance")
@Data
public class ExperimentGuardInstanceDO extends BaseDO {

  @TableId(type = IdType.ID_WORKER_STR)
  private String instanceId;

  private String name;

  private String experimentTaskId;

  private String guardId;

  /** 状态 */
  private GuardRunState state;

  private String appCode;

  /** argument */
  private ExperimentGuardArgument argument;

  /** action类型 0~ACTION_TYPE_MONITOR 1~ACTION_TYPE_RECOVERY */
  private Integer actionType;

  private String triggeredReason;

  /** activityTaskId */
  private String activityTaskId;

  private ExperimentGuardMonitorMetricResultEntity value;

  public boolean isHalted() {
    return GuardRunState.TRIGGERED.equals(state) || GuardRunState.FINISHED.equals(state);
  }
}
