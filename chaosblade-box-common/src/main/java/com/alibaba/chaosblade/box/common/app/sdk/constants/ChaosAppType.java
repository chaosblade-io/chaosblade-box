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

package com.alibaba.chaosblade.box.common.app.sdk.constants;

import java.util.Arrays;

/**
 * All app types
 *
 * @author sunju
 */
public enum ChaosAppType {

  /**
   * component type
   *
   * @since 1.0.0
   */
  COMPONENT,

  /**
   * system type, agent attack/unattach only
   *
   * @since 1.0.0
   */
  SYSTEM,

  /**
   * chaosapp type, develop by 3rd only
   *
   * @since 1.0.0
   */
  CHAOS_APP,

  /**
   * combine type, combine by component type or chaosapp type only
   *
   * @since 1.0.0
   */
  COMBINE;

  public static ChaosAppType of(String name) {
    if (null == name || name.length() <= 0) {
      return null;
    }
    return Arrays.stream(ChaosAppType.values())
        .filter(type -> type.name().toLowerCase().equals(name.toLowerCase()))
        .findFirst()
        .get();
  }
}
