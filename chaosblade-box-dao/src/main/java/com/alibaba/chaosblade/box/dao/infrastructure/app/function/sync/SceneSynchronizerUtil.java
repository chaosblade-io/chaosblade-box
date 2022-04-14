package com.alibaba.chaosblade.box.dao.infrastructure.app.function.sync;

import com.alibaba.chaosblade.box.common.common.constant.ChaosFunctionConstant;
import com.alibaba.chaosblade.box.common.common.domain.user.ChaosUser;
import com.alibaba.chaosblade.box.common.common.enums.CommonErrorCode;
import com.alibaba.chaosblade.box.common.common.util.MiniAppUtils;
import com.alibaba.chaosblade.box.common.infrastructure.constant.PermissionTypes;
import com.alibaba.chaosblade.box.common.infrastructure.domain.scene.SceneAuthorizedQueryRequest;
import com.alibaba.chaosblade.box.common.infrastructure.domain.scene.SceneAuthorizedUpdateRequest;
import com.alibaba.chaosblade.box.common.infrastructure.exception.ChaosException;
import com.alibaba.chaosblade.box.common.infrastructure.util.CollectionUtil;
import com.alibaba.chaosblade.box.common.infrastructure.util.RetryUtil;
import com.alibaba.chaosblade.box.dao.infrastructure.app.function.SceneFunctionAddedEvent;
import com.alibaba.chaosblade.box.dao.infrastructure.event.ChaosEventDispatcher;
import com.alibaba.chaosblade.box.dao.infrastructure.utils.DebugUtils;
import com.alibaba.chaosblade.box.dao.model.SceneAuthorizedDO;
import com.alibaba.chaosblade.box.dao.model.SceneFunctionDO;
import com.alibaba.chaosblade.box.dao.model.SceneFunctionParameterDO;
import com.alibaba.chaosblade.box.dao.repository.SceneAuthorizedRepository;
import com.alibaba.chaosblade.box.dao.repository.SceneFunctionParameterRepository;
import com.alibaba.chaosblade.box.dao.repository.SceneFunctionRepository;
import com.google.common.collect.Sets;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author haibin.lhb
 *
 *
 */
@Component
@Slf4j
public class SceneSynchronizerUtil {
    private static final int RETRY_TIMES = 3;

    @Resource
    private SceneAuthorizedRepository sceneAuthorizedRepository;
    @Resource
    private SceneFunctionRepository sceneFunctionRepository;

    @Autowired

    private ChaosEventDispatcher chaosEventDispatcher;

    @Resource
    private SceneFunctionParameterRepository sceneFunctionParameterRepository;

    public void addAuthorizedRecord(SceneFunctionDO sceneFunction) throws ChaosException {
        SceneAuthorizedQueryRequest request = new SceneAuthorizedQueryRequest();
        request.setFunctionId(sceneFunction.getFunctionId());
        request.setGrantTo(ChaosUser.SYSTEM.getUserId());
        List<SceneAuthorizedDO> authorizedRecords = sceneAuthorizedRepository.getAuthorizedRecords(request);
        if (!CollectionUtil.isNullOrEmpty(authorizedRecords)) {
            updateAuthorizedRecord(authorizedRecords, sceneFunction);
            return;
        }
        SceneAuthorizedDO authorized = new SceneAuthorizedDO();
        authorized.setFunctionId(sceneFunction.getFunctionId());
        authorized.setFunctionName(sceneFunction.getName());
        authorized.setFunctionCode(sceneFunction.getCode());
        authorized.setGrantFrom(ChaosUser.SYSTEM.getUserId());
        authorized.setGrantTo(ChaosUser.SYSTEM.getUserId());
        authorized.setEnabled(sceneFunction.getEnabled());
        authorized.setPhase(sceneFunction.getPhaseFlag());
        authorized.setIsPublic(true);
        setAuthorizedRecord(sceneFunction, authorized);

        boolean result = RetryUtil.retryIfReturnFalse(() -> sceneAuthorizedRepository.add(authorized), RETRY_TIMES);
        if (!result) {
            throw new ChaosException(CommonErrorCode.B_CREATE_MINIAPP_FAILED, "Sync authorized record failed.");
        }
    }

    private void setAuthorizedRecord(SceneFunctionDO sceneFunction, SceneAuthorizedDO authorized) {
        if (MiniAppUtils.isFromChaosBlade(sceneFunction.getCode())) {
            authorized.setSource(ChaosFunctionConstant.SOURCE_CHAOS_BLADE);
            if (sceneFunction.isSupport(ChaosFunctionConstant.PHASE_FLAG_RECOVER) && !MiniAppUtils.isJvmAgentUninstall(sceneFunction.getCode())) {
                authorized.setIsPublic(false);
            }
        } else if (MiniAppUtils.isFromLitmusChaos(sceneFunction.getCode())) {
            authorized.setSource(ChaosFunctionConstant.SOURCE_LITMUS_CHAOS);
            if (sceneFunction.isSupport(ChaosFunctionConstant.PHASE_FLAG_RECOVER) && !MiniAppUtils.isJvmAgentUninstall(sceneFunction.getCode())) {
                authorized.setIsPublic(false);
            }
        } else {
            authorized.setSource(ChaosFunctionConstant.SOURCE_CUSTOM_APP);
        }
        authorized.setIsDelete(false);
        authorized.setSupportScopeTypeList(sceneFunction.getSupportScopeTypeList());
        authorized.setPermission(PermissionTypes.ALL);
        if (null != sceneFunction.getGmtCreate()) {
            authorized.setFunctionCreateTime(sceneFunction.getGmtCreate());
        } else {
            authorized.setFunctionCreateTime(new Timestamp(System.currentTimeMillis()));
        }
    }

    public void updateSceneFunction(SceneFunctionDO existSceneFunction, SceneFunctionDO update, boolean onStarted) {
        boolean result = RetryUtil.retryIfReturnFalse(
            () -> sceneFunctionRepository.updateByCode(update),
            RETRY_TIMES);
        if (!result) {
            throw new ChaosException(CommonErrorCode.B_UPDATE_MINIAPP_FAILED, "Sync scene function failed.");
        }
        updateParameters(update, existSceneFunction, onStarted);
    }

    public void checkSceneFunctionBeforePersistence(SceneFunctionDO updateSceneFunction) {
        if (updateSceneFunction.isSupport(ChaosFunctionConstant.PHASE_FLAG_ATTACK)) {
            if (updateSceneFunction.getDependencyList().isEmpty()) {
                throw new IllegalArgumentException(
                    "scene function sync error,code:" + updateSceneFunction.getCode() +
                        " is attack phase, should have dependencies(such as recovery or "
                        + "prepare)");
            }
        }
    }

    private void updateParameters(SceneFunctionDO sceneFunction, SceneFunctionDO existSceneFunction, boolean onStarted)
        throws ChaosException {
        List<SceneFunctionParameterDO> existParameters = existSceneFunction.getParameters();
        if (existParameters.isEmpty()) {
            addParameters(sceneFunction, sceneFunction.getParameters());
        } else {
            List<SceneFunctionParameterDO> newFunction = deleteDuplicateParams(existSceneFunction,
                existParameters);
            existSceneFunction.setParameters(newFunction);
            Map<String, SceneFunctionParameterDO> parameterMaps = sceneFunction.getParameters()
                .stream()
                .collect(Collectors.toMap(
                    SceneFunctionParameterDO::getAlias,
                    Function.identity(),
                    (k1, k2) -> k2
                ));
            Map<String, SceneFunctionParameterDO> existParameterMaps = newFunction.stream()
                .collect(Collectors.toMap(
                    SceneFunctionParameterDO::getAlias,
                    Function.identity(),
                    (k1, k2) -> k2
                ));
            findAndUpdateParameters(existSceneFunction, parameterMaps, existParameterMaps);
            findAndRemoveParameters(existSceneFunction.getFunctionId(), parameterMaps, existParameterMaps);
            DebugUtils.recordSceneFunctionSync("before-add-exist", existSceneFunction);
            DebugUtils.recordSceneFunctionSync("before-add-update", sceneFunction);
            findAndAddParameters(existSceneFunction, parameterMaps, existParameterMaps);
        }
    }

    private List<SceneFunctionParameterDO> deleteDuplicateParams(SceneFunctionDO sceneFunctionDO,
        List<SceneFunctionParameterDO> existParameters) {
        List<SceneFunctionParameterDO> sceneFunctionParameterDOS = new ArrayList<>();
        Map<String, List<SceneFunctionParameterDO>> aliasToParams = existParameters.stream().collect(
            Collectors.groupingBy(SceneFunctionParameterDO::getAlias));
        aliasToParams.entrySet().forEach(new Consumer<Entry<String, List<SceneFunctionParameterDO>>>() {
            @Override
            public void accept(Entry<String, List<SceneFunctionParameterDO>> stringListEntry) {
                if (stringListEntry.getValue().size() == 1) {
                    sceneFunctionParameterDOS.add(stringListEntry.getValue().get(0));
                } else {
                    sceneFunctionParameterRepository.realDeleteByFunctionIdAndAlias(sceneFunctionDO.getFunctionId(),
                        stringListEntry.getKey());
                    log.info("delete dup alias,code:{},alias:{},count:{}", sceneFunctionDO.getCode(),
                        stringListEntry.getKey(), stringListEntry.getValue().size());
                }
            }
        });
        return sceneFunctionParameterDOS;
    }

    private void findAndUpdateParameters(SceneFunctionDO sceneFunctionDO,
        Map<String, SceneFunctionParameterDO> groupingParameters,
        Map<String, SceneFunctionParameterDO> groupingExistParameters) throws ChaosException {
        Set<String> shouldUpdateParameterAlias = Sets.intersection(groupingParameters.keySet(),
            groupingExistParameters.keySet());
        for (String alias : shouldUpdateParameterAlias) {
            SceneFunctionParameterDO parameter = groupingParameters.get(alias);
            parameter.setParameterId(groupingExistParameters.get(alias).getParameterId());
            parameter.setFunctionId(sceneFunctionDO.getFunctionId());
            log.info("update param,alias:{},id:{},code:{},functionId:{}", parameter.getAlias(),
                parameter.getParameterId(), sceneFunctionDO.getCode(),
                parameter.getFunctionId());
            boolean result = RetryUtil.retryIfReturnFalse(
                () -> sceneFunctionParameterRepository.updateByParameterId(parameter), RETRY_TIMES);
            if (!result) {
                throw new ChaosException(CommonErrorCode.B_UPDATE_MINIAPP_PARAMETER,
                    "Sync parameters failed.");
            }
        }
    }

    private void findAndAddParameters(SceneFunctionDO functionDO,
        Map<String, SceneFunctionParameterDO> groupingParameters,
        Map<String, SceneFunctionParameterDO> groupingExistParameters) throws ChaosException {
        Set<String> shouldAddParameterAlias = Sets.difference(groupingParameters.keySet(),
            groupingExistParameters.keySet());
        if (!shouldAddParameterAlias.isEmpty()) {
            log.info("functionId:{},code:{},,addParamsAlias:{}", functionDO.getFunctionId(), functionDO.getCode(),
                shouldAddParameterAlias);
        }
        for (String alias : shouldAddParameterAlias) {
            SceneFunctionParameterDO parameter = groupingParameters.get(alias);
            if (null != parameter) {
                parameter.setFunctionId(functionDO.getFunctionId());
                parameter.setParameterId(null);
                if (!sceneFunctionParameterRepository.findByAlias(functionDO.getFunctionId(), alias).isEmpty()) {
                    return;
                }
                boolean result = sceneFunctionParameterRepository.add(parameter);
                log.info("functionId:{},code:{},,addParamsAlia:{}", functionDO.getFunctionId(), functionDO.getCode(),
                    shouldAddParameterAlias);
                if (!result) {
                    throw new ChaosException(CommonErrorCode.B_ADD_MINIAPP_PARAMETER, "Sync parameters failed.");
                }
            }
        }
    }

    private void findAndRemoveParameters(String functionId, Map<String, SceneFunctionParameterDO> groupingParameters,
        Map<String, SceneFunctionParameterDO> groupingExistParameters) throws ChaosException {
        Set<String> shouldRemoveParameterAlias = Sets.difference(groupingExistParameters.keySet(),
            groupingParameters.keySet());
        for (String alias : shouldRemoveParameterAlias) {
            SceneFunctionParameterDO parameter = groupingExistParameters.get(alias);
            if (null != parameter) {
                boolean result = RetryUtil.retryIfReturnFalse(
                    () -> sceneFunctionParameterRepository.deleteByParameterId(functionId, parameter.getParameterId()),
                    RETRY_TIMES);
                if (!result) {
                    throw new ChaosException(CommonErrorCode.B_DELETE_MINIAPP_PARAMETER,
                        "Sync parameters failed.");
                }
            }
        }
    }

    private void updateAuthorizedRecord(List<SceneAuthorizedDO> authorizedRecords, SceneFunctionDO sceneFunction)
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
        request.setSource(ChaosFunctionConstant.SOURCE_CHAOS_BLADE);
        request.setSupportScopeTypes(sceneFunction.getSupportScopeTypeList());
        request.setEnabled(sceneFunction.getEnabled());
        request.setIsDelete(sceneFunction.getIsDelete());
        if (MiniAppUtils.isFromChaosBlade(sceneFunction.getCode())) {
            request.setSource(ChaosFunctionConstant.SOURCE_CHAOS_BLADE);
            if (sceneFunction.isSupport(ChaosFunctionConstant.PHASE_FLAG_RECOVER)) {
                request.setIsPublic(false);
            }
        } else if(MiniAppUtils.isFromLitmusChaos(sceneFunction.getCode())) {
            request.setSource(ChaosFunctionConstant.SOURCE_LITMUS_CHAOS);
            if (sceneFunction.isSupport(ChaosFunctionConstant.PHASE_FLAG_RECOVER)) {
                request.setIsPublic(false);
            }
        } else {
            request.setSource(ChaosFunctionConstant.SOURCE_CUSTOM_APP);
        }
        boolean result = RetryUtil.retryIfReturnFalse(() -> sceneAuthorizedRepository.updateByAuthorizedId(request));
        if (!result) {
            throw new ChaosException(CommonErrorCode.B_UPDATE_MINIAPP_FAILED);
        }
    }

    private void addParameters(SceneFunctionDO sceneFunction, List<SceneFunctionParameterDO> parameters)
        throws ChaosException {
        String functionId = sceneFunction.getFunctionId();
        if (CollectionUtil.isNullOrEmpty(parameters)) {
            return;
        }
        for (SceneFunctionParameterDO parameter : parameters) {
            boolean result = RetryUtil.retryIfReturnFalse(
                () -> sceneFunctionParameterRepository.add(wrapper(functionId, parameter)),
                RETRY_TIMES
            );
            if (!result) {
                throw new ChaosException(CommonErrorCode.B_ADD_MINIAPP_PARAMETER, "Sync parameter failed.");
            }
        }
        sceneFunction.setParameters(parameters);
    }

    public SceneFunctionParameterDO wrapper(String functionId, SceneFunctionParameterDO parameter) {
        parameter.setFunctionId(functionId);
        return parameter;
    }

    public void addFunction(SceneFunctionDO sceneFunction) throws ChaosException {
        addParameters(sceneFunction, sceneFunction.getParameters());
        boolean result = RetryUtil.retryIfReturnFalse(() -> sceneFunctionRepository.add(sceneFunction), RETRY_TIMES);
        if (!result) {
            throw new ChaosException(CommonErrorCode.B_CREATE_MINIAPP_FAILED, "Sync scene function failed.");
        }
        chaosEventDispatcher.fireEvent(new SceneFunctionAddedEvent(sceneFunction, true));
    }
}
