package com.alibaba.chaosblade.box.dao.infrastructure.app.descriptors;

import com.alibaba.chaosblade.box.common.app.sdk.SupportScope;
import com.alibaba.chaosblade.box.common.app.sdk.annotations.ChaosListener;
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
public class ChaosListenerDescriptor extends BaseChaosMethodDescriptor {

    String code;
    String namespace;
    String name;
    String description;
    transient Method reference;
    List<ChaosFunctionArgumentDescriptor> argumentDescriptors;
    List<ChaosFunctionDependencyDescriptor> dependencyDescriptors;

    public static ChaosListenerDescriptor of(String appCode, Method method, ChaosListener listener) {
        ChaosListenerDescriptor
            descriptor = new ChaosListenerDescriptor();
        descriptor.setCode(listener.code());
        descriptor.setNamespace(ChaosAppUtil.buildFunctionNamespace(appCode, listener.code()));
        descriptor.setName(listener.name());
        descriptor.setDescription(listener.description());
        descriptor.setReference(method);
        descriptor.setArgumentDescriptors(getFunctionArgumentDescriptors(method));
        return descriptor;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof ChaosListenerDescriptor) {
            ChaosListenerDescriptor
                instance = (ChaosListenerDescriptor)obj;
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
        return Boolean.FALSE;
    }

    @Override
    public PhaseType[] getPhases() {
        return new PhaseType[0];
    }

    @Override
    public String[] getCategories() {
        return new String[0];
    }

    @Override
    public SupportScope[] getSupportScopes() {
        return new SupportScope[0];
    }
}
