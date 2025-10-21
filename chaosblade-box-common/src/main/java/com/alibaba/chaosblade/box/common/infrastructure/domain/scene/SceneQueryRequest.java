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

package com.alibaba.chaosblade.box.common.infrastructure.domain.scene;

import com.alibaba.chaosblade.box.common.common.domain.BaseRequest;
import com.alibaba.chaosblade.box.common.common.domain.user.ChaosUser;
import java.util.List;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

/** @author haibin */
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class SceneQueryRequest extends BaseRequest {

  ChaosUser user;
  String sceneId;
  String functionId;
  String categoryId;
  String parameterId;
  List<String> codes;
  Integer permission;
  Integer phase;
  Integer source;
  Integer enabled = 2;
  Integer supportScopeType;
  Boolean isDelete = false;
  Boolean isPublic;
  String searchKey;
  Integer osType;

  private Integer k8sResourceType;

  private List<String> functionIds;
}
