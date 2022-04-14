package com.alibaba.chaosblade.box.common.config;

/**
 * @author haibin.lhb
 *
 * 
 */
public interface ConfigUpdateListener {

    /**
     * 配置更新监听器，当配置发生变化时候
     *
     * @param chaosSettingInfo
     */
    public void onUpdate(ChaosSettingInfo chaosSettingInfo);

    /**
     * 支持的key
     *
     * @param chaosSettingInfo
     * @return true支持
     */
    public boolean support(ChaosSettingInfo chaosSettingInfo);

}
