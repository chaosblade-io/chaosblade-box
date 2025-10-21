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

package com.alibaba.chaosblade.box.common.infrastructure.exception;

import com.alibaba.chaosblade.box.common.common.domain.ChaosError;
import com.alibaba.chaosblade.box.common.common.domain.IErrorCode;
import lombok.Getter;

/** @author haibin */
@Getter
public class ChaosException extends RuntimeException {

  private IErrorCode errorCode;

  public ChaosException(IErrorCode errorCode, String message) {
    super(message);
    this.errorCode = errorCode;
  }

  public ChaosException(IErrorCode errorCode, Throwable throwable) {
    super(throwable);
    this.errorCode = errorCode;
  }

  public ChaosException(ChaosError chaosError) {
    super(chaosError.getErrorMessage());
    this.errorCode = chaosError.getErrorCode();
  }

  public ChaosException(IErrorCode errorCode) {
    this(errorCode, errorCode.getReadableMessage());
  }

  public ChaosException(IErrorCode errorCode, String message, Throwable cause) {
    super(message, cause);
    this.errorCode = errorCode;
  }
}
