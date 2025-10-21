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

package com.alibaba.chaosblade.box.model;

import com.alibaba.chaosblade.box.common.common.domain.ChaosError;
import com.alibaba.chaosblade.box.common.common.domain.Response;
import com.alibaba.chaosblade.box.service.model.RestResponse;

/** @author haibin */
public class RestResponseUtil {

  public static RestResponse<Void> ok() {
    RestResponse<Void> r = new RestResponse<>();
    r.setSuccess(true);
    return r;
  }

  public static <T> RestResponse<T> okWithData(T t) {
    RestResponse<T> r = new RestResponse<>();
    r.setResult(t);
    r.setSuccess(true);
    r.setCode("200");
    return r;
  }

  public static <T> RestResponse<T> failed(ChaosError chaosError) {
    RestResponse<T> r = new RestResponse<>();
    r.setSuccess(false);
    if (chaosError != null) {
      r.setCode(chaosError.getCode());
      r.setMessage(chaosError.getErrorMessage());
      r.setStatusCode(chaosError.getCodeStatus());
    }
    return r;
  }

  public static <T> RestResponse<T> initWithServiceResponse(Response<T> response) {
    RestResponse<T> r = new RestResponse<>();
    if (null == response) {
      return r;
    }
    r.setResult(response.getResult());
    r.setSuccess(response.isSuccess());
    if (response.getError() != null) {
      return failed(response.getError());
    }
    return r;
  }
}
