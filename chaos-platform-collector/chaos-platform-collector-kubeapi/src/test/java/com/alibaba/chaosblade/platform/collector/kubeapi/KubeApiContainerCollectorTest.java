package com.alibaba.chaosblade.platform.collector.kubeapi;

import com.alibaba.chaosblade.platform.collector.model.Container;
import com.alibaba.chaosblade.platform.collector.model.Query;
import com.alibaba.testable.core.annotation.MockDiagnose;
import com.alibaba.testable.core.annotation.MockMethod;
import com.alibaba.testable.core.model.LogLevel;
import io.kubernetes.client.openapi.ApiCallback;
import io.kubernetes.client.openapi.ApiException;
import io.kubernetes.client.openapi.apis.CoreV1Api;
import io.kubernetes.client.openapi.models.*;
import lombok.extern.slf4j.Slf4j;
import okhttp3.Call;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.CompletableFuture;

/**
 * @author yefei
 */
@Slf4j
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = KubeApiContainerCollectorTest.Config.class)
public class KubeApiContainerCollectorTest {

    @Configuration
    @ComponentScan("com.alibaba.chaosblade.platform.collector.kubeapi")
    public static class Config {
    }

    @Autowired
    private KubeApiContainerCollector containerCollector;

    @MockDiagnose(LogLevel.ENABLE)
    public static class Mock {

        @MockMethod
        public Call listPodForAllNamespacesAsync(
                CoreV1Api self,
                Boolean allowWatchBookmarks, String _continue, String fieldSelector, String labelSelector, Integer limit, String pretty, String resourceVersion, Integer timeoutSeconds, Boolean watch, ApiCallback<V1PodList> _callback) throws ApiException {
            V1PodList v1PodList = new V1PodList();

            // name
            V1Pod v1Pod = new V1Pod();
            V1ObjectMeta metadata = new V1ObjectMeta();
            metadata.setName("tomcat-xyz");
            metadata.setNamespace("default");
            v1Pod.setMetadata(metadata);

            // node name
            V1PodSpec spec = new V1PodSpec();
            spec.setNodeName("node-master");
            v1Pod.setSpec(spec);

            V1PodStatus status = new V1PodStatus();
            status.setPodIP("172.16.1.1");
            v1Pod.setStatus(status);

            // container
            ArrayList<V1ContainerStatus> containerStatuses = new ArrayList<>();
            V1ContainerStatus v1ContainerStatus = new V1ContainerStatus();
            v1ContainerStatus.setName("tomcat");
            v1ContainerStatus.setContainerID("abc");
            containerStatuses.add(v1ContainerStatus);
            status.setContainerStatuses(containerStatuses);

            v1PodList.addItemsItem(v1Pod);

            _callback.onSuccess(v1PodList, 200 , new HashMap<>());
            return null;
        }
    }

    @Test
    public void testAttack() throws Exception {
        CompletableFuture<List<Container>> collect = containerCollector.collect(Query.builder()
                .nodeName("node-master")
                .podName("tomcat-xyz")
                .build());
        List<Container> containers = collect.get();
        Assert.assertEquals(1, containers.size());
        Assert.assertEquals("tomcat", containers.get(0).getName());
    }

}
