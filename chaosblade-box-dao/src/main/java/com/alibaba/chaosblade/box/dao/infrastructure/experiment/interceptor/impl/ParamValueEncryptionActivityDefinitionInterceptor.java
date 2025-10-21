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

package com.alibaba.chaosblade.box.dao.infrastructure.experiment.interceptor.impl;

import com.alibaba.chaosblade.box.common.app.sdk.constants.PhaseType;
import com.alibaba.chaosblade.box.common.common.domain.definition.ExperimentActivityDefinition;
import com.alibaba.chaosblade.box.common.common.domain.definition.ExperimentNodeArgumentsDefinition;
import com.alibaba.chaosblade.box.dao.infrastructure.app.function.SceneFunctionParameterEncoderAndDecoders;
import com.alibaba.chaosblade.box.dao.infrastructure.experiment.definition.ExperimentFlowDefinitionMeta;
import com.alibaba.chaosblade.box.dao.infrastructure.experiment.interceptor.ExperimentActivityDefinitionInterceptor;
import java.util.function.BiConsumer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 如果是密码类型，需要对密码进行加密
 *
 * @author haibin
 */
@Component
public class ParamValueEncryptionActivityDefinitionInterceptor
    implements ExperimentActivityDefinitionInterceptor {

  @Autowired
  private SceneFunctionParameterEncoderAndDecoders sceneFunctionParameterEncoderAndDecoders;

  @Override
  public void preInterceptor(
      ExperimentActivityDefinition originDefinition,
      PhaseType phaseType,
      ExperimentFlowDefinitionMeta experimentFlowDefinitionMeta) {
    ExperimentNodeArgumentsDefinition experimentNodeArgumentsDefinition =
        originDefinition.getArguments();
    experimentNodeArgumentsDefinition
        .getAllArguments()
        .forEach(
            new BiConsumer<String, String>() {
              @Override
              public void accept(String key, String value) {
                experimentNodeArgumentsDefinition.addArgs(
                    key,
                    sceneFunctionParameterEncoderAndDecoders.encode(
                        originDefinition.getExecutableAppCode(),
                        key,
                        value,
                        experimentNodeArgumentsDefinition.getArgsComponents().get(key)));
              }
            });
  }
}
