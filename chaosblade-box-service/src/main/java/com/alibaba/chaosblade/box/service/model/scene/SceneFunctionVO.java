package com.alibaba.chaosblade.box.service.model.scene;

import com.alibaba.chaosblade.box.common.common.constant.ChaosFunctionConstant;
import com.alibaba.chaosblade.box.common.infrastructure.domain.scene.SceneFunctionDependency;
import com.alibaba.chaosblade.box.common.infrastructure.domain.scene.SceneFunctionSystemVersion;
import com.alibaba.chaosblade.box.common.infrastructure.util.CollectionUtil;
import com.alibaba.chaosblade.box.dao.infrastructure.model.Script;
import com.alibaba.chaosblade.box.dao.model.SceneFunctionDO;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author sunju
 *
 */
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@NoArgsConstructor
public class SceneFunctionVO {

    String functionId;
    String parentFunctionId;
    String sceneId;
    String code;
    String name;
    String parentName;
    String type;
    String description;
    Boolean agentRequired;
    Integer phaseFlag;
    Integer enabled;
    Integer source;
    String preDepAppCode;
    String nextDepAppCode;
    List<SceneFunctionParameterVO> parameters;
    String version;
    List<SceneFunctionSystemVersion> systemVersions;
    List<String> categoryIds;
    List<Integer> supportScopeTypes;
    Integer usedCount;
    String creator;
    Date gmtCreate;
    List<Integer> supportOsTypes;

    /**
     * @deprecated 小程序会存在不同授权，而且会区分读、写、执行权限，不能单纯的标记是否公开，粒度太粗
     */
    Boolean isPublic;

    List<SceneFunctionAuthorizedVO> authorizedRecords;

    List<SceneFunctionVO> dependencyFunctions;

    /**
     * @deprecated
     */
    Boolean isFissionFunction;

    private Script script;

    ScenePermission permissions;

    public static SceneFunctionVO from(SceneFunctionDO function) {
        if (function == null) {
            return null;
        }

        SceneFunctionVO sceneFunction = new SceneFunctionVO();
        sceneFunction.setSceneId(function.getSceneId());
        sceneFunction.setFunctionId(function.getFunctionId());
        sceneFunction.setParentFunctionId(function.getFunctionId());
        sceneFunction.setCode(function.getCode());
        sceneFunction.setName(function.getName());
        sceneFunction.setParentName(function.getName());
        sceneFunction.setDescription(function.getDescription());
        sceneFunction.setPhaseFlag(function.getPhaseFlag());
        sceneFunction.setAgentRequired(function.getAgentRequired());
        sceneFunction.setEnabled(function.getEnabled());
        sceneFunction.setSource(function.getSource());
        
        if (null != function.getCategoryList()) {
            sceneFunction.setCategoryIds(function.getCategoryList());
        }
        if (null != function.getSupportScopeTypeList()) {
            sceneFunction.setSupportScopeTypes(function.getSupportScopeTypeList());
        }
        if (null != function.getSupportOsList()) {
            sceneFunction.setSupportOsTypes(function.getSupportOsList());
        }

        List<SceneFunctionDependency> dependencies = function.getDependencyList();
        if (!CollectionUtil.isNullOrEmpty(dependencies)) {
            dependencies.stream()
                .filter(dependency -> Objects.equals(SceneFunctionDependency.TYPE_BEFORE, dependency.getType()))
                .findFirst()
                .ifPresent(dependency -> sceneFunction.setPreDepAppCode(dependency.getCode()));

            dependencies.stream()
                .filter(dependency -> Objects.equals(SceneFunctionDependency.TYPE_AFTER, dependency.getType()))
                .findFirst()
                .ifPresent(dependency -> sceneFunction.setNextDepAppCode(dependency.getCode()));
        }
        //sceneFunction.setPreDepAppCode(function.getPreDepAppCode());
        //sceneFunction.setNextDepAppCode(function.getNextDepAppCode());
        sceneFunction.setVersion(function.getVersion());
        sceneFunction.setGmtCreate(function.getGmtCreate());
        sceneFunction.setSystemVersions(function.getSystemVersionList());
        if (ChaosFunctionConstant.SOURCE_CHAOS_BLADE.equals(function.getSource())) {
            sceneFunction.setType("chaos_blade");
        }
        if (ChaosFunctionConstant.SOURCE_LITMUS_CHAOS.equals(function.getSource())) {
            sceneFunction.setType("litmus_chaos");
        }
        if (ChaosFunctionConstant.SOURCE_CUSTOM_APP.equals(function.getSource())) {
            sceneFunction.setType("chaos_app");
        }
        if (ChaosFunctionConstant.SOURCE_FISSION_APP.equals(function.getSource())) {
            sceneFunction.setType("fission_app");
            sceneFunction.setParentFunctionId(function.getParentFunctionId());
            sceneFunction.setParentName(function.getParentName());
        }

        if (null != function.getParameters() && !function.getParameters().isEmpty()) {
            sceneFunction.setParameters(
                function.getParameters()
                    .stream()
                    .map(SceneFunctionParameterVO::from)
                    .collect(Collectors.toList())
            );
        }

        return sceneFunction;
    }

}
