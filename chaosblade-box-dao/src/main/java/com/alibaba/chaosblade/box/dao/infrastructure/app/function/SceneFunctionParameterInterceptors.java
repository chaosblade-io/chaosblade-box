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

import com.alibaba.chaosblade.box.dao.model.SceneFunctionDO;
import com.alibaba.chaosblade.box.dao.model.SceneFunctionParameterDO;
import java.util.List;
import java.util.function.Consumer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/** @author haibin */
@Component
public class SceneFunctionParameterInterceptors {

  @Autowired private List<SceneFunctionParametersInterceptor> sceneFunctionParametersInterceptors;

  public List<SceneFunctionParameterDO> doUserParameterFilter(
      SceneFunctionDO sceneFunctionDO, List<SceneFunctionParameterDO> sceneFunctionParameterDOS) {
    for (SceneFunctionParametersInterceptor sceneFunctionParametersInterceptor :
        sceneFunctionParametersInterceptors) {
      sceneFunctionParametersInterceptor.afterQuery(sceneFunctionDO, sceneFunctionParameterDOS);
    }
    return sceneFunctionParameterDOS;
  }

  public void beforeSync(SceneSynchronousHelper.FunctionWrapper functionWrapper) {
    sceneFunctionParametersInterceptors.forEach(
        new Consumer<SceneFunctionParametersInterceptor>() {
          @Override
          public void accept(
              SceneFunctionParametersInterceptor sceneFunctionParametersInterceptor) {
            sceneFunctionParametersInterceptor.beforeSync(functionWrapper);
          }
        });
  }
}
