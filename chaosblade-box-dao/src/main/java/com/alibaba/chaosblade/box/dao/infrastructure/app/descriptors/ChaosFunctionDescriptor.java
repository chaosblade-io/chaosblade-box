package com.alibaba.chaosblade.box.dao.infrastructure.app.descriptors;

import com.alibaba.chaosblade.box.common.app.sdk.InvokeMode;
import com.alibaba.chaosblade.box.common.app.sdk.SupportScope;
import com.alibaba.chaosblade.box.common.app.sdk.annotations.ChaosFunction;
import com.alibaba.chaosblade.box.common.app.sdk.constants.PhaseType;
import com.alibaba.chaosblade.box.dao.infrastructure.experiment.task.flow.util.ChaosAppUtil;
import com.google.common.hash.HashCode;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.lang.reflect.Method;
import java.util.List;

/**
 * @author sunju
 *
 */
@Getter
@Setter
@ToString
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ChaosFunctionDescriptor extends BaseChaosMethodDescriptor {

    String code;
    String namespace;
    String name;
    String description;
    Boolean agentRequired;
    InvokeMode invokeMode = InvokeMode.EACH;
    transient Method reference;
    List<ChaosFunctionArgumentDescriptor> argumentDescriptors;
    List<ChaosFunctionDependencyDescriptor> dependencyDescriptors;
    PhaseType[] phases; //
    String[] categories;

    SupportScope[] supportScopes;

    public static ChaosFunctionDescriptor of(String appCode, Method method, ChaosFunction function) {
        ChaosFunctionDescriptor
            descriptor = new ChaosFunctionDescriptor();
        descriptor.setCode(function.code());
        descriptor.setNamespace(ChaosAppUtil.buildFunctionNamespace(appCode, function.code()));
        descriptor.setName(function.name());
        descriptor.setDescription(function.description());
        descriptor.setAgentRequired(function.agentRequired());
        descriptor.setPhases(function.phases());
        descriptor.setSupportScopes(function.scopes());
        descriptor.setCategories(function.categories());
        try {
            descriptor.setInvokeMode(function.mode());
        } catch (Exception ignore) {
            //这里如果老版本没有invokeMode字段，那么忽略
        }
        descriptor.setReference(method);
        descriptor.setArgumentDescriptors(getFunctionArgumentDescriptors(method));
        descriptor.setDependencyDescriptors(getFunctionDependencyDescriptors(method, function));
        return descriptor;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof ChaosFunctionDescriptor) {
            ChaosFunctionDescriptor
                instance = (ChaosFunctionDescriptor)obj;
            return instance.getCode().equals(this.getCode());
        }
        return false;
    }

    @Override
    public int hashCode() {
        return HashCode.fromString(this.getNamespace()).bits();
    }

    @Override
    public Boolean isAgentRequired() {
        return this.agentRequired;
    }

}
