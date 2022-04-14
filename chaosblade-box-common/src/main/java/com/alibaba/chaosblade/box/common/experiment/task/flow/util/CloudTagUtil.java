package com.alibaba.chaosblade.box.common.experiment.task.flow.util;

/**
 * @author haibin
 * 
 *
 */
public class CloudTagUtil {

    public static String buildProxyTag(String privateIp, Integer pid) {
        return PluginType.CHAOS_AGENT.name();
    }

}
