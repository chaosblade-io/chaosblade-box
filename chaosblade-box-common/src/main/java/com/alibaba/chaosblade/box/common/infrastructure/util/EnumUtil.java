package com.alibaba.chaosblade.box.common.infrastructure.util;

import com.alibaba.chaosblade.box.common.common.enums.IEnum;

/**
 * @author haibin
 *
 *
 */
public class EnumUtil {

    public static String nameOf(Enum<?> enums) {
        if (enums == null) { return null; }
        return enums.name();
    }

    public static <E extends Enum<?> & IEnum<Integer>> E integerValueOf(Class<E> enumClass, Integer value) {
        E[] es = enumClass.getEnumConstants();
        for (E e : es) {
            Integer evalue = e.getValue();
            if (evalue.equals(value)) {
                return e;
            }
        }
        return null;
    }
}
