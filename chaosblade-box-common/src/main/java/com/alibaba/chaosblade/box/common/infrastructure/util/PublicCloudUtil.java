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

package com.alibaba.chaosblade.box.common.infrastructure.util;

/** @author haibin */
public class PublicCloudUtil {

  public static boolean isK8SByScope(String scope) {
    return "node".equalsIgnoreCase(scope)
        || "pod".equalsIgnoreCase(scope)
        || "container".equalsIgnoreCase(scope);
  }

  public static boolean isK8SByAppCode(String appCode) {
    String target = appCode.split("\\.")[1];
    return isK8SByScope(getScopeFromTarget(target));
  }

  public static String getScopeFromTarget(String target) {
    String[] splits = target.split("-");
    if (splits.length == 2) {
      return splits[0];
    }
    return null;
  }

  public static boolean isK8SUserMiniApp(String appCode) {
    return appCode.startsWith("chaosapp.k8s");
  }
}
