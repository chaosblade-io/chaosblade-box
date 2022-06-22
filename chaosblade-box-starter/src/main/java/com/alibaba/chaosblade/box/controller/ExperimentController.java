package com.alibaba.chaosblade.box.controller;

import com.alibaba.chaosblade.box.annotation.LoginUser;
import com.alibaba.chaosblade.box.annotation.SwaggerDoc;
import com.alibaba.chaosblade.box.common.common.domain.PageQueryResponse;
import com.alibaba.chaosblade.box.common.common.domain.Response;
import com.alibaba.chaosblade.box.common.common.domain.experiment.BaseExperimentRequest;
import com.alibaba.chaosblade.box.common.common.domain.experiment.ExperimentRunResult;
import com.alibaba.chaosblade.box.common.common.domain.user.ChaosUser;
import com.alibaba.chaosblade.box.common.common.enums.ExperimentRunModeEnum;
import com.alibaba.chaosblade.box.common.experiment.log.ExperimentOperationLog;
import com.alibaba.chaosblade.box.common.experiment.request.ExperimentAppRiskMessageRequest;
import com.alibaba.chaosblade.box.common.experiment.request.ExperimentPageableQueryRequest;
import com.alibaba.chaosblade.box.common.infrastructure.ScopeTypeSelector;
import com.alibaba.chaosblade.box.common.infrastructure.constant.OsTypeEnum;
import com.alibaba.chaosblade.box.common.infrastructure.constant.PermissionTypes;
import com.alibaba.chaosblade.box.common.infrastructure.domain.experiment.flow.ExperimentActivityInfo;
import com.alibaba.chaosblade.box.common.infrastructure.domain.experiment.flow.MiniFlow;
import com.alibaba.chaosblade.box.common.infrastructure.domain.experiment.flow.MiniFlowGroup;
import com.alibaba.chaosblade.box.common.infrastructure.domain.experiment.request.*;
import com.alibaba.chaosblade.box.common.infrastructure.domain.experiment.response.ExperimentAppRisk;
import com.alibaba.chaosblade.box.common.infrastructure.domain.experiment.response.ExperimentFlowInfo;
import com.alibaba.chaosblade.box.common.infrastructure.domain.experiment.response.ExperimentInfo;
import com.alibaba.chaosblade.box.common.infrastructure.domain.experiment.response.UserExperiment;
import com.alibaba.chaosblade.box.common.infrastructure.exception.ChaosException;
import com.alibaba.chaosblade.box.common.infrastructure.exception.PermissionDeniedException;
import com.alibaba.chaosblade.box.dao.infrastructure.experiment.request.ExperimentCreateRequest;
import com.alibaba.chaosblade.box.dao.infrastructure.experiment.request.ExperimentUpdateRequest;
import com.alibaba.chaosblade.box.model.RestResponseUtil;
import com.alibaba.chaosblade.box.service.ExperimentReadService;
import com.alibaba.chaosblade.box.service.ExperimentWriteService;
import com.alibaba.chaosblade.box.service.auth.perimission.PermissionResult;
import com.alibaba.chaosblade.box.service.auth.perimission.UserPermissionService;
import com.alibaba.chaosblade.box.dao.model.base.PageableResponse;
import com.alibaba.chaosblade.box.service.model.RestResponse;
import com.alibaba.chaosblade.box.service.model.experiment.UserExperimentPageableQueryRequest;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Author haibin
 * Date 2018/12/10
 */
@RestController
@SwaggerDoc
@Slf4j
public class ExperimentController extends BaseController {

	@Resource
	private ExperimentReadService experimentReadService;
	
	@Resource
	private UserPermissionService userPermissionService;
	
	@Autowired
	private ExperimentWriteService experimentWriteService;
	
	@Autowired
	private ScopeTypeSelector scopeTypeSelector;
	
	@ApiOperation(value = "创建新的演练")
	@PostMapping(value = "/CreateExperiment")
	public RestResponse<String> createExperiment(@LoginUser ChaosUser chaosUser,
												 @RequestBody ExperimentCreateRequest experimentCreateRequest) throws ChaosException {
		userPermissionService.checkCreateExperimentPermission(chaosUser, experimentCreateRequest);
		experimentCreateRequest.setUser(chaosUser);
		return RestResponseUtil.initWithServiceResponse(
			experimentWriteService.createExperiment(experimentCreateRequest));
	}
	
	@ApiOperation(value = "更新演练")
	@PostMapping(value = "/UpdateExperiment")
	public RestResponse<Boolean> updateExperiment(@LoginUser ChaosUser chaosUser,
		@RequestBody ExperimentUpdateRequest experimentUpdateRequest) throws ChaosException {
		userPermissionService.checkExperimentPermission(PermissionTypes.W, chaosUser,
			experimentUpdateRequest.getExperimentId(), experimentUpdateRequest.getNamespace(), null);
		Response<Boolean> response = experimentWriteService.updateExperiment(experimentUpdateRequest);
		return RestResponseUtil.initWithServiceResponse(response);
	}
	
	@ApiOperation(value = "克隆一个演练")
	@PostMapping(value = "CloneExperiment")
	public RestResponse<String> cloneExperiment(@LoginUser ChaosUser chaosUser,
		@RequestBody ExperimentCloneRequest experimentCloneRequest) {
		userPermissionService.checkExperimentPermission(PermissionTypes.W, chaosUser,
			experimentCloneRequest.getExperimentId(), experimentCloneRequest.getNamespace(), null);
		experimentCloneRequest.setUser(chaosUser);
		return RestResponseUtil.initWithServiceResponse(
			experimentWriteService.cloneExperiment(experimentCloneRequest)
		);
	}
	
	@ApiOperation(value = "自动运行演练")
	@PostMapping(value = "RunExperiment")
	public RestResponse<ExperimentRunResult> runExperiment(@LoginUser ChaosUser chaosUser,
														   @RequestBody ExperimentRunRequest experimentRunRequest) throws ChaosException {
		userPermissionService.checkExperimentPermission(PermissionTypes.X, chaosUser,
			experimentRunRequest.getExperimentId(), experimentRunRequest.getNamespace(), null);
		experimentRunRequest.setUser(chaosUser);
		return RestResponseUtil.initWithServiceResponse(
			experimentWriteService.runExperiment(chaosUser, experimentRunRequest)
		);
	}
	
	@ApiOperation(value = "获取演练信息")
	@PostMapping(value = "/QueryExperiment")
	public RestResponse<ExperimentInfo> queryExperiment(@LoginUser ChaosUser chaosUser,
														@RequestBody BaseExperimentRequest baseExperimentRequest) throws PermissionDeniedException {
		PermissionResult permission = userPermissionService.checkExperimentPermission(PermissionTypes.R, chaosUser,
			baseExperimentRequest.getExperimentId(), baseExperimentRequest.getNamespace(), null);
		Response<ExperimentInfo> experimentInfoResponse = experimentReadService.getExperiment(baseExperimentRequest);
		if (experimentInfoResponse.isSuccess()) {
			experimentInfoResponse.getResult().setPermission(permission.getPermission());
		}
		return RestResponseUtil.initWithServiceResponse(experimentInfoResponse);
	}
	
	@ApiOperation(value = "删除演练")
	@PostMapping(value = "/DeleteExperiment")
	public RestResponse<Void> deleteExperiment(@LoginUser ChaosUser chaosUser,
		@RequestBody ExperimentDeleteRequest experimentDeleteRequest) throws PermissionDeniedException {
		userPermissionService.checkExperimentDeletePermission(PermissionTypes.W, chaosUser,
			experimentDeleteRequest.getExperimentId(), experimentDeleteRequest.getNamespace(), null);
		return RestResponseUtil.okWithData(
			experimentWriteService.deleteExperiment(experimentDeleteRequest).getResult()
		);
	}
	
	@PostMapping(value = "/PageableQueryUserExperiments")
	public RestResponse<PageQueryResponse<UserExperiment>> pageableQueryUserExperiments(@LoginUser ChaosUser chaosUser,
																						@RequestBody UserExperimentPageableQueryRequest userExperimentPageableQueryRequest) {
		userExperimentPageableQueryRequest.setUser(chaosUser);
		
		//免登下的演练查询
		Response<PageQueryResponse<UserExperiment>> result = userPermissionService.getExperimentListByStsUserLogin(
                chaosUser, userExperimentPageableQueryRequest);
		if (null != result) {
			return RestResponseUtil.initWithServiceResponse(result);
		}
		
		return RestResponseUtil.initWithServiceResponse(
			experimentReadService.searchExperiments(userExperimentPageableQueryRequest)
		);
	}
	
	@PostMapping(value = "/InitMiniFlowByAppCode")
	public RestResponse<Map<String, List<ExperimentActivityInfo>>> initMiniFlowByAppCode(@LoginUser ChaosUser chaosUser,
																						 @RequestBody InitMiniFlowRequest initMiniFlowRequest) {
		return RestResponseUtil.initWithServiceResponse(
			experimentWriteService.initMiniFlowByAppCode(initMiniFlowRequest));
	}
	
	@PostMapping(value = "/ListExperimentOperationLogs")
	public RestResponse<PageableResponse<ExperimentOperationLog>> listExperimentOperationLogs(@LoginUser ChaosUser chaosUser,
																							  @RequestBody ExperimentPageableQueryRequest baseExperimentRequest) {
		userPermissionService.checkExperimentPermission(PermissionTypes.R, chaosUser,
			baseExperimentRequest.getExperimentId(), null, null);
		return RestResponseUtil.initWithServiceResponse(
			experimentReadService.listExperimentOperationLogs(baseExperimentRequest));
	}
	
	@ApiOperation(value = "自动运行演练")
	@PostMapping(value = "PreCheckExperiment")
	public RestResponse<ExperimentRunResult> preCheckExperiment(@LoginUser ChaosUser chaosUser,
		@RequestBody ExperimentRunRequest experimentRunRequest) throws ChaosException {
		userPermissionService.checkExperimentPermission(PermissionTypes.X, chaosUser,
			experimentRunRequest.getExperimentId(), experimentRunRequest.getNamespace(), null);
		return RestResponseUtil.initWithServiceResponse(
			experimentWriteService.runExperiment(chaosUser, experimentRunRequest)
		);
	}

	@ApiOperation(value = "检验高危场景并返回高危提醒")
	@PostMapping(value = "/CheckExperimentAppRiskMessage")
	public RestResponse<List<ExperimentAppRisk>> checkExperimentAppRiskMessage(@LoginUser ChaosUser chaosUser,
																			   @RequestBody ExperimentAppRiskMessageRequest request) {
		return RestResponseUtil.initWithServiceResponse(
			experimentReadService.getExperimentAppRisk(request));
	}

	@ApiOperation(value = "根据appCode初始化演练")
	@PostMapping(value = "/InitExperimentByAppCode")
	public RestResponse<ExperimentInfo> initExperimentByAppCode(@LoginUser ChaosUser chaosUser,
		@RequestBody InitMiniFlowRequest initMiniFlowRequest) {
		Response<Map<String, List<ExperimentActivityInfo>>> response = experimentWriteService.initMiniFlowByAppCode(
			initMiniFlowRequest);
		Map<String, List<ExperimentActivityInfo>> map = response.getResult();
		ExperimentInfo experimentInfo = new ExperimentInfo();
		ExperimentFlowInfo flowInfo = new ExperimentFlowInfo();
		flowInfo.setRunMode(ExperimentRunModeEnum.SEQUENCE);
		List<MiniFlowGroup> miniFlowGroups = new ArrayList<>();
		MiniFlowGroup miniFlowGroup = new MiniFlowGroup();
		miniFlowGroup.setGroupName("Default Group");
		miniFlowGroup.setScopeType(scopeTypeSelector.selectByAppCode(initMiniFlowRequest.getAppCode()));
		miniFlowGroup.setOsType(OsTypeEnum.ofAppCode(initMiniFlowRequest.getAppCode()).getType());
		List<MiniFlow> miniFlows = new ArrayList<>();
		MiniFlow miniFlow = new MiniFlow();
		miniFlow.setAttack(map.getOrDefault("attack", new ArrayList<>()));
		miniFlow.setPrepare(map.getOrDefault("prepare", new ArrayList<>()));
		miniFlow.setCheck(map.getOrDefault("check", new ArrayList<>()));
		miniFlow.setRecover(map.getOrDefault("recover", new ArrayList<>()));
		miniFlows.add(miniFlow);
		miniFlowGroup.setFlows(miniFlows);
		miniFlowGroups.add(miniFlowGroup);
		flowInfo.setFlowGroups(miniFlowGroups);
		experimentInfo.setFlowInfo(flowInfo);
		return RestResponseUtil.okWithData(experimentInfo);
	}

	@ApiOperation(value = "详情页仅修改演练机器")
	@PostMapping(value = "/UpdateExperimentHost")
	public RestResponse<Boolean> updateExperimentHost(@LoginUser ChaosUser chaosUser,
												 @RequestBody ExperimentHostUpdateRequest experimentHostUpdateRequest) throws ChaosException {
		userPermissionService.checkExperimentPermission(PermissionTypes.W, chaosUser,
				experimentHostUpdateRequest.getExperimentId(), experimentHostUpdateRequest.getNamespace(), null);
		return RestResponseUtil.initWithServiceResponse(
				experimentWriteService.updateExperimentHost(experimentHostUpdateRequest));
	}


}
