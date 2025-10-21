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

import com.alibaba.chaosblade.box.dao.model.SceneFunctionParameterDO;
import java.util.List;
import lombok.Data;

/**
 * 局部参数定义
 *
 * <p>用于解决chaosblade中不同含义的参数名称相同，由于全局参数已定义导致参数解析有误的情况
 *
 * @author sunpeng
 */
@Data
public class LocalSceneFunctionParam {

  private String name;

  /**
   * 场景类型，模糊匹配
   *
   * <p>String contains 匹配，如果方法code包含则匹配
   */
  private List<String> includeScene;

  /**
   * 场景类型，模糊匹配排除
   *
   * <p>String contains 匹配，如果方法code包含则匹配
   */
  private List<String> excludeScene;

  /** 场景code，单个code精确匹配 */
  private List<String> includeCode;

  /** 排除场景code，单个code精确匹配 */
  private List<String> excludeCode;

  /** 参数内容 */
  private SceneFunctionParameterDO param;
}
