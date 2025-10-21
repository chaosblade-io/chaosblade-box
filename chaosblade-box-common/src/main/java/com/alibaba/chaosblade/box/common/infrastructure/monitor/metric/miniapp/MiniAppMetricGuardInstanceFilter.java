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

package com.alibaba.chaosblade.box.common.infrastructure.monitor.metric.miniapp;

import com.alibaba.chaosblade.box.common.common.util.MiniAppUtils;
import com.google.common.base.Strings;
import org.springframework.stereotype.Component;

/**
 * 只有这些才支持全局变量
 *
 * @author haibin.lhb
 */
@Component
public class MiniAppMetricGuardInstanceFilter {

  /**
   * 支持
   *
   * @return
   */
  public boolean support(String appCode) {
    if (Strings.isNullOrEmpty(appCode)) {
      return false;
    }
    // K8S暂时不支持
    // if (MiniAppUtils.isK8S(appCode)) {
    //    return false;
    // }
    if (!MiniAppUtils.isJvmExcludeAgent(appCode)) {
      return false;
    }
    // 线程池满不支持
    if (appCode.endsWith("threadpoolfull")) {
      return false;
    }
    // oom不支持
    if (appCode.endsWith("OutOfMemoryError")) {
      return false;
    }
    // 连接池满不支持
    if (appCode.endsWith("connectionpoolfull")) {
      return false;
    }
    return true;
  }
}
