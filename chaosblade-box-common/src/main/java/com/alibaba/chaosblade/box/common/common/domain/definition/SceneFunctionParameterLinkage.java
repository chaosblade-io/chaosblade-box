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

package com.alibaba.chaosblade.box.common.common.domain.definition;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

/**
 * Author: sunju
 *
 * <p>Date: 2019/1/21
 */
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public final class SceneFunctionParameterLinkage {

  /** TRUE: display; FALSE: hidden */
  boolean defaultState;

  /** parameter id which depends */
  String depends;

  /**
   * condition of display, condition expression should be evaluated.
   *
   * <p>Component will change to display if condition return TRUE, otherwise will change to hide.
   *
   * <p>e.g
   *
   * <p>For input type component <code>
   *     var parameterAlias = 'myInputParam';
   *
   *     var context = {myInputParam: 'name'};
   *     var condition = 'myInputParam !== null';
   *
   *     var newState = eval(condition, context);
   *     if (newState) {
   *         // display this component
   *     } else {
   *         // hide this component
   *     }
   * </code> </br>
   *
   * <p>For radio or select type component <code>
   *     var parameterAlias = 'mySelectParam';
   *     var selectedOption = {key: 'myKey', value: 'enable'};
   *
   *     var context = {mySelectParam: 'myKey'};
   *     var condition = 'mySelectParam === "myKey"';
   *
   *     var newState = eval(condition, context);
   *     if (newState) {
   *         // display this component
   *     } else {
   *         // hide this component
   *     }
   * </code>
   */
  String condition;
}
