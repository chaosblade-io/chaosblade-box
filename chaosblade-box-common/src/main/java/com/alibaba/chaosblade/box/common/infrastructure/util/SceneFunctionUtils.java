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

package com.alibaba.chaosblade.box.common.infrastructure.util;

import com.alibaba.chaosblade.box.common.infrastructure.constant.CommonConstant;
import com.google.common.base.Preconditions;

/** @author haibin */
public final class SceneFunctionUtils {

  /**
   * functionCode至少三位，a.b.c,前面两位是场景code,a.b
   *
   * @param functionCode
   * @return
   */
  public static String extractSceneCodeFronSunctionCode(String functionCode) {
    Preconditions.checkArgument(functionCode != null, "functionCode not null");
    String[] codes = functionCode.split("\\.");
    Preconditions.checkArgument(codes.length >= 2, "functionCode illegal");
    return codes[0] + CommonConstant.DOT + codes[1];
  }
}
