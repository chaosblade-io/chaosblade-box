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

package com.alibaba.chaosblade.box.common.app.sdk;

/** @author sunju */
public abstract class BaseChaosApp implements ChaosApp, ChaosAppContextAware, ChaosToolkitAware {

  private ChaosAppContext context;

  private ChaosToolkit toolkit;

  @Override
  public void setContext(ChaosAppContext context) {
    this.context = context;
  }

  @Override
  public ChaosAppContext getContext() {
    return this.context;
  }

  @Override
  public void setChaosToolkit(ChaosToolkit toolkit) {
    this.toolkit = toolkit;
  }

  @Override
  public ChaosToolkit toolkit() {
    return this.toolkit;
  }
}
