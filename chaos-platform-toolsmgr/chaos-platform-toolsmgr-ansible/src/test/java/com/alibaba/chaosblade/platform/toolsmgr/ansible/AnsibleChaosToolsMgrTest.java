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

package com.alibaba.chaosblade.platform.toolsmgr.ansible;

import com.alibaba.chaosblade.platform.cmmon.DeviceMeta;
import com.alibaba.chaosblade.platform.cmmon.utils.JsonUtils;
import com.alibaba.chaosblade.platform.toolsmgr.api.Request;
import com.alibaba.chaosblade.platform.toolsmgr.api.Response;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

/**
 * @author yefei
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = Config.class)
public class AnsibleChaosToolsMgrTest {

    @Autowired
    private AnsibleChaosToolsMgr ansibleChaosToolsMgr;

    @Test
    public void test() {
        Response<List<DeviceMeta>> response = ansibleChaosToolsMgr.listHosts(Request.builder().build());
        System.out.println(JsonUtils.writeValueAsString(response));
        Assert.assertTrue(response.isSuccess());
    }

    @Test
    public void deployAgent() {
        Response<String> response = ansibleChaosToolsMgr.deployAgent(Request.builder()
                .host("115.159.227.21")
                .probesId(1L)
                .commandOptions("-r https://chaosblade.oss-cn-hangzhou.aliyuncs.com/demo/chaosagent -t 192.168.1.1:8080")
                .build());
        System.out.println(response.getMessage());
        Assert.assertTrue(response.isSuccess());
    }

    @Test
    public void unDeployAgent() {
        Response<String> response = ansibleChaosToolsMgr.deployAgent(Request.builder()
                .host("192.168.1.1")
                .build());
        Assert.assertTrue(response.isSuccess());
    }
}
