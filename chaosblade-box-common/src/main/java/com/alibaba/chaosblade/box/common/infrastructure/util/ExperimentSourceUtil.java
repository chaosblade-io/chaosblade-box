package com.alibaba.chaosblade.box.common.infrastructure.util;

import com.google.common.base.Strings;

/**
 * @author haibin
 *
 *
 */
public class ExperimentSourceUtil {

    public static String SOURCE_CHAOS1 = "chaos1";

    public static String getSourceByOuterId(String outerId) {
        if (Strings.isNullOrEmpty(outerId)) { return null;}
        if (outerId.startsWith(SOURCE_CHAOS1)) { return SOURCE_CHAOS1; }
        return null;
    }
}
