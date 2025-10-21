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

package com.alibaba.chaosblade.box.service.model.agent;

import java.io.Serializable;
import java.util.List;
import lombok.Data;

@Data
public class PluginDTO implements Serializable {
  private static final long serialVersionUID = -1L;
  private String instanceId;
  private String instanceName;
  private String pluginType;
  private String appName;
  private Integer pluginStatus;
  private String publicIp;
  private String privateIp;
  private Boolean enable;
  private String configurationId;
  private Boolean canAutoInstall;
  private Boolean canAutoUnInstall;
  private Long createTime;
  private Integer osType;
  private String networkType;
  private String version;
  private String installMode;
  private Long connectTime;
  private String link;
  private Boolean upgrade;
  private String upgradeVersion;
  private List<String> chaosTools;
}
