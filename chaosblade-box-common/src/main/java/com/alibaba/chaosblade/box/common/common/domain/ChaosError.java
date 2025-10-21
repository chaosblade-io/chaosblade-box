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

package com.alibaba.chaosblade.box.common.common.domain;

import lombok.Getter;
import lombok.Setter;

/** @author haibin */
@Getter
@Setter
public class ChaosError {

  private IErrorCode errorCode;

  private String code;

  private String errorMessage;

  private Integer codeStatus;

  public ChaosError(IErrorCode code, String errorMessage) {
    this.errorCode = code;
    this.code = code.name();
    this.errorMessage = errorMessage;
    this.codeStatus = code.status();
  }

  public ChaosError(IErrorCode code) {
    this(code, code.getReadableMessage());
  }

  public static ChaosError withCode(IErrorCode errorCode) {
    return new ChaosError(errorCode);
  }

  public static ChaosError withCode(IErrorCode errorCode, String errorMessage) {
    return new ChaosError(errorCode, errorMessage);
  }

  @Override
  public String toString() {
    final StringBuffer sb = new StringBuffer("ChaosError{");
    sb.append("code=").append(code);
    sb.append(", message='").append(errorMessage).append('\'');
    sb.append('}');
    return sb.toString();
  }
}
