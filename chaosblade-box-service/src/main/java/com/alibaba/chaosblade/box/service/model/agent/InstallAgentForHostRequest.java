package com.alibaba.chaosblade.box.service.model.agent;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InstallAgentForHostRequest {

    /**
     * 应用名
     */
    String appName;

    /**
     * 应用分组名
     */
    String appGroupName;

    /**
     * 目标ip
     */
    String instanceId;

    /**
     * 目标port
     */

    String sshPort;

    /**
     * 目标机器用户名
     */
    String sshUser;

    /**
     * 目标机器密码
     */
    String sshPassword;


}
