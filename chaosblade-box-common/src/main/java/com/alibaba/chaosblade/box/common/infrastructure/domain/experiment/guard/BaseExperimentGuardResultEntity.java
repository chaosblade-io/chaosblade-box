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

package com.alibaba.chaosblade.box.common.infrastructure.domain.experiment.guard;

import java.util.ArrayList;
import java.util.List;
import lombok.Data;

/** @author haibin */
@Data
public class BaseExperimentGuardResultEntity {

  /** 指标分类名称,比如CPU */
  private String name;

  private String guardId;

  private GuardRunState state;

  private String alias;

  /** 图标数据 */
  private List<ExperimentGuardMetricDataItem> data = new ArrayList<>();

  /** 具体哪一项Metric,比如:Cpu下面的系统利用率 */
  private String subName;

  /** 图标数据查询不到的错误原因. */
  private String failureDetail;

  /** 失败code */
  private String failureCode;

  private Long lastRecordPoint;
}
