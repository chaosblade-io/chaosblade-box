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

package com.alibaba.chaosblade.platform.invoker.http;

import com.alibaba.chaosblade.platform.invoker.ResponseCommand;
import com.alibaba.chaosblade.platform.invoker.http.model.reuest.HttpChannelRequest;
import com.alibaba.testable.core.annotation.MockWith;
import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * @author yefei
 */
@Slf4j
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = ChaosBladeAttackHttpInvokerTest.Config.class)
public class ChaosBladeAttackHttpInvokerTest {

    @Configuration
    @ComponentScan("com.alibaba.chaosblade.platform.invoker.http")
    public static class Config {
    }

    @Autowired
    private ChaosBladeAttackHttpInvoker chaosBladeAttackHttpInvoker;

    @Test
    public void testAttack() throws Exception {
        HttpChannelRequest requestCommand = new HttpChannelRequest();

        requestCommand.setSceneCode("chaosblade.cpu.fullload");
        requestCommand.setHost("192.168.1.1");
        requestCommand.setPort(19527);

        ResponseCommand responseCommand = chaosBladeAttackHttpInvoker.invoke(requestCommand).get();
        Assert.assertTrue(responseCommand.isSuccess());
    }

}
