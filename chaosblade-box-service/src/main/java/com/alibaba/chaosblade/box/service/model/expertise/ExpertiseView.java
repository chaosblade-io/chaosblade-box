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

import com.alibaba.fastjson.annotation.JSONField;
import java.util.Set;
import lombok.Data;

/**
 * 演练经验缩略图
 *
 * @author haibin
 */
@Data
public class ExpertiseView {

  @JSONField(name = "expertise_id")
  private String expertiseId;

  private String name;

  @JSONField(name = "function_desc")
  private String functionDesc;

  @JSONField(name = "tags")
  private Set<String> tags;

  @JSONField(name = "flow")
  private ExpertiseFlowView flow;

  @JSONField(name = "type")
  private Integer type;

  @JSONField(name = "scope_type")
  private Set<Integer> scopeType;
}
