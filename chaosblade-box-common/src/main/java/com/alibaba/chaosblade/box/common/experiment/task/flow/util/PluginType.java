package com.alibaba.chaosblade.box.common.experiment.task.flow.util;

/**
 * @author haibin
 *
 *
 */
public enum PluginType {
    /**
     * chaos-agent
     */
    CHAOS_AGENT,
    /* AGENT 采用POD方式部署*/
    CHAOS_POD_AGENT,
    /*attach 模式*/
    JAVA_AGENT,
    /*依赖jar模式*/
    JAVA_SDK,
    /*ChaosBlade*/
    CHAOS_BLADE;

    private PluginType() {
    }
}