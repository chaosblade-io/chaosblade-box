package com.alibaba.chaosblade.box.dao.infrastructure.experiment.definition;

import com.alibaba.chaosblade.box.common.app.sdk.constants.PhaseType;
import com.alibaba.chaosblade.box.common.app.sdk.scope.Host;
import com.alibaba.chaosblade.box.common.common.domain.definition.ExperimentActivityDefinition;
import com.alibaba.chaosblade.box.common.common.domain.definition.ExperimentFlowDefinition;
import com.alibaba.chaosblade.box.common.common.util.MiniAppUtils;
import com.alibaba.chaosblade.box.dao.infrastructure.experiment.interceptor.ExperimentActivityDefinitionInterceptor;
import com.alibaba.chaosblade.box.common.experiment.task.flow.ActivityTaskExecutionAttributes;
import com.alibaba.chaosblade.box.common.infrastructure.constant.ExperimentAttributes;
import com.alibaba.chaosblade.box.common.infrastructure.util.CollectionUtil;
import com.alibaba.chaosblade.box.dao.infrastructure.app.MiniAppCodeHelper;
import com.alibaba.chaosblade.box.dao.model.ExperimentActivityDO;
import com.alibaba.chaosblade.box.dao.model.ExperimentDO;
import com.alibaba.chaosblade.box.dao.model.MiniFlowGroupDO;
import com.alibaba.chaosblade.box.dao.model.SceneFunctionDO;
import com.alibaba.chaosblade.box.dao.repository.ActivityRepository;
import com.alibaba.chaosblade.box.dao.repository.ExperimentMiniFlowGroupRepository;
import com.alibaba.chaosblade.box.dao.repository.SceneFunctionRepository;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.google.common.base.Strings;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.AnnotationAwareOrderComparator;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * 演练流程定义管理
 *
 * @author haibin
 *
 *
 */
@Component
public class ExperimentFlowDefinitionManager implements InitializingBean {

    @Autowired
    private ActivityRepository activityRepository;

    @Autowired
    private List<ExperimentActivityDefinitionInterceptor> experimentActivityDefinitionInterceptors;

    @Autowired
    private SceneFunctionRepository sceneFunctionRepository;

    @Autowired
    private MiniAppCodeHelper miniAppCodeHelper;

    @Autowired
    private ExperimentMiniFlowGroupRepository experimentMiniFlowGroupRepository;

    /**
     * 获取一个演练的所有hosts
     *
     * @param experimentId
     * @return
     */
    public List<Host> findAllHostsByExperimentId(String experimentId) {
        return experimentMiniFlowGroupRepository.findByExperimentId(experimentId).stream().flatMap(
            (Function<MiniFlowGroupDO, Stream<Host>>) miniFlowGroupDO -> miniFlowGroupDO.getHosts().getContent()
                .stream()).distinct().collect(Collectors.toList());

    }

    /**
     * 新增演练流程
     *
     * @param experimentDO
     * @param experimentFlowDefinition
     * @return
     */
    public void addFlowDefinition(ExperimentDO experimentDO, ExperimentFlowDefinition experimentFlowDefinition) {
        List<ExperimentActivityDO> experimentActivityDOS = parseFlow(experimentDO, experimentFlowDefinition, true);
        activityRepository.batchInsert(experimentActivityDOS);
    }

    /**
     * 更新演练流程
     *
     * @param experimentDO
     * @param experimentFlowDefinition
     * @return
     */
    public boolean updateFlowDefinition(ExperimentDO experimentDO, ExperimentFlowDefinition experimentFlowDefinition) {
        String experimentId = experimentDO.getExperimentId();
        List<ExperimentActivityDO> newExperimentActivityDOS = parseFlow(experimentDO, experimentFlowDefinition, false);
        Map<String, ExperimentActivityDO> newIdToExperimentActivityDO = newExperimentActivityDOS.stream().collect(
            Collectors.toMap(ExperimentActivityDO::getActivityId, experimentActivityDO -> experimentActivityDO));
        List<ExperimentActivityDO> oldExperimentExperimentActivityDOs = activityRepository.findActivities(experimentId);
        Map<String, ExperimentActivityDO> oldIdToExperimentActivityDO = oldExperimentExperimentActivityDOs.stream()
            .collect(
                Collectors.toMap(ExperimentActivityDO::getActivityId, experimentActivityDO -> experimentActivityDO));
        for (ExperimentActivityDO experimentActivityDO : oldExperimentExperimentActivityDOs) {
            if (newIdToExperimentActivityDO.get(experimentActivityDO.getActivityId()) == null) {
                activityRepository.deleteByActivityId(experimentActivityDO);
            }
        }
        for (ExperimentActivityDO experimentActivityDO : newExperimentActivityDOS) {
            if (oldIdToExperimentActivityDO.get(experimentActivityDO.getActivityId()) == null) {
                activityRepository.add(experimentActivityDO);
            } else {
                activityRepository.update(experimentActivityDO);
            }
        }
        return true;
    }

    /**
     * 解析流程定义，将流程定义分割成活动数组
     *
     * @param experimentDO
     * @param experimentFlowDefinition
     * @return
     */
    private List<ExperimentActivityDO> parseFlow(ExperimentDO experimentDO,
        ExperimentFlowDefinition experimentFlowDefinition,
        boolean create) {
        //首先解析注入环节
        //handle
        ExperimentFlowDefinitionMeta experimentFlowDefinitionMeta = new ExperimentFlowDefinitionMeta();
        experimentFlowDefinitionMeta.setExperimentId(experimentDO.getExperimentId());
        experimentFlowDefinitionMeta.setExperimentDO(experimentDO);
        experimentFlowDefinitionMeta.setCreate(create);
        ExperimentFlowDefinitionMeta.ExperimentActivitiesMap experimentActivitiesMap = new ExperimentFlowDefinitionMeta.ExperimentActivitiesMap();
        experimentFlowDefinitionMeta.setActivitiesMap(experimentActivitiesMap);
        //先解析attack环节
        parsePhase(experimentFlowDefinitionMeta, experimentFlowDefinition.getAttack(), PhaseType.ATTACK);

        //解析准备环节
        parsePhase(experimentFlowDefinitionMeta, experimentFlowDefinition.getPrepare(), PhaseType.PREPARE);

        //解析检查环节
        parsePhase(experimentFlowDefinitionMeta, experimentFlowDefinition.getCheck(), PhaseType.CHECK);

        //解析Recovery环节
        parsePhase(experimentFlowDefinitionMeta, experimentFlowDefinition.getRecover(), PhaseType.RECOVER);
        return experimentActivitiesMap.getExperimentActivityDOs();
    }

    private void parsePhase(ExperimentFlowDefinitionMeta experimentFlowDefinitionMeta,
        List<ExperimentActivityDefinition> activityDefinitions,
        PhaseType phaseType) {
        parserExperimentDefinition(experimentFlowDefinitionMeta, activityDefinitions, phaseType);
        //攻击环节放在最前面
        if (PhaseType.ATTACK.equals(phaseType)) {
            handleAttackPhase(experimentFlowDefinitionMeta);
        }

        //如果是恢复阶段，那么自动增加故障注入对应的恢复阶段
        if (PhaseType.RECOVER.equals(phaseType)) {
            handleRecoveryPhase(experimentFlowDefinitionMeta);
        }
    }

    private void parserExperimentDefinition(ExperimentFlowDefinitionMeta experimentFlowDefinitionMeta,
        List<ExperimentActivityDefinition> activityDefinitions,
        PhaseType phaseType) {
        String experimentId = experimentFlowDefinitionMeta.getExperimentId();
        boolean create = experimentFlowDefinitionMeta.isCreate();
        int count = 0;
        if (!CollectionUtil.isNullOrEmpty(activityDefinitions)) {
            for (ExperimentActivityDefinition experimentActivityDefinition : activityDefinitions) {
                fillExecutableAppCode(experimentActivityDefinition);
                count++;
                preHandleExperimentActivityDefinition(experimentActivityDefinition, phaseType,
                    experimentFlowDefinitionMeta);
                ExperimentActivityDO experimentActivityDO = buildExperimentActivityDO(count, experimentId,
                    experimentActivityDefinition, phaseType, create);
                experimentFlowDefinitionMeta.getActivitiesMap().addExperimentActivityDO(phaseType,
                    experimentActivityDO);
            }
        }
    }

    private void handleAttackPhase(ExperimentFlowDefinitionMeta experimentFlowDefinitionMeta) {
        experimentFlowDefinitionMeta.setIncludeJvm(
            experimentFlowDefinitionMeta.getActivitiesMap().getExperimentActivityDOSByPhase(PhaseType.ATTACK)
                .stream().anyMatch(
                experimentActivityDO -> MiniAppUtils.isJvm(experimentActivityDO.getExecutableAppCode())));
        if (experimentFlowDefinitionMeta.isIncludeJvm()) {
            experimentFlowDefinitionMeta.getExperimentDO().putAttribute(ExperimentAttributes.ATTR_JVM_INCLUDE,
                ActivityTaskExecutionAttributes.ATTRIBUTE_VALUE_TRUE);
        } else {
            experimentFlowDefinitionMeta.getExperimentDO().removeAttribute(ExperimentAttributes.ATTR_JVM_INCLUDE);
        }
    }

    private void handleRecoveryPhase(ExperimentFlowDefinitionMeta experimentFlowDefinitionMeta) {
        experimentFlowDefinitionMeta.getActivitiesMap().getExperimentActivityDOSByPhase(PhaseType.ATTACK).forEach(
            experimentActivityDO -> handleRecoveryActivity(experimentFlowDefinitionMeta, experimentActivityDO));
        experimentFlowDefinitionMeta.getActivitiesMap().getExperimentActivityDOSByPhase(PhaseType.PREPARE).forEach(
            experimentActivityDO -> handleRecoveryActivity(experimentFlowDefinitionMeta, experimentActivityDO));
    }

    private void handleRecoveryActivity(
        ExperimentFlowDefinitionMeta experimentFlowDefinitionMeta,
        ExperimentActivityDO attackExperimentActivityDO) {
        if (MiniAppUtils.isFromChaosBlade(attackExperimentActivityDO.getExecutableAppCode())) {
            String recoverCode = MiniAppUtils.getRecoverCode(attackExperimentActivityDO.getExecutableAppCode());
            //找出所有的恢复节点
            List<ExperimentActivityDO> recoveryActivities = findRecoveryActivities(experimentFlowDefinitionMeta,
                attackExperimentActivityDO, recoverCode);
            if (recoveryActivities.isEmpty()) {
                //如果恢复节点为空，那么自动添加
                experimentFlowDefinitionMeta.getActivitiesMap().addExperimentActivityDO(PhaseType.RECOVER,
                    buildRecoverExperimentActivityDO(recoverCode,
                        attackExperimentActivityDO, experimentFlowDefinitionMeta.isCreate()));
            } else {
                //如果恢复节点不为空，新的流程定义每个微流程只有一个攻击节点,所以只需要对应的攻击节点全部设置为微流程哪一个即可
                if (experimentFlowDefinitionMeta.getExperimentDO().isNewExperimentDefinition()
                    && recoveryActivities.size() == 1) {
                    recoveryActivities.forEach(
                        experimentActivityDO -> experimentActivityDO
                            .setAttackActivityId(attackExperimentActivityDO.getActivityId()));
                } else {
                    //老流程的话，因为没办法区分哪一个恢复节点属于哪一个攻击节点
                    recoveryActivities.removeIf(
                        experimentActivityDO -> experimentActivityDO.getAppCode().equals(recoverCode)
                            && experimentActivityDO.getAttackActivityId() == null);
                    experimentFlowDefinitionMeta.getActivitiesMap().addExperimentActivityDO(PhaseType.RECOVER,
                        buildRecoverExperimentActivityDO(recoverCode,
                            attackExperimentActivityDO, experimentFlowDefinitionMeta.isCreate()));
                }
            }
        }
    }

    private List<ExperimentActivityDO> findRecoveryActivities(ExperimentFlowDefinitionMeta experimentFlowDefinitionMeta,
        ExperimentActivityDO attackExperimentActivity, String recoveryCode) {
        List<ExperimentActivityDO> recoveryActivities = new ArrayList<>();
        if (experimentFlowDefinitionMeta.getExperimentDO().isNewExperimentDefinition()) {
            recoveryActivities = experimentFlowDefinitionMeta.getActivitiesMap()
                .getFlowIdByExperimentActivityDO(attackExperimentActivity.getFlowId()).stream().filter(
                    experimentActivityDO -> experimentActivityDO.getExecutableAppCode().equals(recoveryCode)).collect(
                    Collectors.toList());
        } else {
            recoveryActivities = experimentFlowDefinitionMeta.getActivitiesMap()
                .getExperimentActivityDOSByPhase(PhaseType.RECOVER);
        }
        return recoveryActivities;
    }

    /**
     * 设置恢复活动节点
     *
     * @param attackExperimentActivityDO
     * @return
     */
    private ExperimentActivityDO buildRecoverExperimentActivityDO(String recoveryCode,
        ExperimentActivityDO attackExperimentActivityDO, boolean create) {
        ExperimentActivityDefinition experimentActivityDefinition = new ExperimentActivityDefinition();
        experimentActivityDefinition.setAppCode(recoveryCode);
        experimentActivityDefinition.setExecutableAppCode(recoveryCode);
        experimentActivityDefinition.setFlowId(attackExperimentActivityDO.getFlowId());
        experimentActivityDefinition.setActivityName(
            MiniAppUtils.getRecoverName(attackExperimentActivityDO.getActivityName()));
        experimentActivityDefinition.setScope(attackExperimentActivityDO.getActivityDefinition().getScope());
        experimentActivityDefinition.setSync(attackExperimentActivityDO.getActivityDefinition().isSync());
        experimentActivityDefinition.setUserCheck(attackExperimentActivityDO.getActivityDefinition().isUserCheck());
        ExperimentActivityDO recovery = buildExperimentActivityDO(attackExperimentActivityDO.getActivityOrder(),
            attackExperimentActivityDO.getExperimentId(),
            experimentActivityDefinition, PhaseType.RECOVER, create);
        recovery.setAttackActivityId(attackExperimentActivityDO.getActivityId());
        return recovery;
    }

    /**
     * 保存前进行activity处理
     *
     * @param experimentActivityDefinition
     * @param phaseType
     * @param experimentFlowDefinitionMeta
     */
    private void preHandleExperimentActivityDefinition(ExperimentActivityDefinition experimentActivityDefinition,
        PhaseType phaseType, ExperimentFlowDefinitionMeta experimentFlowDefinitionMeta) {
        for (ExperimentActivityDefinitionInterceptor experimentActivityDefinitionInterceptor :
            experimentActivityDefinitionInterceptors) {
            experimentActivityDefinitionInterceptor.preInterceptor(experimentActivityDefinition, phaseType,
                experimentFlowDefinitionMeta);
        }
    }

    private ExperimentActivityDO buildExperimentActivityDO(int index, String experimentId,
        ExperimentActivityDefinition experimentActivityDefinition, PhaseType phaseType, boolean create) {
        ExperimentActivityDO experimentActivityDO = new ExperimentActivityDO();
        fillActivityId(experimentActivityDefinition, experimentActivityDO, create);
        fillActivityName(experimentActivityDefinition, experimentActivityDO);
        experimentActivityDO.setExperimentId(experimentId);
        experimentActivityDO.setPhase(phaseType);
        experimentActivityDO.setAppCode(experimentActivityDefinition.getAppCode());
        experimentActivityDO.setIsDeleted(false);
        if (experimentActivityDefinition.getExecutableAppCode() == null) {
            experimentActivityDO.setExecutableAppCode(experimentActivityDefinition.getAppCode());
        } else {
            experimentActivityDO.setExecutableAppCode(experimentActivityDefinition.getExecutableAppCode());
        }
        fillOrder(index, experimentActivityDefinition, experimentActivityDO);
        experimentActivityDO.setActivityPriority(ExperimentActivityDO.PRIORITY_COMMON);
        experimentActivityDO.setActivityDefinition(experimentActivityDefinition);
        experimentActivityDO.setUserCheck(
            experimentActivityDefinition.isUserCheck()
                ? ExperimentActivityDO.USER_CHECK_REQUIRED
                : ExperimentActivityDO.USER_CHECK_NOT_REQUIRED
        );
        experimentActivityDO.setFlowId(experimentActivityDefinition.getFlowId());
        return experimentActivityDO;
    }

    /**
     * 设定节点顺序
     *
     * @param index
     * @param experimentActivityDefinition
     * @param experimentActivityDO
     */
    private void fillOrder(int index, ExperimentActivityDefinition experimentActivityDefinition,
        ExperimentActivityDO experimentActivityDO) {
        //设定顺序，以用户传入为准
        Integer order = index;
        if (experimentActivityDefinition.getOrder() != null) {
            order = experimentActivityDefinition.getOrder();
        }
        experimentActivityDO.setActivityOrder(order);

    }

    /**
     * 填充节点ID
     *
     * @param experimentActivityDefinition
     * @param experimentActivityDO
     * @param create
     */
    private void fillActivityId(ExperimentActivityDefinition experimentActivityDefinition,
        ExperimentActivityDO experimentActivityDO, boolean create) {
        if (experimentActivityDefinition.getActivityId() == null) {
            experimentActivityDO.setActivityId(IdWorker.getIdStr());
        } else {
            experimentActivityDO.setActivityId(experimentActivityDefinition.getActivityId());
        }
    }

    /**
     * 设定节点ID
     *
     * @param experimentActivityDefinition
     * @param experimentActivityDO
     */
    private void fillActivityName(ExperimentActivityDefinition experimentActivityDefinition,
        ExperimentActivityDO experimentActivityDO) {
        if (Strings.isNullOrEmpty(experimentActivityDefinition.getActivityName())) {
            experimentActivityDO.setActivityName(sceneFunctionRepository.findByCode(
                experimentActivityDefinition.getAppCode()).orElse(SceneFunctionDO.Empty).getName());
        } else {
            experimentActivityDO.setActivityName(experimentActivityDefinition.getActivityName());
        }
    }

    /**
     * 获取所有活动的定义
     *
     * @param experimentActivityDOS
     * @return
     */
    public List<ExperimentActivityDefinition> getActivityDefinitions(
        List<ExperimentActivityDO> experimentActivityDOS) {
        if (experimentActivityDOS == null) {
            return new ArrayList<>();
        }
        return experimentActivityDOS.stream().sorted(
            Comparator.comparingInt(ExperimentActivityDO::getActivityOrder)).map(experimentActivityDO -> {
            ExperimentActivityDefinition experimentActivityDefinition = experimentActivityDO
                .getActivityDefinition();
            //这里需要增加flowId
            experimentActivityDefinition.setFlowId(experimentActivityDO.getFlowId());
            experimentActivityDefinition.setActivityId(experimentActivityDO.getActivityId());
            if (Strings.isNullOrEmpty(experimentActivityDefinition.getActivityName())) {
                sceneFunctionRepository.findByCode(experimentActivityDefinition.getAppCode()).ifPresent(
                    sceneFunctionDO -> experimentActivityDefinition.setActivityName(sceneFunctionDO.getName()));
            }
            return experimentActivityDefinition;
        }).collect(
            Collectors.toList());
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        AnnotationAwareOrderComparator.sort(experimentActivityDefinitionInterceptors);
    }

    /**
     * 如果是裂变小程序的话，就需要补充一下父code
     *
     * @param experimentActivityDefinition
     */
    private void fillExecutableAppCode(ExperimentActivityDefinition experimentActivityDefinition) {
        Optional<SceneFunctionDO> sceneFunctionDO = miniAppCodeHelper.getExecutableSceneFunction(
            experimentActivityDefinition.getAppCode());
        experimentActivityDefinition.setExecutableAppCode(
            sceneFunctionDO.isPresent() ? sceneFunctionDO.get().getCode() : experimentActivityDefinition.getAppCode());
    }

}
