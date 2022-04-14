package com.alibaba.chaosblade.box.dao.infrastructure.utils;

import com.alibaba.chaosblade.box.common.common.enums.DeviceType;
import com.alibaba.chaosblade.box.common.experiment.task.flow.util.PluginType;
import com.google.common.base.Preconditions;
import org.apache.commons.lang3.StringUtils;

/**
 * @author: heartaway
 * @create: 2018-08-24 下午1:11
 */
public class ProxyHelper {

    /**
     * get proxy connect  ip
     *
     * @param deviceType
     * @param privateIp
     * @param parentIp
     * @return
     */
    public static String getProxyIp(Integer deviceType, String privateIp, String parentIp) {
        Preconditions.checkNotNull(deviceType);
        if (DeviceType.HOST.getType() == deviceType) {
            return privateIp;
        } else if (DeviceType.HOST_POD.getType() == deviceType) {
            return privateIp;
        } else {
            return parentIp;
        }
    }

    /**
     * build proxy tag
     *
     * @param pluginType
     * @param privateIp
     * @param pid
     * @return
     */
    public static String buildProxyTag(String pluginType, String privateIp, Integer pid) {
        if (PluginType.CHAOS_AGENT.name().equalsIgnoreCase(pluginType)) {
            return pluginType;
        }

        if (PluginType.CHAOS_POD_AGENT.name().equalsIgnoreCase(pluginType)) {
            StringBuilder sb = new StringBuilder(pluginType);
            sb.append(":");
            sb.append(privateIp);
            return sb.toString();
        }

        StringBuilder sb = new StringBuilder(pluginType);
        if (StringUtils.isNotBlank(privateIp)) {
            sb.append(":");
            sb.append(privateIp);
        }
        if (pid != null) {
            sb.append(":");
            sb.append(pid);
        }
        return sb.toString();
    }
}
