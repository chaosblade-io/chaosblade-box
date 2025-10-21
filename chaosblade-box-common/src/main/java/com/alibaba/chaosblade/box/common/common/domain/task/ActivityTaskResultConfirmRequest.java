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

package com.alibaba.chaosblade.box.common.common.domain.task;

import com.alibaba.chaosblade.box.common.common.domain.BaseRequest;
import lombok.Data;

/** @author haibin */
@Data
public class ActivityTaskResultConfirmRequest extends BaseRequest {

  /** 需要确认的活动任务ID */
  private String activityTaskId;

  /** 用户确认是否当前活动任务是否运行成功， 如果true，会继续运行下一个结果，如果false，那么会停止并且恢复当前演练任务 */
  private boolean success;

  private String experimentTaskId;
}
