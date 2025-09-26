package com.alibaba.chaosblade.box.controller;

import com.alibaba.chaosblade.box.annotation.LoginUser;
import com.alibaba.chaosblade.box.common.common.domain.feedback.ExperimentTaskFeedback;
import com.alibaba.chaosblade.box.common.common.domain.feedback.ExperimentTaskFeedbackSubmitRequest;
import com.alibaba.chaosblade.box.common.common.domain.task.BaseExperimentTaskRequest;
import com.alibaba.chaosblade.box.common.common.domain.user.ChaosUser;
import com.alibaba.chaosblade.box.common.infrastructure.constant.PermissionTypes;
import com.alibaba.chaosblade.box.dao.infrastructure.experiment.feedback.FeedbackSubmitResult;
import com.alibaba.chaosblade.box.model.RestResponseUtil;
import com.alibaba.chaosblade.box.service.FeedbackService;
import com.alibaba.chaosblade.box.service.auth.perimission.UserPermissionService;
import com.alibaba.chaosblade.box.service.model.RestResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/** @author haibin.lhb */
@RestController
public class FeedbackController extends BaseController {

  @Autowired private UserPermissionService userPermissionService;

  @Autowired private FeedbackService feedbackService;

  /**
   * 获取用户
   *
   * @param chaosUser 登录用户
   * @param baseExperimentTaskRequest 参数选项
   * @return 下拉选项列表
   */
  @PostMapping(value = "/GetExperimentTaskFeedback")
  public RestResponse<ExperimentTaskFeedback> getExperimentTaskFeedback(
      @LoginUser ChaosUser chaosUser,
      @RequestBody BaseExperimentTaskRequest baseExperimentTaskRequest) {
    userPermissionService.checkExperimentTaskPermission(
        PermissionTypes.R, chaosUser, baseExperimentTaskRequest.getTaskId());
    return RestResponseUtil.initWithServiceResponse(
        feedbackService.getExperimentTaskFeedback(baseExperimentTaskRequest.getTaskId()));
  }

  @PostMapping(value = "/SubmitExperimentTaskFeedback")
  public RestResponse<FeedbackSubmitResult> submitExperimentTaskFeedback(
      @LoginUser ChaosUser chaosUser,
      @RequestBody ExperimentTaskFeedbackSubmitRequest experimentTaskFeedbackSubmitRequest) {
    userPermissionService.checkExperimentTaskPermission(
        PermissionTypes.X, chaosUser, experimentTaskFeedbackSubmitRequest.getTaskId());
    experimentTaskFeedbackSubmitRequest.setUser(chaosUser);
    return RestResponseUtil.initWithServiceResponse(
        feedbackService.submitExperimentTaskFeedback(experimentTaskFeedbackSubmitRequest));
  }
}
