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
import com.alibaba.chaosblade.box.common.common.enums.CommonErrorCode;
import com.mysql.jdbc.MysqlDataTruncation;
import com.mysql.jdbc.exceptions.MySQLSyntaxErrorException;
import org.springframework.core.annotation.Order;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Component;

/** @author haibin */
@Component
@Order(value = 2000)
public class DataBaseThrowableChaosErrorWrapper implements ThrowableChaosErrorWrapper {

  @Override
  public ChaosError wrapper(Throwable throwable) {
    if (throwable instanceof DataIntegrityViolationException) {
      DataIntegrityViolationException dataIntegrityViolationException =
          (DataIntegrityViolationException) throwable;
      Throwable cause = dataIntegrityViolationException.getCause();
      if (cause == null) {
        return new ChaosError(CommonErrorCode.S_DATA_VIOLATION);
      }
      if (cause.getClass().getName().equals(MysqlDataTruncation.class.getName())) {
        return new ChaosError(CommonErrorCode.S_DATA_TRUNCATION, cause.getMessage());
      }
    }
    if (throwable instanceof MySQLSyntaxErrorException) {
      return new ChaosError(CommonErrorCode.S_DATA_FIELD_MISSING);
    }
    return null;
  }
}
