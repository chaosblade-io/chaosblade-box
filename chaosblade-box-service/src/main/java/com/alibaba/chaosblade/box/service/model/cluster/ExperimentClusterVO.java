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

package com.alibaba.chaosblade.box.service.model.cluster;

import com.alibaba.chaosblade.box.service.model.experiment.ExperimentScope;
import java.util.List;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/** @author haibin.lhb */
@ToString
@Data
@EqualsAndHashCode
public class ExperimentClusterVO {

  private String clusterId;

  private String clusterName;

  private Long nodeCount;

  private Long onlineCount;

  /** agent状态 */
  private Boolean agentConsistency;
  /** 部分节点 */
  private List<ExperimentScope> partNodes;

  private List<String> chaosTools;

  private Boolean upgrade;

  private String version;

  private String pluginType;

  private String installMode;
}
