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

package com.alibaba.chaosblade.box.cache.utils;

import lombok.experimental.UtilityClass;

/**
 * Author: sunju
 *
 * <p>Date: 2019/11/11
 */
@UtilityClass
public class Strings {

  /**
   * Alternative of {@link String#join(CharSequence, CharSequence...)} without delimiter.
   *
   * @param objects Object array to be joined.
   * @return A string that joined without any delimiter.
   */
  public static String join(Object... objects) {
    StringBuilder stringBuilder = new StringBuilder();
    for (Object object : objects) {
      stringBuilder.append(object);
    }
    return stringBuilder.toString();
  }
}
