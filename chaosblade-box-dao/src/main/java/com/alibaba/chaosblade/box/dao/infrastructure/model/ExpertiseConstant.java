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

package com.alibaba.chaosblade.box.dao.infrastructure.model;

/** @author haibin */
public final class ExpertiseConstant {

  public static Integer STATE_DISABLE = 0;

  public static Integer STATE_ENABLED = 1;

  public static Integer STATE_DELETED = 2;

  /** 系统经验库 */
  public static Integer SYSTEM_EXPERTISE = 0;

  /** 用户经验库 */
  public static Integer USER_EXPERTISE = 1;
}
