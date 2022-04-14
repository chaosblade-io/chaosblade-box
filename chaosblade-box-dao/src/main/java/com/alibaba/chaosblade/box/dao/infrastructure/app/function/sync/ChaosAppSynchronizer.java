package com.alibaba.chaosblade.box.dao.infrastructure.app.function.sync;

import com.alibaba.chaosblade.box.common.common.constant.ChaosFunctionConstant;
import com.alibaba.chaosblade.box.common.common.enums.CommonErrorCode;
import com.alibaba.chaosblade.box.common.infrastructure.constant.CommonConstant;
import com.alibaba.chaosblade.box.common.infrastructure.constant.PermissionTypes;
import com.alibaba.chaosblade.box.common.infrastructure.constant.SceneState;
import com.alibaba.chaosblade.box.common.infrastructure.domain.scene.SceneAuthorizedQueryRequest;
import com.alibaba.chaosblade.box.common.infrastructure.domain.scene.SceneAuthorizedUpdateRequest;
import com.alibaba.chaosblade.box.common.infrastructure.exception.ChaosException;
import com.alibaba.chaosblade.box.common.infrastructure.util.CollectionUtil;
import com.alibaba.chaosblade.box.common.infrastructure.util.EnvironmentUtil;
import com.alibaba.chaosblade.box.common.infrastructure.util.RetryUtil;
import com.alibaba.chaosblade.box.dao.infrastructure.app.function.*;
import com.alibaba.chaosblade.box.dao.infrastructure.event.ChaosEventDispatcher;
import com.alibaba.chaosblade.box.dao.infrastructure.manager.SceneFunctionCategoryManager;
import com.alibaba.chaosblade.box.dao.model.SceneAuthorizedDO;
import com.alibaba.chaosblade.box.dao.model.SceneDO;
import com.alibaba.chaosblade.box.dao.model.SceneFunctionDO;
import com.alibaba.chaosblade.box.dao.model.SceneFunctionParameterDO;
import com.alibaba.chaosblade.box.dao.repository.SceneAuthorizedRepository;
import com.alibaba.chaosblade.box.dao.repository.SceneFunctionParameterRepository;
import com.alibaba.chaosblade.box.dao.repository.SceneFunctionRepository;
import com.alibaba.chaosblade.box.dao.repository.SceneRepository;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.sql.Timestamp;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author sunju
 *
 */
@Slf4j
@Component
public class ChaosAppSynchronizer extends BaseSceneSynchronizer {

    private static final int RETRY_TIMES = 3;

    @Resource
    private SceneRepository sceneRepository;

    @Resource
    private SceneFunctionRepository sceneFunctionRepository;

    @Resource
    private SceneFunctionParameterRepository sceneFunctionParameterRepository;

    @Resource
    private SceneAuthorizedRepository sceneAuthorizedRepository;

    @Resource
    private EnvironmentUtil environmentUtil;

    @Resource
    private SceneSynchronousHelper sceneSynchronousHelper;

    @Autowired
    private ChaosEventDispatcher chaosEventDispatcher;

    @Autowired
    private SceneFunctionCategoryManager sceneFunctionCategoryManager;

    @Override
    public void syncSceneFunctions(SceneDO scene) throws ChaosException {
        if (null == scene) {
            log.info("[ChaosAppSynchronizer] Scene is null.");
            return;
        }

        log.info("[ChaosAppSynchronizer] Start to sync chaosapp.");

        SceneDO exist = null;

        Optional<SceneDO> sceneOptional = sceneRepository.findByCode(scene.getCode());
        if (sceneOptional.isPresent()) {
            exist = sceneOptional.get();
            scene.setSceneId(exist.getSceneId());
        } else {
            scene.setSceneId(IdWorker.getIdStr());
        }

        log.info("[ChaosAppSynchronizer] Sync All functions.");
        syncSceneFunction(scene, wrapper(scene, scene.getFunctions()));

        if (null != exist) {
            updateScene(exist, scene);
        } else {
            addScene(scene);
        }

        log.info("[ChaosAppSynchronizer] Sync chaosapp finished.");
    }

    private List<SceneFunctionDO> wrapper(SceneDO scene, List<SceneFunctionDO> functions) {

        if (!CollectionUtil.isNullOrEmpty(functions)) {
            return functions.stream()
                .peek(function -> {
                    if (Objects.equals(function.getEnabled(), ChaosFunctionConstant.ENABLED_READY)) {
                        function.setEnabled(ChaosFunctionConstant.ENABLED_PUBLISH);
                    }
                    if (Strings.isNullOrEmpty(function.getUserId())) {
                        function.setUserId(scene.getUserId());
                    }
                    if (Strings.isNullOrEmpty(function.getUserId())) {
                        function.setUserId(CommonConstant.CHAOS_SCENE_OWNER);
                    }

                    sceneSynchronousHelper.useDefaultConfig(function);
                })
                .collect(Collectors.toList());
        }
        return functions;
    }


    private void syncSceneFunction(SceneDO scene, List<SceneFunctionDO> functions) throws ChaosException {

        List<SceneFunctionDO> existFunctions = sceneFunctionRepository.findBySceneId(scene.getSceneId());

        List<String> codes = functions.stream().map(SceneFunctionDO::getCode).collect(Collectors.toList());

        for (SceneFunctionDO sceneFunction : existFunctions) {
            // 删除已经不存在的app
            if (!codes.contains(sceneFunction.getCode())) {
                log.info("[ChaosAppSynchronizer] App not exists. code: " + scene.getCode());
                boolean deleteResult = RetryUtil.retryIfReturnFalse(
                    () -> sceneFunctionRepository.deleteByFunctionId(sceneFunction.getFunctionId()), RETRY_TIMES);
                if (!deleteResult) {
                    throw new ChaosException(CommonErrorCode.B_UPDATE_MINIAPP_FAILED,
                        "Delete chaosapp failed before sync.");
                }
                chaosEventDispatcher.fireEvent(new SceneFunctionDeletedEvent(sceneFunction, true));
            }
        }

        for (SceneFunctionDO function : functions) {
            if (ignoreSyncSceneFunction(function)) { continue; }
            try {
                log.info("[ChaosAppSynchronizer] Update function. Name: " + function.getName());

                SceneFunctionDO existFunction = null;

                Optional<SceneFunctionDO> sceneFunctionOptional = sceneFunctionRepository.findByCode(
                    function.getCode());

                if (sceneFunctionOptional.isPresent()) {
                    existFunction = sceneFunctionOptional.get();
                    function.setFunctionId(existFunction.getFunctionId());
                } else {
                    function.setFunctionId(IdWorker.getIdStr());
                }

                syncSceneFunctionParameters(function);

                if (null != existFunction) {
                    existFunction.setSource(ChaosFunctionConstant.SOURCE_CUSTOM_APP);
                    existFunction.setCategories(function.getCategories());
                    existFunction.setSupportScopeTypes(function.getSupportScopeTypes());
                    existFunction.setAgentRequired(function.getAgentRequired());
                    existFunction.setName(function.getName());
                    existFunction.setDescription(function.getDescription());
                    existFunction.setDependencyList(function.getDependencyList());
                    existFunction.setPhaseFlag(function.getPhaseFlag());

                    if (Strings.isNullOrEmpty(existFunction.getUserId())) {
                        existFunction.setUserId(function.getUserId());
                    }
                    if (Objects.equals(existFunction.getEnabled(), ChaosFunctionConstant.ENABLED_READY) && Objects.equals(
                        existFunction.getSource(), ChaosFunctionConstant.SOURCE_CUSTOM_APP)) {
                        existFunction.setEnabled(ChaosFunctionConstant.ENABLED_PUBLISH);
                    }

                    addAuthorizedRecord(existFunction, Boolean.TRUE.equals(scene.getIsPublic()));
                    updateSceneFunction(existFunction, function);
                    chaosEventDispatcher.fireEvent(new SceneFunctionUpdatedEvent(function, true));
                } else {
                    addAuthorizedRecord(function, Boolean.TRUE.equals(scene.getIsPublic()));
                    addSceneFunction(function);
                    chaosEventDispatcher.fireEvent(new SceneFunctionAddedEvent(function, true));
                }
            } catch (Exception e) {
                log.error("[ChaosAppSynchronizer] Sync single function failed. code: " + function.getCode(), e);
            }
        }
        sceneFunctionCategoryManager.rebindFunctionCategories(functions);
    }

    private void syncSceneFunctionParameters(SceneFunctionDO function) throws ChaosException {
        Optional<List<SceneFunctionParameterDO>> parametersOptional = sceneFunctionParameterRepository
            .findAllParamsByFunctionId(function.getFunctionId());
        List<SceneFunctionParameterDO> sceneFunctionParameters = parametersOptional.orElse(Lists.newArrayList());

        if (!sceneFunctionParameters.isEmpty()) {
            // parameters存在，比较后更新
            List<String> aliases = function.getParameters()
                .stream()
                .map(SceneFunctionParameterDO::getAlias)
                .collect(Collectors.toList());

            for (SceneFunctionParameterDO sceneFunctionParameter : sceneFunctionParameters) {
                if (!aliases.contains(sceneFunctionParameter.getAlias())) {
                    boolean deleteResult = RetryUtil.retryIfReturnFalse(() -> sceneFunctionParameterRepository
                            .deleteByParameterId(function.getFunctionId(), sceneFunctionParameter.getParameterId()),
                        RETRY_TIMES);
                    if (!deleteResult) {
                        throw new ChaosException(CommonErrorCode.B_UPDATE_MINIAPP_PARAMETER,
                            "Delete chaosapp parameter failed before sync.");
                    }
                }
            }

            for (SceneFunctionParameterDO parameter : function.getParameters()) {
                List<SceneFunctionParameterDO> existParameters = sceneFunctionParameterRepository.findAllByAlias(
                    function.getFunctionId(), parameter.getAlias());
                if (!CollectionUtil.isNullOrEmpty(existParameters)) {
                    if (existParameters.size() == 1) {
                        updateSceneFunctionParameter(function.getUserId(), existParameters.get(0), parameter);
                    } else {
                        // 处理脏数据，同一个小程序下，不存在alias相同的参数
                        existParameters.forEach(
                            param -> sceneFunctionParameterRepository
                                .deleteByParameterId(function.getFunctionId(), param.getParameterId()));
                        addSceneFunctionParameter(function.getUserId(), function.getFunctionId(), parameter);
                    }
                } else {
                    addSceneFunctionParameter(function.getUserId(), function.getFunctionId(), parameter);
                }
            }
        } else {
            for (SceneFunctionParameterDO parameter : function.getParameters()) {
                addSceneFunctionParameter(function.getUserId(), function.getFunctionId(), parameter);
            }
        }
    }

    private void updateScene(SceneDO existScene, SceneDO scene) throws ChaosException {
        if (!Strings.isNullOrEmpty(scene.getUserId())) {
            existScene.setUserId(scene.getUserId());
        } else {
            existScene.setUserId(CommonConstant.CHAOS_SCENE_OWNER);
        }

        existScene.setIsPublic(scene.getIsPublic());
        existScene.setVersion(scene.getVersion());

        if (!scene.getCode().equals(existScene.getCode())) {
            existScene.setCode(scene.getCode());
        }
        if (Strings.isNullOrEmpty(existScene.getName())) {
            existScene.setName(scene.getName());
        }
        if (Strings.isNullOrEmpty(existScene.getDescription())) {
            existScene.setDescription(scene.getDescription());
        }

        boolean result = RetryUtil.retryIfReturnFalse(() -> sceneRepository.updateByCode(existScene), RETRY_TIMES);
        if (!result) {
            throw new ChaosException(CommonErrorCode.B_UPDATE_MINIAPP_FAILED, "Sync chaosapp group failed.");
        }
    }

    private void addScene(SceneDO scene) throws ChaosException {
        scene.setState(SceneState.DRAFT);
        if (Strings.isNullOrEmpty(scene.getUserId())) {
            scene.setUserId(CommonConstant.CHAOS_SCENE_OWNER);
        }

        boolean result = RetryUtil.retryIfReturnFalse(() -> sceneRepository.add(scene), RETRY_TIMES);
        if (!result) {
            throw new ChaosException(CommonErrorCode.B_CREATE_MINIAPP_FAILED, "Sync chaosapp group failed.");
        }
    }

    private void updateSceneFunction(SceneFunctionDO existSceneFunction, SceneFunctionDO sceneFunction)
        throws ChaosException {
        if (!Objects.equals(sceneFunction.getCode(), existSceneFunction.getCode())) {
            existSceneFunction.setCode(sceneFunction.getCode());
        }
        if (!Objects.equals(sceneFunction.getUserId(), existSceneFunction.getUserId())) {
            existSceneFunction.setUserId(sceneFunction.getUserId());
        }
        if (!Objects.equals(sceneFunction.getVersion(), existSceneFunction.getVersion())) {
            existSceneFunction.setVersion(sceneFunction.getVersion());
        }

        boolean result = RetryUtil.retryIfReturnFalse(() -> sceneFunctionRepository.updateByCode(existSceneFunction),
            RETRY_TIMES);
        if (!result) {
            throw new ChaosException(CommonErrorCode.B_UPDATE_MINIAPP_FAILED, "Sync chaosapp failed.");
        }
    }

    private void addSceneFunction(SceneFunctionDO sceneFunction) throws ChaosException {
        boolean sceneFunctionResult = RetryUtil.retryIfReturnFalse(() -> sceneFunctionRepository.add(sceneFunction));
        if (!sceneFunctionResult) {
            throw new ChaosException(CommonErrorCode.B_CREATE_MINIAPP_FAILED, "Sync chaosapp failed.");
        }
    }

    private void addAuthorizedRecord(SceneFunctionDO sceneFunction, boolean isPublic) throws ChaosException {
        SceneAuthorizedQueryRequest request = new SceneAuthorizedQueryRequest();
        request.setFunctionId(sceneFunction.getFunctionId());
        request.setGrantTo(sceneFunction.getUserId());
        request.setIsPublic(isPublic);
        request.setIsDelete(false);

        List<SceneAuthorizedDO> authorizedRecords = sceneAuthorizedRepository.getAuthorizedRecords(request);

        if (!CollectionUtil.isNullOrEmpty(authorizedRecords)) {
            updateAuthorizedRecord(authorizedRecords, sceneFunction,isPublic);
            return;
        }

        SceneAuthorizedDO authorized = new SceneAuthorizedDO();
        authorized.setFunctionId(sceneFunction.getFunctionId());
        authorized.setFunctionName(sceneFunction.getName());
        authorized.setFunctionCode(sceneFunction.getCode());
        authorized.setGrantFrom(sceneFunction.getUserId());
        authorized.setGrantTo(sceneFunction.getUserId());
        authorized.setPermission(PermissionTypes.ALL);
        authorized.setPhase(sceneFunction.getPhaseFlag());
        authorized.setSource(ChaosFunctionConstant.SOURCE_CUSTOM_APP);
        authorized.setEnabled(sceneFunction.getEnabled());
        authorized.setSupportScopeTypeList(sceneFunction.getSupportScopeTypeList());
        authorized.setIsDelete(sceneFunction.getIsDelete());
        authorized.setIsPublic(isPublic);
        if (null != sceneFunction.getGmtCreate()) {
            authorized.setFunctionCreateTime(sceneFunction.getGmtCreate());
        } else {
            authorized.setFunctionCreateTime(new Timestamp(System.currentTimeMillis()));
        }

        boolean result = RetryUtil.retryIfReturnFalse(() -> sceneAuthorizedRepository.add(authorized), RETRY_TIMES);
        if (!result) {
            throw new ChaosException(CommonErrorCode.B_CREATE_MINIAPP_FAILED, "Sync chaosapp failed.");
        }
    }

    private void updateAuthorizedRecord(List<SceneAuthorizedDO> authorizedRecords, SceneFunctionDO sceneFunction,
        boolean isPublic)
        throws ChaosException {
        List<String> authorizedIds = authorizedRecords
            .stream()
            .map(SceneAuthorizedDO::getAuthorizedId)
            .collect(Collectors.toList());

        SceneAuthorizedUpdateRequest request = new SceneAuthorizedUpdateRequest();
        request.setAuthorizedIds(authorizedIds);
        request.setFunctionName(sceneFunction.getName());
        request.setFunctionCode(sceneFunction.getCode());
        request.setPhase(sceneFunction.getPhaseFlag());
        request.setSource(ChaosFunctionConstant.SOURCE_CUSTOM_APP);
        request.setEnabled(sceneFunction.getEnabled());
        request.setSupportScopeTypes(sceneFunction.getSupportScopeTypeList());
        request.setIsDelete(sceneFunction.getIsDelete());
        request.setIsPublic(isPublic);

        boolean result = RetryUtil.retryIfReturnFalse(() -> sceneAuthorizedRepository.updateByAuthorizedId(request));
        if (!result) {
            throw new ChaosException(CommonErrorCode.B_UPDATE_MINIAPP_FAILED);
        }
    }

    private void updateSceneFunctionParameter(String userId, SceneFunctionParameterDO existParameter,
        SceneFunctionParameterDO parameter) throws ChaosException {
        existParameter.setName(parameter.getName());
        existParameter.setAlias(parameter.getAlias());
        existParameter.setDescription(parameter.getDescription());
        existParameter.setSequence(parameter.getSequence());
        existParameter.setComponent(parameter.getComponent());

        boolean result = RetryUtil.retryIfReturnFalse(() -> sceneFunctionParameterRepository.update(existParameter),
            RETRY_TIMES);
        if (!result) {
            throw new ChaosException(CommonErrorCode.B_UPDATE_MINIAPP_PARAMETER, "Sync chaosapp parameter failed.");
        }
    }

    private void addSceneFunctionParameter(String userId, String functionId, SceneFunctionParameterDO parameter)
        throws ChaosException {
        parameter.setFunctionId(functionId);
        boolean result = RetryUtil.retryIfReturnFalse(() -> sceneFunctionParameterRepository.add(parameter),
            RETRY_TIMES);
        if (!result) {
            throw new ChaosException(CommonErrorCode.B_ADD_MINIAPP_PARAMETER, "Sync chaosapp parameter failed.");
        }
    }

}
