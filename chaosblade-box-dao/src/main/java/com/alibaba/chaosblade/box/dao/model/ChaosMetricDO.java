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

package com.alibaba.chaosblade.box.dao.model;

import com.alibaba.chaosblade.box.dao.model.base.BaseDO;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/** @author haibin */
@TableName(value = "t_chaos_metric")
@Data
public class ChaosMetricDO extends BaseDO {

  /** 机器地址 */
  private String host;

  private String configurationId;

  /** 值 */
  private Float value;

  /** 单位 */
  private String unit;

  /** 具体指标 */
  private String metric;

  /** 指标分类 */
  private String category;

  /** 记录时间 */
  private Long timestamp;

  /** 来源 */
  private Integer source;

  /** 注入ID */
  private String expId;
}
