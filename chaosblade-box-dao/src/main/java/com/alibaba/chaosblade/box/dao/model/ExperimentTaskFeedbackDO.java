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

import com.alibaba.chaosblade.box.common.common.domain.feedback.ExtraFeedbackComponent;
import com.alibaba.chaosblade.box.dao.model.base.BaseDO;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.util.Date;
import lombok.Data;

/** @author haibin.lhb */
@TableName(value = "t_chaos_experiment_task_feedback")
@Data
public class ExperimentTaskFeedbackDO extends BaseDO {

  @TableId(value = "feedback_id", type = IdType.ID_WORKER)
  private String feedbackId;

  /** 任务ID */
  @TableField(value = "experiment_task_id")
  private String experimentTaskId;

  /** 主账号 */
  @TableField(value = "user_id")
  private String userId;

  /** */
  @TableField(value = "experiment_id")
  private String experimentId;

  /** 用户备注 */
  private String memo;

  /** 是否符合预期 */
  @TableField(value = "expectation_status")
  private Integer expectationStatus;

  /** 业务影响程度 */
  @TableField(value = "business_status")
  private Integer businessStatus;

  /** 反馈时间 */
  @TableField(value = "feedback_time")
  private Date feedbackTime;

  @TableId(value = "extra_component")
  private ExtraFeedbackComponent extraComponent;
}
