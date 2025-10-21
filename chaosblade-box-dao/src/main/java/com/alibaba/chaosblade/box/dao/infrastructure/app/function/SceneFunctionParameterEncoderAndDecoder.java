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

import com.alibaba.chaosblade.box.common.common.domain.definition.SceneFunctionParameterComponent;

/**
 * 用来对参数进行加密和解密操作
 *
 * @author haibin
 */
public interface SceneFunctionParameterEncoderAndDecoder {

  /**
   * 加密参数值
   *
   * @param appCode 小程序code
   * @param alias 参数名
   * @param value 参数原始值
   * @param sceneFunctionParameterComponent 参数组件信息
   * @return 编码后的参数值
   * @throws Exception
   */
  public String encodeValue(
      String appCode,
      String alias,
      String value,
      SceneFunctionParameterComponent sceneFunctionParameterComponent)
      throws Exception;

  /**
   * 解密参数值
   *
   * @param appCode 小程序code
   * @param alias 参数名
   * @param value 参数原始值
   * @param sceneFunctionParameterComponent 参数组件信息
   * @return 解码后的值
   * @throws Exception
   */
  public String decodeValue(
      String appCode,
      String alias,
      String value,
      SceneFunctionParameterComponent sceneFunctionParameterComponent)
      throws Exception;

  /**
   * 判断当前参数是否需要进行编码或者解码操作
   *
   * @param sceneFunctionParameterComponent 参数组件信息
   * @return true需要
   */
  public boolean supportSceneFunctionParameterComponent(
      SceneFunctionParameterComponent sceneFunctionParameterComponent);
}
