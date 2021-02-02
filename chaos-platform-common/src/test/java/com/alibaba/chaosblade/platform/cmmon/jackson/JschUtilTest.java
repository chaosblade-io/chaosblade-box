package com.alibaba.chaosblade.platform.cmmon.jackson;

import cn.hutool.core.io.IoUtil;
import cn.hutool.extra.ssh.JschUtil;
import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.Session;
import org.junit.Test;

import java.io.InputStream;

/**
 * @author yefei
 */
public class JschUtilTest {

    @Test
    public void test() throws Exception {
        Session session = JschUtil.getSession("192.168.1.1", 22, "root", "QAZ0okm");
        ChannelExec channelExec = (ChannelExec) session.openChannel("exec");
        channelExec.setCommand("ls /");
        channelExec.connect();
        InputStream in = channelExec.getInputStream();
        String read = IoUtil.read(in, "UTF-8");
        System.out.println(read);
    }

}
