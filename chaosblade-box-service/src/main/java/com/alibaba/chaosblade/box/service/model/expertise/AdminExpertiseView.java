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

package com.alibaba.chaosblade.box.service.model.expertise;

import com.alibaba.chaosblade.box.common.common.domain.user.ChaosUser;
import com.alibaba.fastjson.annotation.JSONField;
import java.util.Date;
import java.util.Set;
import lombok.Data;

/** @author haibin */
@Data
public class AdminExpertiseView {

  @JSONField(name = "expertise_id")
  private String expertiseId;

  private String name;

  @JSONField(name = "function_desc")
  private String functionDesc;

  private Integer state;

  @JSONField(name = "experiment_count")
  private Integer experimentCount;

  @JSONField(name = "gmt_create")
  private Date gmtCreate;

  @JSONField(name = "gmt_modified")
  private Date gmtModified;

  @JSONField(name = "tags")
  private Set<String> tags;

  private ChaosUser creator;
}
