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

import com.alibaba.chaosblade.box.common.common.domain.definition.ExperimentDefinition;
import com.alibaba.chaosblade.box.common.common.enums.ExperimentStateEnum;
import com.alibaba.chaosblade.box.common.common.enums.ResultEnum;
import com.alibaba.chaosblade.box.common.common.enums.StateEnum;
import com.alibaba.chaosblade.box.common.common.enums.UserCheckState;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;
import lombok.Data;

/** @author haibin */
@Data
public class ChaosExperiment implements Serializable {

  /** 演练ID */
  private String experimentId;

  /** 演练名称 */
  private String name;

  /** 演练描述 */
  private String description;

  /** 演练关联的故障等级 */
  private String level;

  /** 演练的负责人 */
  private String ownerUserId;

  /** 演练的创建时间 */
  private Date createTime;

  private Date modifyTime;

  /** 演练的regionId，内部平台不需要 */
  private String regionId;

  /** 演练的namespace，内部平台不需要 */
  private String namespace;

  /** 演练的状态 */
  private ExperimentStateEnum state;

  /** 演练的标签 */
  private List<String> tags = new ArrayList<>();

  /** 演练包含了哪些小程序能力 */
  private List<String> miniAppDesc;

  /** 演练的定义 */
  private ExperimentDefinition definition;

  /** 最近一次任务的运行结果 */
  private ResultEnum taskResult;

  /** 最近一次任务的状态 */
  private StateEnum taskState;

  /** 最后一次执行的任务 */
  private String taskId;

  /** 最后一次执行任务的用户检查状态 */
  private UserCheckState taskUserCheckState;

  /** 演练中涉及的aone应用名列表 */
  private Set<String> aoneApps;

  private List<ExperimentRelation> relations;

  private List<WorkspaceShortInfo> workspaces;

  /** @deprecated use {@link this#permission} instead */
  boolean hasPermission;

  int permission;

  String aclUrl;

  /** 来源系统 */
  private String source;

  private String outerId;

  private ExperimentSchedulerConfig schedulerConfig;
}
