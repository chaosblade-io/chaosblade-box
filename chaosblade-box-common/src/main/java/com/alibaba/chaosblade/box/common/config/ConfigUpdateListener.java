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

package com.alibaba.chaosblade.box.common.config;

/** @author haibin.lhb */
public interface ConfigUpdateListener {

  /**
   * 配置更新监听器，当配置发生变化时候
   *
   * @param chaosSettingInfo
   */
  public void onUpdate(ChaosSettingInfo chaosSettingInfo);

  /**
   * 支持的key
   *
   * @param chaosSettingInfo
   * @return true支持
   */
  public boolean support(ChaosSettingInfo chaosSettingInfo);
}
