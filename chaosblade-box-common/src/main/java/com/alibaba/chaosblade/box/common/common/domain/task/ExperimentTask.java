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

package com.alibaba.chaosblade.box.common.common.domain.task;

import com.alibaba.chaosblade.box.common.common.domain.experiment.PhaseInfo;
import java.util.List;
import lombok.Data;

/**
 * 演练的任务对象，一个演练分为: Experiment->Phase->Activity
 *
 * <p>1/一个演练包含多个阶段(Phase), 2.一个Phase对应了多个Activity 3.一个activity对应一个小程序
 *
 * @author haibin
 */
@Data
public class ExperimentTask extends BaseExperimentTask {

  /** 进度百分比 */
  private long progressPercent;

  /** 每个阶段的信息 */
  private List<PhaseInfo> phases;
}
