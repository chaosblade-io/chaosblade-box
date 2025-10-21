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

package com.alibaba.chaosblade.box.dao.infrastructure.application;

import java.util.List;
import java.util.Map;

/**
 * 参数过滤，如果演练时应用维度，很多的小程序参数是不需要给用户展示的,所以需要做一层过滤
 *
 * @author haibin
 */
public interface ApplicationSceneFunctionParamFilter {

  /**
   * 是否屏蔽这个参数
   *
   * @param appCode 小程序
   * @param paramAlias
   * @return
   */
  public boolean skip(String appCode, String paramAlias);

  /**
   * 加载配置
   *
   * @param configs
   */
  public void load(Map<String, List<String>> configs);
}
