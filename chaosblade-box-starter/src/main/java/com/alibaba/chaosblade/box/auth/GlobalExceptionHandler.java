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

package com.alibaba.chaosblade.box.auth;

import com.alibaba.chaosblade.box.common.common.domain.ChaosError;
import com.alibaba.chaosblade.box.common.common.enums.CommonErrorCode;
import com.alibaba.chaosblade.box.common.infrastructure.error.ThrowableChaosErrorWrappers;
import com.alibaba.chaosblade.box.common.infrastructure.exception.ChaosException;
import com.alibaba.chaosblade.box.common.infrastructure.exception.PermissionDeniedException;
import com.alibaba.chaosblade.box.common.infrastructure.util.ChaosTraceUtil;
import com.alibaba.chaosblade.box.service.model.RestResponse;
import javax.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

/** @author haibin */
@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {

  @Autowired private ThrowableChaosErrorWrappers throwableChaosErrorWrappers;

  @ExceptionHandler(PermissionDeniedException.class)
  public ResponseEntity<RestResponse> handlePermissionDeniedException(
      PermissionDeniedException ex, HttpServletRequest request) {
    // 使用 WARN 级别日志，不打印堆栈
    // 使用异常中的错误码作为默认值，如果没有则使用 P_LOGIN_MISSED
    ChaosError chaosError =
        throwableChaosErrorWrappers.wrapper(
            ex, ex.getErrorCode() != null ? ex.getErrorCode() : CommonErrorCode.P_LOGIN_MISSED);
    log.warn(
        "Permission denied for request {}: {}",
        request.getRequestURI(),
        chaosError.getErrorMessage());

    RestResponse response = new RestResponse();
    response.setSuccess(false);
    response.setCode(chaosError.getCode());
    response.setMessage(chaosError.getErrorMessage());
    response.setStatusCode(chaosError.getCodeStatus());
    response.setRequestId(ChaosTraceUtil.generateTraceId());

    // 返回 200 状态码，避免前端显示 500 错误
    return ResponseEntity.status(HttpStatus.OK).body(response);
  }

  @ExceptionHandler(ChaosException.class)
  public ResponseEntity<RestResponse> handleChaosException(
      ChaosException ex, HttpServletRequest request) {
    // 使用异常中的错误码
    ChaosError chaosError =
        throwableChaosErrorWrappers.wrapper(
            ex, ex.getErrorCode() != null ? ex.getErrorCode() : CommonErrorCode.S_SYSTEM_ERROR);

    // 对于登录相关的错误，使用 WARN 级别日志，不打印堆栈
    // 对于其他错误，根据错误码决定日志级别
    boolean isLoginError =
        chaosError.getErrorCode().equals(CommonErrorCode.P_LOGIN_FORBINATION)
            || chaosError.getErrorCode().equals(CommonErrorCode.P_STS_TOKEN_LOGIN_ILLEGAL);

    if (isLoginError) {
      log.warn(
          "Login failed for request {}: {}", request.getRequestURI(), chaosError.getErrorMessage());
    } else {
      log.error(
          "ChaosException for request {}: {}",
          request.getRequestURI(),
          chaosError.getErrorMessage(),
          ex);
    }

    RestResponse response = new RestResponse();
    response.setSuccess(false);
    response.setCode(chaosError.getCode());
    response.setMessage(chaosError.getErrorMessage());
    response.setStatusCode(chaosError.getCodeStatus());
    response.setRequestId(ChaosTraceUtil.generateTraceId());

    // 返回 200 状态码，避免前端显示 500 错误
    // 登录错误信息已在响应体中明确返回，前端可以根据 code 和 message 进行判断
    return ResponseEntity.status(HttpStatus.OK).body(response);
  }
}
