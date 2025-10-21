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

package com.alibaba.chaosblade.box.dao.infrastructure.app;

import com.alibaba.chaosblade.box.common.common.enums.CommonErrorCode;
import com.alibaba.chaosblade.box.common.infrastructure.domain.scene.SceneAuthorizedUpdateRequest;
import com.alibaba.chaosblade.box.common.infrastructure.exception.ChaosException;
import com.alibaba.chaosblade.box.common.infrastructure.util.RetryUtil;
import com.alibaba.chaosblade.box.dao.model.SceneFunctionDO;
import com.alibaba.chaosblade.box.dao.repository.SceneAuthorizedRepository;
import com.alibaba.chaosblade.box.dao.repository.SceneFunctionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/** @author haibin.lhb */
@Component
public class SceneFunctionUpdateInternalOperator {

  @Autowired private SceneFunctionRepository sceneFunctionRepository;

  @Autowired private SceneAuthorizedRepository sceneAuthorizedRepository;

  public void updateSceneFunction(SceneFunctionDO sceneFunction) {
    boolean updateResult =
        RetryUtil.retryIfReturnFalse(
            () -> sceneFunctionRepository.updateByFunctionId(sceneFunction));
    if (!updateResult) {
      throw new ChaosException(CommonErrorCode.B_UPDATE_MINIAPP_FAILED);
    }
    this.updateSceneFunctionAuthorizedRecord(sceneFunction);
  }

  private void updateSceneFunctionAuthorizedRecord(SceneFunctionDO function) throws ChaosException {
    SceneAuthorizedUpdateRequest request = new SceneAuthorizedUpdateRequest();
    request.setFunctionId(function.getFunctionId());
    request.setFunctionName(function.getName());
    request.setFunctionCode(function.getCode());
    request.setPhase(function.getPhaseFlag());
    request.setSource(function.getSource());
    request.setEnabled(function.getEnabled());
    request.setSupportScopeTypes(function.getSupportScopeTypeList());
    request.setIsDelete(function.getIsDelete());
    boolean result =
        RetryUtil.retryIfReturnFalse(() -> sceneAuthorizedRepository.updateByFunctionId(request));
    if (!result) {
      throw new ChaosException(CommonErrorCode.B_UPDATE_MINIAPP_FAILED);
    }
  }
}
