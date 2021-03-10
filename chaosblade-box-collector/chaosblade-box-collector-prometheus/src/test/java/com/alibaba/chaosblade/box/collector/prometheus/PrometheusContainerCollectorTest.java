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

package com.alibaba.chaosblade.box.collector.prometheus;

import com.alibaba.chaosblade.box.collector.model.Container;
import com.alibaba.chaosblade.box.collector.model.Query;
import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;
import java.util.concurrent.CompletableFuture;

/**
 * @author yefei
 */
@Slf4j
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = PrometheusContainerCollectorTest.Config.class)
public class PrometheusContainerCollectorTest {

    @Configuration
    @ComponentScan("com.alibaba.chaosblade.box.collector.prometheus")
    public static class Config {
    }

    @Autowired
    private PrometheusContainerCollector prometheusContainerCollector;

    @Test
    public void testAttack() throws Exception {

        CompletableFuture<List<Container>> collect = prometheusContainerCollector.collect(Query.builder().build());

        List<Container> containers = collect.get();
        Assert.assertEquals(0, containers.size());
    }

}
