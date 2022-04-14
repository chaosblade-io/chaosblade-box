package com.alibaba.chaosblade.box.dao.infrastructure.app.descriptors;

import com.alibaba.chaosblade.box.common.app.sdk.annotations.ChaosArgs;
import com.alibaba.chaosblade.box.common.app.sdk.constants.ChaosAppArgumentType;
import com.google.common.base.Strings;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.lang.reflect.Field;

/**
 * @author sunju
 *
 */
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ChaosFunctionArgumentDescriptor {

    String name;
    String alias;
    String description;
    ChaosAppArgumentType type;
    transient Field reference;

    public static ChaosFunctionArgumentDescriptor of(ChaosArgs chaosArgs, ChaosAppArgumentType type) {
        ChaosFunctionArgumentDescriptor
            descriptor = new ChaosFunctionArgumentDescriptor();
        descriptor.setAlias(chaosArgs.alias());
        descriptor.setName(Strings.isNullOrEmpty(chaosArgs.name()) ? chaosArgs.alias() : chaosArgs.name());
        descriptor.setDescription(chaosArgs.description());
        descriptor.setType(type);
        return descriptor;
    }

    public static ChaosFunctionArgumentDescriptor of(Field field, ChaosArgs chaosArgs, ChaosAppArgumentType type) {
        ChaosFunctionArgumentDescriptor
            descriptor = ChaosFunctionArgumentDescriptor.of(chaosArgs, type);
        descriptor.setReference(field);
        if (Strings.isNullOrEmpty(descriptor.getAlias())) {
            descriptor.setAlias(field.getName());
        }
        if (Strings.isNullOrEmpty(descriptor.getName())) {
            descriptor.setName(field.getName());
        }
        return descriptor;
    }

}
