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

package com.alibaba.chaosblade.box.service.impl;

import com.alibaba.chaosblade.box.dao.infrastructure.app.function.SceneSynchronousHelper;
import com.alibaba.chaosblade.box.dao.model.SceneFunctionDO;
import com.alibaba.chaosblade.box.dao.model.SceneFunctionParameterDO;
import com.alibaba.chaosblade.box.dao.repository.SceneFunctionParameterRepository;
import com.alibaba.chaosblade.box.dao.repository.SceneFunctionRepository;
import com.alibaba.chaosblade.box.service.SceneFunctionParameterService;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import javax.annotation.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/** @author sunju */
@Service
@Transactional(rollbackFor = Throwable.class)
public class SceneFunctionParameterServiceImpl implements SceneFunctionParameterService {

  @Resource private SceneFunctionParameterRepository sceneFunctionParameterRepository;

  @Resource private SceneFunctionRepository sceneFunctionRepository;

  @Autowired private SceneSynchronousHelper sceneSynchronousHelper;

  @Override
  public List<SceneFunctionParameterDO> queryFilterParametersByFunctionId(String functionId) {
    Optional<SceneFunctionDO> optional = sceneFunctionRepository.findByFunctionId(functionId);
    if (!optional.isPresent()) {
      return Collections.emptyList();
    }
    String appCode = optional.get().getCode();
    String hierarchyDefaultCode =
        sceneSynchronousHelper.getHierarchyDefaultCode(optional.get().getCode());
    if (hierarchyDefaultCode == null) {
      hierarchyDefaultCode = appCode;
    }
    if (!Objects.equals(appCode, hierarchyDefaultCode)) {
      Optional<SceneFunctionDO> defaultScene =
          sceneFunctionRepository.findByCode(hierarchyDefaultCode);
      if (defaultScene.isPresent()) {
        return sceneFunctionParameterRepository.findFilterParamsByFunctionId(defaultScene.get());
      }
    }
    return sceneFunctionParameterRepository.findFilterParamsByFunctionId(optional.get());
  }
}
