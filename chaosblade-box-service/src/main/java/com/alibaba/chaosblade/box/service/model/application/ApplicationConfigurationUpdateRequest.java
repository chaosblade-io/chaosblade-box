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

package com.alibaba.chaosblade.box.service.model.application;

import com.alibaba.chaosblade.box.common.common.domain.BaseRequest;
import com.alibaba.chaosblade.box.dao.infrastructure.configuration.ConfigurationScope;
import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

/** @author haibin.lhb */
@Data
public class ApplicationConfigurationUpdateRequest extends BaseRequest {

  private String value;

  @JSONField(name = "app_id")
  private String appId;

  private String alias;

  /** 选择范围 */
  private ConfigurationScope scope;

  /** 是否覆盖用户的参数值 */
  private Boolean override;

  private String name;
}
