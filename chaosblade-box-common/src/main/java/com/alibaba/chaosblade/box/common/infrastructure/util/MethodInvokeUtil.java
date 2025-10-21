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

import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;
import org.apache.commons.lang3.time.StopWatch;
import org.slf4j.Logger;

/** @author haibin */
public class MethodInvokeUtil {

  public static void invokeWithStopWatch(
      ThrowExceptionRunnable runnable, Logger logger, String runnabeDesc) throws Exception {
    logger.info("Start {}", runnabeDesc);
    StopWatch stopWatch = StopWatch.createStarted();
    try {
      runnable.run();
    } finally {
      stopWatch.stop();
      logger.info(
          "End {},cost {} in milliseconds", runnabeDesc, stopWatch.getTime(TimeUnit.MILLISECONDS));
    }
  }

  public interface ThrowableHandler<T> {
    public T handle(Throwable throwable);
  }

  public static <T> T invoke(Callable<T> callable, ThrowableHandler<T> tThrowableHandler) {
    try {
      return callable.call();
    } catch (Exception exception) {
      return tThrowableHandler.handle(exception);
    }
  }
}
