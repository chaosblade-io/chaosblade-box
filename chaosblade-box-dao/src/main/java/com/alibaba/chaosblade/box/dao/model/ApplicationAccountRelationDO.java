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
import java.io.Serializable;
import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * t_chaos_application_account_relation
 *
 * @author
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@TableName("t_chaos_application_account_relation")
public class ApplicationAccountRelationDO extends BaseDO implements Serializable {

  /** 创建时间 */
  private Date gmtCreate;

  /** 修改时间 */
  private Date gmtModified;

  /** 关联关系ID */
  @TableField("relation_id")
  @TableId(type = IdType.ID_WORKER_STR)
  private String relationId;

  /** 演练应用ID */
  private Long applicationId;

  /** 账号ID */
  private String accountId;

  /** 授权标识，用户细分权限粒度，暂时可能用不到 */
  private String policy;

  /** 正常/禁用 */
  private Byte status;

  /** 账号类型 */
  private String accountType;

  /** 是否删除 */
  private Byte isDelete;

  private static final long serialVersionUID = 1L;
}
