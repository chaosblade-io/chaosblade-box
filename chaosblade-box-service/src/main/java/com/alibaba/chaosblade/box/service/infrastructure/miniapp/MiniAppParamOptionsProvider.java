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

package com.alibaba.chaosblade.box.service.infrastructure.miniapp;

import com.alibaba.chaosblade.box.common.app.sdk.scope.Host;
import com.alibaba.chaosblade.box.common.common.annotation.ExtensionPoint;
import com.alibaba.chaosblade.box.service.model.param.ParamOptionsQueryRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

/** @author haibin.lhb */
@ExtensionPoint
public interface MiniAppParamOptionsProvider {

  default <T> List<T> loopInHosts(List<Host> hosts, Function<Host, List<T>> function) {
    List<T> result = new ArrayList<>();
    for (Host host : hosts) {
      result = function.apply(host);
      if (!result.isEmpty()) {
        break;
      }
    }
    return result;
  }

  /**
   * 提供参数下拉
   *
   * @param paramOptionsQueryRequest
   * @return
   */
  public List<MiniAppParamOption> provide(ParamOptionsQueryRequest paramOptionsQueryRequest);

  /**
   * 是否支持当前provider
   *
   * @param paramOptionsQueryRequest
   * @return
   */
  public boolean isSupported(ParamOptionsQueryRequest paramOptionsQueryRequest);
}
