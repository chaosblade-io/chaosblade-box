package com.alibaba.chaosblade.box.dao.infrastructure.app.function.sync;

import com.alibaba.chaosblade.box.common.app.sdk.constants.PhaseType;
import com.alibaba.chaosblade.box.common.common.constant.ChaosFunctionConstant;
import com.alibaba.chaosblade.box.common.common.domain.chaosblade.ChaosBladeAction;
import com.alibaba.chaosblade.box.common.common.domain.chaosblade.ChaosBladeActionType;
import com.alibaba.chaosblade.box.common.common.domain.chaosblade.ChaosBladeMetaData;
import com.alibaba.chaosblade.box.common.infrastructure.exception.ChaosException;
import com.alibaba.chaosblade.box.common.infrastructure.lock.DistributeLock;
import com.alibaba.chaosblade.box.common.infrastructure.util.CollectionUtil;
import com.alibaba.chaosblade.box.dao.infrastructure.app.function.BaseSceneSynchronizer;
import com.alibaba.chaosblade.box.dao.infrastructure.app.function.SceneFunctionUpdatedEvent;
import com.alibaba.chaosblade.box.dao.infrastructure.app.function.SceneSynchronousHelper;
import com.alibaba.chaosblade.box.dao.infrastructure.event.ChaosEventDispatcher;
import com.alibaba.chaosblade.box.dao.infrastructure.manager.SceneFunctionCategoryManager;
import com.alibaba.chaosblade.box.dao.model.SceneDO;
import com.alibaba.chaosblade.box.dao.model.SceneFunctionDO;
import com.alibaba.chaosblade.box.common.infrastructure.domain.scene.SceneFunctionDependency;
import com.alibaba.chaosblade.box.dao.model.SceneFunctionParameterDO;
import com.alibaba.chaosblade.box.dao.repository.SceneFunctionRepository;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @author sunju
 * date 9/3/19
 */
@Slf4j
@Component
public class LitmusChaosSynchronizer extends BaseSceneSynchronizer {

    @Resource
    private SceneFunctionRepository sceneFunctionRepository;

    @Resource
    SceneSynchronizerUtil sceneSynchronizerUtil;

    @Autowired
    private SceneSynchronousHelper sceneSynchronousHelper;

    private final AtomicBoolean inited = new AtomicBoolean(false);

    @Autowired
    private ChaosEventDispatcher chaosEventDispatcher;

    @Autowired
    private SceneFunctionCategoryManager sceneFunctionCategoryManager;

    @Override
    @DistributeLock(name = "litmuschaos_scene_sync", lockAtLeastFor = "5m", lockAtMostFor = "10m",
        desc = "为了防止多台机器启动时候同步多次，增加一个锁来限制每次发布只有一台机器进行同步")
    public void syncSceneFunctions(SceneDO scene) throws ChaosException {
        if (inited.compareAndSet(false, true)) {
            log.info("[LitmusChaosSynchronizer] Start to sync ChaosBlade models.");
            if (null == scene) {
                return;
            }
            List<SceneFunctionDO> functions = scene.getFunctions();
            if (CollectionUtil.isNullOrEmpty(functions)) {
                return;
            }
            log.info("[LitmusChaosSynchronizer] Sync All functions.");
            functions.forEach(sceneFunctionDO -> {
                if (ignoreSyncSceneFunction(sceneFunctionDO)) { return; }
                try {
                    SceneSynchronousHelper.FunctionWrapper functionWrapper = wrapper(sceneFunctionDO);
                    syncSceneFunction(functionWrapper);
                    log.info("[LitmusChaosSynchronizer] Sync ChaosBlade model successful,code:" + sceneFunctionDO
                        .getCode());

                    if (sceneFunctionDO.getParentCode() != null) {
                        Optional<SceneFunctionDO> byCode = sceneFunctionRepository.findByCode(sceneFunctionDO.getParentCode());
                        if (!byCode.isPresent()) {
                            SceneFunctionDO parent = (SceneFunctionDO) sceneFunctionDO.clone();
                            parent.setId(null);
                            parent.setFunctionId(null);
                            parent.setParentCode(null);
                            parent.setCode(sceneFunctionDO.getParentCode());
                            parent.setName(sceneFunctionDO.getParentCode());
                            for (SceneFunctionParameterDO parameter : parent.getParameters()) {
                                parameter.setId(null);
                                parameter.setFunctionId(null);
                                parameter.setParameterId(null);
                            }
                            loadChaosMetadata(parent);
                            functionWrapper = wrapper(parent);
                            syncSceneFunction(functionWrapper);
                        }
                        Optional<SceneFunctionDO> stop = sceneFunctionRepository.findByCode(sceneFunctionDO.getParentCode() + ".stop" );
                        if (!stop.isPresent()) {
                            SceneFunctionDO parent = (SceneFunctionDO) sceneFunctionDO.clone();
                            parent.setId(null);
                            parent.setFunctionId(null);
                            parent.setParentCode(null);
                            parent.setCode(sceneFunctionDO.getParentCode());
                            parent.setName(sceneFunctionDO.getParentCode());
                            SceneFunctionDO functionDO = buildStopActionFunctionDO(sceneFunctionDO);
                            loadChaosMetadata(functionDO);
                            functionWrapper = wrapper(functionDO);
                            syncSceneFunction(functionWrapper);
                        }
                    }
                } catch (Exception ex) {
                    log.error("sync function failed,code:{}", sceneFunctionDO.getCode(), ex
                    );
                }
            });
            sceneFunctionCategoryManager.rebindFunctionCategories(sceneFunctionRepository.findAvailableFunctions());
            chaosEventDispatcher.fireEvent(new ChaosBladeSyncFinishedOnStartedUpEvent());
            log.info("[LitmusChaosSynchronizer] Sync ChaosBlade models finished.");
        }
    }

    private void syncSceneFunction(SceneSynchronousHelper.FunctionWrapper functionWrapper) throws ChaosException {

        sceneSynchronizerUtil.checkSceneFunctionBeforePersistence(functionWrapper.getUpdateSceneFunction());
        if (functionWrapper.getExistSceneFunction() == null) {
            functionWrapper.getUpdateSceneFunction().setFunctionId(IdWorker.getIdStr());
            sceneSynchronizerUtil.addFunction(functionWrapper.getUpdateSceneFunction());
        } else {
            sceneSynchronizerUtil.updateSceneFunction(functionWrapper.getExistSceneFunction(),
                functionWrapper.getUpdateSceneFunction(), true);
            chaosEventDispatcher.fireEvent(new SceneFunctionUpdatedEvent(functionWrapper.getUpdateSceneFunction(), true));
        }
        sceneSynchronizerUtil.addAuthorizedRecord(functionWrapper.getUpdateSceneFunction());
    }

    public SceneSynchronousHelper.FunctionWrapper wrapper(SceneFunctionDO sceneFunction) {
        return sceneSynchronousHelper.useDefaultConfig(sceneFunction);
    }

    private void loadChaosMetadata(SceneFunctionDO sceneFunctionDO) {
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
    }

    public void parse(ChaosBladeAction chaosBladeAction, String target) {
        String[] splits = target.split("-");
        if (splits.length >= 2) {
            chaosBladeAction.setScope(splits[0]);
            chaosBladeAction.setSubTarget(splits[1]);
        } else {
            chaosBladeAction.setScope(ChaosBladeMetaData.SCOPE_HOST);
            chaosBladeAction.setSubTarget(target);
        }
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

    private SceneFunctionDO buildByBaseSceneFunctionDO() {
        SceneFunctionDO sceneFunctionDO = new SceneFunctionDO();
        sceneFunctionDO.setIsDelete(false);
        sceneFunctionDO.setSource(ChaosFunctionConstant.SOURCE_LITMUS_CHAOS);
        sceneFunctionDO.setAgentRequired(false);
        return sceneFunctionDO;
    }
}

