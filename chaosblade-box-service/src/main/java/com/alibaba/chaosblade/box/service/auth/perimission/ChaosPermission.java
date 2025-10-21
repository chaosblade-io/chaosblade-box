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

package com.alibaba.chaosblade.box.service.auth.perimission;

/** @author haibin */
import static com.alibaba.chaosblade.box.common.infrastructure.constant.CommonConstant.VERTICAL_LINE;

import lombok.Data;

/** @author haibin */
@Data
public class ChaosPermission {

  public static String READ_OPERATION = "READ";

  public static String WRITE_OPERATION = "WRITE";

  private String permission;

  private String target;

  public String getName() {
    return permission + VERTICAL_LINE + target;
  }

  public ChaosPermission() {}

  public ChaosPermission(String permission, String target) {
    this.permission = permission;
    this.target = target;
  }
}
