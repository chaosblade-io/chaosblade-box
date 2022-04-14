package com.alibaba.chaosblade.box.dao.model;

import com.alibaba.chaosblade.box.common.common.domain.definition.SceneFunctionParameterComponent;
import com.alibaba.chaosblade.box.common.common.domain.definition.SceneFunctionParameterLinkage;
import com.alibaba.chaosblade.box.dao.infrastructure.app.descriptors.ChaosFunctionArgumentDescriptor;
import com.alibaba.chaosblade.box.dao.model.base.BaseDO;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.google.common.base.Strings;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.util.Objects;

/**
 * @author sunju
 *
 */
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@TableName("t_chaos_function_parameter")
public final class SceneFunctionParameterDO extends BaseDO {

    public static final Integer TYPE_MATCHER = 0;

    public static final Integer TYPE_ACTION = 1;

    public static final Integer TYPE_USER = 2;

    @TableId(type = IdType.ID_WORKER_STR)
    String parameterId;
    String functionId;
    String name;
    String alias;
    String description;
    Boolean isDelete;

    /**
     * matcher or action
     */
    Integer type;

    /**
     * 参数顺序
     */
    Integer sequence;

    /**
     * 是否是加密字段，默认是false,加密字段的内容只有作者可以看到
     */
    @TableField(exist = false)
    Boolean secret;

    SceneFunctionParameterComponent component;

    @Override
    public boolean equals(Object o) {
        if (this == o) { return true; }
        if (!(o instanceof SceneFunctionParameterDO)) { return false; }
        if (!super.equals(o)) { return false; }
        SceneFunctionParameterDO that = (SceneFunctionParameterDO)o;
        return getFunctionId().equals(that.getFunctionId()) &&
            getAlias().equals(that.getAlias()) &&
            getIsDelete().equals(that.getIsDelete());
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), getFunctionId(), getAlias(), getIsDelete());
    }

    /**
     * 参数是否可见判断
     */
    public void setVisible(boolean visible) {
        if (component == null) {
            component = new SceneFunctionParameterComponent();
        }
        SceneFunctionParameterLinkage sceneFunctionParameterLinkage = component.getLinkage();
        if (sceneFunctionParameterLinkage == null) {
            sceneFunctionParameterLinkage = new SceneFunctionParameterLinkage();
            component.setLinkage(sceneFunctionParameterLinkage);
        }
        sceneFunctionParameterLinkage.setDefaultState(visible);
    }

    public boolean isInvisibleWithNoDep() {
        if (component == null) {
            return false;
        }
        SceneFunctionParameterLinkage sceneFunctionParameterLinkage = component.getLinkage();
        if (sceneFunctionParameterLinkage == null) { return false; }
        return !sceneFunctionParameterLinkage.isDefaultState() && Strings.isNullOrEmpty(
            sceneFunctionParameterLinkage.getDepends());
    }

    public static SceneFunctionParameterDO from(ChaosFunctionArgumentDescriptor descriptor) {
        SceneFunctionParameterDO parameter = new SceneFunctionParameterDO();
        parameter.setName(descriptor.getName());
        parameter.setAlias(descriptor.getAlias());
        parameter.setDescription(descriptor.getDescription());
        parameter.setType(SceneFunctionParameterDO.TYPE_USER);
        SceneFunctionParameterComponent component = new SceneFunctionParameterComponent();
        component.setType(SceneFunctionParameterComponent.TYPE_INPUT);

        SceneFunctionParameterLinkage linkage = new SceneFunctionParameterLinkage();
        linkage.setDefaultState(true);

        component.setLinkage(linkage);

        parameter.setComponent(component);
        return parameter;
    }

    public static SceneFunctionParameterDO fission(SceneFunctionParameterDO parameter) {
        SceneFunctionParameterDO copied = new SceneFunctionParameterDO();
        copied.setFunctionId(parameter.getFunctionId());
        copied.setName(parameter.getName());
        copied.setAlias(parameter.getAlias());
        copied.setDescription(parameter.getDescription());
        copied.setIsDelete(parameter.getIsDelete());
        copied.setType(parameter.getType());
        copied.setSequence(parameter.getSequence());
        copied.setComponent(parameter.getComponent());
        return copied;
    }

}
