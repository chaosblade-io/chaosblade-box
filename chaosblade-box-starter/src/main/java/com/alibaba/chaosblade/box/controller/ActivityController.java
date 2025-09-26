package com.alibaba.chaosblade.box.controller;

import com.alibaba.chaosblade.box.annotation.LoginUser;
import com.alibaba.chaosblade.box.annotation.SwaggerDoc;
import com.alibaba.chaosblade.box.common.common.domain.BaseRequest;
import com.alibaba.chaosblade.box.common.common.domain.Response;
import com.alibaba.chaosblade.box.common.common.domain.activity.ActivityTask;
import com.alibaba.chaosblade.box.common.common.domain.activity.MiniAppTask;
import com.alibaba.chaosblade.box.common.common.domain.activity.MiniAppTaskQueryRequest;
import com.alibaba.chaosblade.box.common.common.domain.activity.QueryActivityTaskRequest;
import com.alibaba.chaosblade.box.common.common.domain.task.ActivityTaskResultConfirmRequest;
import com.alibaba.chaosblade.box.common.common.domain.user.ChaosUser;
import com.alibaba.chaosblade.box.common.infrastructure.constant.PermissionTypes;
import com.alibaba.chaosblade.box.common.infrastructure.domain.activity.ActivityGroupDefinitionCheckRequest;
import com.alibaba.chaosblade.box.common.infrastructure.domain.activity.ActivityGroupDefinitionCheckResponse;
import com.alibaba.chaosblade.box.dao.infrastructure.checker.ActivityTaskChecker;
import com.alibaba.chaosblade.box.dao.infrastructure.experiment.request.ActivityRetryRequest;
import com.alibaba.chaosblade.box.dao.infrastructure.manager.MiniAppTaskManager;
import com.alibaba.chaosblade.box.dao.model.ActivityTaskDO;
import com.alibaba.chaosblade.box.model.RestResponseUtil;
import com.alibaba.chaosblade.box.service.ActivityService;
import com.alibaba.chaosblade.box.service.ActivityTaskService;
import com.alibaba.chaosblade.box.service.auth.perimission.UserPermissionService;
import com.alibaba.chaosblade.box.service.model.RestResponse;
import com.google.common.base.Preconditions;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/** @author haibin */
@RestController
@Api(description = "演练活动")
@SwaggerDoc
public class ActivityController extends BaseController {

  @Autowired private ActivityTaskService activityTaskService;

  @Autowired private ActivityTaskChecker activityTaskChecker;

  @Autowired private UserPermissionService userPermissionService;
  @Autowired private ActivityService activityService;

  @Autowired private MiniAppTaskManager miniAppTaskManager;

  @ApiOperation(value = "演练某个步骤的详情")
  @PostMapping(value = "QueryActivityTask", produces = "application/json")
  RestResponse<ActivityTask> getActivityTaskDetail(
      @LoginUser ChaosUser chaosUser,
      @RequestBody QueryActivityTaskRequest queryActivityTaskRequest) {
    String activityTaskId = queryActivityTaskRequest.getActivityTaskId();

    Preconditions.checkArgument(activityTaskId != null, "activity task id required");

    Response<ActivityTask> activityTaskResponse =
        activityTaskService.findActivityTaskByActivityTaskId(activityTaskId);

    if (null != activityTaskResponse && null != activityTaskResponse.getResult()) {
      userPermissionService.checkExperimentTaskPermission(
          PermissionTypes.R, chaosUser, activityTaskResponse.getResult().getExperimentTaskId());
    }

    return RestResponseUtil.initWithServiceResponse(activityTaskResponse);
  }

  @ApiOperation(value = "演练某个机器的任务结果")
  @PostMapping(value = "QueryMiniAppTask", produces = "application/json")
  RestResponse<MiniAppTask> getMiniAppTask(
      @LoginUser ChaosUser chaosUser,
      @RequestBody MiniAppTaskQueryRequest miniAppTaskQueryRequest) {
    Preconditions.checkArgument(
        miniAppTaskQueryRequest.getMiniAppTaskId() != null, "miniapp task id required");
    MiniAppTask miniAppTask =
        miniAppTaskManager.findMiniAppTask(miniAppTaskQueryRequest.getMiniAppTaskId());
    return RestResponseUtil.okWithData(miniAppTask);
  }

  @ApiOperation(value = "用户确认演练结果")
  @PostMapping(value = "UserCheckActivityTask")
  RestResponse<Void> userCheckActivityTask(
      @LoginUser ChaosUser chaosUser,
      @RequestBody ActivityTaskResultConfirmRequest activityTaskResultConfirmRequest) {
    return RestResponseUtil.initWithServiceResponse(
        activityTaskService.confirmActivityTaskResult(chaosUser, activityTaskResultConfirmRequest));
  }

  @ApiOperation(value = "重试演练步骤")
  @PostMapping(value = "RetryActivityTask")
  RestResponse<String> retryActivityTask(
      @LoginUser ChaosUser chaosUser,
      @RequestBody ActivityTaskResultConfirmRequest activityTaskResultConfirmRequest) {
    String activityTaskId = activityTaskResultConfirmRequest.getActivityTaskId();
    Response<ActivityTaskDO> activityTaskDOResponse =
        activityTaskChecker.checkActivityTaskExist(activityTaskId);
    if (!activityTaskDOResponse.isSuccess()) {
      return RestResponseUtil.failed(activityTaskDOResponse.getError());
    }
    ActivityTaskDO activityTaskDO = activityTaskDOResponse.getResult();
    userPermissionService.checkExperimentTaskPermission(
        PermissionTypes.X,
        activityTaskResultConfirmRequest.getUser(),
        activityTaskDO.getExperimentTaskId());
    ActivityRetryRequest activityRetryRequest = new ActivityRetryRequest();
    BaseRequest.copy(activityTaskResultConfirmRequest, activityRetryRequest);
    activityRetryRequest.setActivityTaskDO(activityTaskDO);
    return RestResponseUtil.initWithServiceResponse(
        activityTaskService.retryActivity(activityRetryRequest));
  }

  /**
   * 校验activity定义
   *
   * @param chaosUser
   * @param activityGroupDefinitionCheckRequest
   * @return
   */
  @PostMapping(value = "CheckActivityGroupDefinition")
  RestResponse<ActivityGroupDefinitionCheckResponse> checkActivityGroupDefinition(
      @LoginUser ChaosUser chaosUser,
      @RequestBody ActivityGroupDefinitionCheckRequest activityGroupDefinitionCheckRequest) {
    return RestResponseUtil.initWithServiceResponse(
        activityService.checkActivityGroupDefinition(activityGroupDefinitionCheckRequest));
  }
}
