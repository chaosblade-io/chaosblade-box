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
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

/** @author sunju */
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@TableName("t_chaos_tag")
public class TagDO extends BaseDO {

  public static final int TAG_TYPE_EXPERIMENT = 0;

  public static final int TAG_TYPE_SCENE_FUNCTION = 1;

  public static final int TAG_TYPE_SCENE_FUNCTION_CATEGORY = 2;

  public static final int TAG_TYPE_EXPERTISE = 3;

  public static final int TAG_TYPE_APPLICATION_HOST = 4;

  @TableId(type = IdType.ID_WORKER_STR)
  String tagId;

  String name;

  String code;

  Integer type;

  String userId;
}
