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

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * 应用表 区别于{@link ApplicationDO}
 *
 * @author haibin
 * @see com.alibaba.chaosblade.box.service.cloud.base.persistence.domain.AhasChaosApplicationGroupDO
 * @see AhasChaosApplicationDeviceDO
 */
@Data
@ToString
@EqualsAndHashCode(callSuper = true)
public class ChaosApplicationDO extends BaseDO {

  /** 应用名 */
  private String appName;

  /** namespace */
  private String namespace;

  /** 应用归属账号 */
  private String userId;

  /** 应用类型 */
  private Integer appType;

  /** 应用是否被禁用 */
  private Boolean disabled;

  /** 资源划分维度 */
  private Integer dimension;
}
