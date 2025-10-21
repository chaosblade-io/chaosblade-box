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

package com.alibaba.chaosblade.box.common.sdk.channel;

import com.alibaba.chaosblade.box.common.common.domain.response.Response;
import com.alibaba.chaosblade.box.common.sdk.constant.Header;
import com.alibaba.chaosblade.box.common.sdk.transport.Request;

/** @author Changjun Xiao */
public class PaasTransportService implements TransportService {

  /** 单位是毫秒 */
  private int timeout;

  public PaasTransportService(int timeout) {
    this.timeout = timeout;
  }

  @Override
  public <R> Response<R> invoke(Request request, Class<?> clazz) {
    String host = request.getHeader(Header.HOST);
    String port = request.getHeader(Header.PORT);

    PaasTransportChannel transportChannel = new PaasTransportChannel(host, port);
    return transportChannel.invoke(request, clazz, timeout);
  }
}
