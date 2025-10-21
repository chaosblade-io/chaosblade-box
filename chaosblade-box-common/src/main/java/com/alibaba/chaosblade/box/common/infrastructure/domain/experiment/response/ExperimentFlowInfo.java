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

package com.alibaba.chaosblade.box.common.infrastructure.domain.experiment.response;

import com.alibaba.chaosblade.box.common.common.domain.experiment.ExperimentSchedulerConfig;
import com.alibaba.chaosblade.box.common.common.enums.ExperimentRunModeEnum;
import com.alibaba.chaosblade.box.common.infrastructure.domain.experiment.flow.MiniFlowGroup;
import com.alibaba.chaosblade.box.common.infrastructure.domain.experiment.guard.ExperimentGuardConfiguration;
import java.util.List;
import lombok.Data;

/** @author haibin */
@Data
public class ExperimentFlowInfo {

  /** 演练ID */
  private String experimentId;

  /** 微流程group */
  private List<MiniFlowGroup> flowGroups;

  /** 守护者 */
  private ExperimentGuardConfiguration guardConf;

  /** 运行模式 */
  private ExperimentRunModeEnum runMode;

  /** 演练持续时间 */
  private Long duration;

  private ExperimentSchedulerConfig schedulerConfig;
}
