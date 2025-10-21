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

import com.alibaba.chaosblade.box.common.app.sdk.scope.Scope;
import com.alibaba.chaosblade.box.common.common.domain.user.ChaosUser;
import com.alibaba.chaosblade.box.common.infrastructure.domain.scope.ScopeQuery;
import com.alibaba.chaosblade.box.dao.model.base.PageableQueryWrapper;
import com.alibaba.chaosblade.box.dao.model.base.PageableResponse;
import java.util.List;

/**
 * Service definition for host,device and etc.
 *
 * @author sunju
 */
public interface ScopeService {

  /**
   * Query all alive devices.
   *
   * @param query query condition
   * @param <T> device type for internal and cloud
   * @return all alive devices
   */
  <T extends Scope> List<T> queryAliveScopes(ScopeQuery query);

  <T extends Scope, Q extends ScopeQuery> PageableResponse<T> queryAliveScopesByPage(
      ChaosUser user, PageableQueryWrapper<Q> query, String nameSpace);
}
