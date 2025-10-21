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

import com.alibaba.chaosblade.box.common.common.domain.response.Response;
import com.alibaba.chaosblade.box.dao.infrastructure.experiment.task.flow.MiniAppInvokeErrorMessageDescProvider;
import com.alibaba.chaosblade.box.dao.infrastructure.experiment.task.flow.step.MiniAppInvokeContext;
import com.google.common.base.Strings;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/** @author haibin */
@Component
public class ChaosBladeResultCodeRepo implements InitializingBean {

  @Autowired
  private List<MiniAppInvokeErrorMessageDescProvider> miniAppInvokeErrorMessageDescProviders;

  protected static List<MiniAppInvokeErrorMessageDescProvider> globalProviders;

  public static Optional<ChaosbladeErrorMessageDesc> getDescByMessage(
      MiniAppInvokeContext miniAppInvokeContext, Response response) {
    if (Strings.isNullOrEmpty(response.getError())) {
      return Optional.empty();
    }
    ChaosbladeErrorMessageDesc chaosbladeErrorMessageDesc = null;
    for (MiniAppInvokeErrorMessageDescProvider miniAppInvokeErrorMessageDescProvider :
        globalProviders) {
      chaosbladeErrorMessageDesc =
          miniAppInvokeErrorMessageDescProvider.getDescByErrorMessage(
              miniAppInvokeContext, response);
      if (chaosbladeErrorMessageDesc != null) {
        break;
      }
    }
    return Optional.ofNullable(chaosbladeErrorMessageDesc);
  }

  public static Optional<CodeMetaData> getByCode(Integer code) {
    if (code == null) {
      return Optional.empty();
    }
    CodeMetaData codeMetaData = null;
    for (MiniAppInvokeErrorMessageDescProvider miniAppInvokeErrorMessageDescProvider :
        globalProviders) {
      codeMetaData = miniAppInvokeErrorMessageDescProvider.getMetaDataByChaosBladeCode(code);
      if (codeMetaData != null) {
        break;
      }
    }
    return Optional.ofNullable(codeMetaData);
  }

  public static Optional<CodeMetaData> getByErrorCode(String errorCode) {
    if (errorCode == null) {
      return Optional.empty();
    }
    CodeMetaData codeMetaData = null;
    for (MiniAppInvokeErrorMessageDescProvider miniAppInvokeErrorMessageDescProvider :
        globalProviders) {
      codeMetaData = miniAppInvokeErrorMessageDescProvider.getMetaDataByErrorCode(errorCode);
      if (codeMetaData != null) {
        break;
      }
    }
    return Optional.ofNullable(codeMetaData);
  }

  @Override
  public void afterPropertiesSet() throws Exception {
    globalProviders = miniAppInvokeErrorMessageDescProviders;
  }
}
