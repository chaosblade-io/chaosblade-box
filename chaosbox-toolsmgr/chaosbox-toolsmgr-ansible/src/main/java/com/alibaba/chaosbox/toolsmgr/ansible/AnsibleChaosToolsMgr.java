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

package com.alibaba.chaosbox.toolsmgr.ansible;

import cn.hutool.core.io.IoUtil;
import com.alibaba.chaosbox.common.DeviceMeta;
import com.alibaba.chaosbox.common.utils.SystemPropertiesUtils;
import com.alibaba.chaosbox.toolsmgr.api.*;
import com.alibaba.chaosbox.toolsmgr.api.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author yefei
 */
@Slf4j
@Component
@ChannelStrategy(ChannelType.ANSIBLE)
public class AnsibleChaosToolsMgr implements ChaosToolsMgr<Request> {

    @Value("${chaos.ctl.agent}")
    private String agentCtl;

    @Value("${chaos.ctl.tools}")
    private String toolsCtl;

    @Override
    public Response<List<DeviceMeta>> listHosts(Request request) {
        List<String> strList = new ArrayList<>();
        try {
            Process process = Runtime.getRuntime().exec(new String[]{"/bin/sh", "-c", "ansible all --list-hosts"}, null, null);
            process.waitFor();
            if (process.getErrorStream().available() > 0) {
                return Response.ofFail(IoUtil.read(process.getErrorStream(), SystemPropertiesUtils.getPropertiesFileEncoding()));
            } else {
                IoUtil.readLines(new InputStreamReader(process.getInputStream()), strList);
                if (strList.size() > 1) {
                    strList.remove(0);
                }
                return Response.ofSuccess(strList.stream()
                        .map(ip -> DeviceMeta.builder().ip(ip.trim()).build())
                        .collect(Collectors.toList()));
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return Response.ofFail(e.getMessage());
        }
    }

    @Override
    public Response<String> deployAgent(Request request) {
        String command = String.format("wget %s -O chaosagentctl.sh " +
                "&& chmod +x chaosagentctl.sh " +
                "&& ./chaosagentctl.sh install -u %d %s", agentCtl, request.getProbesId(), request.getCommandOptions());
        return getStringResponse(request, command);
    }

    @Override
    public Response<String> unDeployAgent(Request request) {
        String command = String.format("wget %s -O chaosagentctl.sh " +
                "&& chmod +x chaosagentctl.sh " +
                "&& ./chaosagentctl.sh uninstall", agentCtl);
        return getStringResponse(request, command);
    }

    @Override
    public Response<String> deployTools(Request request) {
        String command = String.format("wget %s -O chaostoolsctl.sh " +
                "&& chmod +x chaostoolsctl.sh " +
                "&& ./chaostoolsctl.sh install -n %s -v %s -r %s", toolsCtl, request.getToolsName(), request.getToolsVersion(), request.getToolsUrl());
        return getStringResponse(request, command);
    }

    @Override
    public Response<String> unDeployTools(Request request) {
        String command = String.format("wget %s -O chaostoolsctl.sh " +
                "&& chmod +x chaostoolsctl.sh " +
                "&& ./chaostoolsctl.sh uninstall -n %s", toolsCtl, request.getToolsName());
        return getStringResponse(request, command);
    }

    private Response<String> getStringResponse(Request request, String command) {
        try {
            Process process = Runtime.getRuntime().exec(new String[]{"/bin/sh", "-c",
                    String.format("ansible %s -m shell -a 'sh -c  \"%s\"'", request.getHost(), command)}, null, null);
            process.waitFor();

            if (process.getErrorStream().available() > 0) {
                return Response.ofFail(IoUtil.read(process.getErrorStream(), SystemPropertiesUtils.getPropertiesFileEncoding()));
            } else {
                String result = IoUtil.read(process.getInputStream(), SystemPropertiesUtils.getPropertiesFileEncoding());
                if (result.contains("rc=0")) {
                    return Response.ofSuccess(result);
                } else {
                    return Response.ofFail(result);
                }
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return Response.ofFail(e.getMessage());
        }
    }
}
