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

package com.alibaba.chaosblade.platform.invoker.litmus.kubeapi;

import com.alibaba.chaosblade.platform.invoker.RequestCommand;
import com.alibaba.chaosblade.platform.invoker.ResponseCommand;
import com.alibaba.testable.core.annotation.MockDiagnose;
import com.alibaba.testable.core.annotation.MockMethod;
import com.alibaba.testable.core.model.LogLevel;
import io.kubernetes.client.openapi.ApiCallback;
import io.kubernetes.client.openapi.ApiException;
import io.kubernetes.client.openapi.apis.CoreV1Api;
import io.kubernetes.client.openapi.apis.CustomObjectsApi;
import io.kubernetes.client.openapi.apis.RbacAuthorizationV1Api;
import io.kubernetes.client.openapi.models.V1DeleteOptions;
import io.kubernetes.client.openapi.models.V1Status;
import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.HashMap;

/**
 * @author yefei
 */
@Slf4j
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = LitmusRecoverChaosInvokerTest.Config.class)
public class LitmusRecoverChaosInvokerTest {

    @Configuration
    @ComponentScan("com.alibaba.chaosblade.platform.invoker.litmus.kubeapi")
    public static class Config {
    }

    @Autowired
    private LitmusRecoverChaosInvoker litmusRecoverChaosInvoker;

    @MockDiagnose(LogLevel.ENABLE)
    public static class Mock {

        @MockMethod
        public okhttp3.Call deleteNamespacedCustomObjectAsync(
                CustomObjectsApi self,
                String group,
                String version,
                String namespace,
                String plural,
                String name,
                Integer gracePeriodSeconds,
                Boolean orphanDependents,
                String propagationPolicy,
                String dryRun,
                V1DeleteOptions body,
                final ApiCallback<Object> _callback)
                throws ApiException {
            log.info("mock delete chaosengines");
            _callback.onSuccess(null, 200, new HashMap<>());
            return null;
        }

        @MockMethod
        public Object deleteNamespacedCustomObject(
                CustomObjectsApi self,
                String group,
                String version,
                String namespace,
                String plural,
                String name,
                Integer gracePeriodSeconds,
                Boolean orphanDependents,
                String propagationPolicy,
                String dryRun,
                V1DeleteOptions body)
                throws ApiException {
            log.info("mock delete experiments");
            return null;
        }

        @MockMethod
        public V1Status deleteNamespacedServiceAccount(CoreV1Api self,
                                                       String name, String namespace, String pretty, String dryRun, Integer gracePeriodSeconds, Boolean orphanDependents, String propagationPolicy, V1DeleteOptions body) throws ApiException {
            log.info("mock create service account");
            return null;
        }

        @MockMethod
        public V1Status deleteClusterRole(
                RbacAuthorizationV1Api self,
                String name,
                String pretty,
                String dryRun,
                Integer gracePeriodSeconds,
                Boolean orphanDependents,
                String propagationPolicy,
                V1DeleteOptions body)
                throws ApiException {
            log.info("mock delete cluster role");
            return null;
        }

        @MockMethod
        public V1Status deleteClusterRoleBinding(
                RbacAuthorizationV1Api self,
                String name,
                String pretty,
                String dryRun,
                Integer gracePeriodSeconds,
                Boolean orphanDependents,
                String propagationPolicy,
                V1DeleteOptions body)
                throws ApiException {
            log.info("mock delete cluster role binding");
            return null;
        }

    }

    @Test
    public void testAttack() throws Exception {
        RequestCommand requestCommand = new RequestCommand();
        requestCommand.setSceneCode("litmuschaos.node-cpu.hog.stop");
        requestCommand.setNamespace("default");
        requestCommand.setName("node-cpu.hog");

        ResponseCommand responseCommand = litmusRecoverChaosInvoker.invoke(requestCommand).get();
        Assert.assertTrue(responseCommand.isSuccess());
    }

}
