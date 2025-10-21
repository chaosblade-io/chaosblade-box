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

package com.alibaba.chaosblade.box.common.app.sdk;

import com.alibaba.chaosblade.box.common.app.sdk.constants.EnvironmentEnum;
import com.alibaba.chaosblade.box.common.app.sdk.constants.PhaseType;
import com.alibaba.chaosblade.box.common.app.sdk.scope.ActivityScope;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import lombok.Data;
import lombok.ToString;

/** @author sunju */
@ToString
@Data
public final class ChaosAppContext {

  /** Running environment */
  private EnvironmentEnum environment;

  /** Phase which current node belongs. Such as PREPARE,ATTACK,CHECK,RECOVER */
  private PhaseType phase;

  /** current activity scope info ,if activity has no scopes,the value is null */
  private ActivityScope activityScope;

  /** userId */
  private String userId;

  private String subUserId;

  private String stsToken;

  /** currentNamespace */
  private String namespace;

  /**
   * Context data, could be read by all nodes in flow.
   *
   * <p>Your app MUST extends {@link BaseChaosApp} or implements {@link ChaosAppContextAware}
   * interface.
   *
   * <p>Example:
   *
   * <p>In ChaosApp <code>
   * Object someValue = getContext().getData("some key"); // get data from context
   * getContext().setData("some key", someValue); // set data to context
   * </code> <br>
   *
   * <p>Expression
   *
   * <p>Expression MUST surround with #{}. $context is inner variable for access context object.
   * <code>
   * // get phase
   * #{$context.phase}
   *
   * // get data value
   * getContext().addData("name", "Jack Ma");
   * #{$context.data.name}
   * </code>
   */
  private Map<String, Object> data = new ConcurrentHashMap<>();

  public void addData(String key, Object value) {
    if (null == this.data) {
      this.data = new ConcurrentHashMap<>();
    }

    if (null != key && !key.isEmpty() && null != value) {
      this.data.put(key, value);
    }
  }

  public Object getData(String key) {
    if (null == this.data) {
      return null;
    }

    if (null != key && !key.isEmpty()) {
      return this.data.get(key);
    }

    return null;
  }

  public void setData(Map<String, Object> data) {
    if (null == data) {
      this.data = new ConcurrentHashMap<>();
    } else {
      this.data = data;
    }
  }

  public ChaosAppContext() {}
}
