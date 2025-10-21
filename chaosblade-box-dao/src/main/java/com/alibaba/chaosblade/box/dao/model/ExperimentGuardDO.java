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
import com.alibaba.chaosblade.box.dao.model.base.BaseDO;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/** @author haibin */
@Data
@TableName("t_chaos_experiment_guard")
public class ExperimentGuardDO extends BaseDO {

  public static Integer ACTION_TYPE_MONITOR = 0;

  public static Integer ACTION_TYPE_RECOVERY = 1;

  public static Integer ACTION_TYPE_TIMEOUT_RECOVERY = 2;

  public static Integer ACTION_TYPE_HIT_COUNT_MONITOR = 3;

  @TableId(type = IdType.ID_WORKER_STR, value = "guard_id")
  private String guardId;

  private String experimentId;

  private String appCode;

  private String name;

  private Integer actionType;

  private ExperimentGuardArgument argument;

  private Boolean required;
}
