package com.alibaba.chaosblade.box.service.command.experiment;

import com.alibaba.chaosblade.box.common.app.sdk.constants.PhaseType;
import com.alibaba.chaosblade.box.common.app.sdk.scope.Host;
import com.alibaba.chaosblade.box.common.commands.SpringBeanCommand;
import com.alibaba.chaosblade.box.common.common.constant.ChaosConstant;
import com.alibaba.chaosblade.box.common.common.domain.Response;
import com.alibaba.chaosblade.box.common.common.domain.definition.ExperimentActivityDefinition;
import com.alibaba.chaosblade.box.common.common.domain.user.ChaosUser;
import com.alibaba.chaosblade.box.common.common.enums.CommonErrorCode;
import com.alibaba.chaosblade.box.common.common.enums.ExperimentRunModeEnum;
import com.alibaba.chaosblade.box.common.common.enums.ExperimentStateEnum;
import com.alibaba.chaosblade.box.common.common.util.MiniAppUtils;
import com.alibaba.chaosblade.box.dao.infrastructure.experiment.definition.ActivityDefinitionChecker;
import com.alibaba.chaosblade.box.dao.infrastructure.checker.ExperimentChecker;
import com.alibaba.chaosblade.box.dao.infrastructure.checker.definition.BaseMiniGroupCheckNode;
import com.alibaba.chaosblade.box.dao.infrastructure.checker.definition.MiniGroupCheckContext;
import com.alibaba.chaosblade.box.dao.infrastructure.convertor.ArgumentDefinitionConvertor;
import com.alibaba.chaosblade.box.dao.infrastructure.experiment.definition.ExperimentFlowDefinitionManager;
import com.alibaba.chaosblade.box.dao.infrastructure.experiment.interceptor.StableProbeInterceptor;
import com.alibaba.chaosblade.box.dao.infrastructure.model.ExperimentDefinitionContext;
import com.alibaba.chaosblade.box.common.experiment.task.flow.ActivityTaskExecutionAttributes;
import com.alibaba.chaosblade.box.common.infrastructure.SceneArgumentGradeConverter;
import com.alibaba.chaosblade.box.common.infrastructure.chain.ChainFactory;
import com.alibaba.chaosblade.box.common.infrastructure.constant.ExperimentAttributes;
import com.alibaba.chaosblade.box.common.infrastructure.constant.HostSelectTypes;
import com.alibaba.chaosblade.box.common.infrastructure.domain.Hosts;
import com.alibaba.chaosblade.box.common.infrastructure.domain.experiment.SceneArgumentDefinition;
import com.alibaba.chaosblade.box.common.infrastructure.domain.experiment.flow.ExperimentActivityInfo;
import com.alibaba.chaosblade.box.common.infrastructure.domain.experiment.flow.MiniFlow;
import com.alibaba.chaosblade.box.common.infrastructure.domain.experiment.flow.MiniFlowGroup;
import com.alibaba.chaosblade.box.common.infrastructure.domain.experiment.guard.ExperimentGuard;
import com.alibaba.chaosblade.box.common.infrastructure.domain.experiment.guard.ExperimentGuardArgument;
import com.alibaba.chaosblade.box.dao.infrastructure.experiment.request.ExperimentDefinitionRequest;
import com.alibaba.chaosblade.box.common.infrastructure.util.CollectionUtil;
import com.alibaba.chaosblade.box.dao.infrastructure.event.ChaosEventDispatcher;
import com.alibaba.chaosblade.box.dao.infrastructure.tunnel.TransactionUtil;
import com.alibaba.chaosblade.box.dao.model.*;
import com.alibaba.chaosblade.box.dao.repository.*;
import com.alibaba.chaosblade.box.dao.infrastructure.application.ApplicationRelationBinder;
import com.alibaba.fastjson.JSON;
import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;

import java.util.ArrayList;
import java.util.List;

/**
 * @author haibin
 *
 * 
 */
@Slf4j
public abstract class AbstractExperimentDefinitionCommand
		extends SpringBeanCommand<ExperimentDefinitionRequest, Response<Void>> implements InitializingBean {

	@Autowired
	protected ExperimentChecker experimentChecker;

	@Autowired
	protected ChaosEventDispatcher chaosEventDispatcher;

	@Autowired
	protected TransactionUtil transactionUtil;

	@Autowired
	protected ExperimentGuardRepository experimentGuardRepository;

	@Autowired
	protected ExperimentMiniFlowRepository experimentMiniFlowRepository;

	@Autowired
	protected ExperimentMiniFlowGroupRepository experimentMiniFlowGroupRepository;

	@Autowired
	protected ExperimentFlowDefinitionManager experimentFlowDefinitionManager;

	@Autowired
	private ExperimentRepository experimentRepository;

	@Autowired
	private ArgumentDefinitionConvertor argumentDefinitionConvertor;

	@Autowired
	private List<ActivityDefinitionChecker> activityDefinitionCheckers;

	@Autowired
	private ExperimentRelationRepository experimentRelationRepository;

	@Autowired
	private ApplicationRelationBinder applicationRelationBinder;

	private BaseMiniGroupCheckNode miniGroupCheckNode;

	@Autowired
	private ApplicationContext applicationContext;

	@Autowired
	private List<StableProbeInterceptor> stableProbeInterceptors;

	@Autowired
	private SceneArgumentGradeConverter sceneArgumentGradeConverter;

	@Override
	public Response<Void> execute(ExperimentDefinitionRequest experimentFlowDefinitionRequest) {
		Response<ExperimentDO> experimentDOResponse = getExperimentDO(
				experimentFlowDefinitionRequest);
		if (!experimentDOResponse.isSuccess()) {
			return Response.failedWith(experimentDOResponse.getError());
		}
		ExperimentDO experimentDO = experimentDOResponse.getResult();
		try {
			//先给演练打上2.0标记
			setNewFlowTag(experimentDO);
			ExperimentDefinitionContext experimentDefinitionContext = new ExperimentDefinitionContext();
			experimentDefinitionContext.setExperimentDO(experimentDO);
			experimentDefinitionContext.setExperimentDefinitionRequest(experimentFlowDefinitionRequest);
			//先处理微流程组
			handleMiniFlowGroups(experimentDO, experimentDefinitionContext,
					experimentFlowDefinitionRequest.getFlowGroups());
			//处理全局守护节点
			if (experimentFlowDefinitionRequest.getGuardConf() != null) {
				handleGuards(experimentDO, experimentFlowDefinitionRequest.getGuardConf().getGuards());
			}
			//临时处理稳态开关，当创建人是考拉用户并且演练场景为kill pod时自动添加
			handleStableProbe(experimentFlowDefinitionRequest);
			//处理演练对象
			handleExperiment(experimentFlowDefinitionRequest, experimentDO, experimentDefinitionContext);
			//处理专家经验
			handleExpertise(experimentDO, experimentFlowDefinitionRequest);
			//处理应用关系
			handleApplicationRelations(experimentDefinitionContext);
			//事件发送
			fireEvents(experimentFlowDefinitionRequest.getUser(), experimentDO);
			return Response.ok();
		} catch (Exception ex) {
			log.error("save experiment definition failed", ex);
			return Response.failedWith(
					throwableChaosErrorWrappers.wrapper(ex, CommonErrorCode.B_SAVE_EXPERIMENT_DEFINITION));
		}
	}

	private void handleExperiment(ExperimentDefinitionRequest experimentFlowDefinitionRequest,
								  ExperimentDO experimentDO,
								  ExperimentDefinitionContext experimentDefinitionContext) {
		ExperimentRunModeEnum experimentRunModeEnum;
		if (experimentFlowDefinitionRequest.getRunMode() == null) {
			experimentRunModeEnum = ExperimentRunModeEnum.PHASE;
		} else {
			experimentRunModeEnum = experimentFlowDefinitionRequest.getRunMode();
		}
		experimentDO.setRunMode(experimentRunModeEnum);
		if (experimentDO.isNotReady()) {
			experimentDO.setState(ExperimentStateEnum.READY.getValue());
		}
		//设置持续时长
		experimentDO.setMiniAppDesc(JSON.toJSONString(experimentDefinitionContext.getAppCodes()));
		experimentDO.setSchedulerConfig(experimentFlowDefinitionRequest.getSchedulerConfig());
		experimentDO.setDuration(experimentFlowDefinitionRequest.getDuration());
		experimentRepository.update(experimentDO);
	}

	private void handleMiniFlowGroups(ExperimentDO experimentDO,
									  ExperimentDefinitionContext experimentDefinitionContext,
									  List<MiniFlowGroup> miniFlowGroups) throws Exception {
		deleteExtraGroups(experimentDefinitionContext.getExperimentDefinitionRequest());
		setOrderForActivities(miniFlowGroups);
		int order = 0;
		for (MiniFlowGroup miniFlowGroup : miniFlowGroups) {
			miniFlowGroup.setOrder(order);
			experimentDefinitionContext.addApplication(miniFlowGroup.getAppName(), miniFlowGroup.getAppId());
			miniGroupCheckNode.invoke(new MiniGroupCheckContext(miniFlowGroup, experimentDefinitionContext));
			handleMiniFlowGroup(experimentDefinitionContext, experimentDO, miniFlowGroup);
			order++;
		}
		saveExperimentActivityDefinitions(experimentDefinitionContext);
	}

	protected void handleApplicationRelations(ExperimentDefinitionContext experimentDefinitionContext) {
		if (!experimentDefinitionContext.getApplications().isEmpty()) {
			applicationRelationBinder.addOrUpdateApplicationExperimentRelations(
					experimentDefinitionContext.getExperimentDO().getExperimentId(),
					experimentDefinitionContext.getApplications());
		}
	}

	protected abstract void fireEvents(ChaosUser user, ExperimentDO experimentDO);

	protected void handleExpertise(ExperimentDO experimentDO,
								   ExperimentDefinitionRequest experimentDefinitionRequest) {
		if (Strings.isNullOrEmpty(experimentDefinitionRequest.getExpertiseId())) {
			return;
		}
		ExperimentRelationDO experimentRelationDO = experimentRelationRepository
				.findByExperimentIdAndOuterIdAndRelationType(experimentDO.getExperimentId(),
						experimentDefinitionRequest.getExpertiseId(), ExperimentRelationDO.RELATION_TYPE_EXPERTISE);
		if (experimentRelationDO == null) {
			experimentRelationDO = new ExperimentRelationDO();
			experimentRelationDO.setExperimentId(experimentDO.getExperimentId());
			experimentRelationDO.setOuterId(experimentDefinitionRequest.getExpertiseId());
			experimentRelationDO.setRelationType(ExperimentRelationDO.RELATION_TYPE_EXPERTISE);
			experimentRelationRepository.add(experimentRelationDO);
		}

	}

	protected abstract void saveExperimentActivityDefinitions(
			ExperimentDefinitionContext experimentDefinitionContext);

	protected void deleteExtraGroups(ExperimentDefinitionRequest experimentDefinitionRequest) {

	}

	protected Response<ExperimentDO> getExperimentDO(ExperimentDefinitionRequest experimentDefinitionRequest) {
		if (experimentDefinitionRequest.getExperimentDO() != null) {
			return Response.okWithData(experimentDefinitionRequest.getExperimentDO());
		}
		Response<ExperimentDO> experimentDOResponse = experimentChecker.assertExperimentExist(
				experimentDefinitionRequest.getExperimentId());
		if (!experimentDOResponse.isSuccess()) {
			return Response.failedWith(experimentDOResponse.getError());
		}
		return experimentDOResponse;
	}

	protected Hosts extraHostsFromMiniFlowGroup(MiniFlowGroup miniFlowGroup) {
		Hosts hosts = new Hosts();
		hosts.setContent(miniFlowGroup.getHosts());
		hosts.setScopeType(miniFlowGroup.getScopeType());
		hosts.setAppName(miniFlowGroup.getAppName());
		hosts.setAppType(miniFlowGroup.getAppType());
		hosts.setAppGroups(miniFlowGroup.getAppGroups());
		hosts.setAppId(miniFlowGroup.getAppId());
		if (null != miniFlowGroup.getSelectType()) {
			hosts.setSelectType(miniFlowGroup.getSelectType());
		} else {
			//默认为IP方式，兼容旧数据和openapi
			hosts.setSelectType(HostSelectTypes.SELECT_TYPE_IP);
		}
		hosts.setHostPercent(miniFlowGroup.getHostPercent());
		hosts.setCloudServiceType(miniFlowGroup.getCloudServiceType());
		hosts.setCloudServiceName(miniFlowGroup.getCloudServiceName());
		hosts.setOsType(miniFlowGroup.getOsType());
		return hosts;
	}

	private void setOrderForActivities(
			List<MiniFlowGroup> miniFlowGroups) {
		int count = 0;
		for (MiniFlowGroup miniFlowGroup : miniFlowGroups) {
			for (MiniFlow miniFlow : miniFlowGroup.getFlows()) {
				if (miniFlow.getPrepare() != null) {
					for (ExperimentActivityInfo experimentActivityInfo : miniFlow.getPrepare()) {
						experimentActivityInfo.setOrder(count++);
					}
				}
				if (miniFlow.getAttack() != null) {
					for (ExperimentActivityInfo experimentActivityInfo : miniFlow.getAttack()) {
						experimentActivityInfo.setOrder(count++);
					}
				}
				if (miniFlow.getCheck() != null) {
					for (ExperimentActivityInfo experimentActivityInfo : miniFlow.getCheck()) {
						experimentActivityInfo.setOrder(count++);
					}
				}
				if (miniFlow.getRecover() != null) {
					for (ExperimentActivityInfo experimentActivityInfo : miniFlow.getRecover()) {
						experimentActivityInfo.setOrder(count++);
					}
				}
			}
		}
	}

	/**
	 * 创建演练流程定义
	 */
	void fillExperimentFlowDefinition(ExperimentDefinitionContext experimentDefinitionContext,
									  MiniFlowGroup miniFlowGroup, MiniFlowGroupDO miniFlowGroupDO) {
		//处理微流程
		int order = 0;
		for (MiniFlow miniFlow : miniFlowGroup.getFlows()) {
			miniFlow.setOrder(order);
			MiniFlowDO miniFlowDO = addMiniFlowIfNotExist(miniFlow, miniFlowGroupDO);
			String miniFlowId = miniFlowDO.getFlowId();
			String attackFlowName = miniFlow.getAttack().get(0).getActivityName();
			//将所有微流程里面不同阶段的步骤聚合在一起
			experimentDefinitionContext.addDefinitions(
					PhaseType.ATTACK,
					convertNewExperimentActivityDefinition(experimentDefinitionContext, miniFlow.getAttack(), miniFlowId,
							miniFlowGroup.getHosts()
					)
			);
			experimentDefinitionContext.addDefinitions(
					PhaseType.PREPARE,
					convertNewExperimentActivityDefinition(experimentDefinitionContext, miniFlow.getPrepare(), miniFlowId,
							miniFlowGroup.getHosts()
					)
			);
			experimentDefinitionContext.addDefinitions(
					PhaseType.CHECK,
					convertNewExperimentActivityDefinition(experimentDefinitionContext, miniFlow.getCheck(), miniFlowId,
							miniFlowGroup.getHosts()
					)
			);
			experimentDefinitionContext.addDefinitions(
					PhaseType.RECOVER,
					convertNewExperimentActivityDefinition(experimentDefinitionContext,
							miniFlow.getRecover(), miniFlowId, miniFlowGroup.getHosts(), attackFlowName
					)
			);
			order++;
		}
	}

	/**
	 * 处理微流程
	 */
	protected abstract void handleMiniFlowGroup(ExperimentDefinitionContext experimentDefinitionContext,
												ExperimentDO experimentDO, MiniFlowGroup miniFlowGroup);

	/**
	 * 处理全局节点
	 */
	protected void handleGuards(ExperimentDO experimentDO, List<ExperimentGuard> experimentGuards) {
		if (experimentGuards == null) { return; }
		for (ExperimentGuard experimentGuard : experimentGuards) {
			addGuard(experimentDO, experimentGuard);
		}
	}

	/**
	 * 增加全局节点
	 */
	protected void addGuard(ExperimentDO experimentDO, ExperimentGuard experimentGuard) {
		Preconditions.checkArgument(experimentGuard.getActionType() != null, "actionType not null");
		ExperimentGuardDO experimentGuardDO = new ExperimentGuardDO();
		experimentGuardDO.setAppCode(experimentGuard.getAppCode());
		experimentGuardDO.setActionType(experimentGuard.getActionType());
		experimentGuardDO.setExperimentId(experimentDO.getExperimentId());
		ExperimentGuardArgument experimentGuardArgument = new ExperimentGuardArgument();
		experimentGuardArgument.setArguments(experimentGuard.getArguments());
		experimentGuardArgument.setFields(experimentGuard.getFields());
		experimentGuardArgument.setTolerance(experimentGuard.getTolerance());
		experimentGuardDO.setArgument(experimentGuardArgument);
		experimentGuardDO.setName(experimentGuard.getName());
		experimentGuardDO.setRequired(experimentGuard.isRequired());
		List<SceneArgumentDefinition> sceneArgumentDefinitions = new ArrayList<>();
		if (experimentGuard.getArguments() != null) {
			sceneArgumentDefinitions.addAll(experimentGuard.getArguments());
		}
		if (experimentGuard.getFields() != null) {
			sceneArgumentDefinitions.addAll(experimentGuard.getFields());
		}
		if (experimentGuard.getTolerance() != null) {
			sceneArgumentDefinitions.addAll(experimentGuard.getTolerance());
		}
		experimentGuardRepository.add(experimentGuardDO);
	}

	/**
	 * 添加微流程组
	 */
	protected void addMiniGroup(ExperimentDefinitionContext experimentDefinitionContext, ExperimentDO experimentDO,
								MiniFlowGroup miniFlowGroup) {
		String experimentId = experimentDO.getExperimentId();
		MiniFlowGroupDO miniFlowGroupDO = new MiniFlowGroupDO();
		miniFlowGroupDO.setGroupName(miniFlowGroup.getGroupName());
		miniFlowGroupDO.setGroupOrder(miniFlowGroup.getOrder());
		miniFlowGroupDO.setExperimentId(experimentId);
		miniFlowGroupDO.setRequired(miniFlowGroup.isRequired());
		miniFlowGroupDO.setHosts(extraHostsFromMiniFlowGroup(miniFlowGroup));
		miniFlowGroupDO.setGroupId(miniFlowGroup.getGroupId());
		experimentMiniFlowGroupRepository.add(miniFlowGroupDO);
		fillExperimentFlowDefinition(
				experimentDefinitionContext,
				miniFlowGroup,
				miniFlowGroupDO
		);
	}


	protected MiniFlowDO addMiniFlowIfNotExist(MiniFlow miniFlow, MiniFlowGroupDO miniFlowGroupDO) {
		MiniFlowDO miniFlowDO = null;
		if (!Strings.isNullOrEmpty(miniFlow.getFlowId())) {
			miniFlowDO = experimentMiniFlowRepository.findById(miniFlow.getFlowId()).orElse(null);
		}
		if (miniFlowDO == null) {
			miniFlowDO = new MiniFlowDO();
			miniFlowDO.setFlowId(miniFlow.getFlowId());
			miniFlowDO.setFlowOrder(miniFlow.getOrder());
			miniFlowDO.setGroupId(miniFlowGroupDO.getGroupId());
			miniFlowDO.setExperimentId(miniFlowGroupDO.getExperimentId());
			miniFlowDO.setName(miniFlow.getName());
			miniFlowDO.setGroupOrder(miniFlowGroupDO.getGroupOrder());
			miniFlowDO.setRequired(miniFlow.getRequired());
			experimentMiniFlowRepository.add(miniFlowDO);
		} else {
			miniFlowDO.setFlowOrder(miniFlow.getOrder());
			miniFlowDO.setName(miniFlow.getName());
			miniFlowDO.setGroupOrder(miniFlowGroupDO.getGroupOrder());
			experimentMiniFlowRepository.update(miniFlowDO);
		}
		return miniFlowDO;
	}

	/**
	 * 流程节点要和微流程去绑定
	 */
	protected List<ExperimentActivityDefinition> convertNewExperimentActivityDefinition(
			ExperimentDefinitionContext experimentDefinitionContext,
			List<ExperimentActivityInfo> experimentActivityDefinitions, String flowId, List<Host> hosts) {
		return convertNewExperimentActivityDefinition(experimentDefinitionContext, experimentActivityDefinitions,
				flowId, hosts, null);
	}

	/**
	 * 流程节点要和微流程去绑定
	 */
	protected List<ExperimentActivityDefinition> convertNewExperimentActivityDefinition(
			ExperimentDefinitionContext experimentDefinitionContext,
			List<ExperimentActivityInfo> experimentActivityDefinitions, String flowId, List<Host> hosts,
			String attackName) {
		if (CollectionUtil.isNullOrEmpty(experimentActivityDefinitions)) { return new ArrayList<>(); }
		List<ExperimentActivityDefinition> result = new ArrayList<>();
		for (ExperimentActivityInfo experimentActivityDefinition : experimentActivityDefinitions) {
			experimentActivityDefinition.setFlowId(flowId);
			experimentActivityDefinition.setScope(hosts);
			if (!Strings.isNullOrEmpty(attackName) && MiniAppUtils.isChaosRecovery(
					experimentActivityDefinition.getAppCode())) {
				if (experimentDefinitionContext.getExperimentDefinitionRequest().getLang().equals(ChaosConstant.LANGUAGE_EN)) {
					experimentActivityDefinition.setActivityName("recovery(" + attackName + ")");
				}else {
					experimentActivityDefinition.setActivityName("恢复(" + attackName + ")");
				}

			}
			ExperimentActivityDefinition temp = new ExperimentActivityDefinition();
			BeanUtils.copyProperties(experimentActivityDefinition, temp, "arguments");
			temp.setArguments(
					argumentDefinitionConvertor.mergeArgumentDefinitions(
							experimentActivityDefinition.getAppCode(),
							sceneArgumentGradeConverter.tranToArgumentsList(experimentActivityDefinition.getArguments())
					));
			if (!experimentDefinitionContext.getExperimentDefinitionRequest().isOldAdapter()) {
				for (ActivityDefinitionChecker activityDefinitionChecker : activityDefinitionCheckers) {
					activityDefinitionChecker.check(temp, false);
				}
			}
			experimentDefinitionContext.addAppCode(experimentActivityDefinition.getAppCode());
			result.add(temp);
		}
		return result;
	}

	protected void handleStableProbe(ExperimentDefinitionRequest experimentDefinitionRequest) {
		stableProbeInterceptors.forEach(stableProbeInterceptor -> {
			stableProbeInterceptor.handleStableProbe(experimentDefinitionRequest);
		});
	}

	/**
	 * 给演练打上2.0标记
	 * @param experimentDO
	 */
	protected void setNewFlowTag(ExperimentDO experimentDO) {
		experimentDO.putAttribute(ExperimentAttributes.ATTR_NEW_DEFINITION_FLOW,
				ActivityTaskExecutionAttributes.ATTRIBUTE_VALUE_TRUE);
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		this.miniGroupCheckNode = ChainFactory.createSpringChain(applicationContext,
				BaseMiniGroupCheckNode.class);
	}
}
