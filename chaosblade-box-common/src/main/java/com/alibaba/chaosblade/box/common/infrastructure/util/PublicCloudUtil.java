package com.alibaba.chaosblade.box.common.infrastructure.util;

/**
 * @author haibin
 *
 *
 */
public class PublicCloudUtil {

    public static boolean isK8SByScope(String scope) {
        return "node".equalsIgnoreCase(scope) || "pod".equalsIgnoreCase(scope) || "container".equalsIgnoreCase(scope);
    }

    public static boolean isK8SByAppCode(String appCode) {
        String target = appCode.split("\\.")[1];
        return isK8SByScope(getScopeFromTarget(target));
    }

    public static String getScopeFromTarget(String target) {
        String[] splits = target.split("-");
        if (splits.length == 2) {
            return splits[0];
        }
        return null;
    }

    public static boolean isK8SUserMiniApp(String appCode) {
        return appCode.startsWith("chaosapp.k8s");
    }

}
