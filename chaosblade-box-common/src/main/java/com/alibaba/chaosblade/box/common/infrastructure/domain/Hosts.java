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

package com.alibaba.chaosblade.box.common.infrastructure.domain;

import com.alibaba.chaosblade.box.common.app.sdk.scope.Host;
import com.google.common.collect.Sets;
import java.util.*;
import lombok.Data;

/** @author haibin */
@Data
public class Hosts {

  public List<Host> getContent() {
    return content;
  }

  private List<Host> content = new ArrayList<>();

  private Integer scopeType;

  private String appName;

  private String appId;

  private Integer appType;

  private List<String> appGroups;

  /** 机器选择方式 {@link HostSelectTypes} */
  private Integer selectType;

  private Integer hostPercent;

  /** 云服务类型 */
  private String cloudServiceType;

  /** 云服务类型名称 */
  private String cloudServiceName;

  /** 机器操作系统类型 */
  private Integer osType;

  public Hosts() {}

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof Hosts)) {
      return false;
    }
    Hosts hosts1 = (Hosts) o;
    Set<Integer> aHashCodes = new HashSet<>();
    for (Host host : getContent()) {
      aHashCodes.add(host.hashCode());
    }
    Set<Integer> bHashCodes = new HashSet<>();
    for (Host host : hosts1.getContent()) {
      bHashCodes.add(host.hashCode());
    }
    return Sets.difference(aHashCodes, bHashCodes).isEmpty();
  }

  @Override
  public int hashCode() {
    return Objects.hash(getContent());
  }
}
