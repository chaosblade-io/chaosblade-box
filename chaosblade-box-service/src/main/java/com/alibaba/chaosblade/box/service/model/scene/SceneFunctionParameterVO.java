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

package com.alibaba.chaosblade.box.service.model.scene;

import com.alibaba.chaosblade.box.common.common.domain.definition.SceneFunctionParameterComponent;
import com.alibaba.chaosblade.box.dao.model.SceneFunctionParameterDO;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

/** @author sunju */
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@NoArgsConstructor
public class SceneFunctionParameterVO {

  String parameterId;
  String functionId;
  String name;
  String alias;
  String description;
  Integer type;
  Integer sequence;
  SceneFunctionParameterComponent component;

  public static SceneFunctionParameterVO from(SceneFunctionParameterDO parameter) {
    if (null == parameter) {
      return null;
    }
    SceneFunctionParameterVO functionParameter = new SceneFunctionParameterVO();
    functionParameter.setFunctionId(parameter.getFunctionId());
    functionParameter.setParameterId(parameter.getParameterId());
    functionParameter.setName(parameter.getName());
    functionParameter.setAlias(parameter.getAlias());
    functionParameter.setDescription(parameter.getDescription());
    functionParameter.setComponent(parameter.getComponent());
    functionParameter.setType(parameter.getType());
    functionParameter.setSequence(parameter.getSequence());
    return functionParameter;
  }
}
