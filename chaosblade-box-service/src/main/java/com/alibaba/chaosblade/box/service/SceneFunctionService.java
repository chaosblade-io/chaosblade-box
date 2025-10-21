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

package com.alibaba.chaosblade.box.service;

import com.alibaba.chaosblade.box.common.infrastructure.domain.scene.SceneFunctionCreateRequest;
import com.alibaba.chaosblade.box.common.infrastructure.domain.scene.SceneQueryRequest;
import com.alibaba.chaosblade.box.common.infrastructure.exception.ChaosException;
import com.alibaba.chaosblade.box.dao.model.SceneFunctionDO;
import com.alibaba.chaosblade.box.dao.model.base.PageableResponse;

/**
 * @author sunju
 *     <p>Modified by jiumu at 2019-07-24
 */
public interface SceneFunctionService {

  /**
   * 根据appCode查找小程序
   *
   * @param code
   * @return
   */
  SceneFunctionDO querySceneFunctionByCode(String code);

  /**
   * 创建小程序基本信息
   *
   * @param sceneFunctionCreateRequest
   * @return 新增成功时返回functionId
   */
  String addSceneFunction(SceneFunctionCreateRequest sceneFunctionCreateRequest)
      throws ChaosException;

  /**
   * 查询小程序是不是裂变小程序
   *
   * @deprecated
   */
  Boolean queryIsFissionSceneFunction(String functionId);

  PageableResponse<SceneFunctionDO> querySceneFunctions(
      int pageNo, int pageSize, SceneQueryRequest queryRequest);
}
