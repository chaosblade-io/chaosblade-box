package com.alibaba.chaosblade.box.dao.infrastructure.app.descriptors;

import com.alibaba.chaosblade.box.common.app.sdk.annotations.ChaosDependency;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

/**
 * @author sunju
 *
 */
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ChaosFunctionDependencyDescriptor {

    String code;

    boolean required;

    int phase;

    int type;

    public static ChaosFunctionDependencyDescriptor of(ChaosDependency dependency) {
        ChaosFunctionDependencyDescriptor
                descriptor = new ChaosFunctionDependencyDescriptor();
        descriptor.setCode(dependency.code());
        descriptor.setPhase(dependency.phase());
        descriptor.setRequired(dependency.required());
        descriptor.setType(dependency.type());
        return descriptor;
    }

}
