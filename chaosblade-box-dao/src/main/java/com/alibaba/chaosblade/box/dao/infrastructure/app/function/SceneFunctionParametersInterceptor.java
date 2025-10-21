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

package com.alibaba.chaosblade.box.dao.infrastructure.app.function;

import com.alibaba.chaosblade.box.dao.model.SceneFunctionDO;
import com.alibaba.chaosblade.box.dao.model.SceneFunctionParameterDO;
import java.util.List;

/**
 * 小程序参数过滤
 *
 * @author haibin
 */
public interface SceneFunctionParametersInterceptor {

  /**
   * 小程序参数过滤
   *
   * @param sceneFunctionDO 小程序函数
   * @param parameters 小程序参数
   */
  public void afterQuery(
      SceneFunctionDO sceneFunctionDO, List<SceneFunctionParameterDO> parameters);

  /**
   * 启动时候会去同步小程序,在同步之前做拦截
   *
   * @param functionWrapper
   */
  public void beforeSync(SceneSynchronousHelper.FunctionWrapper functionWrapper);
}
