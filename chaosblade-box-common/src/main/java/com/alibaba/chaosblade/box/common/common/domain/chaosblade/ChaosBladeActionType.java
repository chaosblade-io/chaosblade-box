package com.alibaba.chaosblade.box.common.common.domain.chaosblade;

/**
 * @author haibin
 *
 *
 */
public enum ChaosBladeActionType {

    /**
     * 安装agent
     */
    INSTALL_AGENT,
    /**
     * 卸载agent
     */
    UNINSTALL_AGENT,

    /**
     * 注入故障
     */
    ATTACK,

    /**
     * 停止故障注入
     */
    STOP_ATTACK

}
