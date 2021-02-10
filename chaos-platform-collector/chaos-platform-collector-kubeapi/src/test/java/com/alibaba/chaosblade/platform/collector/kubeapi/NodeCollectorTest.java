package com.alibaba.chaosblade.platform.collector.kubeapi;

import io.kubernetes.client.openapi.ApiClient;
import io.kubernetes.client.openapi.Configuration;
import io.kubernetes.client.openapi.apis.CoreV1Api;
import io.kubernetes.client.openapi.models.V1Node;
import io.kubernetes.client.openapi.models.V1NodeList;
import io.kubernetes.client.util.Config;
import org.junit.Test;

/**
 * @author yefei
 */
public class NodeCollectorTest {

    @Test
    public void test() throws Exception {
        ApiClient client = Config.defaultClient();
        Configuration.setDefaultApiClient(client);

        CoreV1Api api = new CoreV1Api();
        V1NodeList list = api.listNode(null, null, null, null, null, null, null, null, null);
        for (V1Node item : list.getItems()) {
            System.out.println(item.getMetadata().getName() + ":" + item.getStatus().getNodeInfo().getMachineID());
        }
    }
}
