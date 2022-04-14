package com.alibaba.chaosblade.box.common.sdk.entity;

/**
 * @author Changjun Xiao
 */
public class ExpStatusBean {

    private String uid;
    private String command;
    private String subCommand;
    private String rule;
    private String status;
    private String error;
    private String createTime;
    private String updateTime;

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getCommand() {
        return command;
    }

    public ExpStatusBean setCommand(String command) {
        this.command = command;
        return this;
    }

    public String getSubCommand() {
        return subCommand;
    }

    public ExpStatusBean setSubCommand(String subCommand) {
        this.subCommand = subCommand;
        return this;
    }

    public String getRule() {
        return rule;
    }

    public ExpStatusBean setRule(String rule) {
        this.rule = rule;
        return this;
    }

    public String getStatus() {
        return status;
    }

    public ExpStatusBean setStatus(String status) {
        this.status = status;
        return this;
    }

    public String getError() {
        return error;
    }

    public ExpStatusBean setError(String error) {
        this.error = error;
        return this;
    }

    public String getCreateTime() {
        return createTime;
    }

    public ExpStatusBean setCreateTime(String createTime) {
        this.createTime = createTime;
        return this;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public ExpStatusBean setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
        return this;
    }

    /**
     * 混沌实验是否进行中
     *
     * @return
     */
    public boolean isRunning() {
        return BladeStatus.SUCCESS.equals(status);
    }
}
