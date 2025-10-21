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

package com.alibaba.chaosblade.box.common.infrastructure;

import com.alibaba.chaosblade.box.common.common.domain.user.ChaosUser;
import java.util.Optional;
import java.util.function.Function;
import org.springframework.core.NamedThreadLocal;

/** @author haibin */
public class ChaosRequestContextHolder {

  private static final ThreadLocal<ChaosApplicationContext> applicationContextThreadLocal =
      new NamedThreadLocal<ChaosApplicationContext>("chaos-application-context");

  public static void resetApplicationContextContext() {
    applicationContextThreadLocal.remove();
  }

  public static void setApplicationContext(ChaosApplicationContext chaosApplicationContext) {
    if (chaosApplicationContext == null) {
      resetApplicationContextContext();
    }
    applicationContextThreadLocal.set(chaosApplicationContext);
  }

  public static Optional<ChaosApplicationContext> getApplicationContext() {
    return Optional.ofNullable(applicationContextThreadLocal.get());
  }

  public static Optional<ChaosUser> getLoginUser() {
    return getApplicationContext()
        .map(
            new Function<ChaosApplicationContext, ChaosUser>() {
              @Override
              public ChaosUser apply(ChaosApplicationContext chaosApplicationContext) {
                return chaosApplicationContext.getLoginUser();
              }
            });
  }
}
