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
import java.util.HashSet;
import java.util.Set;
import lombok.Data;

/** @author haibin */
@Data
public class ExpertiseBasicInfo {

  /** 名称 */
  private String name;

  /** 功能描述 */
  @JSONField(name = "function_desc")
  private String functionDesc;

  /** 背景描述 */
  @JSONField(name = "background_desc")
  private String backgroundDesc;

  /** 设计理念 */
  @JSONField(name = "design_concept")
  private String designConcept;

  /** ExpertiseConstant */
  private Integer state;

  /** 标签 */
  private Set<String> tags = new HashSet<>();
}
