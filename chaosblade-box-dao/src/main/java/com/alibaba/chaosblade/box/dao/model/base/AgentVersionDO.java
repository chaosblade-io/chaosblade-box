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

package com.alibaba.chaosblade.box.dao.model.base;

import java.util.Date;
import lombok.Data;

@Data
public class AgentVersionDO {
  private String pluginType;
  private String version;
  private String parentPluginType;
  private String parentVersion;
  private Boolean latest;
  private Date uptime;
  private String md5;
  private String stage;
  private String description;
  private String grayStrategy;
  private String grayTag;
  private String upgradeStrategy;
  private Double coverage;
  private String optId;
  private String optNick;
  private Integer osType;
}
