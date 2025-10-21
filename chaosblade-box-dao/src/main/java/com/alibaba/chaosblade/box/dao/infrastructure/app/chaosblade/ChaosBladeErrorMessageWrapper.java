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

package com.alibaba.chaosblade.box.dao.infrastructure.app.chaosblade;

import com.alibaba.chaosblade.box.common.app.sdk.constants.EnvironmentEnum;
import com.alibaba.chaosblade.box.common.common.domain.response.Response;
import com.google.common.base.Strings;

/** @author haibin */
public class ChaosBladeErrorMessageWrapper {

  public static String wrapper(
      EnvironmentEnum environmentEnum,
      Response response,
      ChaosbladeErrorMessageDesc chaosbladeErrorMessageDesc) {
    if (response.isSuccess()) {
      return null;
    }
    int code = response.getCode();
    if (chaosbladeErrorMessageDesc == null) {
      return response.getError();
    }
    CodeMetaData codeMetaData =
        ChaosBladeResultCodeRepo.getByCode(code)
            .orElse(ConfigFileMiniAppInvokeErrorMessageDescProvider.AGENT_INTERNAL_ERROR);
    String errorSummary =
        !Strings.isNullOrEmpty(codeMetaData.getCnDesc())
            ? codeMetaData.getCnDesc()
            : codeMetaData.getEnDesc();
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append(errorSummary);
    stringBuilder.append(":");
    stringBuilder.append(chaosbladeErrorMessageDesc.getCnDesc());
    if (chaosbladeErrorMessageDesc.isShowOriginError()) {
      stringBuilder.append("(");
      stringBuilder.append(response.getError());
      stringBuilder.append(")");
    }
    return stringBuilder.toString();
  }
}
