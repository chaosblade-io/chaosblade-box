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

import com.alibaba.chaosblade.box.common.app.sdk.scope.Host;
import lombok.Getter;

/** @author haibin */
@Getter
public class ChaosBladeExpQueryRequest extends ChaosBladeHostQueryRequest {

  private String expId;

  private boolean k8s = false;

  public ChaosBladeExpQueryRequest(Host host, String expId) {
    super(host);
    this.expId = expId;
  }

  public ChaosBladeExpQueryRequest(Host host, String expId, boolean isK8s) {
    super(host);
    this.expId = expId;
    this.k8s = isK8s;
  }
}
