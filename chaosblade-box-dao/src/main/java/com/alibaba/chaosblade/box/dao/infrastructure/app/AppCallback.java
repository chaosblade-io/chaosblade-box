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

package com.alibaba.chaosblade.box.dao.infrastructure.app;

import com.alibaba.chaosblade.box.common.app.sdk.ChaosAppResponse;
import com.alibaba.chaosblade.box.common.app.sdk.constants.PhaseType;
import com.alibaba.chaosblade.box.common.app.sdk.scope.Scope;

/** @author sunju */
public interface AppCallback {

  /**
   * invoke after agent attached succeed if agent required
   *
   * @param scope target device which agent attached
   * @param phase phase
   * @param response result of agent attached, include some response data from agent
   */
  void agentAttached(Scope scope, PhaseType phase, ChaosAppResponse response);

  /**
   * invoke after agent attached failed if agent required
   *
   * @param scope target device which agent attached
   * @param phase phase
   * @param throwable error
   */
  void agentAttachFailed(Scope scope, PhaseType phase, Throwable throwable);

  /**
   * invoke after chaosapp running finished
   *
   * @param scope target device which chaosapp running
   * @param phase phase
   * @param response result of chaosapp executing
   */
  void completed(Scope scope, PhaseType phase, ChaosAppResponse response);

  /**
   * invoke after chaosapp running failed
   *
   * @param scope target device which chaosapp running
   * @param phase phase
   * @param throwable error
   */
  void failed(Scope scope, PhaseType phase, Throwable throwable);
}
