package com.alibaba.chaosblade.box.service.model.migration;


import java.util.List;


public class FlowGroup {

    /**
     * 微流程组名称
     */
    private String groupName;

    /**
     * 机器列表
     */
    private List<String> hosts;

    /**
     * 微流程列表
     */
    private List<Flow> flows;

    /**
     * 应用名称
     */
    private String appName;

    /**
     * 应用分组名称
     */
    private List<String> appGroups;


    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public List<String> getHosts() {
        return hosts;
    }

    public void setHosts(List<String> hosts) {
        this.hosts = hosts;
    }

    public List<Flow> getFlows() {
        return flows;
    }

    public void setFlows(List<Flow> flows) {
        this.flows = flows;
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public List<String> getAppGroups() {
        return appGroups;
    }

    public void setAppGroups(List<String> appGroups) {
        this.appGroups = appGroups;
    }
}
