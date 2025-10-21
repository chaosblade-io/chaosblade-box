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

package com.alibaba.chaosblade.box.dao.infrastructure.app;

import com.alibaba.chaosblade.box.common.common.domain.IErrorCode;

/**
 * 仅限于小程序调用错误码
 *
 * @author haibin.lhb
 */
public enum MiniAppInvokeStatusCode implements IErrorCode {
  SC_OK("执行成功", 200),
  SC_BAD_REQUEST("异常请求", 400),
  SC_UNAUTHORIZED("鉴权失败", 401),
  SC_AGENT_ERROR("探针异常", 402),
  SC_TARGET_ENV_UNSUPPORTED("目标环境不支持", 403),
  SC_CONNECT_ERROR("通信错误", 405),
  SC_FAILED("执行失败", 500);

  private String readableMessage;

  private Integer status;

  public static MiniAppInvokeStatusCode ofErrorStatus(Integer status) {
    if (status != null) {
      for (MiniAppInvokeStatusCode miniAppInvokeStatusCode : MiniAppInvokeStatusCode.values()) {
        if (miniAppInvokeStatusCode.status().equals(status)) {
          return miniAppInvokeStatusCode;
        }
      }
    }
    return MiniAppInvokeStatusCode.SC_FAILED;
  }

  MiniAppInvokeStatusCode(String readableMessage, int status) {
    this.readableMessage = readableMessage;
    this.status = status;
  }

  @Override
  public Integer status() {
    return this.status;
  }

  @Override
  public String getReadableMessage() {
    return this.readableMessage;
  }

  @Override
  public boolean logWhenThrowable() {
    return false;
  }
}
