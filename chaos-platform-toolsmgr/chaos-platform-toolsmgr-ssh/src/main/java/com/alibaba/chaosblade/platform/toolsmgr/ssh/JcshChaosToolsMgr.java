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

package com.alibaba.chaosblade.platform.toolsmgr.ssh;

import cn.hutool.core.io.IoUtil;
import cn.hutool.extra.ssh.JschUtil;
import com.alibaba.chaosblade.platform.cmmon.DeviceMeta;
import com.alibaba.chaosblade.platform.cmmon.utils.SystemPropertiesUtils;
import com.alibaba.chaosblade.platform.toolsmgr.api.ChannelStrategy;
import com.alibaba.chaosblade.platform.toolsmgr.api.ChannelType;
import com.alibaba.chaosblade.platform.toolsmgr.api.ChaosToolsMgr;
import com.alibaba.chaosblade.platform.toolsmgr.api.Response;
import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.Session;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.InputStream;
import java.util.List;

/**
 * @author yefei
 */
@Slf4j
@Component
@ChannelStrategy(ChannelType.SSH)
public class JcshChaosToolsMgr implements ChaosToolsMgr<JschRequest> {

    @Value("${chaos.ctl.agent}")
    private String agentCtl;

    @Value("${chaos.ctl.tools}")
    private String toolsCtl;

    @Override
    public Response<List<DeviceMeta>> listHosts(JschRequest request) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Response<String> deployAgent(JschRequest request) {
        String command = String.format("wget %s -O chaosagentctl.sh " +
                "&& chmod +x chaosagentctl.sh " +
                "&& ./chaosagentctl.sh install -u %d %s", agentCtl, request.getProbesId(), request.getCommandOptions());
        return getStringResponse(request, command);
    }

    @Override
    public Response<String> unDeployAgent(JschRequest request) {
        String command = String.format("wget %s -O chaosagentctl.sh " +
                "&& chmod +x chaosagentctl.sh " +
                "&& ./chaosagentctl.sh uninstall", agentCtl);
        return getStringResponse(request, command);
    }

    @Override
    public Response<String> deployTools(JschRequest request) {
        String command = String.format("wget %s -O chaostoolsctl.sh " +
                "&& chmod +x chaostoolsctl.sh " +
                "&& ./chaostoolsctl.sh install -n %s -v %s -r %s", toolsCtl, request.getToolsName(), request.getToolsVersion(), request.getToolsUrl());
        return getStringResponse(request, command);
    }

    @Override
    public Response<String> unDeployTools(JschRequest request) {
        String command = String.format("wget %s -O chaostoolsctl.sh " +
                "&& chmod +x chaostoolsctl.sh " +
                "&& ./chaostoolsctl.sh uninstall -n %s", toolsCtl, request.getToolsName());
        return getStringResponse(request, command);
    }

    private Response<String> getStringResponse(JschRequest request, String command) {
        try {
            Session session = JschUtil.getSession(request.getHost(), request.getPort(), request.getUser(), request.getPassword());
            ChannelExec channelExec = (ChannelExec) session.openChannel("exec");
            channelExec.setCommand(String.format("/bin/sh -c '%s'", command));
            channelExec.connect();
            channelExec.wait();
            channelExec.getExitStatus();
            if (channelExec.getErrStream().available() > 0) {
                return Response.ofFail(IoUtil.read(channelExec.getErrStream(), SystemPropertiesUtils.getPropertiesFileEncoding()));
            } else {
                InputStream in = channelExec.getInputStream();
                return Response.ofSuccess(IoUtil.read(in, SystemPropertiesUtils.getPropertiesFileEncoding()));
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return Response.ofFail(e.getMessage());
        }
    }
}
