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

package com.alibaba.chaosblade.box.common.sdk.transport;

/**
 * Add and check timestamp
 *
 * @author changjun.xcj
 */
public class TimestampInterceptor extends RequestInterceptorChain {

  /** max invalid request time (millis) */
  public static final int MAX_INVALID_TIME = 2 * 60 * 1000;

  public static final String TIMESTAMP_KEY = "ts";

  @Override
  public void doHandle(Request request) throws RequestException {
    String ts = request.getParam("ts");
    if (ts == null || ts.length() == 0) {
      throw new InvalidTimestampException("missing timestamp");
    }
    try {
      long clientTime = Long.valueOf(ts);
      long serverTime = System.currentTimeMillis();
      if (serverTime - clientTime > MAX_INVALID_TIME) {
        throw new InvalidTimestampException(
            "exceeding maximum failure time, server time: "
                + serverTime
                + ", "
                + "client time: "
                + clientTime);
      }
    } catch (NumberFormatException e) {
      throw new InvalidTimestampException(ts + ", " + e.getMessage());
    }
  }

  @Override
  public void doInvoke(Request request) throws RequestException {
    request.addParam(TIMESTAMP_KEY, System.currentTimeMillis() + "");
  }
}
