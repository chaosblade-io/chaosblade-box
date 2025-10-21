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

package com.alibaba.chaosblade.box.common.sdk.parser;

import com.alibaba.chaosblade.box.common.sdk.entity.ChaosModels;
import java.io.InputStream;

/** @author Changjun Xiao */
public interface YamlService {

  /**
   * yaml inputstream
   *
   * @param source
   * @return
   */
  InputStream getInputStream(String source);

  /** 关闭流 */
  void close();

  /**
   * 解析 yaml 为 bladeModels
   *
   * @param source
   * @return
   */
  ChaosModels parse(String source);
}
