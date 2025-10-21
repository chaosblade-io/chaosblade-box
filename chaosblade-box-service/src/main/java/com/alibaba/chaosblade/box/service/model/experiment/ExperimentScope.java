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

package com.alibaba.chaosblade.box.service.model.experiment;

import java.io.Serializable;
import java.util.List;
import lombok.Data;

/** @author haibin */
@Data
public class ExperimentScope implements Serializable {

  private String privateIp;

  private String publicIp;

  private Integer agentStatus;

  private Long connectTime;

  private String configurationId;

  private String hostConfigurationId;

  /** 是否被演练 */
  private Boolean isExperimented;

  private Integer experimentTaskCount;

  private String agentVersion;

  private String deviceId;

  private String hostName;

  private String clusterName;

  private List<String> groups;

  private String deviceName;

  private Integer deviceType;

  private List<String> deviceTags;

  private Integer osType;

  private List<String> chaosTools;

  private String pluginType;

  /** 插件状态 */
  private Integer pluginStatus;

  /** 插件是否开启 */
  private Boolean enable;

  /** 是否可以白屏化安装卸载 */
  private Boolean canAutoInstall;

  /** 安装模式 */
  private String installMode;
}
