package com.alibaba.chaosblade.platform.collector.kubeapi;

import io.kubernetes.client.openapi.ApiClient;
import io.kubernetes.client.openapi.Configuration;
import io.kubernetes.client.openapi.apis.CoreV1Api;
import io.kubernetes.client.openapi.models.V1Container;
import io.kubernetes.client.openapi.models.V1Pod;
import io.kubernetes.client.openapi.models.V1PodList;
import io.kubernetes.client.util.Config;
import org.junit.Test;

/**
 * @author yefei
 */
public class ContainerCollectorTest {

    @Test
    public void test() throws Exception {
        ApiClient client = Config.defaultClient();
        Configuration.setDefaultApiClient(client);

        CoreV1Api api = new CoreV1Api();
        V1PodList list = api.listPodForAllNamespaces(null, null, "metadata.name=chaos-platform-5765cd7867-wmz2w", null, null, null, null, null, null);
        for (V1Pod item : list.getItems()) {
            for (V1Container container : item.getSpec().getContainers()) {
                System.out.println(container.getName() + ":" + container.getImage());
            }
        }
    }
}
