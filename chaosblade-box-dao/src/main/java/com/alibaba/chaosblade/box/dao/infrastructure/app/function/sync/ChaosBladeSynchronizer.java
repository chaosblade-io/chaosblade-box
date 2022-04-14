package com.alibaba.chaosblade.box.dao.infrastructure.app.function.sync;

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
import com.alibaba.chaosblade.box.dao.repository.SceneFunctionRepository;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Consumer;

/**
 * @author sunju
 * date 9/3/19
 */
@Slf4j
@Component
public class ChaosBladeSynchronizer extends BaseSceneSynchronizer {

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
    @DistributeLock(name = "chaosblade_scene_sync", lockAtLeastFor = "5m", lockAtMostFor = "10m",
        desc = "为了防止多台机器启动时候同步多次，增加一个锁来限制每次发布只有一台机器进行同步")
    public void syncSceneFunctions(SceneDO scene) throws ChaosException {
        if (inited.compareAndSet(false, true)) {
            log.info("[ChaosBladeSynchronizer] Start to sync ChaosBlade models.");
            if (null == scene) {
                return;
            }
            List<SceneFunctionDO> functions = scene.getFunctions();
            if (CollectionUtil.isNullOrEmpty(functions)) {
                return;
            }
            log.info("[ChaosBladeSynchronizer] Sync All functions.");
            functions.forEach(new Consumer<SceneFunctionDO>() {
                @Override
                public void accept(SceneFunctionDO sceneFunctionDO) {
                    if (ignoreSyncSceneFunction(sceneFunctionDO)) { return; }
                    try {
                        SceneSynchronousHelper.FunctionWrapper functionWrapper = wrapper(sceneFunctionDO);
                        syncSceneFunction(functionWrapper);
                        log.info("[ChaosBladeSynchronizer] Sync ChaosBlade model successful,code:" + sceneFunctionDO
                            .getCode());
                    } catch (Exception ex) {
                        log.error("sync function failed,code:{}", sceneFunctionDO.getCode(), ex
                        );
                    }
                }
            });
            sceneFunctionCategoryManager.rebindFunctionCategories(sceneFunctionRepository.findAvailableFunctions());
            chaosEventDispatcher.fireEvent(new ChaosBladeSyncFinishedOnStartedUpEvent());
            log.info("[ChaosBladeSynchronizer] Sync ChaosBlade models finished.");
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

}

