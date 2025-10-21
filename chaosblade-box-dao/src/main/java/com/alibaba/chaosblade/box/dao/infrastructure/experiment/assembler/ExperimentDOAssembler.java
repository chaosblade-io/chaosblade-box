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

package com.alibaba.chaosblade.box.dao.infrastructure.experiment.assembler;

import com.alibaba.chaosblade.box.common.common.domain.experiment.BaseExperimentRequest;
import com.alibaba.chaosblade.box.common.common.domain.user.ChaosUser;
import com.alibaba.chaosblade.box.common.infrastructure.util.ChaosIdGenerator;
import com.alibaba.chaosblade.box.dao.model.ExperimentDO;
import com.alibaba.fastjson.JSON;

/** @author haibin */
public class ExperimentDOAssembler {

  public static ExperimentDO assembleExperimentDO(BaseExperimentRequest baseExperimentRequest) {
    ExperimentDO experimentDO = new ExperimentDO();
    // experimentDO.setExperimentId(IdWorker.getIdStr());
    experimentDO.setName(baseExperimentRequest.getName());
    experimentDO.setDescription(baseExperimentRequest.getDescription());
    experimentDO.setExperimentId(ChaosIdGenerator.generateId());
    experimentDO.setNamespace(baseExperimentRequest.getNamespace());
    experimentDO.setMiniAppDesc(JSON.toJSONString(baseExperimentRequest.getMiniAppDesc()));

    ChaosUser user = baseExperimentRequest.getUser();
    if (null != user) {
      experimentDO.setUserId(user.getUserId());
    }

    return experimentDO;
  }
}
