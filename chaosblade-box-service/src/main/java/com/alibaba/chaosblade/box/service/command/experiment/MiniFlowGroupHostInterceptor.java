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

package com.alibaba.chaosblade.box.service.command.experiment;

import com.alibaba.chaosblade.box.common.app.sdk.scope.Host;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/** @author sunpeng */
public interface MiniFlowGroupHostInterceptor {

  /**
   * 校验host是否存在
   *
   * @param host
   */
  default void check(Host host) {}

  /**
   * 批量校验
   *
   * @param list
   */
  default List<String> batchCheck(List<String> list, Set<String> appId, Set<String> groupName) {
    return new ArrayList<>();
  }
}
