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

package com.alibaba.chaosblade.box.common.infrastructure.error;

import com.alibaba.chaosblade.box.common.common.domain.ChaosError;
import com.alibaba.chaosblade.box.common.common.domain.IErrorCode;
import com.alibaba.chaosblade.box.common.common.enums.CommonErrorCode;
import com.alibaba.chaosblade.box.common.experiment.task.flow.exception.ExceptionHelper;
import com.alibaba.chaosblade.box.common.infrastructure.exception.ChaosException;
import com.alibaba.chaosblade.box.common.infrastructure.exception.ScriptException;
import org.springframework.core.annotation.Order;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.jdbc.UncategorizedSQLException;
import org.springframework.stereotype.Component;

/** @author haibin */
@Component
@Order
public class DefaultThrowableChaosErrorWrapper implements ThrowableChaosErrorWrapper {

  private static String DB_DOWN_ERROR =
      "It seems a very real possibility that this DB is down. Please contact DBA to check.";

  @Override
  public ChaosError wrapper(Throwable throwable) {
    IErrorCode iErrorCode = null;
    String message = ExceptionHelper.detailedMessage(throwable);
    if (throwable instanceof ChaosException) {
      ChaosException ChaosException = (ChaosException) throwable;
      iErrorCode = ChaosException.getErrorCode();
      if (ChaosException.getMessage() == null) {
        message = ChaosException.getErrorCode().getReadableMessage();
      } else {
        message = ChaosException.getMessage();
      }
    } else if (throwable instanceof IllegalArgumentException) {
      iErrorCode = CommonErrorCode.P_ARGUMENT_ILLEGAL;
    } else if (throwable instanceof ScriptException) {
      iErrorCode = CommonErrorCode.B_ERROR_COMPILE_SCRIPT;
    } else if (throwable instanceof NullPointerException) {
      iErrorCode = CommonErrorCode.S_SYSTEM_ERROR;
      message = CommonErrorCode.S_SYSTEM_ERROR.getReadableMessage();
    } else if (throwable instanceof UncategorizedSQLException) {
      iErrorCode = CommonErrorCode.S_DB_ERROR;
      if (throwable.getMessage().contains(DB_DOWN_ERROR)) {
        iErrorCode = CommonErrorCode.S_DB_DOWN_ERROR;
        message = CommonErrorCode.S_DB_DOWN_ERROR.getReadableMessage();
      }
    } else if (throwable instanceof DataIntegrityViolationException) {
      iErrorCode = CommonErrorCode.S_DATA_VIOLATION;
      message = CommonErrorCode.S_DATA_VIOLATION.getReadableMessage();
    } else {
      iErrorCode = CommonErrorCode.S_SYSTEM_ERROR;
      message = CommonErrorCode.S_SYSTEM_ERROR.getReadableMessage();
    }
    return new ChaosError(iErrorCode, message);
  }
}
