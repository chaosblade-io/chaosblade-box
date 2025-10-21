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

package com.alibaba.chaosblade.box.dao.infrastructure.app.function;

import com.alibaba.chaosblade.box.common.common.domain.definition.SceneFunctionParameterComponent;
import org.springframework.stereotype.Component;

/** @author haibin */
@Component
public class PasswordComponentParameterInterceptor
    implements SceneFunctionParameterEncoderAndDecoder {

  @Override
  public String encodeValue(
      String appCode,
      String alias,
      String value,
      SceneFunctionParameterComponent sceneFunctionParameterComponent)
      throws Exception {
    return SceneParamPasswordHandler.encrypt(value);
  }

  @Override
  public String decodeValue(
      String appCode,
      String alias,
      String value,
      SceneFunctionParameterComponent sceneFunctionParameterComponent)
      throws Exception {
    return SceneParamPasswordHandler.decrypt(value);
  }

  @Override
  public boolean supportSceneFunctionParameterComponent(
      SceneFunctionParameterComponent sceneFunctionParameterComponent) {
    return SceneFunctionParameterComponent.TYPE_PASSWORD.equals(
        sceneFunctionParameterComponent.getType());
  }
}
