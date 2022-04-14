package com.alibaba.chaosblade.box.common.app.sdk.constants;

import java.util.Arrays;

/**
 * All app types
 *
 * @author sunju
 */
public enum ChaosAppType {

    /**
     * component type
     *
     * @since 1.0.0
     */
    COMPONENT,

    /**
     * system type, agent attack/unattach only
     *
     * @since 1.0.0
     */
    SYSTEM,

    /**
     * chaosapp type, develop by 3rd only
     *
     * @since 1.0.0
     */
    CHAOS_APP,

    /**
     * combine type, combine by component type or chaosapp type only
     *
     * @since 1.0.0
     */
    COMBINE;

    public static ChaosAppType of(String name) {
        if (null == name || name.length() <= 0) {
            return null;
        }
        return Arrays.stream(ChaosAppType.values())
            .filter(type -> type.name().toLowerCase().equals(name.toLowerCase()))
            .findFirst().get();
    }

}
