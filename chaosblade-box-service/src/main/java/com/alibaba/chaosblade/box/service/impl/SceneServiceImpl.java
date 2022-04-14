package com.alibaba.chaosblade.box.service.impl;

import com.alibaba.chaosblade.box.common.common.domain.user.ChaosUser;
import com.alibaba.chaosblade.box.common.infrastructure.constant.ChangelogTypes.ChangeActionType;
import com.alibaba.chaosblade.box.common.infrastructure.constant.ChangelogTypes.ChangeOperatorType;
import com.alibaba.chaosblade.box.common.infrastructure.constant.ChangelogTypes.ChangeTargetType;
import com.alibaba.chaosblade.box.common.infrastructure.constant.SceneState;
import com.alibaba.chaosblade.box.common.infrastructure.domain.scene.SceneQueryRequest;
import com.alibaba.chaosblade.box.common.infrastructure.domain.scene.SceneUpdateRequest;
import com.alibaba.chaosblade.box.dao.infrastructure.monitor.trace.Trackers;
import com.alibaba.chaosblade.box.common.infrastructure.util.ApplicationStartUpConfig;
import com.alibaba.chaosblade.box.dao.infrastructure.app.LocalChaosAppLoader;
import com.alibaba.chaosblade.box.dao.infrastructure.app.ChaosAppLoader;
import com.alibaba.chaosblade.box.dao.infrastructure.app.function.sync.ChaosAppSynchronizer;
import com.alibaba.chaosblade.box.dao.infrastructure.experiment.task.flow.FlowEngine;
import com.alibaba.chaosblade.box.dao.model.SceneDO;
import com.alibaba.chaosblade.box.dao.model.base.PageableQueryWrapper;
import com.alibaba.chaosblade.box.dao.model.base.PageableResponse;
import com.alibaba.chaosblade.box.dao.repository.SceneFunctionParameterRepository;
import com.alibaba.chaosblade.box.dao.repository.SceneFunctionRepository;
import com.alibaba.chaosblade.box.dao.repository.SceneRepository;
import com.alibaba.chaosblade.box.service.SceneService;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.google.common.collect.Lists;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.time.StopWatch;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;

/**
 * @author sunju
 *
 */
@Slf4j
@Service
@Setter
public class SceneServiceImpl implements SceneService, ApplicationListener<ContextRefreshedEvent>, InitializingBean {

    @Resource
    protected SceneRepository sceneRepository;

    @Resource
    protected SceneFunctionRepository sceneFunctionRepository;

    @Resource
    protected SceneFunctionParameterRepository sceneFunctionParameterRepository;

    @Resource
    private ChaosAppSynchronizer chaosAppSynchronizer;

    @Resource
    private Trackers trackers;

    @Resource
    private ApplicationStartUpConfig applicationStartUpConfig;

    @Autowired
    private FlowEngine flowEngine;


    private final AtomicBoolean inited = new AtomicBoolean(false);

    @Override
    public void syncChaosAppScenes() {
        if (inited.get()) {
            return;
        }

        if (!inited.compareAndSet(false, true)) {
            return;
        }

        log.info("[SYNC]Start to sync all scenes.");
        ChaosAppLoader loader = LocalChaosAppLoader.getInstance();
        StopWatch stopWatch = StopWatch.createStarted();
        loader.getAllChaosAppDescriptors()
            .forEach(descriptor -> {
                try {
                    chaosAppSynchronizer.syncSceneFunctions(SceneDO.from(descriptor));

                } catch (Exception e) {
                    log.error("[SYNC]Sync scene failed. namespace: " + descriptor.getNamespace(), e);
                }
            });
        stopWatch.stop();
        log.info("Sync all scenes cost:" + stopWatch.getTime(TimeUnit.SECONDS) + "s");
    }

    @Override
    public SceneDO queryScene(String sceneId) {
        return sceneRepository.findBySceneId(sceneId)
            .map(scene -> {
                scene.setFunctions(
                    sceneFunctionRepository.findBySceneId(scene.getSceneId())
                        .stream()
                        .peek(function ->
                            sceneFunctionParameterRepository.findAllParamsByFunctionId(function.getFunctionId())
                                .ifPresent(function::setParameters)
                        )
                        .collect(Collectors.toList())
                );
                return scene;
            })
            .orElse(null);
    }

    @Override
    public SceneDO queryBaseScene(String sceneId) {
        return sceneRepository.findBySceneId(sceneId).orElse(null);
    }

    @Override
    public List<SceneDO> search(String key) {
        return sceneRepository.search(key, SceneState.ACTIVE).orElse(Lists.newArrayList());
    }

    @Override
    public List<SceneDO> searchDraft(String key) {
        return sceneRepository.search(key, SceneState.DRAFT).orElse(Lists.newArrayList());
    }

    @Override
    public List<SceneDO> queryScenes() {
        return sceneRepository.queryAll().orElse(Lists.newArrayList());
    }

    @Override
    public PageableResponse<SceneDO> queryScenes(int pageNo, int pageSize, SceneQueryRequest queryRequest) {
        PageableQueryWrapper<SceneQueryRequest> query = PageableQueryWrapper.of(queryRequest)
            .pageNumber(pageNo)
            .pageSize(pageSize);

        return sceneRepository.getScenes(query);
    }

    @Override
    public List<SceneDO> queryUserScenes(ChaosUser user) {
        return sceneRepository.findByUser(user).orElse(Lists.newArrayList());
    }

    @Override
    public boolean updateSceneBySceneId(SceneUpdateRequest request) {
        SceneDO scene = new SceneDO();
        scene.setState(request.getState());
        scene.setSceneId(request.getSceneId());
        scene.setCode(request.getCode());
        scene.setName(request.getName());
        scene.setDescription(request.getDescription());
        scene.setVersion(request.getVersion());

        ChaosUser user = request.getUser();
        scene.setUserId(user.getUserId());
        boolean result = sceneRepository.updateBySceneId(scene);
        trackers.track(
            ChangeActionType.Update,
            ChangeOperatorType.USER,
            request.getUser().getCurrentUserId(),
            ChangeTargetType.SCENE_FUNCTION,
            request.getSceneId(),
            null,
            null,
            null
        );

        return result;
    }

    @Override
    public boolean deleteSceneBySceneId(ChaosUser user, String sceneId) {
        boolean result = sceneRepository.deleteBySceneId(sceneId);

        trackers.track(
            ChangeActionType.DELETE,
            ChangeOperatorType.USER,
            user.getCurrentUserId(),
            ChangeTargetType.SCENE,
            sceneId,
            null,
            null,
            null
        );

        return result;
    }

    @Override
    public String findOrCreateIfNotExistBySceneCode(ChaosUser user, String sceneCode) {
        Optional<SceneDO> optionalSceneDO = sceneRepository.findByCode(sceneCode);
        if (optionalSceneDO.isPresent()) {
            return optionalSceneDO.get().getSceneId();
        } else {
            return createNewSceneForUser(user, sceneCode);
        }
    }

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {

    }

    private String createNewSceneForUser(ChaosUser user, String sceneCode) {
        SceneDO scene = new SceneDO();
        String sceneId = IdWorker.getIdStr();

        scene.setUserId(user.getUserId());

        scene.setName("DefaultScene_" + user.getCurrentUserId());
        scene.setState(SceneState.ACTIVE);
        scene.setIsPublic(false);
        scene.setCode(sceneCode);
        scene.setSceneId(sceneId);
        sceneRepository.add(scene);
        return sceneId;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        if (applicationStartUpConfig.isSync(ApplicationStartUpConfig.SyncFunctionType.UserApp)) {
            this.syncChaosAppScenes();
        }
    }
}
