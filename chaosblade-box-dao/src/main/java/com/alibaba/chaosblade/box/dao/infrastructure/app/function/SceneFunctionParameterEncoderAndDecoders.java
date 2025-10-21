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
import com.google.common.base.Strings;
import java.util.List;
import java.util.Objects;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/** @author haibin */
@Component
@Slf4j
public class SceneFunctionParameterEncoderAndDecoders {

  @Autowired
  private List<SceneFunctionParameterEncoderAndDecoder> sceneFunctionParameterEncoderAndDecoderList;

  public String decode(
      String appCode,
      String alias,
      String value,
      SceneFunctionParameterComponent sceneFunctionParameterComponent) {
    if (sceneFunctionParameterComponent == null || Strings.isNullOrEmpty(value)) {
      return value;
    }
    for (SceneFunctionParameterEncoderAndDecoder sceneFunctionParameterEncoderAndDecoder :
        sceneFunctionParameterEncoderAndDecoderList) {
      if (sceneFunctionParameterEncoderAndDecoder.supportSceneFunctionParameterComponent(
          sceneFunctionParameterComponent)) {
        try {
          return sceneFunctionParameterEncoderAndDecoder.decodeValue(
              appCode, alias, value, sceneFunctionParameterComponent);
        } catch (Exception ex) {
          log.error("decode param value failed", ex);
          return value;
        }
      }
    }
    return value;
  }

  public String encode(
      String appCode,
      String alias,
      String value,
      SceneFunctionParameterComponent sceneFunctionParameterComponent) {
    if (sceneFunctionParameterComponent == null || Strings.isNullOrEmpty(value)) {
      return value;
    }
    for (SceneFunctionParameterEncoderAndDecoder sceneFunctionParameterEncoderAndDecoder :
        sceneFunctionParameterEncoderAndDecoderList) {
      if (sceneFunctionParameterEncoderAndDecoder.supportSceneFunctionParameterComponent(
          sceneFunctionParameterComponent)) {
        try {
          // 如果密文保持不变，那就不需要修改了
          if (Objects.equals(value, sceneFunctionParameterComponent.getCipherText())) {
            return value;
          }
          String newValue =
              sceneFunctionParameterEncoderAndDecoder.encodeValue(
                  appCode, alias, value, sceneFunctionParameterComponent);
          sceneFunctionParameterComponent.setPlainText(value);
          sceneFunctionParameterComponent.setCipherText(newValue);
          return newValue;
        } catch (Exception e) {
          log.error("encode param value failed", e);
          return value;
        }
      }
    }
    return value;
  }
}
