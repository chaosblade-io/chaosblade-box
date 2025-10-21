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

import java.util.concurrent.Future;

/** @author haibin */
public interface SpringCommandExecutable {

  /**
   * 异步运行
   *
   * @param beanCommandClass
   * @param request
   * @return
   */
  public <Request, Response> Future<Response> asyncRun(
      Class<? extends SpringBeanCommand<Request, Response>> beanCommandClass, Request request);

  /**
   * 同步运行
   *
   * @param beanCommandClass
   * @param request
   * @return
   */
  public <Request, Response> Response syncRun(
      Class<? extends SpringBeanCommand<Request, Response>> beanCommandClass, Request request);
}
