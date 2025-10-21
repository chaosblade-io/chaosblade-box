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
import java.util.Objects;
import org.springframework.stereotype.Component;

/**
 * Author: sunju
 *
 * <p>Date: 2020/1/3
 */
@Component
public class MetricParametersInterceptor implements SceneFunctionParametersInterceptor {

  private static final String METRIC_CODE = "chaosapp.metric";

  @Override
  public void afterQuery(
      SceneFunctionDO sceneFunctionDO, List<SceneFunctionParameterDO> parameters) {
    if (null != sceneFunctionDO && sceneFunctionDO.getCode().startsWith(METRIC_CODE)) {
      parameters.removeIf(parameter -> Objects.equals(parameter.getAlias(), "from"));
      parameters.removeIf(parameter -> Objects.equals(parameter.getAlias(), "to"));
    }
  }

  @Override
  public void beforeSync(SceneSynchronousHelper.FunctionWrapper functionWrapper) {}
}
