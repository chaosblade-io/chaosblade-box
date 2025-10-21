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

package com.alibaba.chaosblade.box.dao.infrastructure.experiment.task.async;

import com.alibaba.chaosblade.box.dao.model.ChaosBladeExpUidDO;
import com.alibaba.chaosblade.box.dao.model.ExperimentMiniAppTaskDO;
import lombok.Data;

/** @author sunpeng */
@Data
public class AsyncCallBackContext {

  private ExperimentMiniAppTaskDO experimentMiniAppTaskDO;

  private ChaosBladeExpUidDO chaosBladeExpUidDO;

  private String uid;

  private String status;

  private String error;

  private String toolType;

  public AsyncCallBackContext() {}

  public AsyncCallBackContext(
      ExperimentMiniAppTaskDO experimentMiniAppTaskDO,
      ChaosBladeExpUidDO chaosBladeExpUidDO,
      String uid,
      String status,
      String error,
      String toolType) {
    this.experimentMiniAppTaskDO = experimentMiniAppTaskDO;
    this.chaosBladeExpUidDO = chaosBladeExpUidDO;
    this.status = status;
    this.uid = uid;
    this.toolType = toolType;
    this.error = error;
  }
}
