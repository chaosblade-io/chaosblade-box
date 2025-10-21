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

package com.alibaba.chaosblade.box.common.commands;

import com.alibaba.chaosblade.box.common.commands.interceptor.CommandInterceptor;
import com.google.common.collect.ImmutableList;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import org.springframework.core.annotation.AnnotationAwareOrderComparator;

/** @author haibin */
public abstract class CommandExecutor implements CommandExecutable, SpringCommandExecutable {

  protected List<CommandInterceptor> commandInterceptors = new ArrayList<>();

  public void addCommandInterceptors(List<CommandInterceptor> newCommandInterceptors) {
    if (newCommandInterceptors == null || newCommandInterceptors.isEmpty()) {
      return;
    }
    for (CommandInterceptor commandInterceptor : newCommandInterceptors) {
      if (!commandInterceptors.contains(commandInterceptor)) {
        commandInterceptors.add(commandInterceptor);
      }
    }
    AnnotationAwareOrderComparator.sort(commandInterceptors);
  }

  protected void onStarted(Command<?> command) {
    for (CommandInterceptor commandInterceptor : commandInterceptors) {
      commandInterceptor.onStarted(command);
    }
  }

  protected void onReturn(InvocationCommand invocationCommand, Object result) {
    for (CommandInterceptor commandInterceptor : commandInterceptors) {
      commandInterceptor.onReturn(invocationCommand, result);
    }
  }

  protected void onError(InvocationCommand invocationCommand, Throwable throwable) {
    for (CommandInterceptor commandInterceptor : commandInterceptors) {
      commandInterceptor.onError(invocationCommand, throwable);
    }
  }

  public List<CommandInterceptor> getCommandInterceptors() {
    return ImmutableList.copyOf(commandInterceptors);
  }

  protected <Response> Response internalInvoke(
      Command<?> command, Callable<Response> responseCallable) {
    InvocationCommand invocationCommand = new InvocationCommand(command);
    onStarted(command);
    invocationCommand.start();
    try {
      Response response = responseCallable.call();
      onReturn(invocationCommand, response);
      return response;
    } catch (Throwable throwable) {
      onError(invocationCommand, throwable);
      throw new CommandRunTimeException(throwable);
    } finally {
      invocationCommand.finish();
    }
  }

  /** @return */
  public abstract String getCommandExecutorName();

  /**
   * 获取执行器信息
   *
   * @return
   */
  public abstract CommandExecutorInfo getCommandExecutorInfo();
}
