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

import lombok.Getter;

/**
 * 环境
 *
 * @author sunju
 */
public enum EnvironmentEnum {

  /** 日常环境 */
  DAILY(0),
  /** 预发环境 */
  PREPUB(0),
  /** 生产环境 */
  ONLINE(0);

  @Getter int flag;

  EnvironmentEnum(int flag) {
    this.flag = flag;
  }

  public boolean isDaily() {
    return DAILY.equals(this);
  }

  public static EnvironmentEnum of(String env) {
    if (null == env || env.isEmpty()) {
      return null;
    }
    for (EnvironmentEnum environment : EnvironmentEnum.values()) {
      if (environment.name().equals(env.toUpperCase())) {
        return environment;
      }
    }
    return null;
  }
}
