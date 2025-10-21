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
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import lombok.Data;

/** @author haibin */
@Data
@TableName(value = "t_chaos_scheduler_job")
public class SchedulerJobDO extends BaseDO {

  public static Integer TASK_STATUS_RUNNING = 1;

  public static Integer TASK_STATUS_READY = 0;

  @TableId(type = IdType.ID_WORKER)
  private String jobId;

  /** 任务名字 */
  private String name;

  /** cron表达式 */
  private String cronExpression;

  /** 是否启用 */
  private Boolean enabled;

  /** 用户 */
  private String userId;

  /** 外部系统ID */
  private String outerId;

  /** 业务类型 */
  private Integer businessType;

  /** 业务ID */
  private String businessId;

  /** 额外信息 */
  private Map<String, String> extraInfos;

  private String schedulerBeanClass;

  private Date startTime;

  /** 是否在运行 */
  private Integer status;

  public void addExtraInfo(String key, String value) {
    if (extraInfos == null) {
      extraInfos = new HashMap<>();
    }
    extraInfos.put(key, value);
  }
}
