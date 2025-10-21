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

package com.alibaba.chaosblade.box.common.common.domain.definition;

import com.alibaba.chaosblade.box.common.common.annotation.ApiParam;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

/**
 * 实验的定义，包含运行环境、执行方式、流程定义
 *
 * @author sunju
 */
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ExperimentDefinition {

  /** 演练流程定义 */
  @ApiParam ExperimentFlowDefinition flow;

  /** 回滚策略定义,可以为空 */
  @Deprecated ExperimentRollbackDefinition rollback;
}
