package com.alibaba.chaosblade.box.common.infrastructure.util;

import java.util.UUID;

/**
 * @author haibin
 *
 *
 */
public class ChaosTraceUtil {

    public static String generateTraceId() {
        return replaceTraceId();
    }

    // generateTraceId
    private static String replaceTraceId() {
        return UUID.randomUUID().toString();
    }
}
