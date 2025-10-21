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

package com.alibaba.chaosblade.box.common.infrastructure.domain.experiment.flow;

import com.alibaba.chaosblade.box.common.common.annotation.ApiParam;
import com.alibaba.chaosblade.box.common.common.domain.definition.BaseExperimentActivityDefinition;
import com.alibaba.chaosblade.box.common.infrastructure.domain.experiment.SceneArgumentGrade;
import com.alibaba.fastjson.annotation.JSONField;
import java.util.List;
import lombok.Data;

/** @author haibin */
@Data
public class ExperimentActivityInfo extends BaseExperimentActivityDefinition {

  /** 前端界面的ID */
  @JSONField(name = "id")
  private String fontId;

  /** 活动的参数 */
  @ApiParam(required = false)
  List<SceneArgumentGrade> arguments;
}
