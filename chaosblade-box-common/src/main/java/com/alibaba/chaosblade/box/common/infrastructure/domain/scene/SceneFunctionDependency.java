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

import com.alibaba.chaosblade.box.common.app.sdk.constants.PhaseType;
import java.util.Objects;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

/** @author sunju */
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@NoArgsConstructor
public class SceneFunctionDependency {

  /** 前置依赖 */
  public static final Integer TYPE_BEFORE = 0;

  /** 后置依赖 */
  public static final Integer TYPE_AFTER = 1;

  Integer type;

  String code;
  Integer phase;

  public boolean isBefore() {
    return Objects.equals(TYPE_BEFORE, type);
  }

  public boolean isAfter() {
    return Objects.equals(TYPE_AFTER, type);
  }

  boolean required = true;

  public SceneFunctionDependency(Integer type, String code, Integer phase) {
    this.type = type;
    this.code = code;
    this.phase = phase;
  }

  public static SceneFunctionDependency before(String code, PhaseType phaseType) {
    return new SceneFunctionDependency(TYPE_BEFORE, code, phaseType.getType());
  }

  public static SceneFunctionDependency after(String code, PhaseType phaseType) {
    return new SceneFunctionDependency(TYPE_AFTER, code, phaseType.getType());
  }
}
