package com.alibaba.chaosblade.box.service.command.experiment;

import com.alibaba.chaosblade.box.common.app.sdk.constants.PhaseType;
import com.alibaba.chaosblade.box.common.app.sdk.scope.Host;
import com.alibaba.chaosblade.box.common.app.sdk.scope.Scope;
import com.alibaba.chaosblade.box.common.commands.SpringBeanCommand;
import com.alibaba.chaosblade.box.common.common.domain.Response;
import com.alibaba.chaosblade.box.common.common.enums.CommonErrorCode;
import com.alibaba.chaosblade.box.common.common.enums.ExperimentRunModeEnum;
import com.alibaba.chaosblade.box.dao.infrastructure.experiment.task.ActivityQueryManager;
import com.alibaba.chaosblade.box.dao.infrastructure.convertor.ArgumentDefinitionConvertor;
import com.alibaba.chaosblade.box.dao.infrastructure.experiment.definition.ExperimentFlowDefinitionManager;
import com.alibaba.chaosblade.box.common.infrastructure.SceneArgumentGradeConverter;
import com.alibaba.chaosblade.box.common.infrastructure.constant.HostSelectTypes;
import com.alibaba.chaosblade.box.common.infrastructure.domain.Hosts;
import com.alibaba.chaosblade.box.common.infrastructure.domain.experiment.flow.ExperimentActivityInfo;
import com.alibaba.chaosblade.box.common.infrastructure.domain.experiment.flow.MiniFlow;
import com.alibaba.chaosblade.box.common.infrastructure.domain.experiment.flow.MiniFlowGroup;
import com.alibaba.chaosblade.box.dao.infrastructure.experiment.request.ExperimentDefinitionRequest;
import com.alibaba.chaosblade.box.common.infrastructure.domain.experiment.response.ExperimentFlowInfo;
import com.alibaba.chaosblade.box.common.infrastructure.exception.ChaosException;
import com.alibaba.chaosblade.box.common.infrastructure.util.CollectionUtil;
import com.alibaba.chaosblade.box.dao.model.ExperimentActivityDO;
import com.alibaba.chaosblade.box.dao.model.ExperimentDO;
import com.alibaba.chaosblade.box.dao.model.MiniFlowDO;
import com.alibaba.chaosblade.box.dao.model.MiniFlowGroupDO;
import com.alibaba.chaosblade.box.dao.repository.ActivityRepository;
import com.alibaba.chaosblade.box.dao.repository.ExperimentMiniFlowGroupRepository;
import com.alibaba.chaosblade.box.dao.repository.ExperimentMiniFlowRepository;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * @author haibin
 *
 *
 */
@Component
public class ExperimentFlowDefinitionQueryCommand
        extends SpringBeanCommand<ExperimentDO, ExperimentFlowInfo> {

    @Autowired
    private ExperimentFlowDefinitionManager experimentFlowDefinitionManager;

    @Autowired
    private ExperimentMiniFlowGroupRepository miniFlowGroupRepository;

    @Autowired
    private ActivityQueryManager activityQueryManager;

    @Autowired
    private ExperimentMiniFlowRepository experimentMiniFlowRepository;

    @Autowired
    private ActivityRepository activityRepository;

    @Autowired
    private ArgumentDefinitionConvertor argumentDefinitionConvertor;

    @Autowired
    private MiniFlowGroupHostInterceptor miniFlowGroupHostCheckInterceptor;

    @Autowired
    private SceneArgumentGradeConverter sceneArgumentGradeConverter;

    @Override
    public ExperimentFlowInfo execute(ExperimentDO experimentDO) {
        ExperimentFlowInfo experimentFlowInfo = new ExperimentFlowInfo();
        experimentFlowInfo.setExperimentId(experimentDO.getExperimentId());
        if (experimentDO.isDraft()) {
            return null;
        }
        if (experimentDO.getRunMode() == null) {
            experimentFlowInfo.setRunMode(ExperimentRunModeEnum.PHASE);
        } else {
            experimentFlowInfo.setRunMode(experimentDO.getRunMode());
        }
        //查询所有的guard
        experimentFlowInfo.setGuardConf(
                commandBus.syncRun(ExperimentGuardConfQueryCommand.class, experimentDO.getExperimentId()));
        //查到所有的演练的定义
        List<ExperimentActivityDO> experimentActivityDOS = activityQueryManager.findActivitiesByExperimentId(
                experimentDO.getExperimentId());
        List<MiniFlowGroup> miniFlowGroups = new ArrayList<>();
        if (!experimentDO.isNewExperimentDefinition()) {
            adapt(experimentActivityDOS, experimentDO);
            experimentActivityDOS = activityQueryManager.findActivitiesByExperimentId(
                    experimentDO.getExperimentId());
        }
        miniFlowGroups = queryNewMiniFlowGroups(experimentActivityDOS, experimentDO);
        experimentFlowInfo.setFlowGroups(miniFlowGroups);
        experimentFlowInfo.setSchedulerConfig(experimentDO.getSchedulerConfig());
        experimentFlowInfo.setDuration(experimentDO.getDuration());
        return experimentFlowInfo;
    }

    private List<MiniFlowGroup> queryNewMiniFlowGroups(List<ExperimentActivityDO> experimentActivityDOS,
                                                       ExperimentDO experimentDO) {
        //按照flowId来进行分组
        Map<String, List<ExperimentActivityDO>> FlowIdToExperimentActivityDO = experimentActivityDOS
                .stream().collect(
                        Collectors.groupingBy(ExperimentActivityDO::getFlowId));
        //查找所有的miniFlowGroup
        List<MiniFlowGroupDO> miniFlowGroupDOS = miniFlowGroupRepository.findByExperimentId(
                experimentDO.getExperimentId());
        return miniFlowGroupDOS.stream().map(
                miniFlowGroupDO -> handleMiniFlowGroupDO(miniFlowGroupDO, FlowIdToExperimentActivityDO))
                .collect(Collectors.toList());
    }

    /**
     * 处理微流程组
     *
     * @param miniFlowGroupDO             微流程组数据对象
     * @param flowIdToActivityDefinitions 微流程ID和微流程里面每个节点组合的对应map
     * @return 微流程组
     */
    private MiniFlowGroup handleMiniFlowGroupDO(MiniFlowGroupDO miniFlowGroupDO,
                                                Map<String, List<ExperimentActivityDO>> flowIdToActivityDefinitions) {
        MiniFlowGroup miniFlowGroup = new MiniFlowGroup();
        miniFlowGroup.setGroupId(miniFlowGroupDO.getGroupId());
        miniFlowGroup.setGroupName(miniFlowGroupDO.getGroupName());
        miniFlowGroup.setOrder(miniFlowGroupDO.getGroupOrder());
        miniFlowGroup.setRequired(miniFlowGroupDO.getRequired());
        List<MiniFlowDO> miniFlowDOS = experimentMiniFlowRepository.findByGroupIdAndExperimentId(
                miniFlowGroupDO.getGroupId(), miniFlowGroupDO.getExperimentId());
        miniFlowGroup.setFlows(miniFlowDOS.stream().map(
                miniFlowDO -> handleMiniFlowDO(miniFlowDO,
                        flowIdToActivityDefinitions.getOrDefault(miniFlowDO.getFlowId(), new ArrayList<>()))).filter(
                new Predicate<MiniFlow>() {
                    @Override
                    public boolean test(MiniFlow miniFlow) {
                        return miniFlow != null;
                    }
                }).collect(Collectors.toList()));
        miniFlowGroup.setScopeType(miniFlowGroupDO.getHosts().getScopeType());
        miniFlowGroup.setSelectType(miniFlowGroupDO.getHosts().getSelectType());
        miniFlowGroup.setHostPercent(miniFlowGroupDO.getHosts().getHostPercent());
        miniFlowGroup.setOsType(miniFlowGroupDO.getHosts().getOsType());
        if(!HostSelectTypes.SELECT_TYPE_PERCENT.equals(miniFlowGroup.getSelectType())){
            checkHosts(miniFlowGroup,miniFlowGroupDO);
        }

        if (miniFlowGroupDO.getHosts() != null) {
            miniFlowGroup.setAppName(miniFlowGroupDO.getHosts().getAppName());
            miniFlowGroup.setAppGroups(miniFlowGroupDO.getHosts().getAppGroups());
            miniFlowGroup.setAppType(miniFlowGroupDO.getHosts().getAppType());
            miniFlowGroup.setAppId(miniFlowGroupDO.getHosts().getAppId());
            miniFlowGroup.setScopeType(miniFlowGroupDO.getHosts().getScopeType());
            miniFlowGroup.setCloudServiceType(miniFlowGroupDO.getHosts().getCloudServiceType());
            miniFlowGroup.setCloudServiceName(miniFlowGroupDO.getHosts().getCloudServiceName());
        }
        return miniFlowGroup;
    }

    /**
     * 处理微流程
     *
     * @param miniFlowDO            微流程数据对象
     * @param experimentActivityDOS 微流程里面每个节点的对象
     * @return 微流程
     */
    private MiniFlow handleMiniFlowDO(MiniFlowDO miniFlowDO,
                                      List<ExperimentActivityDO> experimentActivityDOS) {
        MiniFlow miniFlow = new MiniFlow();
        miniFlow.setFlowId(miniFlowDO.getFlowId());
        miniFlow.setName(miniFlowDO.getName());
        miniFlow.setOrder(miniFlowDO.getFlowOrder());
        miniFlow.setRequired(miniFlowDO.getRequired());
        Map<PhaseType, List<ExperimentActivityDO>> phaseToActivity = experimentActivityDOS.stream()
                .collect(Collectors.groupingBy(ExperimentActivityDO::getPhase));
        miniFlow.setRecover(
                handleExperimentActivityDOs(phaseToActivity.getOrDefault(PhaseType.RECOVER, new ArrayList<>())));
        miniFlow.setPrepare(
                handleExperimentActivityDOs(phaseToActivity.getOrDefault(PhaseType.PREPARE, new ArrayList<>())));
        miniFlow.setCheck(
                handleExperimentActivityDOs(phaseToActivity.getOrDefault(PhaseType.CHECK, new ArrayList<>())));
        miniFlow.setAttack(
                handleExperimentActivityDOs(phaseToActivity.getOrDefault(PhaseType.ATTACK, new ArrayList<>())));
        if(!checkMiniFlowParam(miniFlow)) {
            return null;
        }
        return miniFlow;
    }

    /**
     * 校验一下微流程的参数，因为存在有空的微流程这样的脏数据
     * 在查询微流程信息时，过滤掉这种空的微流程数据
     *
     * @param miniFlow
     * @return
     */
    private boolean checkMiniFlowParam(MiniFlow miniFlow) {
        if (!CollectionUtil.isNullOrEmpty(miniFlow.getPrepare()) ||
                !CollectionUtil.isNullOrEmpty(miniFlow.getAttack()) ||
                !CollectionUtil.isNullOrEmpty(miniFlow.getCheck()) ||
                !CollectionUtil.isNullOrEmpty(miniFlow.getRecover())) {
            return true;
        }
        return false;
    }

    /**
     * 转换activityInfo
     *
     * @param experimentActivityDOS
     * @return
     */
    private List<ExperimentActivityInfo> handleExperimentActivityDOs(
            List<ExperimentActivityDO> experimentActivityDOS) {
        if (CollectionUtil.isNullOrEmpty(experimentActivityDOS)) { return new ArrayList<>(); }
        return experimentFlowDefinitionManager.getActivityDefinitions(experimentActivityDOS).stream().map(
                experimentActivityDefinition -> {
                    ExperimentActivityInfo experimentActivityInfo
                            = new ExperimentActivityInfo();
                    BeanUtils.copyProperties(experimentActivityDefinition, experimentActivityInfo,
                            "arguments");
                    experimentActivityInfo.setArguments(sceneArgumentGradeConverter.tranToArgumentsGrade(argumentDefinitionConvertor
                            .splitExperimentNodeArgumentsDefinition(experimentActivityDefinition.getAppCode(),
                                    experimentActivityDefinition.getArguments())));
                    return experimentActivityInfo;
                }).collect(Collectors.toList());
    }

    /**
     * 如果是老的流程定义，因为没有微流程，所以需要自动加上微流程组
     *
     * @param experimentActivityDOS
     * @param experimentDO
     */
    public void adapt(List<ExperimentActivityDO> experimentActivityDOS, ExperimentDO experimentDO) {
        Map<Hosts, List<ExperimentActivityDO>> hostsToActivities = experimentActivityDOS.stream().collect(
                Collectors.groupingBy(experimentActivityDO -> {
                    Hosts hosts = new Hosts();
                    hosts.setContent(experimentActivityDO.getActivityDefinition().getScope());
                    return hosts;
                }));

        List<MiniFlowGroup> miniFlowGroups = new ArrayList<>();
        int count = 1;
        for (Map.Entry<Hosts, List<ExperimentActivityDO>> hostsListEntry : hostsToActivities.entrySet()) {
            if (hostsListEntry.getValue().isEmpty()) { continue; }
            MiniFlowGroup miniFlowGroup = new MiniFlowGroup();
            miniFlowGroup.setGroupName("分组-" + count);
            miniFlowGroup.setFlows(
                    Lists.newArrayList(handleMiniFlowDO(new MiniFlowDO(), hostsListEntry.getValue())));
            miniFlowGroup.setHosts(hostsListEntry.getKey().getContent());
            miniFlowGroups.add(miniFlowGroup);
            count++;
        }
        createNewExperimentDefinition(experimentDO, miniFlowGroups);
        deleteExperimentActivityWithNoFlowIds(experimentDO);
    }

    /**
     * 删除那些没有微流程
     *
     * @param experimentDO
     */
    private void deleteExperimentActivityWithNoFlowIds(ExperimentDO experimentDO) {
        activityRepository.deleteByNoFlowId(experimentDO.getExperimentId());
    }

    private void createNewExperimentDefinition(ExperimentDO experimentDO, List<MiniFlowGroup> miniFlowGroups) {
        ExperimentDefinitionRequest experimentDefinitionRequest = new ExperimentDefinitionRequest();
        experimentDefinitionRequest.setExperimentId(experimentDO.getExperimentId());
        experimentDefinitionRequest.setRunMode(ExperimentRunModeEnum.PHASE);
        experimentDefinitionRequest.setFlowGroups(miniFlowGroups);
        experimentDefinitionRequest.setOldAdapter(true);
        Response response = commandBus.syncRun(ExperimentDefinitionCreateCommand.class,
                experimentDefinitionRequest);
        if (!response.isSuccess()) {
            if (!CommonErrorCode.B_DUMP_EXPERIMENT_DEFINITION.equals(response.getError().getErrorCode())) {
                throw new ChaosException(response.getError());
            }
        }
    }


    private void checkHosts(MiniFlowGroup miniFlowGroup,MiniFlowGroupDO miniFlowGroupDO) {
        List<String> configurationIdList = new ArrayList<>();
        Set<String> appId = new HashSet<>();
        Set<String> groupName = new HashSet<>();
        List<Host> hosts = miniFlowGroupDO.getHosts().getContent().stream().peek(host -> {
            //2代表k8s,0代表host
            if (host.getScopeType() == null) {
                host.setScopeType(host.isK8s() ? Scope.SCOPE_TYPE_K8S : Scope.SCOPE_TYPE_HOST);
            }
            if(!Strings.isNullOrEmpty(host.getAppId())) {
                appId.add(host.getAppId());
            }
            if(!Strings.isNullOrEmpty(host.getNodeGroup())) {
                groupName.add(host.getNodeGroup());
            }
            configurationIdList.add(host.isK8s() ? host.getAppConfigurationId() : host.getDeviceConfigurationId());
        }).collect(Collectors.toList());

        //校验已选机器是否已经失效
        if(!CollectionUtil.isNullOrEmpty(appId) && !CollectionUtil.isNullOrEmpty(groupName)) {
            List<String> existConfigurationIdList = miniFlowGroupHostCheckInterceptor.batchCheck(configurationIdList,appId,groupName);
            miniFlowGroup.setHosts(hosts.stream().peek(host -> {
                if(existConfigurationIdList.contains(host.getDeviceConfigurationId())) {
                    host.setInvalid(false);
                } else {
                    host.setInvalid(true);
                }
            }).collect(Collectors.toList()));
        } else {
            //非应用不做校验
            miniFlowGroup.setHosts(hosts);
        }
    }

}
