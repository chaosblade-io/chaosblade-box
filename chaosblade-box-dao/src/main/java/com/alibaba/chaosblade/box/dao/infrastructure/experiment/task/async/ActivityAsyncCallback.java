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

package com.alibaba.chaosblade.box.dao.infrastructure.experiment.task.async;

/** @author sunpeng */
public interface ActivityAsyncCallback {

  /**
   * 回调处理
   *
   * @param asyncCallBackContext
   */
  void execute(AsyncCallBackContext asyncCallBackContext);

  /**
   * 支持工具类型
   *
   * @param toolType
   * @return
   */
  boolean support(String toolType);

  /**
   * 根据appCode判断是否支持
   *
   * @param appCode
   * @return
   */
  boolean supportByAppCode(String appCode);

  /**
   * 演练过期处理
   *
   * @param asyncCallBackContext
   */
  void expired(AsyncCallBackContext asyncCallBackContext);
}
