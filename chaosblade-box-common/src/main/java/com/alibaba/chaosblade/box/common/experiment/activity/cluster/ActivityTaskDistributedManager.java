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

package com.alibaba.chaosblade.box.common.experiment.activity.cluster;

import com.alibaba.chaosblade.box.common.infrastructure.util.FastJsonDecoder;
import com.google.common.base.Strings;
import feign.Feign;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/** @author haibin */
@Component
@Slf4j
public class ActivityTaskDistributedManager {

  @Value("${server.port}")
  private int serverPort;

  public void sendImmediatelyShutdownSignal(String targetHost, String experimentTaskId) {
    try {
      if (Strings.isNullOrEmpty(targetHost)) {
        return;
      }
      buildTransport(targetHost).shutdownTask(experimentTaskId);
    } catch (Throwable throwable) {
      log.error("activity remote request failed", throwable);
    }
  }

  public ExecutionJobTransport buildTransport(String host) {
    String url = "http://" + host + ":" + serverPort;
    return Feign.builder()
        .decoder(new FastJsonDecoder())
        .decoder(new FastJsonDecoder())
        .target(ExecutionJobTransport.class, url);
  }

  public boolean isRunning(String targetHost, String activityTaskId) {
    try {
      if (Strings.isNullOrEmpty(targetHost)) {
        return false;
      }
      return buildTransport(targetHost).isActivityTaskRunning(activityTaskId).isSuccess();
    } catch (Throwable throwable) {
      log.error("activity remote request failed", throwable);
      return false;
    }
  }
}
