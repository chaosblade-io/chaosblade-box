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

/**
 * 演练和标签的绑定关系
 *
 * @author haibin
 */
@Data
@TableName(value = "t_chaos_experiment_tag")
public class ExperimentTagRelationDO extends BaseDO {

  @TableField(value = "tag_id")
  private String tagId;

  @TableField(value = "experiment_id")
  private String relationId;

  @TableField(value = "type")
  private Integer tagType;

  @TableField(value = "tag_name")
  private String tagName;

  @TableField(value = "user_id")
  private String userId;
}
