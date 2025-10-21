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

package com.alibaba.chaosblade.box.dao.scheduler.domain;

import java.io.Serializable;
import java.util.*;
import lombok.Data;

/** @author haibin */
@Data
public class SchedulerJobCreateRequest implements Serializable {

  /** cron表达式 */
  private String cron;

  /** cron表达式偏移 */
  private int cronDataOffset;

  /** 定时任务名字 */
  private String name;

  /** 业务类型 */
  private Integer businessType;

  /** 业务ID */
  private String businessId;

  /** 类名 */
  private String className;

  private Date startTime;

  public SchedulerJobCreateRequest(
      String cron,
      int cronDataOffset,
      String name,
      Integer businessType,
      String businessId,
      String className) {
    this.cron = cron;
    this.cronDataOffset = cronDataOffset;
    this.name = name;
    this.businessType = businessType;
    this.businessId = businessId;
    this.className = className;
  }

  private String executeMode;

  /** 负责人 */
  private String userId;

  /** 通知人员 */
  private List<String> notificationUsers = new ArrayList<>();

  private Map<String, String> parameters = new HashMap<>();
}
