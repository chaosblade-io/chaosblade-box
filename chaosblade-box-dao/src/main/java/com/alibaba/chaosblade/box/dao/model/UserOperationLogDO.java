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

import com.alibaba.chaosblade.box.dao.model.base.BaseDO;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/** @author haibin */
@TableName("t_chaos_user_operation_log")
@Data
public class UserOperationLogDO extends BaseDO {

  public static String TARGET_TYPE_EXPERIMENT = "experiment";

  public static String TARGET_TYPE_EXPERIMENT_TASK = "experiment_task";

  public static String OPERATION_CREATE = "create";

  public static String OPERATION_CLONE = "clone";

  public static String OPERATION_DELETE = "delete";

  public static String OPERATION_QUERY = "query";

  public static String OPERATION_RUN = "run";

  public static String OPERATION_UPDATE = "update";
  public static String OPERATION_STOP = "stop";

  @TableField(value = "user_id")
  private String userId;

  @TableField(value = "target")
  private String target;

  @TableField(value = "target_type")
  private String targetType;

  @TableField(value = "operation")
  private String operation;
}
