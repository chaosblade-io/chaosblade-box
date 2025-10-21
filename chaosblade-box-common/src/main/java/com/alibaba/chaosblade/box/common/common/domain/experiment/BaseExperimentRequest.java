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

package com.alibaba.chaosblade.box.common.common.domain.experiment;

import com.alibaba.chaosblade.box.common.common.annotation.ApiParam;
import com.alibaba.chaosblade.box.common.common.domain.BaseRequest;
import java.util.List;
import lombok.Data;

/** @author haibin */
@Data
public class BaseExperimentRequest extends BaseRequest {

  /** 演练名字 */
  @ApiParam private String name;

  /** 演练描述 */
  private String description;

  /** 标签 */
  private List<String> tags;

  /** 小程序描述 */
  private List<String> miniAppDesc;

  /** 演练空间 */
  private List<String> workspaces;

  /** 外部ID */
  private String outerId;

  /** 演练ID */
  private String experimentId;

  private List<ExperimentRelation> relations;

  private Integer source;
}
