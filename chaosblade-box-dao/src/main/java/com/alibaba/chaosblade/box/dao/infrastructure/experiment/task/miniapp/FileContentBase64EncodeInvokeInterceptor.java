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

package com.alibaba.chaosblade.box.dao.infrastructure.experiment.task.miniapp;

import com.alibaba.chaosblade.box.common.app.sdk.ChaosAppRequest;
import com.alibaba.chaosblade.box.common.app.sdk.ChaosAppResponse;
import com.alibaba.chaosblade.box.common.common.util.MiniAppUtils;
import com.alibaba.chaosblade.box.common.experiment.task.flow.interceptor.InterceptorDesc;
import com.alibaba.chaosblade.box.dao.infrastructure.experiment.task.flow.step.MiniAppInvokeContext;
import com.alibaba.chaosblade.box.dao.infrastructure.experiment.task.interceptor.BaseMiniAppInvokeInterceptor;
import com.google.common.base.Strings;
import java.io.UnsupportedEncodingException;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Base64;
import org.springframework.stereotype.Component;

/** @author sunpeng */
@Slf4j
@Component
@InterceptorDesc("File content encode")
public class FileContentBase64EncodeInvokeInterceptor extends BaseMiniAppInvokeInterceptor {

  private static String FILE_CONTENT = "content";

  private static String ENABLE_BASE64 = "enable-base64";

  @Override
  protected boolean preHandle(
      MiniAppInvokeContext miniAppInvokeContext, ChaosAppResponse chaosAppResponse) {
    try {
      handleFileAppendBase64Format(miniAppInvokeContext);
    } catch (UnsupportedEncodingException e) {
      log.error("文件追加base64编码失败！", e);
      return false;
    }
    return true;
  }

  private void handleFileAppendBase64Format(MiniAppInvokeContext appInvokeContext)
      throws UnsupportedEncodingException {
    if (MiniAppUtils.isFileAppend(
            appInvokeContext.getActivityInvokeContext().getExecutableAppCode())
        && !MiniAppUtils.isChaosRecovery(
            appInvokeContext.getActivityInvokeContext().getExecutableAppCode())) {
      ChaosAppRequest chaosAppRequest = appInvokeContext.getChaosAppRequest();
      Map<String, String> action = chaosAppRequest.getAction();
      // 这里做一个兼容，如果已经指定了要使用base64编码，说明是旧数据，那么就不在编码防止二次编码
      String enableBase64 = action.get(ENABLE_BASE64);
      if (!Strings.isNullOrEmpty(enableBase64)) {
        if (Boolean.parseBoolean(enableBase64)) {
          return;
        }
      }
      String content = action.get(FILE_CONTENT);
      if (!Strings.isNullOrEmpty(content)) {
        action.put(FILE_CONTENT, base64Format(content));
        action.put(ENABLE_BASE64, "true");
      }
    }
  }

  private String base64Format(String str) throws UnsupportedEncodingException {
    Base64 base64 = new Base64();
    return base64.encodeToString(str.getBytes("UTF-8"));
  }
}
