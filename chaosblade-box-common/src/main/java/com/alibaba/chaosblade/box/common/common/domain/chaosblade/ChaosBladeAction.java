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

package com.alibaba.chaosblade.box.common.common.domain.chaosblade;

import lombok.Data;

/** @author haibin */
@Data
public class ChaosBladeAction {

  private ChaosBladeActionType actionType;

  public ChaosBladeActionType getActionType() {
    return actionType;
  }

  public String getAction() {
    return action;
  }

  private String action;

  private String target;

  private String subTarget;

  private String prepareType;

  /** 范围 */
  private String scope;

  private boolean hasJavaInstall;

  public boolean isJvm() {
    if (hasJavaInstall) {
      return true;
    }
    if (ChaosBladeMetaData.SUB_TARGET_JVM.equals(subTarget)) {
      return true;
    }
    if (!ChaosBladeMetaData.SCOPE_HOST.equals(scope)) {
      return ChaosBladeMetaData.SubTargetWithsJavaAgentInstall.contains(subTarget);
    }
    return false;
  }

  public void setJvm(boolean jvm) {
    isJvm = jvm;
  }

  public void setHasJavaInstall(boolean has) {
    this.hasJavaInstall = has;
  }

  private boolean isJvm;

  /** 是否上架 */
  private boolean enabled;
}
