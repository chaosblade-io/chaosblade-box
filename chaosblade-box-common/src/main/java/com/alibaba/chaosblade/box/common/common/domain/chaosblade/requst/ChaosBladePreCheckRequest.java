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

package com.alibaba.chaosblade.box.common.common.domain.chaosblade.requst;

import com.alibaba.chaosblade.box.common.app.sdk.ChaosAppRequest;
import com.alibaba.chaosblade.box.common.app.sdk.scope.Host;
import com.alibaba.chaosblade.box.common.common.domain.chaosblade.ChaosBladeAction;
import lombok.Getter;

/** @author sunpeng */
@Getter
public class ChaosBladePreCheckRequest {

  private final String ak;

  private final String sk;

  private final Host host;

  private final ChaosBladeAction chaosBladeAction;

  private final ChaosAppRequest chaosAppRequest;

  private final String evnType;

  public ChaosBladePreCheckRequest(
      String ak,
      String sk,
      Host host,
      ChaosBladeAction chaosBladeAction,
      ChaosAppRequest chaosAppRequest,
      String evnType) {
    this.ak = ak;
    this.sk = sk;
    this.host = host;
    this.chaosBladeAction = chaosBladeAction;
    this.chaosAppRequest = chaosAppRequest;
    this.evnType = evnType;
  }
}
