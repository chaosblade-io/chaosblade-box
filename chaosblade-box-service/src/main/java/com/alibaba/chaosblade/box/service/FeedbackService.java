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

package com.alibaba.chaosblade.box.service;

import com.alibaba.chaosblade.box.common.common.domain.Response;
import com.alibaba.chaosblade.box.common.common.domain.feedback.ExperimentTaskFeedback;
import com.alibaba.chaosblade.box.common.common.domain.feedback.ExperimentTaskFeedbackSubmitRequest;
import com.alibaba.chaosblade.box.dao.infrastructure.experiment.feedback.FeedbackSubmitResult;

/** @author haibin.lhb */
public interface FeedbackService {

  /**
   * 查询演练反馈
   *
   * @param experimentTaskId 演练任务ID
   * @return
   */
  public Response<ExperimentTaskFeedback> getExperimentTaskFeedback(String experimentTaskId);

  /**
   * 提交返回,
   *
   * @param experimentTaskFeedbackSubmitRequest
   * @return 返回反馈ID
   */
  public Response<FeedbackSubmitResult> submitExperimentTaskFeedback(
      ExperimentTaskFeedbackSubmitRequest experimentTaskFeedbackSubmitRequest);
}
