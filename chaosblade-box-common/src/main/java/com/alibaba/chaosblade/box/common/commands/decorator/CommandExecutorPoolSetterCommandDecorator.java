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

package com.alibaba.chaosblade.box.common.commands.decorator;

import com.alibaba.chaosblade.box.common.commands.BasePoolCommand;
import com.alibaba.chaosblade.box.common.commands.Command;
import com.alibaba.chaosblade.box.common.commands.CommandBus;
import com.alibaba.chaosblade.box.common.commands.CommandDecorator;

/** @author haibin */
public class CommandExecutorPoolSetterCommandDecorator implements CommandDecorator {

  private CommandBus commandBus;

  public CommandExecutorPoolSetterCommandDecorator(CommandBus commandBus) {
    this.commandBus = commandBus;
  }

  @Override
  public <Response> Command<Response> decorate(Command<Response> sourceCommand) {
    if (sourceCommand instanceof BasePoolCommand) {
      ((BasePoolCommand) sourceCommand).setCommandExecutorPool(commandBus);
    }
    return sourceCommand;
  }
}
