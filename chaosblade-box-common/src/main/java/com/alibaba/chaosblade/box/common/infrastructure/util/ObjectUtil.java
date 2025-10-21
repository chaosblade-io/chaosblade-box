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

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.alibaba.fastjson.parser.Feature;
import com.alibaba.fastjson.util.IOUtils;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import lombok.experimental.UtilityClass;

/** @author sunju */
@UtilityClass
public class ObjectUtil {

  public static <T> T deepClone(T object) {
    try {
      return JSON.parseObject(JSON.toJSONString(object), new TypeReference<T>() {});
    } catch (Exception e) {
      return object;
    }
  }

  public static <T> T parseResourceObject(String resourcePath, Type type, Feature... features)
      throws IOException {
    InputStream messageInputstream =
        Thread.currentThread().getContextClassLoader().getResourceAsStream(resourcePath);
    return JSON.parseObject(messageInputstream, IOUtils.UTF8, type, features);
  }
}
