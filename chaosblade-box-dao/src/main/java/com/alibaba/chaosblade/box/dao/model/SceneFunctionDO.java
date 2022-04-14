package com.alibaba.chaosblade.box.dao.model;

import com.alibaba.chaosblade.box.common.app.sdk.SupportScope;
import com.alibaba.chaosblade.box.common.app.sdk.constants.PhaseType;
import com.alibaba.chaosblade.box.common.common.constant.ChaosFunctionConstant;
import com.alibaba.chaosblade.box.common.infrastructure.constant.SystemVersions;
import com.alibaba.chaosblade.box.common.infrastructure.domain.scene.SceneFunctionDependency;
import com.alibaba.chaosblade.box.common.infrastructure.domain.scene.SceneFunctionSystemVersion;
import com.alibaba.chaosblade.box.dao.infrastructure.app.descriptors.BaseChaosMethodDescriptor;
import com.alibaba.chaosblade.box.dao.infrastructure.app.descriptors.ChaosAppDescriptor;
import com.alibaba.chaosblade.box.dao.infrastructure.app.descriptors.ChaosFunctionArgumentDescriptor;
import com.alibaba.chaosblade.box.dao.infrastructure.app.descriptors.ChaosFunctionDependencyDescriptor;
import com.alibaba.chaosblade.box.dao.model.base.BaseDO;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author sunju
 * 
 */
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@TableName("t_chaos_scene_function")
public final class SceneFunctionDO extends BaseDO implements Cloneable {

    public static SceneFunctionDO Empty = new SceneFunctionDO();


    @TableId(type = IdType.ID_WORKER_STR)
    String functionId;

    /**
     * 场景ID
     */
    String sceneId;

    /**
     * 函数code，全局唯一
     */
    String code;

    /**
     *
     */
    String parentCode;

    /**
     * 函数名字
     */
    String name;

    /**
     * 函数描述
     */
    String description;

    /**
     * 是否需要agent
     */
    Boolean agentRequired;

    /**
     * 是否删除
     */
    Boolean isDelete;

    /**
     * 来自chaos-blade还是用户自定义的能力
     */
    Integer source;

    /**
     * 前置依赖的functionId
     *
     * @deprecated use  instead
     */
    @Deprecated
    String preDepAppCode;

    /**
     * 下一个依赖的functionID
     *
     * @deprecated use  instead
     */
    @Deprecated
    String nextDepAppCode;

    /**
     * 阶段标志位,支持哪个阶段
     */
    Integer phaseFlag;

    /**
     * chaos-blade的动作类型
     */
    Integer chaosBladeActionType;

    /**
     * 版本信息
     */
    String version;

    /**
     * 记录小程序支持的系统
     */
    @TableField
    List<SceneFunctionSystemVersion> systemVersions;

    /**
     * 是否生效，影响是否对用户可见
     * <p>
     * 0: 下架
     * 1: 上架
     * 2: 待发布
     */
    Integer enabled;

    /**
     * 风险等级
     */
    Integer riskLevel;

    /**
     * 小程序依赖关系
     */
    @TableField
    List<SceneFunctionDependency> dependencies;

    /**
     * 所属的类目
     */
    @TableField
    List<String> categories = new ArrayList<>();

    @TableField
    List<Integer> supportScopeTypes = new ArrayList<>();

    @TableField(exist = false)
    List<SceneFunctionParameterDO> parameters = new ArrayList<>();

    @TableField(exist = false)
    String parentFunctionId;

    @TableField(exist = false)
    String parentName;

    /**
     * 非数据库字段
     */
    @TableField(exist = false)
    String prepareType;

    @TableField(exist = false)
    String userId;

    @TableField
    List<Integer> supportOsTypes = new ArrayList<>();

    public boolean isFissionMiniApp() {
        return ChaosFunctionConstant.SOURCE_FISSION_APP.equals(this.source);
    }

    public static SceneFunctionDO from(ChaosAppDescriptor descriptor, BaseChaosMethodDescriptor methodDescriptor) {
        SceneFunctionDO function = new SceneFunctionDO();
        function.setCode(methodDescriptor.getNamespace());
        function.setName(methodDescriptor.getName());
        function.setDescription(methodDescriptor.getDescription());
        function.setAgentRequired(methodDescriptor.isAgentRequired());
        function.setEnabled(0);
        function.setPhaseFlag(0);
        function.setIsDelete(false);
        function.setUserId(descriptor.getEmpId());
        function.setVersion(descriptor.getVersion());
        function.setSource(ChaosFunctionConstant.SOURCE_CUSTOM_APP);
        for (PhaseType phaseType : methodDescriptor.getPhases()) {
            switch (phaseType) {
                case PREPARE:
                    function.support(ChaosFunctionConstant.PHASE_FLAG_PREPARE);
                    break;
                case ATTACK:
                    function.support(ChaosFunctionConstant.PHASE_FLAG_ATTACK);
                    break;
                case CHECK:
                    function.support(ChaosFunctionConstant.PHASE_FLAG_CHECK);
                    break;
                case RECOVER:
                    function.support(ChaosFunctionConstant.PHASE_FLAG_RECOVER);
                    break;
            }
        }
        function.setCategoryList(Lists.newArrayList(methodDescriptor.getCategories()));
        function.setSystemVersionList(Lists.newArrayList(
            SceneFunctionSystemVersion.of(
                SystemVersions.LINUX.name(),
                SystemVersions.getLowestVersion(SystemVersions.LINUX)
            )
        ));
        function.setSupportScopeTypeList(
            Stream.of(methodDescriptor.getSupportScopes()).map(SupportScope::getType).collect(Collectors.toList()));
        List<ChaosFunctionArgumentDescriptor> argumentDescriptors = methodDescriptor.getArgumentDescriptors();
        if (null != argumentDescriptors) {
            function.setParameters(
                argumentDescriptors.stream().map(SceneFunctionParameterDO::from).collect(Collectors.toList())
            );
        }

        List<ChaosFunctionDependencyDescriptor> dependencyDescriptors = methodDescriptor.getDependencyDescriptors();
        if (null != argumentDescriptors) {
            function.setDependencyList(
                dependencyDescriptors.stream().map(descriptorDescriptor -> {
                    SceneFunctionDependency sceneFunctionDependency = new SceneFunctionDependency();
                    sceneFunctionDependency.setCode(descriptorDescriptor.getCode());
                    sceneFunctionDependency.setPhase(descriptorDescriptor.getPhase());
                    sceneFunctionDependency.setRequired(descriptorDescriptor.isRequired());
                    sceneFunctionDependency.setType(descriptorDescriptor.getType());
                    return sceneFunctionDependency;
                }).collect(Collectors.toList())
            );
        }

        return function;
    }

    public List<String> getCategoryList() {
        return this.categories;
    }

    public void setCategoryList(List<String> categories) {
        this.categories = categories;
    }

    public void addCategory(String category) {
        if (category == null) { return; }
        if (categories == null) {
            this.categories = new ArrayList<>();
        }
        if (!this.categories.contains(category)) {
            this.categories.add(category);
        }
    }

    public String getCategories() {
        return JSON.toJSONString(this.categories);
    }

    public void setCategories(String categories) {
        this.categories = JSON.parseArray(categories, String.class);
    }

    public List<Integer> getSupportScopeTypeList() {
        return this.supportScopeTypes;
    }

    public void setSupportScopeTypeList(List<Integer> supportScopeTypes) {
        this.supportScopeTypes = supportScopeTypes;
    }

    public void appendSupportScopeType(Integer scopeType) {
        if (scopeType == null) { return; }
        if (this.supportScopeTypes == null) {
            this.supportScopeTypes = new ArrayList<>();
        }
        if (!this.supportScopeTypes.contains(scopeType)) {
            this.supportScopeTypes.add(scopeType);
        }
    }

    public String getSupportScopeTypes() {
        return JSON.toJSONString(this.supportScopeTypes);
    }

    public String getSystemVersions() {
        if (null == this.systemVersions) {
            return "";
        }
        return JSON.toJSONString(this.systemVersions);
    }

    public List<SceneFunctionSystemVersion> getSystemVersionList() {
        return this.systemVersions;
    }

    public void setSystemVersions(String systemVersions) {
        if (!Strings.isNullOrEmpty(systemVersions)) {
            this.systemVersions = JSON.parseArray(systemVersions, SceneFunctionSystemVersion.class);
        }
    }

    public void setSystemVersionList(List<SceneFunctionSystemVersion> systemVersions) {
        this.systemVersions = systemVersions;
    }

    public List<SceneFunctionDependency> getDependencyList() {
        if (null == this.dependencies) {
            return Lists.newArrayList();
        }
        return this.dependencies;
    }

    public String getDependencies() {
        if (null == this.dependencies) {
            return "";
        }
        return JSON.toJSONString(this.dependencies);
    }

    public void setDependencies(String dependencies) {
        if (!Strings.isNullOrEmpty(dependencies)) {
            this.dependencies = JSON.parseArray(dependencies, SceneFunctionDependency.class);
        }
    }

    public List<Integer> getSupportOsList() {
        return this.supportOsTypes;
    }

    public void setSupportScopeTypes(String supportScopeTypes) {
        this.supportScopeTypes = JSON.parseArray(supportScopeTypes, Integer.class);
    }

    public String getSupportOsTypes() {
        return JSON.toJSONString(this.supportOsTypes);
    }

    public void setSupportOsTypes(String supportOsTypes) {
        this.supportOsTypes = JSON.parseArray(supportOsTypes, Integer.class);
    }

    public void appendSupportOsTypes(Integer osType) {
        if (osType == null) { return; }
        if (this.supportOsTypes == null) {
            this.supportOsTypes = new ArrayList<>();
        }
        if (!this.supportOsTypes.contains(osType)) {
            this.supportOsTypes.add(osType);
        }
    }

    public void setDependencyList(List<SceneFunctionDependency> dependencies) {
        this.dependencies = dependencies;
    }

    public boolean isSupport(int phaseFlag) {
        return (this.phaseFlag & phaseFlag) == phaseFlag;
    }

    public void support(int phaseFlag) {
        if (this.phaseFlag == null) {
            this.phaseFlag = phaseFlag;
        } else {
            this.phaseFlag |= phaseFlag;
        }
    }

    public boolean isSourceScript() {
        return ChaosFunctionConstant.SOURCE_CUSTOM_APP_SCRIPT.equals(source);
    }

    public void addDependency(SceneFunctionDependency dependency) {
        if (null == dependency) {
            return;
        }

        if (null == this.dependencies) {
            this.dependencies = Lists.newArrayList();
        }

        if (this.dependencies.isEmpty()) {
            this.dependencies.add(dependency);
            return;
        }

        if (this.dependencies.stream()
            .noneMatch(dep ->
                Objects.equals(dep.getType(), dependency.getType())
                    && Objects.equals(dep.getCode(), dependency.getCode())
            )) {
            this.dependencies.add(dependency);
        }
    }

    public static SceneFunctionDO fission(SceneFunctionDO functionDO) {
        SceneFunctionDO copied = new SceneFunctionDO();
        copied.setPhaseFlag(functionDO.getPhaseFlag());
        copied.setDescription(functionDO.getDescription());
        copied.setSceneId(functionDO.getSceneId());
        copied.setSource(ChaosFunctionConstant.SOURCE_FISSION_APP);
        copied.setEnabled(functionDO.getEnabled());
        copied.setVersion(functionDO.getVersion());
        copied.setSystemVersions(functionDO.getSystemVersions());
        copied.setDependencyList(functionDO.getDependencyList());
        copied.setPreDepAppCode(functionDO.getPrepareType());
        copied.setNextDepAppCode(functionDO.getNextDepAppCode());
        copied.setCategories(functionDO.getCategories());
        return copied;
    }

    /**
     * 历史数据存在parameters出现alias重复的情况，所以去重一下
     */
    public void distinctParams() {
        if (this.parameters != null) {
            this.parameters = this.parameters.stream().distinct().collect(Collectors.toList());
        }
    }

    @Override
    public Object clone()  {
        try {
           return super.clone();
        } catch (CloneNotSupportedException e) {
            return new SceneFunctionDO();
        }
    }
}
