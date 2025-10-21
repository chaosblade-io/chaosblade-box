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

package com.alibaba.chaosblade.box.service;

import com.alibaba.chaosblade.box.common.common.annotation.ExtensionPoint;
import com.alibaba.chaosblade.box.service.model.param.ParamOptionsQueryRequest;
import java.util.Set;

/** @author haibin */
@ExtensionPoint
public interface MiniAppParamOptionsService {

  /**
   * 获取所有的网络设备
   *
   * @return
   */
  public Set<String> queryNetworkDevice(ParamOptionsQueryRequest paramOptionsQueryRequest);

  /**
   * 获取所有的磁盘
   *
   * @return
   */
  public Set<String> queryDiskDevice(ParamOptionsQueryRequest paramOptionsQueryRequest);

  /**
   * 获取io hang的块设备
   *
   * @param paramOptionsQueryRequest
   * @return
   */
  public Set<String> queryDiskBlockDevice(ParamOptionsQueryRequest paramOptionsQueryRequest);

  /**
   * 通用的参数描述信息
   *
   * @param paramOptionsQueryRequest
   * @return
   */
  public Set<String> queryParamOptions(ParamOptionsQueryRequest paramOptionsQueryRequest);
}
