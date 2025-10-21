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

package com.alibaba.chaosblade.box.dao.infrastructure.service;

import com.alibaba.chaosblade.box.common.infrastructure.exception.ChaosException;
import com.alibaba.chaosblade.box.dao.infrastructure.app.function.sync.ChaosBladeSynchronizer;
import com.alibaba.chaosblade.box.dao.model.SceneDO;
import com.alibaba.chaosblade.box.dao.model.SceneFunctionDO;
import com.alibaba.chaosblade.box.dao.repository.SceneFunctionParameterRepository;
import com.alibaba.chaosblade.box.dao.repository.SceneFunctionRepository;
import java.util.List;
import java.util.Optional;
import javax.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class SceneDaoFunctionServiceImpl implements SceneDaoFunctionService {
  @Resource private ChaosBladeSynchronizer chaosBladeSynchronizer;

  @Resource private SceneFunctionRepository sceneFunctionRepository;

  @Resource private SceneFunctionParameterRepository sceneFunctionParameterRepository;

  @Override
  public void rebuildChaosBladeFunctions(List<SceneFunctionDO> sceneFunctionDOS, boolean force)
      throws ChaosException {
    SceneDO scene = new SceneDO();
    scene.setFunctions(sceneFunctionDOS);
    chaosBladeSynchronizer.syncSceneFunctions(scene);
  }

  @Override
  public SceneFunctionDO querySceneFunctionByCode(String code) {
    Optional<SceneFunctionDO> sceneFunctionDOOptional = sceneFunctionRepository.findByCode(code);
    return sceneFunctionDOOptional
        .map(this::withParameter)
        .orElseGet(() -> sceneFunctionDOOptional.orElse(null));
  }

  private SceneFunctionDO withParameter(SceneFunctionDO sceneFunctionDO) {
    sceneFunctionParameterRepository
        .findAllParamsByFunctionId(sceneFunctionDO.getFunctionId())
        .ifPresent(sceneFunctionDO::setParameters);
    return sceneFunctionDO;
  }
}
