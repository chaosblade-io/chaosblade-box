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

package com.alibaba.chaosblade.box.service.model.expertise;

import com.alibaba.chaosblade.box.common.common.domain.ChaosError;
import com.alibaba.chaosblade.box.common.common.enums.CommonErrorCode;
import com.google.common.base.Strings;
import org.springframework.stereotype.Component;

/** @author haibin */
@Component
public class ExpertiseParamChecker {

  public ChaosError checkExpertiseBasicInfo(ExpertiseBasicInfo expertiseBasicInfo) {
    if (Strings.isNullOrEmpty(expertiseBasicInfo.getName())) {
      return ChaosError.withCode(CommonErrorCode.P_ARGUMENT_ILLEGAL, "name Required");
    }
    if (Strings.isNullOrEmpty(expertiseBasicInfo.getFunctionDesc())) {
      return ChaosError.withCode(CommonErrorCode.P_ARGUMENT_ILLEGAL, "functionDesc Required");
    }
    if (Strings.isNullOrEmpty(expertiseBasicInfo.getBackgroundDesc())) {
      return ChaosError.withCode(CommonErrorCode.P_ARGUMENT_ILLEGAL, "backGroundDesc Required");
    }
    if (Strings.isNullOrEmpty(expertiseBasicInfo.getDesignConcept())) {
      return ChaosError.withCode(CommonErrorCode.P_ARGUMENT_ILLEGAL, "designConcept Required");
    }
    return null;
  }
}
