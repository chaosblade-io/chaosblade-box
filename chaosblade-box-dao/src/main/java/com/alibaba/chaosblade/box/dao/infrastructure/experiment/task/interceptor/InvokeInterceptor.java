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

package com.alibaba.chaosblade.box.dao.infrastructure.experiment.task.interceptor;

import com.alibaba.chaosblade.box.common.experiment.task.flow.step.OnceInvoke;
import com.alibaba.chaosblade.box.dao.infrastructure.experiment.task.flow.step.InvokeContext;

/** @author haibin */
public interface InvokeInterceptor<Re extends InvokeContext, Rs> {

  public static Integer MIN_ORDER = Integer.MAX_VALUE;

  public static Integer MAX_ORDER = Integer.MIN_VALUE;

  /**
   * 在进行真实调用之前的预处理
   *
   * @param re 请求参数
   */
  public boolean preHandle(Re re, Rs rs);

  /**
   * 执行真实调用
   *
   * @param re 调用结果
   * @return 调用结果
   */
  public void postHandle(OnceInvoke<Re, Rs> onceInvoke, Re re, Rs rs);

  /**
   * 调用之后的处理，包括异常
   *
   * @param re 调用参数
   * @param rs 调用结果
   * @param throwable 可能为空
   */
  public void afterHandle(Re re, Rs rs, Throwable throwable);

  /**
   * 获取调用顺序，数字越大，越先调用
   *
   * @return
   */
  public Integer getOrder();
}
