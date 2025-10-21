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
public class OverviewPayPackInfo {

  /** 资源包名称 */
  private String payPackName;

  /** 资源包过期时间 */
  private String expiredTime;

  /** 资源包空间总数 */
  private Integer workspaceTotalCount;

  /** 资源包剩余空间数 */
  private Integer workspaceCount;

  /** 演练次数总数 */
  private Integer totalCount;

  /** 演练次数剩余数 */
  private Integer remainingCount;

  /** 支持演练对象数量 */
  private Integer targetCount;
}
