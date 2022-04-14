package com.alibaba.chaosblade.box.cache.utils;

import lombok.experimental.UtilityClass;

/**
 * Author: sunju
 *
 * Date:   2019/11/11
 */
@UtilityClass
public class Strings {

    /**
     * Alternative of {@link String#join(CharSequence, CharSequence...)} without delimiter.
     *
     * @param objects Object array to be joined.
     * @return A string that joined without any delimiter.
     */
    public static String join(Object... objects) {
        StringBuilder stringBuilder = new StringBuilder();
        for (Object object : objects) {
            stringBuilder.append(object);
        }
        return stringBuilder.toString();
    }

}
