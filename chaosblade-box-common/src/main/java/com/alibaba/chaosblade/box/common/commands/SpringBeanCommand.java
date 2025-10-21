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

import com.alibaba.chaosblade.box.common.infrastructure.error.ThrowableChaosErrorWrappers;
import org.springframework.beans.factory.annotation.Autowired;

/** @author haibin */
public abstract class SpringBeanCommand<Request, Response> implements Command<Response> {

  @Autowired protected ThrowableChaosErrorWrappers throwableChaosErrorWrappers;

  @Autowired protected CommandBus commandBus;

  /**
   * 单例的话执行要带参数
   *
   * @param request
   * @return
   */
  public abstract Response execute(Request request);

  @Override
  public Response execute() {
    throw new UnsupportedOperationException("Default not support execute()");
  }

  @Override
  public String getCommandExecutorName() {
    return CommandBus.SPRING_BEAN_POOL_NAME;
  }
}
