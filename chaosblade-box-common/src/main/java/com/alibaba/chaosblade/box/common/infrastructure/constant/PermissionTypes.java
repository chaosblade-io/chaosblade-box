package com.alibaba.chaosblade.box.common.infrastructure.constant;

import com.google.common.base.Strings;
import com.google.common.collect.BiMap;
import com.google.common.collect.ImmutableBiMap;
import lombok.experimental.UtilityClass;

import java.util.Map;

/**
 *
 * Permission types.
 *
 * @author sunju
 *
 */
@UtilityClass
public class PermissionTypes {

    /**
     * no permission
     */
    public static final int NONE = 0;

    /**
     * read permission
     */
    public static final int R = 1;

    /**
     * write permission
     */
    public static final int W = 1 << 1;

    /**
     * execute permission
     */
    public static final int X = 1 << 2;

    /**
     * all permissions
     */
    public static final int ALL = R | W | X;

    public static boolean contains(int permission, int type) {
        return (permission & type) == type;
    }

    private static final BiMap<Integer, String> ALL_TYPES = ImmutableBiMap.of(R, "R", W, "W", X, "X");

    public static int valueOf(String pname) {
        Integer permission = ALL_TYPES.inverse().get(pname);
        if (null == permission) {
            return PermissionTypes.NONE;
        }
        return permission;
    }

    public static String describeOf(int permission) {
        return ALL_TYPES.entrySet()
                .stream()
                .filter(entry -> contains(permission, entry.getKey()))
                .map(Map.Entry::getValue)
                .reduce((s1, s2) -> String.join("", Strings.nullToEmpty(s1), Strings.nullToEmpty(s2)))
                .orElse("");
    }

}
