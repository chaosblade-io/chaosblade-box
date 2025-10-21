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
import lombok.Data;

/** @author haibin */
@Data
public class ExpertiseInfo {

  /** 经验ID */
  @JSONField(name = "expertise_id")
  private String expertiseId;

  /** 基本信息 */
  @JSONField(name = "basic_info")
  private ExpertiseBasicInfo basicInfo;

  /** 执行信息 */
  @JSONField(name = "executable_info")
  private ExpertiseExecutableInfo executableInfo;

  /** 评测信息 */
  @JSONField(name = "evaluation_info")
  private ExpertiseEvaluationInfo evaluationInfo;

  /** 扩展信息 */
  private ExpertiseExtInfo extInfo;
}
