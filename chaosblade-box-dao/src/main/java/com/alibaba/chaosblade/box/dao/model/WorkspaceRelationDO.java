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
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

/** @author sunju */
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@TableName("t_chaos_workspace_relation")
public class WorkspaceRelationDO extends BaseDO {

  /** Id of workspace relation */
  @TableField("relation_id")
  @TableId(type = IdType.ID_WORKER_STR)
  String relationId;

  /** Id of related workspace */
  @TableField("workspace_id")
  String workspaceId;

  /**
   * Entity id which related with workspace.
   *
   * <p>e.g. Experiment - experiment id User - user id Micro app - micro app code
   */
  @TableField("outer_id")
  String outerId;

  /**
   * Entity description which related with workspace.
   *
   * <p>e.g. Experiment - experiment name User - user name or nick Micro app - micro app name
   */
  @TableField("outer_description")
  String outerDescription;

  /**
   * Type of workspace relation
   *
   * @see WorkspaceRelationTypes
   */
  @TableField("relation_type")
  String relationType;

  /**
   * @see PermissionTypes
   *     <p>Default permission is {@link PermissionTypes#R}
   */
  @TableField("permission")
  Integer permission;

  /**
   * Flag of workspace relation which mark it enable or not
   *
   * <p>0 - enable 1 - disable
   */
  @TableField("is_delete")
  Boolean isDelete;

  @TableField("namespace")
  String namespace;

  /** 演练创建人员 */
  @TableField("experiment_creator")
  String experimentCreator;
}
