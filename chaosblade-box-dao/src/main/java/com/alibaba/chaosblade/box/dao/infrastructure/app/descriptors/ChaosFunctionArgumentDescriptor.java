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

package com.alibaba.chaosblade.box.dao.infrastructure.app.descriptors;

import com.alibaba.chaosblade.box.common.app.sdk.annotations.ChaosArgs;
import com.alibaba.chaosblade.box.common.app.sdk.constants.ChaosAppArgumentType;
import com.google.common.base.Strings;
import java.lang.reflect.Field;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

/** @author sunju */
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ChaosFunctionArgumentDescriptor {

  String name;
  String alias;
  String description;
  ChaosAppArgumentType type;
  transient Field reference;

  public static ChaosFunctionArgumentDescriptor of(ChaosArgs chaosArgs, ChaosAppArgumentType type) {
    ChaosFunctionArgumentDescriptor descriptor = new ChaosFunctionArgumentDescriptor();
    descriptor.setAlias(chaosArgs.alias());
    descriptor.setName(
        Strings.isNullOrEmpty(chaosArgs.name()) ? chaosArgs.alias() : chaosArgs.name());
    descriptor.setDescription(chaosArgs.description());
    descriptor.setType(type);
    return descriptor;
  }

  public static ChaosFunctionArgumentDescriptor of(
      Field field, ChaosArgs chaosArgs, ChaosAppArgumentType type) {
    ChaosFunctionArgumentDescriptor descriptor =
        ChaosFunctionArgumentDescriptor.of(chaosArgs, type);
    descriptor.setReference(field);
    if (Strings.isNullOrEmpty(descriptor.getAlias())) {
      descriptor.setAlias(field.getName());
    }
    if (Strings.isNullOrEmpty(descriptor.getName())) {
      descriptor.setName(field.getName());
    }
    return descriptor;
  }
}
