package com.alibaba.chaosblade.box.dao.infrastructure.utils;


import com.alibaba.chaosblade.box.common.common.enums.DeviceType;
import com.alibaba.chaosblade.box.common.experiment.task.flow.util.PluginType;

/**
 * @author: xinyuan.ymm
 * @create: 2020-05-08 10:08 AM
 */
public class PluginTypeUtil {

    /**
     * 基于设备探针安装的设备类型，获取其映射的插件类型
     *
     * @param deviceType
     * @return
     */
    public static final String getPluginTypeByDeviceType(Integer deviceType) {
        if (DeviceType.HOST.getType() == deviceType) {
            return PluginType.CHAOS_AGENT.name();
        }

        if (DeviceType.HOST_POD.getType() == deviceType) {
            return PluginType.CHAOS_POD_AGENT.name();
        }

        return null;
    }
}
