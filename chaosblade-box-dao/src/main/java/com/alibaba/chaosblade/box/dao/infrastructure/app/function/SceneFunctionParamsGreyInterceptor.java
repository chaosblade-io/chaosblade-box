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

import com.alibaba.chaosblade.box.common.common.domain.user.ChaosUser;
import com.alibaba.chaosblade.box.common.infrastructure.ChaosApplicationContext;
import com.alibaba.chaosblade.box.common.infrastructure.ChaosRequestContextHolder;
import com.alibaba.chaosblade.box.dao.model.SceneFunctionDO;
import com.alibaba.chaosblade.box.dao.model.SceneFunctionParameterDO;
import com.google.common.base.Predicate;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.stereotype.Component;

/** @author haibin.lhb */
@Component
public class SceneFunctionParamsGreyInterceptor implements SceneFunctionParametersInterceptor {

  private Map<String, List<String>> paramsToUserId = new ConcurrentHashMap<>();

  public boolean isAvailableToUser(String paramsAlias, ChaosUser chaosUser) {
    if (chaosUser == null) {
      return true;
    }
    if (paramsToUserId.containsKey(paramsAlias)) {
      return paramsToUserId.get(paramsAlias).contains(chaosUser.getUserId());
    }
    return true;
  }

  public void syncParams(Map<String, List<String>> params) {
    paramsToUserId.putAll(params);
  }

  @Override
  public void afterQuery(
      SceneFunctionDO sceneFunctionDO, List<SceneFunctionParameterDO> parameters) {
    Optional<ChaosApplicationContext> chaosApplicationContext =
        ChaosRequestContextHolder.getApplicationContext();
    if (!chaosApplicationContext.isPresent()) {
      return;
    }
    ChaosUser chaosUser = chaosApplicationContext.get().getLoginUser();
    if (chaosUser == null) {
      return;
    }
    parameters.removeIf(
        (Predicate<SceneFunctionParameterDO>)
            input -> {
              if (input == null) {
                return false;
              }
              return !isAvailableToUser(input.getAlias(), chaosUser);
            });
  }

  @Override
  public void beforeSync(SceneSynchronousHelper.FunctionWrapper functionWrapper) {}
}
