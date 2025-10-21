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

package com.alibaba.chaosblade.box.service.model.expertise;

import lombok.Data;

/** @author haibin */
@Data
public class ExperimentExpertiseCreateRequest extends BaseExpertiseOperationRequest {

  /** 是否配置同步 */
  private boolean configSync = false;

  private String experimentId;

  /** 如果expertiseId为空，则会重新生成一个id,这个参数现在只用于configSync=true的情况下 */
  private String expertiseId;
}
