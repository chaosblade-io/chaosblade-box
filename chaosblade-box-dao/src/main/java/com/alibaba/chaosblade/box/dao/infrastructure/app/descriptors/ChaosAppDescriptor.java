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

import com.alibaba.chaosblade.box.common.app.sdk.ChaosApp;
import com.alibaba.chaosblade.box.common.app.sdk.annotations.ChaosApplication;
import com.alibaba.chaosblade.box.common.app.sdk.constants.ChaosAppType;
import com.alibaba.chaosblade.box.dao.infrastructure.experiment.task.flow.util.ChaosAppUtil;
import com.google.common.base.Strings;
import com.google.common.hash.HashCode;
import java.util.List;
import lombok.*;
import lombok.experimental.FieldDefaults;

/** @author sunju */
@Getter
@Setter
@ToString
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ChaosAppDescriptor {

  String empId;
  String code;
  String namespace;
  String name;
  String description;
  String version;
  ChaosAppType type;
  Boolean forPublic;
  transient ChaosApp reference;
  List<BaseChaosMethodDescriptor> methodDescriptors;

  public static ChaosAppDescriptor of(ChaosApp app, ChaosApplication config) {
    ChaosAppDescriptor descriptor = new ChaosAppDescriptor();
    descriptor.setCode(config.code());
    descriptor.setName(config.name());
    descriptor.setNamespace(ChaosAppUtil.buildAppNamespace(config.code()));
    descriptor.setVersion(config.version());
    descriptor.setDescription(config.description());
    descriptor.setType(config.type());
    descriptor.setReference(app);
    return descriptor;
  }

  public ChaosFunctionDescriptor getFunctionDescriptor(String namespace) {
    BaseChaosMethodDescriptor methodDescriptor = getMethodDescriptor(namespace);
    if (methodDescriptor instanceof ChaosFunctionDescriptor) {
      return (ChaosFunctionDescriptor) methodDescriptor;
    }
    return null;
  }

  public BaseChaosMethodDescriptor getMethodDescriptor(String namespace) {
    if (Strings.isNullOrEmpty(namespace)) {
      return null;
    }

    if (null != methodDescriptors) {
      return methodDescriptors.stream()
          .filter(descriptor -> descriptor.getNamespace().equals(namespace))
          .findFirst()
          .get();
    }

    return null;
  }

  @Override
  public boolean equals(Object obj) {
    if (obj instanceof ChaosAppDescriptor) {
      ChaosAppDescriptor instance = (ChaosAppDescriptor) obj;
      return instance.getCode().equals(this.getCode());
    }
    return false;
  }

  @Override
  public int hashCode() {
    return HashCode.fromString(this.getNamespace()).bits();
  }
}
