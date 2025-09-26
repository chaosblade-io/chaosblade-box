package com.alibaba.chaosblade.box.controller;

import com.alibaba.chaosblade.box.annotation.LoginUser;
import com.alibaba.chaosblade.box.common.app.sdk.scope.Host;
import com.alibaba.chaosblade.box.common.common.domain.experiment.BaseExperimentRequest;
import com.alibaba.chaosblade.box.common.common.domain.user.ChaosUser;
import com.alibaba.chaosblade.box.common.infrastructure.constant.PermissionTypes;
import com.alibaba.chaosblade.box.common.infrastructure.domain.experiment.ExperimentBasicInfo;
import com.alibaba.chaosblade.box.common.infrastructure.domain.experiment.flow.MiniFlowGroup;
import com.alibaba.chaosblade.box.common.infrastructure.exception.ChaosException;
import com.alibaba.chaosblade.box.common.infrastructure.util.CollectionUtil;
import com.alibaba.chaosblade.box.dao.infrastructure.experiment.request.ExperimentDefinitionRequest;
import com.alibaba.chaosblade.box.dao.infrastructure.experiment.request.ExperimentUpdateRequest;
import com.alibaba.chaosblade.box.model.RestResponseUtil;
import com.alibaba.chaosblade.box.service.ExperimentReadService;
import com.alibaba.chaosblade.box.service.ExperimentWriteService;
import com.alibaba.chaosblade.box.service.auth.perimission.UserPermissionService;
import com.alibaba.chaosblade.box.service.model.RestResponse;
import io.swagger.annotations.ApiOperation;
import java.util.List;
import java.util.stream.Collectors;
import javax.annotation.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/** @author haibin.lhb */
@RestController
public class DeprecateController extends BaseController {

  @Resource private ExperimentReadService experimentReadService;

  @Autowired private ExperimentWriteService experimentWriteService;

  @Resource private UserPermissionService userPermissionService;

  @PostMapping(value = "/QueryExperimentBasicInfo")
  @Deprecated
  public RestResponse<ExperimentBasicInfo> queryExperimentBasicInfo(
      @LoginUser ChaosUser chaosUser, @RequestBody BaseExperimentRequest baseExperimentRequest) {
    userPermissionService.checkExperimentPermission(
        PermissionTypes.R,
        chaosUser,
        baseExperimentRequest.getExperimentId(),
        baseExperimentRequest.getNamespace(),
        null);
    return RestResponseUtil.initWithServiceResponse(
        experimentReadService.getExperimentBasicInfo(baseExperimentRequest));
  }

  @PostMapping(value = "/UpdateExperimentBasicInfo")
  @Deprecated
  public RestResponse<Boolean> updateExperimentBasicInfo(
      @LoginUser ChaosUser chaosUser,
      @RequestBody ExperimentUpdateRequest experimentBasicInfoUpdateRequest)
      throws ChaosException {
    userPermissionService.checkExperimentPermission(
        PermissionTypes.W,
        chaosUser,
        experimentBasicInfoUpdateRequest.getExperimentId(),
        experimentBasicInfoUpdateRequest.getNamespace(),
        null);
    return RestResponseUtil.initWithServiceResponse(
        experimentWriteService.updateExperimentBasicInfo(
            chaosUser, experimentBasicInfoUpdateRequest));
  }

  @ApiOperation(value = "更新演练定义")
  @PostMapping(value = "/UpdateExperimentFlowDefinition")
  @Deprecated
  public RestResponse<Void> updateExperimentDefinition(
      @LoginUser ChaosUser chaosUser,
      @RequestBody ExperimentDefinitionRequest experimentFlowDefinitionRequest) {

    userPermissionService.checkExperimentPermission(
        PermissionTypes.W,
        chaosUser,
        experimentFlowDefinitionRequest.getExperimentId(),
        experimentFlowDefinitionRequest.getNamespace(),
        mergeRequestHosts(experimentFlowDefinitionRequest));
    return RestResponseUtil.initWithServiceResponse(
        experimentWriteService.updateExperimentDefinition(experimentFlowDefinitionRequest));
  }

  private List<Host> mergeRequestHosts(ExperimentDefinitionRequest experimentDefinitionRequest) {
    List<MiniFlowGroup> miniFlowGroups = experimentDefinitionRequest.getFlowGroups();
    if (CollectionUtil.isNullOrEmpty(miniFlowGroups)) {
      return null;
    }

    return miniFlowGroups.stream()
        .filter(mg -> null != mg && !CollectionUtil.isNullOrEmpty(mg.getHosts()))
        .flatMap(mg -> mg.getHosts().stream())
        .collect(Collectors.toList());
  }
}
