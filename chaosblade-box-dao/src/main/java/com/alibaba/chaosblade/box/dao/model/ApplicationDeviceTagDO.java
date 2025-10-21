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

/** @author sunpeng */
@Data
@TableName(value = "t_chaos_application_device_tag")
public class ApplicationDeviceTagDO extends BaseDO {

  /** 标签ID */
  @TableField(value = "tag_id")
  private String tagId;

  /** 机器ip */
  @TableField(value = "configuration_id")
  private String configurationId;

  /** 标签名 */
  @TableField(value = "tag_name")
  private String tagName;

  /** 应用ID */
  @TableField(value = "app_id")
  private String appId;

  /** 应用分组 */
  @TableField(value = "group_name")
  private String groupName;

  /** userId */
  @TableField(value = "user_id")
  private String userId;
}
