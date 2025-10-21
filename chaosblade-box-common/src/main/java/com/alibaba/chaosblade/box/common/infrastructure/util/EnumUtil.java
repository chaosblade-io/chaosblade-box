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

import com.alibaba.chaosblade.box.common.common.enums.IEnum;

/** @author haibin */
public class EnumUtil {

  public static String nameOf(Enum<?> enums) {
    if (enums == null) {
      return null;
    }
    return enums.name();
  }

  public static <E extends Enum<?> & IEnum<Integer>> E integerValueOf(
      Class<E> enumClass, Integer value) {
    E[] es = enumClass.getEnumConstants();
    for (E e : es) {
      Integer evalue = e.getValue();
      if (evalue.equals(value)) {
        return e;
      }
    }
    return null;
  }
}
