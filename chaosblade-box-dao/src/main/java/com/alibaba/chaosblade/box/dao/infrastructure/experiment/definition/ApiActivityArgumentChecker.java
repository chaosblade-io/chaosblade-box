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

package com.alibaba.chaosblade.box.dao.infrastructure.experiment.definition;

import com.alibaba.chaosblade.box.common.common.domain.definition.ExperimentActivityDefinition;
import com.alibaba.chaosblade.box.common.common.domain.definition.ExperimentNodeArgumentsDefinition;
import com.alibaba.chaosblade.box.common.common.domain.definition.SceneFunctionParameterComponent;
import com.alibaba.chaosblade.box.common.infrastructure.exception.ChaosException;
import com.alibaba.chaosblade.box.dao.model.SceneFunctionDO;
import com.alibaba.chaosblade.box.dao.model.SceneFunctionParameterDO;
import com.alibaba.chaosblade.box.dao.repository.SceneFunctionParameterRepository;
import com.alibaba.chaosblade.box.dao.repository.SceneFunctionRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

/** @author haibin */
@Component
@Order(value = 1000)
public class ApiActivityArgumentChecker implements ActivityDefinitionChecker {

  @Autowired private SceneFunctionParameterRepository sceneFunctionParameterRepository;

  @Autowired private SceneFunctionRepository sceneFunctionRepository;

  @Override
  public void check(ExperimentActivityDefinition experimentActivityDefinition, boolean fromApi)
      throws ChaosException {
    if (fromApi) {
      String appCode = experimentActivityDefinition.getAppCode();
      ExperimentNodeArgumentsDefinition experimentNodeArgumentsDefinition =
          experimentActivityDefinition.getArguments();
      if (experimentNodeArgumentsDefinition != null) {
        SceneFunctionDO sceneFunctionDO = sceneFunctionRepository.findByCode(appCode).orElse(null);
        if (sceneFunctionDO == null) {
          return;
        }
        List<SceneFunctionParameterDO> sceneFunctionParameterDOS =
            sceneFunctionParameterRepository
                .findAllParamsByFunctionId(sceneFunctionDO.getFunctionId())
                .get();
        Map<String, SceneFunctionParameterDO> sceneFunctionParameterDOMap =
            sceneFunctionParameterDOS.stream()
                .collect(
                    Collectors.toMap(
                        SceneFunctionParameterDO::getAlias,
                        sceneFunctionParameterDO -> sceneFunctionParameterDO));
        Map<String, String> arguments = experimentNodeArgumentsDefinition.getAllArguments();
        List<String> notExistParams = new ArrayList<>();
        List<String> requiredParams = new ArrayList<>();
        for (String key : arguments.keySet()) {
          SceneFunctionParameterDO sceneFunctionParameterDO = sceneFunctionParameterDOMap.get(key);
          if (sceneFunctionParameterDO == null || sceneFunctionParameterDO.getIsDelete()) {
            notExistParams.add(key);
          }
        }
        for (Map.Entry<String, SceneFunctionParameterDO> entry :
            sceneFunctionParameterDOMap.entrySet()) {
          if (arguments.get(entry.getKey()) == null) {
            SceneFunctionParameterComponent sceneFunctionParameterComponent =
                entry.getValue().getComponent();
            if (sceneFunctionParameterComponent != null) {
              if (sceneFunctionParameterComponent.isRequired()) {
                requiredParams.add(entry.getKey());
              }
            }
          }
        }
        StringBuilder stringBuilder = new StringBuilder();
        if (!notExistParams.isEmpty()) {
          stringBuilder.append("not exist:").append(notExistParams.toString());
        }
        if (!requiredParams.isEmpty()) {
          stringBuilder.append(",required:").append(requiredParams.toString());
        }
        String error = stringBuilder.toString();
        if (!error.isEmpty()) {
          throw new ActivityDefinitionIllegalException(
              "AppCode:" + appCode + ",params error," + error);
        }
      }
    }
  }
}
