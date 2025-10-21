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
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.util.Date;
import lombok.Data;

/** @author haibin.lhb */
@TableName(value = "t_chaos_scheduler_trigger_log")
@Data
public class SchedulerJobTriggerLogDO extends BaseDO {

  @TableId protected Long id;

  private String jobGroup;
  private String jobId;
  private String executorAddress;
  private String executorHandler;
  private String executorParam;
  private Date endTime;
  private String businessId;
  private Date fireTime;
  private String fireInstanceId;
}
