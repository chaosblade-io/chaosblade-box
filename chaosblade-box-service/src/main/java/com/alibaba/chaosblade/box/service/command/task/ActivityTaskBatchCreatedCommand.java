package com.alibaba.chaosblade.box.service.command.task;

import com.alibaba.chaosblade.box.common.app.sdk.constants.PhaseType;
import com.alibaba.chaosblade.box.common.app.sdk.scope.Scope;
import com.alibaba.chaosblade.box.common.common.domain.definition.ExperimentActivityDefinition;
import com.alibaba.chaosblade.box.common.common.domain.experiment.ActivityRunParam;
import com.alibaba.chaosblade.box.common.common.enums.ExperimentRunModeEnum;
import com.alibaba.chaosblade.box.common.common.enums.StateEnum;
import com.alibaba.chaosblade.box.common.common.util.MiniAppUtils;
import com.alibaba.chaosblade.box.dao.infrastructure.experiment.ActivityComparator;
import com.alibaba.chaosblade.box.dao.infrastructure.experiment.task.ActivityTasksCreateRequest;
import com.alibaba.chaosblade.box.common.experiment.task.flow.util.CompareUtils;
import com.alibaba.chaosblade.box.common.infrastructure.util.CollectionUtil;
import com.alibaba.chaosblade.box.dao.infrastructure.experiment.task.ExperimentTaskHostRecorder;
import com.alibaba.chaosblade.box.common.infrastructure.enums.ActivityTaskMode;
import com.alibaba.chaosblade.box.common.infrastructure.util.EnvironmentUtil;
import com.alibaba.chaosblade.box.dao.model.*;
import com.alibaba.chaosblade.box.dao.repository.*;
import com.alibaba.chaosblade.box.dao.infrastructure.manager.MiniAppTaskManager;
import com.alibaba.chaosblade.box.common.commands.SpringBeanCommand;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
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
public class ActivityTaskBatchCreatedCommand
    extends SpringBeanCommand<ActivityTasksCreateRequest, List<ActivityTaskDO>> {

    @Autowired
    private ActivityTaskRepository activityTaskRepository;

    @Autowired
    private ActivityTargetExecutionResultRepository activityTargetExecutionResultRepository;

    @Autowired
    private ExperimentMiniFlowRepository experimentMiniFlowRepository;

    @Autowired
    private ActivityRepository activityRepository;

    @Autowired
    private MiniAppTaskManager miniAppTaskManager;

    @Autowired
    private EnvironmentUtil environmentUtil;

    @Autowired
    private ExperimentTaskHostRecorder experimentTaskHostRecorder;

    @Autowired
    private ExperimentMiniFlowGroupRepository experimentMiniFlowGroupRepository;

    @Autowired
    private ActivityTaskHostPercentIntercept activityTaskHostPercentIntercept;

    @Override
    public List<ActivityTaskDO> execute(ActivityTasksCreateRequest activityTasksCreateRequest) {
        ExperimentTaskDO experimentTaskDO = activityTasksCreateRequest.getExperimentTaskDO();
        ExperimentDO experimentDO = activityTasksCreateRequest.getExperimentDO();
        String experimentId = experimentDO.getExperimentId();
        List<ActivityRunParam> params = activityTasksCreateRequest.getParams();
        String experimentTaskId = experimentTaskDO.getTaskId();
        //首先对activity进行排序
        List<ExperimentActivityDO> experimentActivityDOS = activityRepository.findActivities(
            experimentId);

        //校验机器的选择方式是否是百分比，如果是百分比，那么为微流程组下所有节点选取机器
        checkAndSelectHost(experimentActivityDOS);

        experimentActivityDOS = sortExperimentActivityDOS(experimentDO,
            activityTasksCreateRequest.getExperimentRunModeEnum(),
            experimentActivityDOS);
        List<ActivityTaskDO> activityTaskDOS = new ArrayList<>();
        //上一个活动任务
        ActivityTaskDO preActivityTaskDO = null;
        Map<String, ActivityRunParam> activityIdToRunParam = buildActivityIdToRunParamMap(params);
        //依次创建每个活动的任务
        int activityTaskOrder = 0;
        Map<String, ExperimentActivityDO> taskIdToActivity = new HashMap<>();
        for (ExperimentActivityDO experimentActivityDO : experimentActivityDOS) {
            ActivityTaskDO activityTaskDO = buildBaseActivityTaskDO(experimentTaskDO, experimentActivityDO,
                activityIdToRunParam.get(experimentActivityDO.getActivityId()), activityTaskOrder);
            if (preActivityTaskDO != null) {
                preActivityTaskDO.setNextActivityTaskId(activityTaskDO.getTaskId());
                activityTaskDO.setPreActivityTaskId(preActivityTaskDO.getTaskId());
            }
            taskIdToActivity.put(activityTaskDO.getTaskId(), experimentActivityDO);
            activityTaskDO.setTaskMode(ActivityTaskMode.EXPERIMENT.getValue());
            preActivityTaskDO = activityTaskDO;
            activityTaskDOS.add(activityTaskDO);
            activityTaskOrder++;
        }

        List<ExperimentMiniAppTaskDO> experimentMiniAppTaskDOS = new ArrayList<>();
        //设定相对应的攻击环节
        Set<Scope> uniqueHosts = new HashSet<>();
        for (ActivityTaskDO activityTaskDO : activityTaskDOS) {
            //绑定恢复节点对应的攻击节点
            if (PhaseType.ATTACK.equals(activityTaskDO.getPhase()) || PhaseType.PREPARE.equals(
                activityTaskDO.getPhase())) {
                for (ActivityTaskDO activityTaskDO2 : activityTaskDOS) {
                    if (PhaseType.RECOVER.equals(activityTaskDO2.getPhase())) {
                        if (isActivityEqual(activityTaskDO.getActivityId(),
                            taskIdToActivity.get(activityTaskDO2.getTaskId()).getAttackActivityId())) {
                            activityTaskDO2.setAttackActivityTaskId(activityTaskDO.getTaskId());
                        }
                    }
                }
            }
            experimentMiniAppTaskDOS.addAll(batchCreateExperimentMiniAppTasks(activityTaskDO, uniqueHosts));
        }
        activityTaskRepository.saveBatch(activityTaskDOS);
        activityTargetExecutionResultRepository.saveBatch(experimentMiniAppTaskDOS);
        experimentTaskHostRecorder.record(experimentTaskDO, uniqueHosts);
        return activityTaskDOS;
    }

    private List<ExperimentMiniAppTaskDO> batchCreateExperimentMiniAppTasks(ActivityTaskDO activityTaskDO,
        Set<Scope> uniqueHosts) {
        ActivityRunParam activityRunParam = activityTaskDO.getRunParam();
        if (activityRunParam == null) { return new ArrayList<>(); }
        return Optional.ofNullable(activityRunParam.getScope()).orElse(new ArrayList<>())
            .stream().map(host -> {
                ExperimentMiniAppTaskDO experimentMiniAppTaskDO = miniAppTaskManager.initExperimentMiniAppTaskDO(
                    activityTaskDO, host);
                uniqueHosts.add(host);
                return experimentMiniAppTaskDO;
            }).collect(Collectors.toList());

    }

    /**
     * 需要将演练活动进行排序,根据不同的执行策略可以分为按照flow顺序执行，还有按照阶段执行
     *
     * @param experimentDO
     * @param runModeEnum
     * @param experimentActivityDOS
     */
    private List<ExperimentActivityDO> sortExperimentActivityDOS(ExperimentDO experimentDO,
        ExperimentRunModeEnum runModeEnum,
        List<ExperimentActivityDO> experimentActivityDOS) {
        return sortByActivityMode(experimentDO, experimentActivityDOS, runModeEnum);
    }

    private List<ExperimentActivityDO> sortByActivityMode(ExperimentDO experimentDO,
        List<ExperimentActivityDO> experimentActivityDOS, ExperimentRunModeEnum experimentRunModeEnum) {
        //首先按照流程来分类
        if (!experimentDO.isNewExperimentDefinition()) {
            return experimentActivityDOS.stream().sorted((o1, o2) -> ActivityComparator.INSTANCE.compare(o1, o2))
                .collect(Collectors.toList());
        } else {
            //新流程的话排序比较麻烦
            List<MiniFlowDO> miniFlowDOS = experimentMiniFlowRepository.findByExperimentId(
                experimentDO.getExperimentId());
            Map<String, MiniFlowDO> flowIdToFlow = miniFlowDOS.stream().collect(
                Collectors.toMap(MiniFlowDO::getFlowId, e -> e));
            //这里排序两次，首先根据flowId的顺序进行排，然后同一个flowId内部再进行排序
            return experimentActivityDOS.stream().sorted((o1, o2) -> {
                //首先按照groupId来比
                MiniFlowDO o1Flow = flowIdToFlow.get(o1.getFlowId());
                MiniFlowDO o2Flow = flowIdToFlow.get(o2.getFlowId());
                int compareResult = 0;
                if (experimentRunModeEnum.equals(ExperimentRunModeEnum.PHASE)) {
                    compareResult = ActivityComparator.INSTANCE.compare(o1, o2);
                    if (compareResult == 0) {
                        compareResult = CompareUtils.integerCompare(o1Flow.getGroupOrder(), o2Flow.getGroupOrder());
                        if (compareResult == 0) {
                            compareResult = CompareUtils.integerCompare(o1Flow.getFlowOrder(), o2Flow.getFlowOrder());
                        }
                    }
                } else {
                    compareResult = CompareUtils.integerCompare(o1Flow.getGroupOrder(), o2Flow.getGroupOrder());
                    if (compareResult == 0) {
                        compareResult = CompareUtils.integerCompare(o1Flow.getFlowOrder(), o2Flow.getFlowOrder());
                        if (compareResult == 0) {
                            compareResult = ActivityComparator.INSTANCE.compare(o1, o2);
                        }
                    }
                }
                return compareResult;
            }).collect(Collectors.toList());
        }
    }

    private boolean isActivityEqual(String activityId, String activityId2) {
        return Objects.equals(activityId, activityId2);
    }

    private Map<String, ActivityRunParam> buildActivityIdToRunParamMap(List<ActivityRunParam> params) {
        if (CollectionUtil.isNullOrEmpty(params)) {
            return new HashMap<>();
        }
        Map<String, ActivityRunParam> map = new HashMap<>();
        params.forEach(activityRunParam -> map.put(activityRunParam.getActivityId(), activityRunParam));
        return map;
    }

    private ActivityTaskDO buildBaseActivityTaskDO(ExperimentTaskDO experimentTaskDO,
        ExperimentActivityDO experimentActivityDO,
        ActivityRunParam activityRunParam, int activityOrder) {
        String currentTaskId = IdWorker.getIdStr();
        ActivityTaskDO activityTaskDO = new ActivityTaskDO();
        activityTaskDO.setState(StateEnum.READY.getValue());
        activityTaskDO.setTaskId(currentTaskId);
        activityTaskDO.setAppCode(experimentActivityDO.getAppCode());
        activityTaskDO.setExecutableAppCode(experimentActivityDO.getExecutableAppCode());
        activityTaskDO.setActivityOrder(activityOrder);
        activityTaskDO.setExperimentTaskId(experimentTaskDO.getTaskId());
        activityTaskDO.setUserId(experimentTaskDO.getUserId());
        activityTaskDO.setActivityName(experimentActivityDO.getActivityName());
        activityTaskDO.setActivityId(experimentActivityDO.getActivityId());
        activityTaskDO.setFlowId(experimentActivityDO.getFlowId());
        ExperimentActivityDefinition experimentActivityDefinition = experimentActivityDO.getActivityDefinition();
        activityTaskDO.setPhase(experimentActivityDO.getPhase());
        if (activityRunParam == null) {
            activityRunParam = new ActivityRunParam();
            activityRunParam.setActivityId(experimentActivityDO.getActivityId());
            activityRunParam.setArguments(experimentActivityDefinition.getArguments());
            activityRunParam.setScope(experimentActivityDefinition.getScope());
        } else {
            if (activityRunParam.getScope() == null) {
                activityRunParam.setScope(experimentActivityDefinition.getScope());
            }
            if (activityRunParam.getArguments() == null) {
                activityRunParam.setArguments(experimentActivityDefinition.getArguments());
            }
        }
        activityRunParam.setFailedTolerance(experimentActivityDefinition.getFailedTolerance());
        activityRunParam.setNeedUserCheck(experimentActivityDefinition.isUserCheck());
        activityRunParam.setInterruptedIfFailed(experimentActivityDefinition.isInterruptedIfFailed());
        if (!CollectionUtil.isNullOrEmpty(experimentActivityDefinition.getScope())) {
            activityTaskDO.setAppId(experimentActivityDefinition.getScope().get(0).getLongAppId());
        }
        activityTaskDO.setRunParam(activityRunParam);
        if (MiniAppUtils.isFromLitmusChaos(activityTaskDO.getAppCode())
            && PhaseType.ATTACK.equals(activityTaskDO.getPhase())) {
            //如果是litmus的注入阶段，默认异步
            activityRunParam.getArguments().addArgs("async", "true");
        }

        return activityTaskDO;
    }

    private void checkAndSelectHost(List<ExperimentActivityDO> activityDOS) {
        activityTaskHostPercentIntercept.hostPercent(activityDOS);
    }

}
