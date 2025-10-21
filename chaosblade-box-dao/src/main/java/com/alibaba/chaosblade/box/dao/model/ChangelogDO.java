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

import com.alibaba.chaosblade.box.common.infrastructure.constant.ChangelogTypes;
import com.alibaba.chaosblade.box.dao.model.base.BaseDO;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

/**
 * Data object for change log.
 *
 * <p>Three roles for a change of blow:
 *
 * <p>Target - Entity which changed. e.g experiment, workspace, etc. Operator - Operator who change
 * the target. e.g user, system, etc. Property - Changed property of target. e.g owner of
 * experiment, member of workspace, etc.
 *
 * @author sunju
 */
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@TableName("t_chaos_changelog")
public class ChangelogDO extends BaseDO {

  @TableField("changelog_id")
  @TableId(type = IdType.ID_WORKER_STR)
  String changelogId;

  /** @see ChangelogTypes */
  @TableField("change_type")
  String changeType;

  /** 类型描述 */
  @TableField("type_description")
  String typeDescription;

  @TableField("target_id")
  String targetId;

  @TableField("target_description")
  String targetDescription;

  /** @see ChangelogTypes */
  @TableField("target_type")
  String targetType;

  @TableField("operator_id")
  String operatorId;

  @TableField("operator_description")
  String operatorDescription;

  /** @see ChangelogTypes */
  @TableField("operator_type")
  String operatorType;

  @TableField("property_id")
  String propertyId;

  @TableField("property_description")
  String propertyDescription;

  /** @see ChangelogTypes */
  @TableField("property_type")
  String propertyType;

  /** @see ChangelogTypes */
  @TableField("property_change_type")
  String propertyChangeType;

  @TableField("change_description")
  String changeDescription;

  /** 错误码 */
  @TableField("error_code")
  String errorCode;
}
