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

package com.alibaba.chaosblade.box.common.infrastructure.excel;

import java.io.Serializable;
import lombok.Data;

/** @author haibin */
@Data
public class SceneFunctionParamsExcelRow implements Serializable {

  private String code;

  private String description;
  private String codeDesc;

  private String name;

  private String alias;

  private boolean required = false;

  private String defaultValue;

  private boolean isNumber = false;
}
