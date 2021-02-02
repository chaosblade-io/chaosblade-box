package com.alibaba.chaosblade.platform.cmmon.jackson;

import com.alibaba.chaosblade.platform.cmmon.ansible.AnsibleResponse;
import com.alibaba.chaosblade.platform.cmmon.ansible.AnsibleUtil;
import org.junit.Assert;
import org.junit.Test;

/**
 * @author yefei
 */
public class AnsibleUtilTest {

    @Test
    public void test() {
        AnsibleResponse response = AnsibleUtil.getHosts();
        System.out.println(response.getHost());
        Assert.assertEquals(true, response.getChanged());
    }

    @Test
    public void deployAgent() {
        AnsibleResponse response = AnsibleUtil.deployAgent(
                "192.168.1.1",
                0L,
                "-r https://chaosblade.oss-cn-hangzhou.aliyuncs.com/demo/chaosagent -t 192.168.1.1:8080");
        System.out.println(response);
        Assert.assertEquals(true, response.getChanged());
    }

    @Test
    public void unDeployAgent() {
        AnsibleResponse response = AnsibleUtil.unDeployAgent("192.168.1.1");
        System.out.println(response);
        Assert.assertEquals(true, response.getChanged());
    }

}
