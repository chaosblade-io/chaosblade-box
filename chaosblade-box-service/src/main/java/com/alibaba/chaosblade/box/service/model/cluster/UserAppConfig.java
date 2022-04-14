package com.alibaba.chaosblade.box.service.model.cluster;

import java.io.Serializable;
import java.util.Set;

/**
 * 用户配置的应用信息解析配置
 *
 * @author: xinyuan.ymm
 * @create: 2021-04-20 9:43 上午
 */
public class UserAppConfig implements Serializable {

    private static final long serialVersionUID = -1;

    /**
     * 应用聚合方式
     */
    private String appFromType;

    /**
     * 应用信息Key，允许多个Key,以逗号分隔
     */
    private String appNameKeys;

    /**
     * 应用信息Key分组
     */
    private Set<String> appNameSet;

    /**
     * 分组信息Key，允许多个Key,以逗号分隔
     */
    private String appGroupNameKeys;

    /**
     * 分组信息Key分组
     */
    private Set<String> appGroupNameKeySet;

    /**
     * 是否使用默认的应用解析链
     */
    private Boolean useDefaultAppChain;

    /**
     * 配置变更时，是否采用暴力模式，暴力模式下，会清空历史所以应用实例并重新使用新配置进行处理
     */
    private Boolean force;

    public String getAppFromType() {
        return appFromType;
    }

    public void setAppFromType(String appFromType) {
        this.appFromType = appFromType;
    }

    public String getAppNameKeys() {
        return appNameKeys;
    }

    public void setAppNameKeys(String appNameKeys) {
        this.appNameKeys = appNameKeys;
    }

    public String getAppGroupNameKeys() {
        return appGroupNameKeys;
    }

    public void setAppGroupNameKeys(String appGroupNameKeys) {
        this.appGroupNameKeys = appGroupNameKeys;
    }

    public Set<String> getAppNameSet() {
        return appNameSet;
    }

    public void setAppNameSet(Set<String> appNameSet) {
        this.appNameSet = appNameSet;
    }

    public Set<String> getAppGroupNameKeySet() {
        return appGroupNameKeySet;
    }

    public void setAppGroupNameKeySet(Set<String> appGroupNameKeySet) {
        this.appGroupNameKeySet = appGroupNameKeySet;
    }

    public Boolean getUseDefaultAppChain() {
        return useDefaultAppChain;
    }

    public void setUseDefaultAppChain(Boolean useDefaultAppChain) {
        this.useDefaultAppChain = useDefaultAppChain;
    }

    public Boolean getForce() {
        return force;
    }

    public void setForce(Boolean force) {
        this.force = force;
    }
}
