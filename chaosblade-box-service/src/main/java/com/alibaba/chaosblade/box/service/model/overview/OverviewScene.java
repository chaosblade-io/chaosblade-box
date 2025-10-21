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
public class OverviewScene {

  /** 场景名称 */
  private String name;

  /** 场景小程序编码 */
  private String appCode;

  /** 场景排序 */
  private Integer order;

  /** 场景目标资源 */
  private String sceneTarget;

  /** 场景类型 */
  private String sceneType;

  public OverviewScene(
      String name, String appCode, Integer order, String sceneTarget, String sceneType) {
    this.name = name;
    this.appCode = appCode;
    this.order = order;
    this.sceneTarget = sceneTarget;
    this.sceneType = sceneType;
  }

  public OverviewScene() {}
}
