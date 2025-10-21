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

package com.alibaba.chaosblade.box.service.model.overview;

import lombok.Data;

/** @author sunpeng */
@Data
public class OverviewAgentInfo {

  /** 探针总数 */
  private Integer totalCount;

  /** 在线数量 */
  private Integer onlineCount;

  /** 正常数量 */
  private Integer normalCount;

  /** 异常数量 */
  private Integer errorCount;

  public OverviewAgentInfo() {}

  public OverviewAgentInfo(
      Integer totalCount, Integer onlineCount, Integer normalCount, Integer errorCount) {
    this.totalCount = totalCount;
    this.onlineCount = onlineCount;
    this.normalCount = normalCount;
    this.errorCount = errorCount;
  }
}
