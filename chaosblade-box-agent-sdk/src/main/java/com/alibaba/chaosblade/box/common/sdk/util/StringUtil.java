package com.alibaba.chaosblade.box.common.sdk.util;

/**
 * @author Changjun Xiao
 */
public class StringUtil {

    /**
     * 判断是为空
     *
     * @param value
     * @return
     */
    public static boolean isBlank(String value) {
        return value == null || value.trim().equals("");
    }
}
