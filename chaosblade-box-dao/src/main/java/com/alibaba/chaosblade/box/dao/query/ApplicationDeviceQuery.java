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

package com.alibaba.chaosblade.box.dao.query;

import java.util.List;
import lombok.Data;

/** @author haibin */
@Data
public class ApplicationDeviceQuery {

  /** 应用Id */
  private Long appId;
  /** 分组名 */
  private String groupName;

  /** 设备关键词 */
  private String partName;

  private String userId;

  private String namespace;

  /** 应用名 */
  private String appName;

  /** private ip */
  private String ip;

  private List<String> groups;

  private List<String> tags;

  private List<String> kubNamespaces;

  private List<String> ips;

  private Integer status;

  private List<Integer> dimensions;

  private String hostConfigurationId;

  private Integer osType;

  private String clusterId;

  private List<String> clusterIds;
}
