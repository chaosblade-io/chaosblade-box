package com.alibaba.chaosblade.box.dao.infrastructure.app.litmuschaos;

import com.alibaba.chaosblade.box.common.app.sdk.constants.PhaseType;
import com.alibaba.chaosblade.box.common.common.constant.ChaosFunctionConstant;
import com.alibaba.chaosblade.box.common.common.domain.chaosblade.ChaosBladeAction;
import com.alibaba.chaosblade.box.common.common.domain.chaosblade.ChaosBladeActionType;
import com.alibaba.chaosblade.box.common.common.domain.chaosblade.ChaosBladeMetaData;
import com.alibaba.chaosblade.box.common.common.domain.definition.SceneFunctionParameterComponent;
import com.alibaba.chaosblade.box.common.common.util.MiniAppUtils;
import com.alibaba.chaosblade.box.common.infrastructure.constant.CommonConstant;
import com.alibaba.chaosblade.box.common.infrastructure.constant.SceneState;
import com.alibaba.chaosblade.box.common.infrastructure.domain.scene.SceneFunctionDependency;
import com.alibaba.chaosblade.box.common.infrastructure.exception.ChaosException;
import com.alibaba.chaosblade.box.common.infrastructure.util.ApplicationStartUpConfig;
import com.alibaba.chaosblade.box.common.infrastructure.util.EnvironmentUtil;
import com.alibaba.chaosblade.box.common.sdk.entity.*;
import com.alibaba.chaosblade.box.dao.infrastructure.app.function.sync.LitmusChaosSynchronizer;
import com.alibaba.chaosblade.box.dao.model.SceneDO;
import com.alibaba.chaosblade.box.dao.model.SceneFunctionDO;
import com.alibaba.chaosblade.box.dao.model.SceneFunctionParameterDO;
import com.alibaba.chaosblade.box.dao.repository.SceneRepository;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * @author yefei
 */
@Component
@Slf4j
public class LitmusChaosFunctionLoader {

    @Autowired
    private EnvironmentUtil environmentUtil;

    @Autowired
    private LitmusChaosSynchronizer litmusChaosSynchronizer;

    @Autowired
    private ApplicationStartUpConfig applicationStartUpConfig;

    @Autowired
    private SceneRepository sceneRepository;

    public List<SceneFunctionDO> load(LitmusChaosInvoker litmusChaosInvoker, boolean forceFresh)
        throws Exception {
        log.info("[LitmusChaosFunctionLoader] Load scene functions.");
        ChaosModels bladeModels = litmusChaosInvoker.getBladeModels();
        Map<String, SceneFunctionDO> appCodeToSceneFunctionDO = new HashMap<>();
        for (ModelSpecBean modelSpecBean : bladeModels.getItems()) {
            SceneFunctionDO prepareSceneFunctionDO = null;
            PrepareSpecBean prepareSpecBean = modelSpecBean.getPrepare();
            if (needPrepare(modelSpecBean)) {
                prepareSceneFunctionDO = buildFromPrepareSpecBean(modelSpecBean, prepareSpecBean);
                appCodeToSceneFunctionDO.put(prepareSceneFunctionDO.getCode(), prepareSceneFunctionDO);
            }
            if (modelSpecBean.getActions() != null) {
                for (ActionSpecBean actionSpecBean : modelSpecBean.getActions()) {
                    SceneFunctionDO sceneFunctionDO = buildByActionSpecBean(modelSpecBean, actionSpecBean,
                        prepareSceneFunctionDO);
                    SceneFunctionDO stopActionFunctionDO = buildStopActionFunctionDO(sceneFunctionDO);
                    appCodeToSceneFunctionDO.put(sceneFunctionDO.getCode(), sceneFunctionDO);
                    appCodeToSceneFunctionDO.put(stopActionFunctionDO.getCode(), stopActionFunctionDO);
                    log.info("load attack scene function from LitmusChaos,code:{},deps:{}", sceneFunctionDO.getCode(),
                        sceneFunctionDO.getDependencies());
                }
            }
            if (prepareSceneFunctionDO != null) {
                SceneFunctionDO uninstallSceneFunctionDO = buildUninstallActionFunctionDO(prepareSpecBean,
                    modelSpecBean, prepareSceneFunctionDO);
                appCodeToSceneFunctionDO.put(uninstallSceneFunctionDO.getCode(), uninstallSceneFunctionDO);
            }
        }
        List<SceneFunctionDO> sceneFunctionDOS = Lists.newArrayList(appCodeToSceneFunctionDO.values());
        String sceneId = findOrCreateSceneIfNotExist();
        sceneFunctionDOS.forEach(sceneFunctionDO -> {
            sceneFunctionDO.setEnabled(0);
            sceneFunctionDO.setSceneId(sceneId);
        });
        loadChaosMetadata(sceneFunctionDOS);
        distinctSceneFunctionParameters(sceneFunctionDOS);

        sceneFunctionDOS.forEach(sceneFunctionDO -> {
            if (sceneFunctionDO.isSupport(ChaosFunctionConstant.PHASE_FLAG_ATTACK)) {
                if (sceneFunctionDO.getDependencyList().isEmpty()) {
                    throw new IllegalArgumentException(
                        "scene function sync error,code:" + sceneFunctionDO.getCode() +
                            " is attack phase, should have dependencies(such as recovery or "
                            + "prepare)");
                }
            }
        });
        saveSceneFunctionDOs(sceneFunctionDOS, forceFresh);
        return sceneFunctionDOS;
    }

    private void distinctSceneFunctionParameters(List<SceneFunctionDO> sceneFunctionDOS) {
        sceneFunctionDOS.forEach(SceneFunctionDO::distinctParams);
    }

    private void loadChaosMetadata(List<SceneFunctionDO> sceneFunctionDOS) {
        sceneFunctionDOS.forEach(sceneFunctionDO -> {
            ChaosBladeAction chaosBladeAction = new ChaosBladeAction();
            String[] appCodeSplit = sceneFunctionDO.getCode().split("\\.");
            String target = appCodeSplit[1];
            String action = appCodeSplit[2];
            if (ChaosFunctionConstant.CHAOS_BLADE_ACTION_ATTACK == sceneFunctionDO.getChaosBladeActionType()) {
                chaosBladeAction.setActionType(ChaosBladeActionType.ATTACK);
                chaosBladeAction.setAction(action);
            }
            if (ChaosFunctionConstant.CHAOS_BLADE_ACTION_INSTALL == sceneFunctionDO.getChaosBladeActionType()) {
                chaosBladeAction.setActionType(ChaosBladeActionType.INSTALL_AGENT);
            }
            if (ChaosFunctionConstant.CHAOS_BLADE_ACTION_UNINSTALL == sceneFunctionDO.getChaosBladeActionType()) {
                chaosBladeAction.setActionType(ChaosBladeActionType.UNINSTALL_AGENT);
            }
            if (ChaosFunctionConstant.CHAOS_BLADE_ACTION_STOP == sceneFunctionDO.getChaosBladeActionType()) {
                chaosBladeAction.setActionType(ChaosBladeActionType.STOP_ATTACK);
            }
            chaosBladeAction.setTarget(target);
            chaosBladeAction.setPrepareType(sceneFunctionDO.getPrepareType());
            parse(chaosBladeAction, target);
            ChaosBladeMetaData.getInstance().add(sceneFunctionDO.getCode(), chaosBladeAction);
            chaosBladeAction.setHasJavaInstall(hasAgentInstall(sceneFunctionDO, target, chaosBladeAction));
            if (chaosBladeAction.isHasJavaInstall()) {
                ChaosBladeMetaData.SubTargetWithsJavaAgentInstall.add(chaosBladeAction.getSubTarget());
            }
        });
    }

    public void parse(ChaosBladeAction chaosBladeAction, String target) {
        String[] splits = target.split("-");
        if (splits.length >= 2) {
            chaosBladeAction.setScope(splits[0]);
            chaosBladeAction.setSubTarget(splits[1]);
        } else {
            chaosBladeAction.setScope(SCOPE_HOST);
            chaosBladeAction.setSubTarget(target);
        }
    }

    private boolean hasAgentInstall(SceneFunctionDO sceneFunctionDO, String target, ChaosBladeAction
        chaosBladeAction) {
        return sceneFunctionDO.getDependencyList().stream()
            .anyMatch(dependency ->
                Objects.equals(SceneFunctionDependency.TYPE_BEFORE, dependency.getType())
                    && MiniAppUtils.isJvmAgentInstall(dependency.getCode())
            )
            || MiniAppUtils.isJvmAgentInstall(sceneFunctionDO.getCode());
    }

    private boolean needPrepare(ModelSpecBean modelSpecBean) {
        return modelSpecBean.getPrepare() != null && modelSpecBean.getPrepare().isRequired();
    }

    private void saveSceneFunctionDOs(List<SceneFunctionDO> sceneFunctionDOS, boolean forceFresh)
        throws ChaosException {
        if (applicationStartUpConfig.isSync(ApplicationStartUpConfig.SyncFunctionType.LITMUS_CHAOS)) {
            try {
                TimeUnit.SECONDS.sleep(new Random().nextInt(10));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            SceneDO scene = new SceneDO();
            scene.setFunctions(sceneFunctionDOS);
            litmusChaosSynchronizer.syncSceneFunctions(scene);
        }
    }

    private SceneFunctionDO buildUninstallActionFunctionDO(
        PrepareSpecBean prepareSpecBean, ModelSpecBean modelSpecBean,
        SceneFunctionDO prepareSceneFunctionDO) {
        SceneFunctionDO sceneFunctionDO = buildByBaseSceneFunctionDO();
        sceneFunctionDO.setChaosBladeActionType(ChaosFunctionConstant.CHAOS_BLADE_ACTION_UNINSTALL);
        sceneFunctionDO.setCode(buildAppCode(prepareSpecBean.getType(), modelSpecBean.getScope(), "uninstall"));
        sceneFunctionDO.support(ChaosFunctionConstant.PHASE_FLAG_RECOVER);
        sceneFunctionDO.setName(sceneFunctionDO.getCode());
        sceneFunctionDO.addDependency(
            SceneFunctionDependency.before(prepareSceneFunctionDO.getCode(), PhaseType.PREPARE));
        prepareSceneFunctionDO.addDependency(
            SceneFunctionDependency.after(sceneFunctionDO.getCode(), PhaseType.RECOVER));
        return sceneFunctionDO;
    }

    private SceneFunctionDO buildStopActionFunctionDO(SceneFunctionDO actionFunctionDO) {
        SceneFunctionDO sceneFunctionDO = buildByBaseSceneFunctionDO();
        sceneFunctionDO.setDescription("stop action");
        sceneFunctionDO.setCode(actionFunctionDO.getCode() + "." + "stop");
        sceneFunctionDO.setName(sceneFunctionDO.getCode());
        sceneFunctionDO.setChaosBladeActionType(ChaosFunctionConstant.CHAOS_BLADE_ACTION_STOP);
        sceneFunctionDO.support(ChaosFunctionConstant.PHASE_FLAG_RECOVER);
        sceneFunctionDO.addDependency(SceneFunctionDependency.before(actionFunctionDO.getCode(), PhaseType.PREPARE));
        actionFunctionDO.addDependency(SceneFunctionDependency.after(sceneFunctionDO.getCode(), PhaseType.RECOVER));
        return sceneFunctionDO;
    }

    private SceneFunctionDO buildByActionSpecBean(ModelSpecBean modelSpecBean,
        ActionSpecBean actionSpecBean,
        SceneFunctionDO prepareFunction) {
        SceneFunctionDO sceneFunctionDO = buildByBaseSceneFunctionDO();
        if (prepareFunction != null) {
            sceneFunctionDO.setAgentRequired(true);
            sceneFunctionDO.addDependency(SceneFunctionDependency.before(prepareFunction.getCode(), PhaseType.PREPARE));
        }
        sceneFunctionDO.support(ChaosFunctionConstant.PHASE_FLAG_ATTACK);
        sceneFunctionDO.setChaosBladeActionType(ChaosFunctionConstant.CHAOS_BLADE_ACTION_ATTACK);
        sceneFunctionDO.setCode(
            buildAppCode(modelSpecBean.getTarget(), modelSpecBean.getScope(), actionSpecBean.getAction()));
        sceneFunctionDO.setDescription(actionSpecBean.getLongDesc());
        sceneFunctionDO.setName(sceneFunctionDO.getCode());
        List<SceneFunctionParameterDO> parameters = new ArrayList<>();
        sceneFunctionDO.setParameters(parameters);
        if (actionSpecBean.getFlags() != null) {
            for (FlagSpecBean flagSpecBean : actionSpecBean.getFlags()) {
                parameters.add(buildByFlagSpecBean(sceneFunctionDO, flagSpecBean));
            }
        }
        if (actionSpecBean.getMatchers() != null) {
            for (MatcherSpecBean matcherSpecBean : actionSpecBean.getMatchers()) {
                parameters.add(buildByMatcherSpecBean(sceneFunctionDO, matcherSpecBean));
            }
        }
        parameters.add(buildGlobalParam());
        return sceneFunctionDO;
    }

    private SceneFunctionDO buildFromPrepareSpecBean(ModelSpecBean modelSpecBean,
        PrepareSpecBean prepareSpecBean) {
        SceneFunctionDO sceneFunctionDO = buildByBaseSceneFunctionDO();
        sceneFunctionDO.support(ChaosFunctionConstant.PHASE_FLAG_PREPARE);
        sceneFunctionDO.setChaosBladeActionType(ChaosFunctionConstant.CHAOS_BLADE_ACTION_INSTALL);
        sceneFunctionDO.setCode(buildAppCode(prepareSpecBean.getType(), modelSpecBean.getScope(), "install"));
        sceneFunctionDO.setName(sceneFunctionDO.getCode());
        sceneFunctionDO.setPrepareType(prepareSpecBean.getType());
        List<SceneFunctionParameterDO> parameters = new ArrayList<>();
        sceneFunctionDO.setParameters(parameters);
        if (prepareSpecBean.getFlags() != null) {
            for (FlagSpecBean flagSpecBean : prepareSpecBean.getFlags()) {
                parameters.add(buildByFlagSpecBean(sceneFunctionDO, flagSpecBean));
            }
        }
        return sceneFunctionDO;
    }

    private SceneFunctionParameterDO buildGlobalParam() {
        SceneFunctionParameterDO sceneFunctionParameterDO = new SceneFunctionParameterDO();
        sceneFunctionParameterDO.setIsDelete(false);
        sceneFunctionParameterDO.setName("debug");
        sceneFunctionParameterDO.setAlias("debug");
        sceneFunctionParameterDO.setType(SceneFunctionParameterDO.TYPE_MATCHER);
        SceneFunctionParameterComponent sceneFunctionParameterComponent = new SceneFunctionParameterComponent();
        sceneFunctionParameterComponent.setRequired(false);
        sceneFunctionParameterComponent.setType(SceneFunctionParameterComponent.TYPE_INPUT);
        sceneFunctionParameterDO.setComponent(sceneFunctionParameterComponent);
        sceneFunctionParameterDO.setSequence(0);
        return sceneFunctionParameterDO;
    }

    private SceneFunctionParameterDO buildByMatcherSpecBean(SceneFunctionDO sceneFunctionDO,
        MatcherSpecBean matcherSpecBean) {
        SceneFunctionParameterDO sceneFunctionParameterDO = new SceneFunctionParameterDO();
        sceneFunctionParameterDO.setIsDelete(false);
        sceneFunctionParameterDO.setName(matcherSpecBean.getName());
        sceneFunctionParameterDO.setAlias(matcherSpecBean.getName());
        sceneFunctionParameterDO.setDescription(matcherSpecBean.getDesc());
        sceneFunctionParameterDO.setType(SceneFunctionParameterDO.TYPE_MATCHER);
        SceneFunctionParameterComponent sceneFunctionParameterComponent = new SceneFunctionParameterComponent();
        sceneFunctionParameterComponent.setRequired(matcherSpecBean.isRequired());
        sceneFunctionParameterComponent.setType(SceneFunctionParameterComponent.TYPE_INPUT);
        sceneFunctionParameterDO.setComponent(sceneFunctionParameterComponent);
        return sceneFunctionParameterDO;
    }

    private SceneFunctionParameterDO buildByFlagSpecBean(SceneFunctionDO sceneFunctionDO, FlagSpecBean flagSpecBean) {
        SceneFunctionParameterDO sceneFunctionParameterDO = new SceneFunctionParameterDO();
        sceneFunctionParameterDO.setIsDelete(false);
        sceneFunctionParameterDO.setFunctionId(sceneFunctionDO.getFunctionId());
        sceneFunctionParameterDO.setName(flagSpecBean.getName());
        sceneFunctionParameterDO.setAlias(flagSpecBean.getName());
        sceneFunctionParameterDO.setDescription(flagSpecBean.getDesc());
        sceneFunctionParameterDO.setType(SceneFunctionParameterDO.TYPE_ACTION);
        SceneFunctionParameterComponent sceneFunctionParameterComponent = new SceneFunctionParameterComponent();
        sceneFunctionParameterComponent.setRequired(flagSpecBean.isRequired());
        sceneFunctionParameterComponent.setType(SceneFunctionParameterComponent.TYPE_INPUT);
        sceneFunctionParameterDO.setComponent(sceneFunctionParameterComponent);
        return sceneFunctionParameterDO;
    }

    private SceneFunctionDO buildByBaseSceneFunctionDO() {
        SceneFunctionDO sceneFunctionDO = new SceneFunctionDO();
        sceneFunctionDO.setIsDelete(false);
        sceneFunctionDO.setSource(ChaosFunctionConstant.SOURCE_CHAOS_BLADE);
        sceneFunctionDO.setAgentRequired(false);
        return sceneFunctionDO;
    }

    private static String SCOPE_HOST = "host";

    private static String buildAppCode(String target, String scope, String action) {
        String newTarget = target;
        if (!Strings.isNullOrEmpty(scope) && !SCOPE_HOST.equalsIgnoreCase(scope)) {
            newTarget = scope + "-" + target;
        }
        return "litmuschaos" + "." + newTarget + "." + action;
    }

    private String findOrCreateSceneIfNotExist() {
        SceneDO sceneDO = sceneRepository.findByCode(CommonConstant.CHAOS_BLADE_SCENE_CODE).orElse(null);
        if (sceneDO == null) {
            sceneDO = new SceneDO();
            sceneDO.setCode(CommonConstant.CHAOS_BLADE_SCENE_CODE);
            sceneDO.setUserId(CommonConstant.CHAOS_SCENE_OWNER);
            sceneDO.setName(CommonConstant.CHAOS_BLADE_SCENE_NAME);
            sceneDO.setState(SceneState.ACTIVE);
            sceneDO.setIsDelete(false);
            sceneRepository.add(sceneDO);
        }
        return sceneDO.getSceneId();
    }

}
