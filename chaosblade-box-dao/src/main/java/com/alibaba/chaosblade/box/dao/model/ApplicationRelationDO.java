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
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/** @author haibin */
@Data
@TableName(value = "t_chaos_application_relation")
public class ApplicationRelationDO extends BaseDO {

  public static String RELATION_EXPERIMENT_TASK = "experiment_task";
  public static String RELATION_EXPERIMENT = "experiment";

  public static String RELATION_SCENE = "scene";

  public static final String COLUMN_EXPERIMENT_TASK_STATE = "experiment_task_state";

  public static final String COLUMN_EXPERIMENT_TASK_RESULT = "experiment_task_result";

  /** mk员工工号与应用关系 */
  public static String RELATION_EMP_ID = "emp_id";

  /** 关系ID */
  @TableField("relation_id")
  @TableId(type = IdType.ID_WORKER_STR)
  String relationId;

  /** 实验ID */
  @TableField("app_id")
  Long appId;

  /** 关联的外部ID */
  @TableField("outer_id")
  String outerId;

  /** 关联的外部描述信息 */
  @TableField("outer_description")
  String outerDescription;

  /** 关联的外部数据类型 */
  @TableField("relation_type")
  String relationType;

  /** 是否删除 */
  @TableField("is_delete")
  Boolean isDelete;

  /** 演练任务的运行状态 */
  @TableField(COLUMN_EXPERIMENT_TASK_STATE)
  protected Integer taskState;

  @TableField(COLUMN_EXPERIMENT_TASK_RESULT)
  protected Integer taskResult;
}
