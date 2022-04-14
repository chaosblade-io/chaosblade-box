package com.alibaba.chaosblade.box.common.sdk.entity;

import java.util.Map;

/**
 * @author Changjun Xiao
 */
public class ModelArgs {
    /**
     * 实验准备类型
     */
    private String target;
    /**
     * 实验准备子类型，比如 kubernetes jvm， target 是 kubernetes，subTarget 是 jvm
     */
    private String subTarget;
    /**
     * 执行命令的目标信息类型
     */
    private String machineType;
    /**
     * 执行命令的目标机器, Agent Pod IP
     */
    private String machine;
    /**
     * 执行目标机器的port
     */
    private String port;
    /**
     * 实验scope
     */
    private String scope;
    /**
     * 实验名称
     */
    private String action;
    /**
     * 实验准备 action 所需参数，比如 delay 中的 time
     */
    private Map<String, String> flags;

    /**
     * 实验准备匹配器参数，比如 dubbo 中的 service,method
     */
    private Map<String, String> matcherFlags;

    public String getTarget() {
        return target;
    }

    public ModelArgs setTarget(String target) {
        this.target = target;
        return this;
    }

    public String getSubTarget() {
        return subTarget;
    }

    public ModelArgs setSubTarget(String subTarget) {
        this.subTarget = subTarget;
        return this;
    }

    public String getScope() {
        return scope;
    }

    public ModelArgs setScope(String scope) {
        this.scope = scope;
        return this;
    }

    public String getAction() {
        return action;
    }

    public ModelArgs setAction(String action) {
        this.action = action;
        return this;
    }

    public Map<String, String> getFlags() {
        return flags;
    }

    public ModelArgs setFlags(Map<String, String> flags) {
        this.flags = flags;
        return this;
    }

    public Map<String, String> getMatcherFlags() {
        return matcherFlags;
    }

    public ModelArgs setMatcherFlags(Map<String, String> matcherFlags) {
        this.matcherFlags = matcherFlags;
        return this;
    }

    public String getMachine() {
        return machine;
    }

    public void setMachine(String machine) {
        this.machine = machine;
    }

    public String getPort() {
        return port;
    }

    public ModelArgs setPort(String port) {
        this.port = port;
        return this;
    }

    public String getMachineType() {
        return machineType;
    }

    public ModelArgs setMachineType(String machineType) {
        this.machineType = machineType;
        return this;
    }
}
