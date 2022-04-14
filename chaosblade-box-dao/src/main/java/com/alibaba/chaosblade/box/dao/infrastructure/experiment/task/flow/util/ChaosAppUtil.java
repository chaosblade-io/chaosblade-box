package com.alibaba.chaosblade.box.dao.infrastructure.experiment.task.flow.util;

import com.alibaba.chaosblade.box.dao.infrastructure.app.LocalChaosAppLoader;
import com.alibaba.chaosblade.box.dao.infrastructure.app.descriptors.BaseChaosMethodDescriptor;
import com.alibaba.chaosblade.box.dao.infrastructure.app.descriptors.ChaosAppDescriptor;
import com.google.common.base.Joiner;
import com.google.common.base.Splitter;
import lombok.experimental.UtilityClass;

import java.util.List;

/**
 * @author sunju
 *
 */
@UtilityClass
public class ChaosAppUtil {

    private static final String APP_NAMESPACE_PREFIX = "chaosapp";

    private static final String APP_NAMESPACE_SEPARATOR = ".";

    public static BaseChaosMethodDescriptor getChaosMethodDescriptor(String namespace) {
        String appNamespace = getAppNamespace(namespace, true);
        ChaosAppDescriptor appDescriptor = LocalChaosAppLoader.getInstance().getChaosAppDescriptor(appNamespace);
        if (null == appDescriptor) {
            appNamespace = getAppNamespace(namespace, false);
            appDescriptor = LocalChaosAppLoader.getInstance().getChaosAppDescriptor(appNamespace);
        }
        if (appDescriptor == null) { return null; }
        return appDescriptor.getMethodDescriptor(namespace);

    }

    public static String buildAppNamespace(String appCode) {
        return Joiner.on(APP_NAMESPACE_SEPARATOR).join(APP_NAMESPACE_PREFIX, appCode);
    }

    public static String buildFunctionNamespace(String appCode, String functionCode) {
        return Joiner.on(APP_NAMESPACE_SEPARATOR).join(APP_NAMESPACE_PREFIX, appCode, functionCode);
    }

    public static String getAppNamespace(String code, boolean leftMatch) {
        List<String> codes = Splitter.on(APP_NAMESPACE_SEPARATOR)
            .omitEmptyStrings()
            .trimResults()
            .splitToList(code);

        if (leftMatch) {
            return Joiner.on(APP_NAMESPACE_SEPARATOR).join(codes.subList(0, codes.size() - 1));
        }
        return Joiner.on(APP_NAMESPACE_SEPARATOR).join(codes.get(0), codes.get(1));
    }

    public static ChaosAppDescriptor getChaosAppDescriptorByAppCode(String code) {
        String appNamespace = getAppNamespace(code, true);
        ChaosAppDescriptor appDescriptor = LocalChaosAppLoader.getInstance().getChaosAppDescriptor(appNamespace);
        if (null == appDescriptor) {
            appNamespace = getAppNamespace(code, false);
            appDescriptor = LocalChaosAppLoader.getInstance().getChaosAppDescriptor(appNamespace);
        }
        return appDescriptor;
    }
}
