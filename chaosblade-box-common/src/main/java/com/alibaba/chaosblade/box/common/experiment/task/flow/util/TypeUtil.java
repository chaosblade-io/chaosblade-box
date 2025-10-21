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

package com.alibaba.chaosblade.box.common.experiment.task.flow.util;

import jodd.typeconverter.TypeConverter;
import jodd.typeconverter.TypeConverterManager;
import lombok.experimental.UtilityClass;

/** @author sunju */
@UtilityClass
public class TypeUtil {

  public static <F, T> T convert(F object, Class<T> targetType) {
    if (null == object || object.getClass() == targetType) {
      return (T) object;
    }
    try {
      TypeConverter<T> converter = TypeConverterManager.get().lookup(targetType);
      if (null != converter) {
        return converter.convert(object);
      }
    } catch (Exception ignored) {
    }
    return null;
  }
}
