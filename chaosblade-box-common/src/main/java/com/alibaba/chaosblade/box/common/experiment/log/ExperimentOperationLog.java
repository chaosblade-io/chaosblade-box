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

package com.alibaba.chaosblade.box.common.experiment.log;

import com.alibaba.fastjson.annotation.JSONField;
import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/** @author haibin.lhb */
@Data
public class ExperimentOperationLog implements Serializable {

  /** 操作时间 */
  private Date time;

  /** 操作人员 */
  @JSONField(name = "operator")
  private String operator;

  /** 操作类型 */
  @JSONField(name = "change_type")
  private String changeType;

  /** 描述 */
  @JSONField(name = "change_desc")
  private String description;

  @JSONField(name = "property_id")
  private String propertyId;
}
