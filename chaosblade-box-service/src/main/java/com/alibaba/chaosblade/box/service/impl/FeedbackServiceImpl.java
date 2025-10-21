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

package com.alibaba.chaosblade.box.service.impl;

import com.alibaba.chaosblade.box.common.common.domain.Response;
import com.alibaba.chaosblade.box.common.common.domain.feedback.ExperimentTaskFeedback;
import com.alibaba.chaosblade.box.common.common.domain.feedback.ExperimentTaskFeedbackSubmitRequest;
import com.alibaba.chaosblade.box.dao.infrastructure.experiment.feedback.FeedbackProcessor;
import com.alibaba.chaosblade.box.dao.infrastructure.experiment.feedback.FeedbackSubmitResult;
import com.alibaba.chaosblade.box.dao.model.ExperimentTaskDO;
import com.alibaba.chaosblade.box.service.FeedbackService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/** @author haibin.lhb */
@Component
public class FeedbackServiceImpl implements FeedbackService {

  @Autowired private FeedbackProcessor feedbackProcessor;

  @Override
  public Response<ExperimentTaskFeedback> getExperimentTaskFeedback(String experimentTaskId) {
    Response<ExperimentTaskDO> response =
        feedbackProcessor.checkTaskAllowFeedback(experimentTaskId);
    if (!response.isSuccess()) {
      return Response.failedWith(response.getError());
    }
    return Response.okWithData(feedbackProcessor.getExperimentTaskFeedback(response.getResult()));
  }

  @Override
  public Response<FeedbackSubmitResult> submitExperimentTaskFeedback(
      ExperimentTaskFeedbackSubmitRequest experimentTaskFeedbackSubmitRequest) {
    Response<ExperimentTaskDO> response =
        feedbackProcessor.checkTaskAllowFeedback(experimentTaskFeedbackSubmitRequest.getTaskId());
    if (!response.isSuccess()) {
      return Response.failedWith(response.getError());
    }
    return feedbackProcessor.submitExperimentTaskFeedback(
        experimentTaskFeedbackSubmitRequest.getUser(),
        response.getResult(),
        experimentTaskFeedbackSubmitRequest.getFeedback());
  }
}
