package com.alibaba.chaosblade.box.toolsmgr.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class MgrRequest {
    String license;

    /**
     * 应用名
     */
    String appName;

    /**
     * 应用分组名
     */
    String appGroupName;

    /**
     * chaosbox ip:port
     */
    String chaosboxEndpoint;


    /**
     * 目标ip
     */
    String instanceIp;


    /**
     * 目标port
     */
    String instancePort;

    /**
     * 目标机器用户名
     */
    String instanceUser;

    /**
     * 目标机器密码
     */
    String instancePassword;


    /**
     * command
     */
    String command;

    /**
     * agent download url
     */
    String agentCtl;

    /**
     * need password or not
     */
    Boolean needPassword;
}
