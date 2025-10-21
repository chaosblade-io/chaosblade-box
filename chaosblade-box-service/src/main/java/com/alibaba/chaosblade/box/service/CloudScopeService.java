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

import com.alibaba.chaosblade.box.common.common.domain.Response;
import com.alibaba.chaosblade.box.dao.model.base.PageableResponse;
import com.alibaba.chaosblade.box.service.model.experiment.ExperimentScope;
import com.alibaba.chaosblade.box.service.model.scope.*;
import java.util.List;

/** @author haibin */
public interface CloudScopeService {

  /**
   * 查找一个scope
   *
   * @param scopeQueryRequest
   * @return
   */
  public Response<ScopeInfo> findOneScope(ScopeInfoQueryRequest scopeQueryRequest);

  /**
   * 分页查询资源信息
   *
   * @param experimentScopePageableRequest
   * @return
   */
  public Response<PageableResponse<ExperimentScope>> pageableQueryExperimentScopes(
      ExperimentScopePageableRequest experimentScopePageableRequest);

  /**
   * 当前机器的累计演练的次数
   *
   * @param scopeQueryRequest
   * @return
   */
  public Response<List<ExperimentScopeInvokeCount>> countExperimentScopeInvocation(
      ScopeInfoQueryRequest scopeQueryRequest);

  /**
   * 当前机器涉及到的演练场景
   *
   * @param scopeQueryRequest
   * @return
   */
  public Response<List<FunctionInvocationCount>> countExperimentScopeSceneFunctionCount(
      ScopeInfoQueryRequest scopeQueryRequest);

  /**
   * @param scopeInfoQueryRequest
   * @return
   */
  public Response<PageableResponse<ScopeExperimentTask>> pageableQueryExperimentTaskByScope(
      PageableScopeExperimentTaskQueryRequest scopeInfoQueryRequest);
}
