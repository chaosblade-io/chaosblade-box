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

package com.alibaba.chaosblade.box.common.app.sdk;

import com.alibaba.chaosblade.box.common.app.sdk.scope.Scope;
import com.alibaba.chaosblade.box.common.common.domain.IErrorCode;
import java.util.HashMap;
import java.util.Map;
import lombok.Data;

/**
 * chaos app小程序返回值
 *
 * @author sunju
 */
@Data
public class ChaosAppResponse {

  boolean success = false;

  /** 小程序返回值，页面以Pretty JSON String形式展示 */
  Map<String, Object> data;

  String errorMessage;

  String errorCode;

  Scope scope;

  String originErrorMessage;

  public void withErrorCode(IErrorCode iErrorCode) {
    this.success = false;
    this.errorCode = iErrorCode.name();
    this.errorMessage = iErrorCode.getReadableMessage();
  }

  public ChaosAppResponse(
      boolean success, Map<String, Object> data, String errorMessage, Scope scope) {
    this.success = success;
    this.data = data;
    this.errorMessage = errorMessage;
    this.scope = scope;
  }

  public ChaosAppResponse(boolean success, Map<String, Object> data, Scope scope) {
    this.success = success;
    this.data = data;
    this.scope = scope;
  }

  public void addResponseData(String name, Object value) {
    if (null == data) {
      data = new HashMap<>(1);
    }
    data.put(name, value);
  }

  public ChaosAppResponse() {}

  public ChaosAppResponse(boolean success) {
    this.success = success;
  }
}
