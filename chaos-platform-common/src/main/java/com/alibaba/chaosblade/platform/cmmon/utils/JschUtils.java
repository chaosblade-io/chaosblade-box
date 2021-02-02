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

package com.alibaba.chaosblade.platform.cmmon.utils;

import cn.hutool.core.io.IoUtil;
import cn.hutool.extra.ssh.JschUtil;
import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.Session;

import java.io.InputStream;

/**
 * @author yefei
 */
public class JschUtils {

    public static void uploadFile(String host, String user, String password, int port, String src, String dest) throws Exception {
        Session session = JschUtil.getSession(host, port, user, password);
        Channel channel = null;
        try {
            channel = session.openChannel("sftp");
            ChannelSftp sftp = (ChannelSftp) channel;
            sftp.connect();
            sftp.put(src, dest);
        } finally {
            session.disconnect();
            channel.disconnect();
        }
    }

    public static String unarchive(String host, String user, String password, int port, String src, String dest) throws Exception {
        Session session = JschUtil.getSession(host, port, user, password);
        ChannelExec channelExec = (ChannelExec) session.openChannel("exec");
        channelExec.setCommand(String.format("tar -zxvf %s -C %s", src, dest));
        channelExec.connect();
        InputStream errStream = channelExec.getErrStream();
        if (errStream.available() > 0) {
            return IoUtil.read(errStream, "UTF-8");
        }
        InputStream in = channelExec.getInputStream();
        return IoUtil.read(in, "UTF-8");
    }

    public static String start(String host, String user, String password, int port, String version, String platFormHost) throws Exception {
        Session session = JschUtil.getSession(host, port, user, password);
        ChannelExec channelExec = (ChannelExec) session.openChannel("exec");
        channelExec.setCommand(String.format("sh -c 'nohup /opt/chaos-agent-%s/chaosAgent --transport.endpoint=%s:8080 > chaos-agent.log 2>&1 &'", version, platFormHost));
        channelExec.connect();
        InputStream errStream = channelExec.getErrStream();
        if (errStream.available() > 0) {
            return IoUtil.read(errStream, "UTF-8");
        }
        InputStream in = channelExec.getInputStream();
        return IoUtil.read(in, "UTF-8");
    }


    public static void uploadFile(String host, String user, int port, String privateKey, byte[] passphrase, String src, String dest) throws Exception {
        Session session = JschUtil.getSession(host, port, user, privateKey, passphrase);
        Channel channel = null;
        try {
            channel = session.openChannel("sftp");
            ChannelSftp sftp = (ChannelSftp) channel;
            sftp.connect();
            sftp.put(src, dest);
        } finally {
            session.disconnect();
            channel.disconnect();
        }
    }
}
