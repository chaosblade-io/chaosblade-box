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

package com.alibaba.chaosblade.box.dao.infrastructure.utils;

import com.alibaba.chaosblade.box.common.common.enums.DeviceType;
import com.alibaba.chaosblade.box.common.experiment.task.flow.util.PluginType;

/**
 * @author: xinyuan.ymm
 * @create: 2020-05-08 10:08 AM
 */
public class PluginTypeUtil {

  /**
   * 基于设备探针安装的设备类型，获取其映射的插件类型
   *
   * @param deviceType
   * @return
   */
  public static final String getPluginTypeByDeviceType(Integer deviceType) {
    if (DeviceType.HOST.getType() == deviceType) {
      return PluginType.CHAOS_AGENT.name();
    }

    if (DeviceType.HOST_POD.getType() == deviceType) {
      return PluginType.CHAOS_POD_AGENT.name();
    }

    return null;
  }
}
