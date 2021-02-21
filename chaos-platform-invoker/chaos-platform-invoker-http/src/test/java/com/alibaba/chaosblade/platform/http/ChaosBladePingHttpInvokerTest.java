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

package com.alibaba.chaosblade.platform.http;

import com.alibaba.chaosblade.platform.http.model.reuest.HttpChannelRequest;
import com.alibaba.chaosblade.platform.invoker.ResponseCommand;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.concurrent.CompletableFuture;

/**
 * @author yefei
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = Config.class)
public class ChaosBladePingHttpInvokerTest {

    @Autowired
    private ChaosBladePingHttpInvoker chaosBladePingHttpInvoker;

    private int chaosAgentPort = 19527;

    @Test
    public void pingTest() throws Throwable {
        HttpChannelRequest request = new HttpChannelRequest();
        request.setRequestURL("/ping");
        request.setHost("192.168.0.1");
        request.setPort(chaosAgentPort);

        CompletableFuture<ResponseCommand> future = chaosBladePingHttpInvoker.invoke(request);
        ResponseCommand responseCommand = future.get();
        Assert.assertTrue(responseCommand.isSuccess());
    }
}
