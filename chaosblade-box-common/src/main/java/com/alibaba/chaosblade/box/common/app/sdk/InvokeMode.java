package com.alibaba.chaosblade.box.common.app.sdk;

/**
 * @author haibin
 *
 * 
 */
public enum InvokeMode {

    /**
     * 表明用户配置了多少机器就调用多少次
     */
    EACH,

    /**
     * 只是调用一次，和机器无关，就算小程序配置了10台机器，也只是调用一次
     */
    ONCE

}
