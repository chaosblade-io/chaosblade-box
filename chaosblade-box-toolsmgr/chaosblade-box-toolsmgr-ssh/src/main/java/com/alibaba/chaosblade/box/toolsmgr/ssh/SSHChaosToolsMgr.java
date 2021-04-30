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

package com.alibaba.chaosblade.box.toolsmgr.ssh;

import ch.ethz.ssh2.ChannelCondition;
import ch.ethz.ssh2.Connection;
import ch.ethz.ssh2.Session;
import cn.hutool.core.io.IoUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.chaosblade.box.common.DeviceMeta;
import com.alibaba.chaosblade.box.toolsmgr.api.ChannelStrategy;
import com.alibaba.chaosblade.box.toolsmgr.api.ChannelType;
import com.alibaba.chaosblade.box.toolsmgr.api.ChaosToolsMgr;
import com.alibaba.chaosblade.box.toolsmgr.api.Response;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.nio.charset.Charset;
import java.util.List;

/**
 * @author yefei
 */
@Slf4j
@Component
@ChannelStrategy(ChannelType.SSH)
public class SSHChaosToolsMgr implements ChaosToolsMgr<SSHRequest> {

    @Value("${chaos.ctl.agent}")
    private String agentCtl;

    @Value("${chaos.ctl.tools}")
    private String toolsCtl;

    @Override
    public Response<List<DeviceMeta>> listHosts(SSHRequest request) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Response<String> deployAgent(SSHRequest request) {
        String command = String.format("wget %s -O chaosagentctl.sh " +
                "&& chmod +x chaosagentctl.sh " +
                "&& ./chaosagentctl.sh install -u %d %s", agentCtl, request.getProbesId(), request.getCommandOptions());
        return getStringResponse(request, command);
    }

    @Override
    public Response<String> unDeployAgent(SSHRequest request) {
        String command = String.format("wget %s -O chaosagentctl.sh " +
                "&& chmod +x chaosagentctl.sh " +
                "&& ./chaosagentctl.sh uninstall " +
                "&& rm -rf chaosagentctl.sh " +
                "&& rm -f /opt/chaos/* ", agentCtl);
        return getStringResponse(request, command);
    }

    @Override
    public Response<String> deployTools(SSHRequest request) {
        String command = String.format("wget %s -O chaostoolsctl.sh " +
                "&& chmod +x chaostoolsctl.sh " +
                "&& ./chaostoolsctl.sh install -n %s -v %s -r %s", toolsCtl, request.getToolsName(), request.getToolsVersion(), request.getToolsUrl());
        return getStringResponse(request, command);
    }

    @Override
    public Response<String> unDeployTools(SSHRequest request) {
        String command = String.format("wget %s -O chaostoolsctl.sh " +
                "&& chmod +x chaostoolsctl.sh " +
                "&& ./chaostoolsctl.sh uninstall -n %s", toolsCtl, request.getToolsName());
        return getStringResponse(request, command);
    }

    private Response<String> getStringResponse(SSHRequest request, String command) {
        InputStream stdOut = null;
        InputStream stdErr = null;
        Connection conn = null;
        Session session = null;
        try {
            conn = new Connection(request.getHost());
            conn.connect();
            conn.authenticateWithPassword(request.getUser(), request.getPassword());

            session = conn.openSession();
            session.requestPTY("bash");
            session.startShell();
            stdOut = session.getStdout();
            stdErr = session.getStderr();

            PrintWriter out = new PrintWriter(session.getStdin());
            out.println(command);
            out.println("exit");
            out.close();

            session.waitForCondition(ChannelCondition.CLOSED | ChannelCondition.EOF | ChannelCondition.EXIT_STATUS, 20000);

            String errorMessage = IoUtil.read(stdErr, Charset.defaultCharset());
            String message = IoUtil.read(stdOut, Charset.defaultCharset());

            if (session.getExitStatus() != null && session.getExitStatus() == 0) {
                return Response.ofSuccess(StrUtil.appendIfMissing(errorMessage, message));
            } else {
                return Response.ofFail(StrUtil.appendIfMissing(errorMessage, message));
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return Response.ofFail(e.getMessage());
        } finally {
            if (conn != null) {
                conn.close();
            }
            if (session != null) {
                session.close();
            }
            IoUtil.close(stdOut);
            IoUtil.close(stdErr);
        }
    }
}
