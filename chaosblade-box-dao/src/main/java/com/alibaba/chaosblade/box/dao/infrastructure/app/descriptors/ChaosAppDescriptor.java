package com.alibaba.chaosblade.box.dao.infrastructure.app.descriptors;

import com.alibaba.chaosblade.box.common.app.sdk.ChaosApp;
import com.alibaba.chaosblade.box.common.app.sdk.annotations.ChaosApplication;
import com.alibaba.chaosblade.box.common.app.sdk.constants.ChaosAppType;
import com.alibaba.chaosblade.box.dao.infrastructure.experiment.task.flow.util.ChaosAppUtil;
import com.google.common.base.Strings;
import com.google.common.hash.HashCode;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

/**
 * @author sunju
 */
@Getter
@Setter
@ToString
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ChaosAppDescriptor {

    String empId;
    String code;
    String namespace;
    String name;
    String description;
    String version;
    ChaosAppType type;
    Boolean forPublic;
    transient ChaosApp reference;
    List<BaseChaosMethodDescriptor> methodDescriptors;

    public static ChaosAppDescriptor of(ChaosApp app, ChaosApplication config) {
        ChaosAppDescriptor descriptor = new ChaosAppDescriptor();
        descriptor.setCode(config.code());
        descriptor.setName(config.name());
        descriptor.setNamespace(ChaosAppUtil.buildAppNamespace(config.code()));
        descriptor.setVersion(config.version());
        descriptor.setDescription(config.description());
        descriptor.setType(config.type());
        descriptor.setReference(app);
        return descriptor;
    }

    public ChaosFunctionDescriptor getFunctionDescriptor(String namespace) {
        BaseChaosMethodDescriptor methodDescriptor = getMethodDescriptor(namespace);
        if (methodDescriptor instanceof ChaosFunctionDescriptor) {
            return (ChaosFunctionDescriptor) methodDescriptor;
        }
        return null;
    }

    public BaseChaosMethodDescriptor getMethodDescriptor(String namespace) {
        if (Strings.isNullOrEmpty(namespace)) {
            return null;
        }

        if (null != methodDescriptors) {
            return methodDescriptors.stream()
                .filter(descriptor -> descriptor.getNamespace().equals(namespace))
                .findFirst()
                .get();
        }

        return null;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof ChaosAppDescriptor) {
            ChaosAppDescriptor instance = (ChaosAppDescriptor) obj;
            return instance.getCode().equals(this.getCode());
        }
        return false;
    }

    @Override
    public int hashCode() {
        return HashCode.fromString(this.getNamespace()).bits();
    }
}
