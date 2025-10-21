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

package com.alibaba.chaosblade.box.common.infrastructure.constant;

import com.alibaba.chaosblade.box.common.common.util.MiniAppUtils;

/** @author haibin.lhb */
public enum OsTypeEnum {

  /** Linux系统 */
  Linux(0),
  /** Windows系统 */
  Windows(1);

  public int getType() {
    return type;
  }

  private int type;

  OsTypeEnum(int type) {
    this.type = type;
  }

  public static OsTypeEnum ofAppCode(String appCode) {
    return MiniAppUtils.isWin(appCode) ? Windows : Linux;
  }
}
