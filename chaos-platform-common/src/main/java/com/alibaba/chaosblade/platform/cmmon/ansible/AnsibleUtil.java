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

package com.alibaba.chaosblade.platform.cmmon.ansible;

import cn.hutool.core.io.IoUtil;
import lombok.extern.slf4j.Slf4j;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author yefei
 */
@Slf4j
public class AnsibleUtil {

    public static AnsibleResponse getHosts() {
        List<String> strList = new ArrayList<>();
        try {
            Process process = Runtime.getRuntime().exec(new String[]{"/bin/sh", "-c", "ansible all --list-hosts"}, null, null);
            IoUtil.readLines(new InputStreamReader(process.getInputStream()), strList);
            if (strList.size() > 1) {
                strList.remove(0);
            }
            return AnsibleResponse.builder()
                    .changed(true)
                    .host(strList.stream().map(s -> s.trim()).collect(Collectors.toList()))
                    .build();
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return AnsibleResponse.builder().changed(false).msg(e.getMessage()).build();
        }
    }

    public static AnsibleResponse deployAgent(String host, Long probesId, String commandOptions) {
        try {
            String command = String.format("wget https://chaosblade.oss-cn-hangzhou.aliyuncs.com/demo/chaosagentctl.sh -O chaosagentctl.sh " +
                    "&& chmod +x chaosagentctl.sh " +
                    "&& ./chaosagentctl.sh install -u %d %s", probesId, commandOptions);

            Process process = Runtime.getRuntime().exec(new String[]{"/bin/sh", "-c",
                    String.format("ansible %s -m shell -a 'sh -c  \"%s\"'", host, command)}, null, null);
            InputStream errorStream = process.getErrorStream();
            if (errorStream.available() > 0) {
                return AnsibleResponse.builder().msg(IoUtil.read(errorStream, "UTF-8")).build();
            } else {
                String result = IoUtil.read(process.getInputStream(), "UTF-8");
                if (result.contains("CHANGED")) {
                    return AnsibleResponse.builder().changed(true).msg(result).build();
                } else {
                    return AnsibleResponse.builder().changed(false).msg(result).build();
                }
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return AnsibleResponse.builder().msg(e.getMessage()).build();
        }
    }

    public static AnsibleResponse unDeployAgent(String host) {
        try {
            String command = String.format("wget https://chaosblade.oss-cn-hangzhou.aliyuncs.com/demo/chaosagentctl.sh -O chaosagentctl.sh " +
                    "&& chmod +x chaosagentctl.sh " +
                    "&& ./chaosagentctl.sh uninstall");

            Process process = Runtime.getRuntime().exec(new String[]{"/bin/sh", "-c",
                    String.format("ansible %s -m shell -a 'sh -c  \"%s\"'", host, command)}, null, null);
            InputStream errorStream = process.getErrorStream();
            if (errorStream.available() > 0) {
                return AnsibleResponse.builder().changed(false).msg(IoUtil.read(errorStream, "UTF-8")).build();
            } else {
                String result = IoUtil.read(process.getInputStream(), "UTF-8");
                if (result.contains("CHANGED")) {
                    return AnsibleResponse.builder().changed(true).msg(result).build();
                } else {
                    return AnsibleResponse.builder().changed(false).msg(result).build();
                }
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return AnsibleResponse.builder().msg(e.getMessage()).build();
        }
    }

    public static AnsibleResponse deployTools(String host, String name, String version, String url) {
        try {
            String command = String.format("wget https://chaosblade.oss-cn-hangzhou.aliyuncs.com/demo/chaostoolsctl.sh -O chaostoolsctl.sh " +
                    "&& chmod +x chaostoolsctl.sh " +
                    "&& ./chaostoolsctl.sh install -n %s -v %s -r %s", name, version, url);

            Process process = Runtime.getRuntime().exec(new String[]{"/bin/sh", "-c",
                    String.format("ansible %s -m shell -a 'sh -c  \"%s\"'", host, command)}, null, null);
            InputStream errorStream = process.getErrorStream();
            if (errorStream.available() > 0) {
                return AnsibleResponse.builder().msg(IoUtil.read(errorStream, "UTF-8")).build();
            } else {
                String result = IoUtil.read(process.getInputStream(), "UTF-8");
                if (result.contains("CHANGED")) {
                    return AnsibleResponse.builder().changed(true).msg(result).build();
                } else {
                    return AnsibleResponse.builder().changed(false).msg(result).build();
                }
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return AnsibleResponse.builder().msg(e.getMessage()).build();
        }
    }

    public static AnsibleResponse unDeployTools(String host, String name) {
        try {
            String command = String.format("wget https://chaosblade.oss-cn-hangzhou.aliyuncs.com/demo/chaostoolsctl.sh -O chaostoolsctl.sh " +
                    "&& chmod +x chaostoolsctl.sh " +
                    "&& ./chaostoolsctl.sh uninstall -n %s", name);

            Process process = Runtime.getRuntime().exec(new String[]{"/bin/sh", "-c",
                    String.format("ansible %s -m shell -a 'sh -c  \"%s\"'", host, command)}, null, null);
            InputStream errorStream = process.getErrorStream();
            if (errorStream.available() > 0) {
                return AnsibleResponse.builder().msg(IoUtil.read(errorStream, "UTF-8")).build();
            } else {
                String result = IoUtil.read(process.getInputStream(), "UTF-8");
                if (result.contains("CHANGED")) {
                    return AnsibleResponse.builder().changed(true).msg(result).build();
                } else {
                    return AnsibleResponse.builder().changed(false).msg(result).build();
                }
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return AnsibleResponse.builder().msg(e.getMessage()).build();
        }
    }

}
