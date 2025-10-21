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

package com.alibaba.chaosblade.box.common.sdk.entity;

import com.alibaba.chaosblade.box.common.sdk.constant.Target;
import java.util.Map;

/** @author Changjun Xiao */
public class PrepareArgs {

  /** {@link Target} */
  private String machineType;

  /** Agent Pod IP */
  private String machine;
  /** Agent port */
  private String port;
  /** host|node|pod|container */
  private String scope;
  /** jvm */
  private String type;

  /** 如果是集群 Container，需要添加 container-names|container-ids|names|namespace 参数 */
  private Map<String, String> flags;

  public String getMachineType() {
    return machineType;
  }

  public void setMachineType(String machineType) {
    this.machineType = machineType;
  }

  public String getMachine() {
    return machine;
  }

  public void setMachine(String machine) {
    this.machine = machine;
  }

  public String getPort() {
    return port;
  }

  public PrepareArgs setPort(String port) {
    this.port = port;
    return this;
  }

  public String getScope() {
    return scope;
  }

  public PrepareArgs setScope(String scope) {
    this.scope = scope;
    return this;
  }

  public String getType() {
    return type;
  }

  public PrepareArgs setType(String type) {
    this.type = type;
    return this;
  }

  public Map<String, String> getFlags() {
    return flags;
  }

  public PrepareArgs setFlags(Map<String, String> flags) {
    this.flags = flags;
    return this;
  }
}
