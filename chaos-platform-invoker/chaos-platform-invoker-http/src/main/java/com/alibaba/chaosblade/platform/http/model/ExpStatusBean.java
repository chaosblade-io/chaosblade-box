/*
 * Copyright 1999-2021 Alibaba Group Holding Ltd.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.alibaba.chaosblade.platform.http.model;

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
