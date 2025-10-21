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

package com.alibaba.chaosblade.box.dao.model;

import com.alibaba.chaosblade.box.dao.infrastructure.app.descriptors.BaseChaosMethodDescriptor;
import com.alibaba.chaosblade.box.dao.infrastructure.app.descriptors.ChaosAppDescriptor;
import com.alibaba.chaosblade.box.dao.model.base.BaseDO;
import com.baomidou.mybatisplus.annotation.TableName;
import java.util.List;
import java.util.stream.Collectors;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

/** @author sunju */
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@TableName("t_chaos_scene")
public final class SceneDO extends BaseDO {

  Long id;
  Integer state;
  String sceneId;
  String code;
  String name;
  String description;
  String version;
  String userId;
  Boolean isDelete = false;
  Boolean isPublic = false;
  transient List<SceneFunctionDO> functions;

  public static SceneDO from(ChaosAppDescriptor descriptor) {
    SceneDO scene = new SceneDO();
    scene.setCode(descriptor.getNamespace());
    scene.setName(descriptor.getName());
    scene.setDescription(descriptor.getDescription());
    scene.setVersion(descriptor.getVersion());
    scene.setUserId(descriptor.getEmpId());
    scene.setIsPublic(descriptor.getForPublic());
    List<BaseChaosMethodDescriptor> methodDescriptors = descriptor.getMethodDescriptors();
    if (null != methodDescriptors) {
      scene.setFunctions(
          methodDescriptors.stream()
              .map(methodDescriptor -> SceneFunctionDO.from(descriptor, methodDescriptor))
              .collect(Collectors.toList()));
    }

    return scene;
  }
}
