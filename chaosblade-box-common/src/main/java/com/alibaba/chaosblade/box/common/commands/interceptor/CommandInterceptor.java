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

package com.alibaba.chaosblade.box.common.commands.interceptor;

import com.alibaba.chaosblade.box.common.commands.Command;
import com.alibaba.chaosblade.box.common.commands.InvocationCommand;

/** @author haibin.lhb */
public interface CommandInterceptor {

  /**
   * 执行前
   *
   * @param command
   */
  public void onStarted(Command<?> command);

  /**
   * 执行之后，处理返回值
   *
   * @param command
   * @param result
   */
  public void onReturn(InvocationCommand command, Object result);

  /**
   * 执行出错
   *
   * @param command
   * @param throwable 执行异常
   * @return transfer error
   */
  public void onError(InvocationCommand command, Throwable throwable);
}
