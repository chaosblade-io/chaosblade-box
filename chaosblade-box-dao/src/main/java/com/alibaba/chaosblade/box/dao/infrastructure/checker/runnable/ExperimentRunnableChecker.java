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

package com.alibaba.chaosblade.box.dao.infrastructure.checker.runnable;

import com.alibaba.chaosblade.box.common.common.domain.ChaosError;
import com.alibaba.chaosblade.box.common.infrastructure.domain.experiment.request.ExperimentRunRequest;
import com.alibaba.chaosblade.box.dao.model.ExperimentDO;

/**
 * 判断一个演练是否能运行的接口
 *
 * @author haibin
 */
public interface ExperimentRunnableChecker {

  /**
   * 演练是否能运行
   *
   * @param experimentDO 演练对象
   * @param experimentRunRequest experiment Run request
   * @return ChaosError 如果可以返回Null
   */
  ChaosError checkRunnable(ExperimentRunRequest experimentRunRequest, ExperimentDO experimentDO);
}
