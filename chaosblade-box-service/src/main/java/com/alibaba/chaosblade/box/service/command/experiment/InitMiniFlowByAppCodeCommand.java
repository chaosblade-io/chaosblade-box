package com.alibaba.chaosblade.box.service.command.experiment;

import com.alibaba.chaosblade.box.common.app.sdk.constants.PhaseType;
import com.alibaba.chaosblade.box.common.commands.SpringBeanCommand;
import com.alibaba.chaosblade.box.common.common.constant.ChaosConstant;
import com.alibaba.chaosblade.box.common.common.constant.ChaosFunctionConstant;
import com.alibaba.chaosblade.box.common.common.domain.Response;
import com.alibaba.chaosblade.box.common.common.enums.CommonErrorCode;
import com.alibaba.chaosblade.box.common.common.util.MiniAppUtils;
import com.alibaba.chaosblade.box.dao.infrastructure.app.function.SceneFunctionNameParser;
import com.alibaba.chaosblade.box.dao.infrastructure.convertor.ArgumentDefinitionConvertor;
import com.alibaba.chaosblade.box.common.infrastructure.SceneArgumentGradeConverter;
import com.alibaba.chaosblade.box.common.infrastructure.domain.experiment.flow.ExperimentActivityInfo;
import com.alibaba.chaosblade.box.common.infrastructure.domain.experiment.request.InitMiniFlowRequest;
import com.alibaba.chaosblade.box.common.infrastructure.util.CollectionUtil;
import com.alibaba.chaosblade.box.dao.model.SceneFunctionDO;
import com.alibaba.chaosblade.box.common.infrastructure.domain.scene.SceneFunctionDependency;
import com.alibaba.chaosblade.box.dao.model.SceneFunctionParameterDO;
import com.alibaba.chaosblade.box.dao.repository.SceneFunctionParameterRepository;
import com.alibaba.chaosblade.box.dao.repository.SceneFunctionRepository;
import com.alibaba.chaosblade.box.dao.infrastructure.app.SceneFunctionUpdateInternalOperator;
import com.alibaba.chaosblade.box.dao.infrastructure.app.function.SceneSynchronousHelper;
import com.google.common.base.Strings;
import com.google.common.collect.Sets;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author haibin
 *
 * 
 */
@Component
public class InitMiniFlowByAppCodeCommand
		extends SpringBeanCommand<InitMiniFlowRequest, Response<Map<String, List<ExperimentActivityInfo>>>> {

	@Autowired
	private SceneFunctionRepository sceneFunctionRepository;

	@Autowired
	private SceneFunctionParameterRepository sceneFunctionParameterRepository;

	@Autowired
	private ArgumentDefinitionConvertor argumentDefinitionConvertor;

	@Autowired
	private List<InitMiniFlowByAppCodeInterceptor> initMiniFlowByAppCodeInterceptors;

	@Autowired
	private SceneFunctionUpdateInternalOperator sceneFunctionUpdateInternalOperator;

	@Autowired
	private SceneSynchronousHelper sceneSynchronousHelper;

	@Autowired
	private SceneArgumentGradeConverter sceneArgumentGradeConverter;

	@Autowired
	private SceneFunctionNameParser sceneFunctionNameParser;

	public InitMiniFlowByAppCodeCommand() {
	}

	private static String lang = "";

	@Override
	public Response<Map<String, List<ExperimentActivityInfo>>> execute(
			InitMiniFlowRequest initMiniFlowRequest) {
		String appCode = initMiniFlowRequest.getAppCode();
		SceneFunctionDO sceneFunctionDO = resetFunctionAppCode(
				appCode);
		if (sceneFunctionDO == null) {
			return Response.failedWith(CommonErrorCode.B_MINIAPP_NOT_EXIST, appCode);
		}
		if (!sceneFunctionDO.isSupport(ChaosFunctionConstant.PHASE_FLAG_ATTACK)) {
			return Response.failedWith(CommonErrorCode.B_ONLY_SUPPORT_ATTACK);
		}
		lang = initMiniFlowRequest.getLang();

		Map<String, List<ExperimentActivityInfo>> phaseToActivities = new HashMap<>();
		initAttachActivity(initMiniFlowRequest, sceneFunctionDO, phaseToActivities);
		fillDependencies(initMiniFlowRequest, appCode, sceneFunctionDO, phaseToActivities);
		sortActivities(phaseToActivities);
		doInterceptor(initMiniFlowRequest, sceneFunctionDO, phaseToActivities);
		return Response.okWithData(phaseToActivities);
	}

	private void doInterceptor(InitMiniFlowRequest initMiniFlowRequest, SceneFunctionDO sceneFunctionDO,
							   Map<String, List<ExperimentActivityInfo>> phaseToActivities) {
		InitMiniFlowByAppCodeInterceptorContext initMiniFlowByAppCodeInterceptorContext
				= new InitMiniFlowByAppCodeInterceptorContext();
		initMiniFlowByAppCodeInterceptorContext.setInitMiniFlowRequest(initMiniFlowRequest);
		initMiniFlowByAppCodeInterceptorContext.setResponse(phaseToActivities);
		initMiniFlowByAppCodeInterceptorContext.setAppCode(sceneFunctionDO.getCode());
		checkChaosBladeRecoveryAttack(initMiniFlowByAppCodeInterceptorContext);
		for (InitMiniFlowByAppCodeInterceptor initMiniFlowByAppCodeInterceptor :
				initMiniFlowByAppCodeInterceptors) {
			initMiniFlowByAppCodeInterceptor.afterInit(initMiniFlowByAppCodeInterceptorContext);
		}
	}

	private void sortActivities(Map<String, List<ExperimentActivityInfo>> phaseToActivities) {
		phaseToActivities.forEach((key, value) -> value.sort((o1, o2) -> {
			Integer order1 = o1.getOrder() == null ? 0 : o1.getOrder();
			Integer order2 = o2.getOrder() == null ? 0 : o2.getOrder();
			return order1.compareTo(order2);
		}));
	}

	private void fillDependencies(InitMiniFlowRequest initMiniFlowRequest, String appCode,
								  SceneFunctionDO sceneFunctionDO,
								  Map<String, List<ExperimentActivityInfo>> phaseToActivities) {
		List<SceneFunctionDependency> sceneFunctionDependencies = sceneFunctionDO.getDependencyList();
		handleDependencies(sceneFunctionDependencies, phaseToActivities, Sets.newHashSet(appCode),
				initMiniFlowRequest);
	}

	private void initAttachActivity(InitMiniFlowRequest initMiniFlowRequest, SceneFunctionDO sceneFunctionDO,
									Map<String, List<ExperimentActivityInfo>> phaseToActivities) {
		List<ExperimentActivityInfo> attacks = phaseToActivities.computeIfAbsent(
				PhaseType.ATTACK.name().toLowerCase(),
				phaseType -> new ArrayList<>());
		ExperimentActivityInfo experimentActivityInfo = createBySceneFunctionDO(sceneFunctionDO,
				initMiniFlowRequest);
		experimentActivityInfo.setRequired(true);
		attacks.add(experimentActivityInfo);
	}

	private SceneFunctionDO resetFunctionAppCode(String appCode) {
		if (Strings.isNullOrEmpty(appCode)) {
			return null;
		}
		String defaultCode = sceneSynchronousHelper.getHierarchyDefaultCode(appCode);
		SceneFunctionDO sceneFunctionDO = sceneFunctionRepository.findByCode(appCode).orElse(null);
		SceneFunctionDO defaultScene = sceneFunctionRepository.findByCode(defaultCode).orElse(null);

		if (defaultScene != null) {
			sceneFunctionDO.setParameters(defaultScene.getParameters());
			sceneFunctionDO.setCode(defaultScene.getCode());
			sceneFunctionDO.setDependencyList(defaultScene.getDependencyList());
		}
		return sceneFunctionDO;
	}

	private void handleDependencies(List<SceneFunctionDependency> sceneFunctionDependencies,
									Map<String, List<ExperimentActivityInfo>> result, Set<String> appCodes,
									InitMiniFlowRequest initMiniFlowRequest) {
		if (CollectionUtil.isNullOrEmpty(sceneFunctionDependencies)) { return; }
		SceneFunctionDO sceneFunctionDO = null;
		for (SceneFunctionDependency sceneFunctionDependency : sceneFunctionDependencies) {
			sceneFunctionDO = sceneFunctionRepository.findByCode(sceneFunctionDependency.getCode()).orElse(null);
			if (sceneFunctionDO == null || !sceneFunctionDO.getEnabled().equals(ChaosFunctionConstant.ENABLED_ONLINE)) {
				continue;
			}
			ExperimentActivityInfo experimentActivityInfo = createBySceneFunctionDO(sceneFunctionDO,
					initMiniFlowRequest);
			experimentActivityInfo.setRequired(sceneFunctionDependency.isRequired());
			appCodes.add(sceneFunctionDO.getCode());
			result.computeIfAbsent(PhaseType.ofType(sceneFunctionDependency.getPhase()).name().toLowerCase(),
					phaseType -> new ArrayList<>()).add(experimentActivityInfo);
			List<SceneFunctionDependency> secondSceneFunctionDependencies =
					sceneFunctionDO.getDependencyList().stream()
							.filter(sceneFunctionDependency1 -> !appCodes.contains(sceneFunctionDependency1.getCode())).collect(
							Collectors.toList());
			handleDependencies(secondSceneFunctionDependencies, result, appCodes, initMiniFlowRequest);
		}
	}

	/**
	 * 防止恢复节点已经下架
	 *
	 * @param initMiniFlowByAppCodeInterceptorContext
	 */
	public void checkChaosBladeRecoveryAttack(
			InitMiniFlowByAppCodeInterceptorContext initMiniFlowByAppCodeInterceptorContext) {
		if (!MiniAppUtils.isFromChaosBlade(
				initMiniFlowByAppCodeInterceptorContext.getAppCode()) &&
				!MiniAppUtils.isFromLitmusChaos(
						initMiniFlowByAppCodeInterceptorContext.getAppCode())) {
			return;
		}
		Map<String, List<ExperimentActivityInfo>> response = initMiniFlowByAppCodeInterceptorContext.getResponse();
		String appCode = initMiniFlowByAppCodeInterceptorContext.getAppCode();
		List<ExperimentActivityInfo> experimentActivityInfos = response.computeIfAbsent(
				PhaseType.RECOVER.name().toLowerCase(), s -> new ArrayList<>());
		String recoveryCode = MiniAppUtils.getRecoverCode(appCode);
		boolean existRecovery = experimentActivityInfos.stream().anyMatch(
				experimentActivityInfo -> Objects.equals(experimentActivityInfo.getAppCode(), recoveryCode));
		if (!existRecovery) {
			sceneFunctionRepository.findByCode(recoveryCode).ifPresent(sceneFunctionDO -> {
				ExperimentActivityInfo recovery = createBySceneFunctionDO(sceneFunctionDO,
						initMiniFlowByAppCodeInterceptorContext.getInitMiniFlowRequest());
				experimentActivityInfos.add(recovery);
				sceneFunctionDO.setEnabled(ChaosFunctionConstant.ENABLED_ONLINE);
				sceneFunctionUpdateInternalOperator.updateSceneFunction(sceneFunctionDO);
			});
		}
	}

	private ExperimentActivityInfo createBySceneFunctionDO(SceneFunctionDO sceneFunctionDO,
														   InitMiniFlowRequest initMiniFlowRequest) {
		if (Strings.isNullOrEmpty(lang) || lang.equals(ChaosConstant.LANGUAGE_ZH)) {
			sceneFunctionNameParser.parseSceneFunctionOne(sceneFunctionDO);
		}

		ExperimentActivityInfo experimentActivityInfo = new ExperimentActivityInfo();
		experimentActivityInfo.setActivityName(sceneFunctionDO.getName());
		experimentActivityInfo.setAppCode(sceneFunctionDO.getCode());
		List<SceneFunctionParameterDO> sceneFunctionParameterDOS = sceneFunctionParameterRepository
				.findFilterParamsByFunctionId(
						sceneFunctionDO);
		experimentActivityInfo.setArguments(sceneArgumentGradeConverter.tranToArgumentsGrade(argumentDefinitionConvertor
				.convertToArgumentDefinitions(sceneFunctionDO.getCode(), sceneFunctionParameterDOS,
						initMiniFlowRequest.getSource().equals(InitMiniFlowRequest.SOURCE_APP))));
		if (MiniAppUtils.isChaosUninstall(sceneFunctionDO.getCode())) {
			experimentActivityInfo.setOrder(Integer.MAX_VALUE);
		}
		if (MiniAppUtils.isChaosRecovery(sceneFunctionDO.getCode())) {
			experimentActivityInfo.setOrder(Integer.MAX_VALUE - 1);
		}

		experimentActivityInfo.setUserCheck(true);
		return experimentActivityInfo;
	}

}
