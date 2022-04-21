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

package com.alibaba.chaosblade.box.toolsmgr.impl;

import cn.hutool.core.io.IoUtil;
import com.alibaba.chaosblade.box.common.common.domain.response.Response;
import com.alibaba.chaosblade.box.common.common.util.SystemPropertiesUtils;
import com.alibaba.chaosblade.box.toolsmgr.ChaosToolsMgr;
import com.alibaba.chaosblade.box.toolsmgr.annotation.ChannelStrategy;
import com.alibaba.chaosblade.box.toolsmgr.ansible.AnsibleCommand;
import com.alibaba.chaosblade.box.toolsmgr.ansible.command.AnsibleAppendHostCommand;
import com.alibaba.chaosblade.box.toolsmgr.ansible.command.AnsibleListAllHostsCommand;
import com.alibaba.chaosblade.box.toolsmgr.ansible.command.AnsiblePingCommand;
import com.alibaba.chaosblade.box.toolsmgr.ansible.command.AnsibleShellScriptCommand;
import com.alibaba.chaosblade.box.toolsmgr.enums.ChannelType;
import com.alibaba.chaosblade.box.toolsmgr.model.MgrRequest;
import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static com.alibaba.chaosblade.box.toolsmgr.ansible.AnsibleConstants.ANSIBLE_LINUX_DEFAULT_PORT;
import static com.alibaba.chaosblade.box.toolsmgr.ansible.AnsibleConstants.SSH_FILE_COPY_DEFAULT_PATH;

/**
 * @author yefei
 */
@Slf4j
@Component
@ChannelStrategy(ChannelType.ANSIBLE)
public class AnsibleChaosToolsMgr implements ChaosToolsMgr<MgrRequest> {

    private static final long DEFAULT_TIME_OUT = 3000;

    @Override
    public Response<String> deployAndInstallAgent(MgrRequest mgrRequest) {


        AnsibleCommand ansibleCommand = new AnsibleShellScriptCommand();
        return ansibleExecutor(ansibleCommand.getCommand(mgrRequest), ansibleCommand, 3 * 60000, TimeUnit.MICROSECONDS);
    }

    @Override
    public Response<String> uninstallAgent(MgrRequest mgrRequest) {
        String command = String.format("sh /opt/chaos/chaosctl.sh uninstall");
        mgrRequest.setCommand(command);
        AnsibleCommand ansibleCommand = new AnsibleShellScriptCommand();
        return ansibleExecutor(ansibleCommand.getCommand(mgrRequest), ansibleCommand);
    }



    @Override
    public Response<String> testConnection(MgrRequest mgrRequest) {
        if (checkLocalHostAndAppend(mgrRequest)) {
            // ping test
            AnsibleCommand ansiblePingCommand = new AnsiblePingCommand();
            Response<String> pingResult1 = ansibleExecutor(ansiblePingCommand.getCommand(mgrRequest), ansiblePingCommand);
            if (pingResult1.isSuccess()){
                return pingResult1;
            }

            // ping failed, need send sshkey
            Response<String> sendResult = sendSSHKeyIfNotExitOnPassword(mgrRequest);
            if (!sendResult.isSuccess()) {
                log.error("[ansible] send ssh key failed. "+ sendResult.getError());
                return sendResult;
            }

            return ansibleExecutor(ansiblePingCommand.getCommand(mgrRequest), ansiblePingCommand);
        }
        return Response.ofFailure(Response.Code.INVALID_Parameter, "can not append to /etc/ansible/hosts");
    }

    @Override
    public Response<String> pingConnection(MgrRequest mgrRequest) {
        AnsibleCommand ansiblePingCommand = new AnsiblePingCommand();
        return ansibleExecutor(ansiblePingCommand.getCommand(mgrRequest), ansiblePingCommand);
    }


    /**
     * 发送 SSH Key 授权文件
     *
     * @param mgrRequest
     * @return
     */
    private static Response<String> sendSSHKeyIfNotExitOnPassword(MgrRequest mgrRequest) {
        log.info("[ansible] send ssh key, starting...");
        if (StringUtils.isBlank(mgrRequest.getInstancePassword())) {
            return Response.ofFailure(Response.Code.INVALID_Parameter, "SSH password must be input");
        }
        String port = mgrRequest.getInstancePort() == null ? ANSIBLE_LINUX_DEFAULT_PORT.toString()
                : mgrRequest.getInstancePort();

        String[] cmds = new String[] {"/usr/bin/expect", SSH_FILE_COPY_DEFAULT_PATH, mgrRequest.getInstanceIp(),
                mgrRequest.getInstanceUser(), mgrRequest.getInstancePassword(), port};

        try {
            Process process = Runtime.getRuntime().exec(cmds, null, null);
            process.waitFor(DEFAULT_TIME_OUT, TimeUnit.MICROSECONDS);
            if (process.getErrorStream().available() > 0) {
                String error = IoUtil.read(process.getErrorStream(), System.getProperty("file.encoding"));
                log.error("[ansible] send sshKey failed : " + error);
                return Response.ofFailure(Response.Code.INVALID_Parameter, error);
            } else {
                List<String> content = new ArrayList<>(16);
                IoUtil.readLines(new InputStreamReader(process.getInputStream()), content);
                log.info("[ansible] send ssh key content: "+JSON.toJSONString(content));

                String result = IoUtil.read(process.getInputStream(), System.getProperty("file.encoding"));
                log.info("[ansible] send ssh key result: "+ result);
                if (result.contains("rc=0") || result.contains("Number of key(s) added: 1")) {
                    return Response.ofSuccess("");
                } else {
                    return Response.ofFailure(Response.Code.INVALID_Parameter, result);
                }
            }
        } catch (Exception e) {
            log.error("Ansible send ssh key exception " + mgrRequest + e.getMessage(), e);
            return Response.ofFailure(Response.Code.INVALID_Parameter, e.getMessage());
        }
    }

    /**
     * 检查本地文件是否存在，若不存在则追加
     *
     * @return
     */
    private static boolean checkLocalHostAndAppend(MgrRequest mgrRequest) {
        AnsibleCommand ansibleAllListHostCommand = new AnsibleListAllHostsCommand();
        Response<String> returnT = ansibleExecutor(ansibleAllListHostCommand.getCommand(mgrRequest), ansibleAllListHostCommand);
        if (returnT.isSuccess() && !returnT.getResult().isEmpty()){
            if (returnT.getResult().contains(mgrRequest.getInstanceIp())) {
                return true;
            }

            AnsibleCommand ansibleAppendHostCommand = new AnsibleAppendHostCommand();
            Response<String> returns = ansibleExecutor(ansibleAppendHostCommand.getCommand(mgrRequest), ansibleAppendHostCommand);
            return returns.isSuccess();
        }

        return false;
    }

    private static Response<String> ansibleExecutor(String ansibleCommand, AnsibleCommand ansibleCommandd) {
        return ansibleExecutor(ansibleCommand, ansibleCommandd, DEFAULT_TIME_OUT, TimeUnit.MICROSECONDS);
    }

    private static Response<String> ansibleExecutor(String ansibleCommand, AnsibleCommand ansibleCommandd,
                                                    long timeout, TimeUnit unit) {
        try {
            Process process = Runtime.getRuntime().exec(new String[]{
                    "/bin/sh", "-c", ansibleCommand}, null, null);
            process.waitFor(timeout, unit);

            /**
             * [WARNING]: No inventory was parsed, only implicit localhost is available
             * [WARNING]: provided hosts list is empty, only localhost is available. Note that
             * the implicit localhost does not match 'all'
             * [WARNING]: Could not match supplied host pattern, ignoring: 47.99.143.216
             */
            if (process.getErrorStream().available() > 0) {
                log.warn("[ansible executor] failed, command: {}, err: {}",ansibleCommand, IoUtil.read(process.getErrorStream(), SystemPropertiesUtils.getPropertiesFileEncoding()));
                Response<String> re =  Response.ofFailure(Response.Code.Deploy_Error, IoUtil.read(process.getErrorStream(), SystemPropertiesUtils.getPropertiesFileEncoding()));
                return re;
            } else {
                List<String> content = new ArrayList<>(16);
                IoUtil.readLines(new InputStreamReader(process.getInputStream()), content);

                if (!ansibleCommandd.resultPredicate(content, true)) {
                    log.warn("[ansible executor] failed, command: {}, err: {}", ansibleCommand, JSON.toJSONString(content));
                    return Response.ofFailure(Response.Code.Deploy_Error, JSON.toJSONString(content));
                }
                log.info("ansible success! command: {}", ansibleCommand);
                return Response.ofSuccess(JSON.toJSONString(content));

            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return Response.ofFailure(Response.Code.Deploy_Error, e.getMessage());
        }
    }
}
