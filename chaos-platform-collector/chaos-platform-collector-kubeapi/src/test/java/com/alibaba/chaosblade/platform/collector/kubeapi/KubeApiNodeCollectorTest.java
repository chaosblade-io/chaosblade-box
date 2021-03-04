package com.alibaba.chaosblade.platform.collector.kubeapi;

import com.alibaba.chaosblade.platform.collector.model.Node;
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
@ContextConfiguration(classes = KubeApiNodeCollectorTest.Config.class)
public class KubeApiNodeCollectorTest {

    @Configuration
    @ComponentScan("com.alibaba.chaosblade.platform.collector.kubeapi")
    public static class Config {
    }

    @Autowired
    private KubeApiNodeCollector nodeCollector;

    @MockDiagnose(LogLevel.ENABLE)
    public static class Mock {

        @MockMethod
        public Call listNodeAsync(
                CoreV1Api self,
                String pretty, Boolean allowWatchBookmarks, String _continue, String fieldSelector, String labelSelector, Integer limit, String resourceVersion, Integer timeoutSeconds, Boolean watch, ApiCallback<V1NodeList> _callback) throws ApiException {
            V1NodeList v1NodeList = new V1NodeList();
            ArrayList<V1Node> v1Nodes = new ArrayList<>();
            V1Node node = new V1Node();

            // name
            V1ObjectMeta metadata = new V1ObjectMeta();
            metadata.setName("node-master");
            node.setMetadata(metadata);

            // address
            ArrayList<V1NodeAddress> addresses = new ArrayList<>();
            V1NodeAddress nodeAddress = new V1NodeAddress();
            nodeAddress.setAddress("192.168.1.1");
            addresses.add(nodeAddress);

            V1NodeStatus status = new V1NodeStatus();
            status.setAddresses(addresses);
            node.setStatus(status);

            v1Nodes.add(node);
            v1NodeList.setItems(v1Nodes);
            _callback.onSuccess(v1NodeList, 200 , new HashMap<>());
            return null;
        }

    }

    @Test
    public void testAttack() throws Exception {
        CompletableFuture<List<Node>> collect = nodeCollector.collect(Query.builder().build());
        List<Node> nodes = collect.get();
        Assert.assertEquals(1, nodes.size());
        Assert.assertEquals("node-master", nodes.get(0).getName());
    }

}
