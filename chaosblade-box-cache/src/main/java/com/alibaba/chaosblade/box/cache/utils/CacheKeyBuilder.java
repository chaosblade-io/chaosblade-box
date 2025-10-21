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

import com.google.common.base.Preconditions;
import org.apache.commons.lang3.StringUtils;

public class CacheKeyBuilder {

  public static String buildCacheKey(String prefix, String... content) {
    Preconditions.checkArgument(StringUtils.isNotBlank(prefix));
    StringBuilder sb = new StringBuilder();
    sb.append(prefix);
    sb.append("::");

    for (int i = 0; i < content.length; ++i) {
      sb.append(content[i]);
      if (i < content.length - 1) {
        sb.append(":");
      }
    }

    return sb.toString();
  }
}
