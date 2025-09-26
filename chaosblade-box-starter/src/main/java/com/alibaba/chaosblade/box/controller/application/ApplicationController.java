package com.alibaba.chaosblade.box.controller.application;

import com.alibaba.chaosblade.box.annotation.LoginUser;
import com.alibaba.chaosblade.box.common.app.sdk.scope.Host;
import com.alibaba.chaosblade.box.common.common.domain.ChaosError;
import com.alibaba.chaosblade.box.common.common.domain.Response;
import com.alibaba.chaosblade.box.common.common.domain.task.BaseExperimentTask;
import com.alibaba.chaosblade.box.common.common.domain.user.ChaosUser;
import com.alibaba.chaosblade.box.common.common.enums.CommonErrorCode;
import com.alibaba.chaosblade.box.common.experiment.model.AppNameAndIdPair;
import com.alibaba.chaosblade.box.common.infrastructure.constant.PermissionTypes;
import com.alibaba.chaosblade.box.common.infrastructure.util.CollectionUtil;
import com.alibaba.chaosblade.box.controller.BaseController;
import com.alibaba.chaosblade.box.dao.model.base.PageableResponse;
import com.alibaba.chaosblade.box.model.RestResponseUtil;
import com.alibaba.chaosblade.box.service.ApplicationService;
import com.alibaba.chaosblade.box.service.auth.perimission.UserPermissionService;
import com.alibaba.chaosblade.box.service.infrastructure.configuration.ApplicationConfiguration;
import com.alibaba.chaosblade.box.service.model.RestResponse;
import com.alibaba.chaosblade.box.service.model.application.*;
import com.alibaba.chaosblade.box.service.model.device.CloudDevice;
import com.alibaba.chaosblade.box.service.model.experiment.ExperimentScope;
import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import java.util.List;
import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/** @author haibin */
@RestController
public class ApplicationController extends BaseController {

  @Autowired private ApplicationService applicationService;

  @Autowired private UserPermissionService userPermissionService;

  /**
   * 获取用户的应用信息概况，包括了基本信息以及演练情况
   *
   * @param user 当前用户
   * @param getUserApplicationSummariesRequest 查询请求，支持分页
   * @return 用户应用信息情况
   */
  @PostMapping(value = "GetUserApplicationSummaries")
  public RestResponse<PageableResponse<UserApplicationSummary>> getUserApplicationSummaries(
      @LoginUser ChaosUser user,
      @RequestBody GetUserApplicationSummariesRequest getUserApplicationSummariesRequest) {
    getUserApplicationSummariesRequest.setUser(user);
    return RestResponseUtil.initWithServiceResponse(
        applicationService.getUserApplicationSummaries(getUserApplicationSummariesRequest));
  }

  /**
   * 搜索应用
   *
   * @param user
   * @param userApplicationSearchRequest
   * @return
   */
  @PostMapping(value = "SearchApplications")
  public RestResponse<List<UserApplicationSummary>> searchApplications(
      @LoginUser ChaosUser user,
      @RequestBody UserApplicationSearchRequest userApplicationSearchRequest) {
    userApplicationSearchRequest.setUser(user);
    return RestResponseUtil.initWithServiceResponse(
        applicationService.searchApplicationsForSummary(userApplicationSearchRequest));
  }

  /**
   * 获取应用基本信息
   *
   * @param user
   * @param userApplicationQueryRequest
   * @return
   * @throws Throwable
   */
  @PostMapping(value = "GetApplicationBasic")
  public RestResponse<ApplicationBasicInfo> getApplicationBasic(
      @LoginUser ChaosUser user,
      @RequestBody UserApplicationQueryRequest userApplicationQueryRequest)
      throws Throwable {

    userApplicationQueryRequest.setUser(user);
    if (userApplicationQueryRequest.getAppId() != null) {
      userPermissionService.checkUserApplicationPermission(
          PermissionTypes.R, user, userApplicationQueryRequest.getAppId());
    } else {
      userPermissionService.checkUserApplicationPermission(
          PermissionTypes.R,
          user,
          userApplicationQueryRequest.getNamespace(),
          userApplicationQueryRequest.getAppName());
    }
    return RestResponseUtil.initWithServiceResponse(
        applicationService.getApplicationBasic(userApplicationQueryRequest));
  }

  @PostMapping(value = "/StopExperimentsByApplication")
  public RestResponse<Integer> stopExperimentsByApplication(
      @LoginUser ChaosUser chaosUser,
      @RequestBody UserApplicationQueryRequest userApplicationQueryRequest) {
    userPermissionService.checkUserApplicationPermission(
        PermissionTypes.W,
        chaosUser,
        userApplicationQueryRequest.getNamespace(),
        userApplicationQueryRequest.getAppName());
    return RestResponseUtil.initWithServiceResponse(
        applicationService.stopExperimentsByApplication(userApplicationQueryRequest));
  }

  /**
   * 获取包含当前应用信息的演练任务记录
   *
   * @param user
   * @param userApplicationQueryRequest
   * @return
   * @throws Throwable
   */
  @PostMapping(value = "GetApplicationExperimentTasks")
  public RestResponse<PageableResponse<BaseExperimentTask>> getApplicationExperimentTasks(
      @LoginUser ChaosUser user,
      @RequestBody UserApplicationQueryRequest userApplicationQueryRequest)
      throws Throwable {
    userPermissionService.checkUserApplicationPermission(
        PermissionTypes.R, user, userApplicationQueryRequest.getAppId());
    return RestResponseUtil.initWithServiceResponse(
        applicationService.getApplicationExperimentTasks(userApplicationQueryRequest));
  }

  /**
   * @param user
   * @param applicationScopeSearchRequest
   * @return
   * @throws Throwable
   */
  @PostMapping(value = "GetApplicationHosts")
  public RestResponse<PageableResponse<ExperimentScope>> getApplicationHosts(
      @LoginUser ChaosUser user,
      @RequestBody ApplicationScopeSearchRequest applicationScopeSearchRequest)
      throws Throwable {
    userPermissionService.checkUserApplicationPermission(
        PermissionTypes.R, user, applicationScopeSearchRequest.getAppId());
    return RestResponseUtil.initWithServiceResponse(
        applicationService.searchApplicationHosts(applicationScopeSearchRequest));
  }

  @PostMapping(value = "SearchApplicationHosts")
  public RestResponse<PageableResponse<ExperimentScope>> searchApplicationHosts(
      @LoginUser ChaosUser user,
      @RequestBody ApplicationScopeSearchRequest applicationScopeSearchRequest)
      throws Throwable {
    return RestResponseUtil.initWithServiceResponse(
        applicationService.searchApplicationHosts(applicationScopeSearchRequest));
  }

  @PostMapping(value = "GetUserApplications")
  public RestResponse<PageableResponse<AppNameAndIdPair>> getUserApplications(
      @LoginUser ChaosUser user, @RequestBody ApplicationSearchRequest applicationSearchRequest)
      throws Throwable {
    if (applicationSearchRequest.getSize() == 10) {
      applicationSearchRequest.setSize(50);
    }
    applicationSearchRequest.setUser(user);
    Response<PageableResponse<AppNameAndIdPair>> listResponse =
        applicationService.pageUserApplicationNames(applicationSearchRequest);
    userPermissionService.filterApplicationPair(user, listResponse);
    return RestResponseUtil.initWithServiceResponse(listResponse);
  }

  @PostMapping(value = "GetUserApplicationGroups")
  public RestResponse<List<String>> getUserApplicationGroups(
      @LoginUser ChaosUser user,
      @RequestBody UserApplicationQueryRequest userApplicationQueryRequest)
      throws Throwable {

    try {
      userPermissionService.checkUserApplicationPermission(
          PermissionTypes.R, user, userApplicationQueryRequest.getAppId());
    } catch (IllegalArgumentException e) {
      return RestResponseUtil.failed(new ChaosError(CommonErrorCode.P_APPLICATION_ID_REQUIRED));
    }
    return RestResponseUtil.initWithServiceResponse(
        applicationService.getGroupsByApp(userApplicationQueryRequest));
  }

  @PostMapping(value = "GetScopesByApplication")
  public RestResponse<PageableResponse<CloudDevice>> getScopesByApplication(
      @LoginUser ChaosUser user,
      @RequestBody PageableQueryApplicationHostRequest pageableQueryApplicationHostRequest)
      throws Throwable {
    userPermissionService.checkUserApplicationPermission(
        PermissionTypes.R, user, pageableQueryApplicationHostRequest.getAppId());
    return RestResponseUtil.initWithServiceResponse(
        applicationService.getScopesByApplication(user, pageableQueryApplicationHostRequest));
  }

  @PostMapping(value = "ListApplicationConfigurations")
  public RestResponse<List<ApplicationConfiguration>> listApplicationConfigurations(
      @LoginUser ChaosUser user,
      @RequestBody UserApplicationQueryRequest userApplicationQueryRequest)
      throws Throwable {
    userPermissionService.checkUserApplicationPermission(
        PermissionTypes.R, user, userApplicationQueryRequest.getAppId());
    return RestResponseUtil.initWithServiceResponse(
        applicationService.listApplicationConfigurations(userApplicationQueryRequest));
  }

  @PostMapping(value = "UpdateApplicationConfiguration")
  public RestResponse updateApplicationConfiguration(
      @LoginUser ChaosUser user,
      @RequestBody ApplicationConfigurationUpdateRequest configurationUpdateRequest)
      throws Throwable {
    userPermissionService.checkUserApplicationPermission(
        PermissionTypes.R, user, Long.valueOf(configurationUpdateRequest.getAppId()));
    return RestResponseUtil.initWithServiceResponse(
        applicationService.updateApplicationConfiguration(configurationUpdateRequest));
  }

  @PostMapping(value = "UpdateApplicationTag")
  public RestResponse<Boolean> updateApplicationTag(
      @LoginUser ChaosUser user,
      @RequestBody ApplicationTagsUpdateRequest applicationTagsUpdateRequest)
      throws Throwable {
    applicationTagsUpdateRequest.setUser(user);
    userPermissionService.checkUserApplicationPermission(
        PermissionTypes.W, user, Long.valueOf(applicationTagsUpdateRequest.getAppId()));
    return RestResponseUtil.initWithServiceResponse(
        applicationService.updateApplicationTag(applicationTagsUpdateRequest));
  }

  @PostMapping(value = "BatchAddApplicationTag")
  public RestResponse<Boolean> batchAddApplicationTag(
      @LoginUser ChaosUser user,
      @RequestBody ApplicationTagsBatchUpdateRequest applicationTagsBatchUpdateRequest)
      throws Throwable {
    applicationTagsBatchUpdateRequest.setUser(user);
    userPermissionService.checkUserApplicationPermission(
        PermissionTypes.W, user, applicationTagsBatchUpdateRequest.getAppId());
    return RestResponseUtil.initWithServiceResponse(
        applicationService.batchAddApplicationTag(applicationTagsBatchUpdateRequest));
  }

  @PostMapping(value = "SearchDeviceTags")
  public RestResponse<List<String>> searchDeviceTags(
      @LoginUser ChaosUser user,
      @RequestBody ApplicationTagsSearchRequest applicationTagsUpdateRequest)
      throws Throwable {
    applicationTagsUpdateRequest.setUser(user);
    return RestResponseUtil.initWithServiceResponse(
        applicationService.searchDeviceTagsByAppIdAndGroupName(applicationTagsUpdateRequest));
  }

  @PostMapping(value = "SearchClusterNamespace")
  public RestResponse<Set<String>> searchClusterNamespace(
      @LoginUser ChaosUser user,
      @RequestBody ApplicationTagsSearchRequest applicationTagsUpdateRequest)
      throws Throwable {
    Preconditions.checkArgument(
        applicationTagsUpdateRequest.getAppId() != null, "appId id is required");
    Preconditions.checkArgument(
        !CollectionUtil.isNullOrEmpty(applicationTagsUpdateRequest.getGroupNames()),
        "group is required");
    applicationTagsUpdateRequest.setUser(user);
    return RestResponseUtil.initWithServiceResponse(
        applicationService.searchClusterNamespacesByAppIdAndGroupName(
            applicationTagsUpdateRequest));
  }

  @PostMapping(value = "CountScopesByApplication")
  public RestResponse<ApplicationHostsCount> countScopesByApplication(
      @LoginUser ChaosUser user,
      @RequestBody ApplicationHostsCountRequest applicationHostsCountRequest)
      throws Throwable {
    return RestResponseUtil.initWithServiceResponse(
        applicationService.countScopesByApplication(user, applicationHostsCountRequest));
  }

  @PostMapping(value = "GetScopeByAppConfigurationId")
  public RestResponse<Host> getApplicationScopeByConfigurationId(
      @LoginUser ChaosUser user,
      @RequestBody ApplicationScopeQueryRequest applicationScopeQueryRequest)
      throws Throwable {
    if (Strings.isNullOrEmpty(applicationScopeQueryRequest.getAppConfigurationId())) {
      return RestResponseUtil.okWithData(null);
    }
    return RestResponseUtil.initWithServiceResponse(
        applicationService.getScopeByAppConfigurationId(
            applicationScopeQueryRequest.getAppConfigurationId()));
  }
}
