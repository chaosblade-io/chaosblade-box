package com.alibaba.chaosblade.platform.cmmon.jackson;

import org.junit.Test;

/**
 * @author yefei
 */
public class JsonUtilsTest {

    @Test
    public void testMapToArgumentsDeserializer() {

//        String str = "{\"categoryId\":\"2\",\"parentId\":null,\"level\":0,\"name\":\"CPU监控\",\"code\":\"metric.prometheus\",\"params\":[{\"name\":\"url\",\"value\":\"http://192.168.0.1:9090/api/v1/query_range\"},{\"name\":\"query\",\"value\":\"(1-(sum(increase(node_cpu_seconds_total{instance=\\\"172.19.128.190:9100\\\",mode=\\\"idle\\\"}[1m]))by(instance))/(sum(increase(node_cpu_seconds_total[1m]))by(instance)))*100\"}],\"children\":null}";
//        MetricModel metricModel = new MetricModel();
//        HashMap<String, String> params = new HashMap<>();
//        params.put("key", "v");
//        params.put("key2", "v2");
//        metricModel.setParams(null);
//
//        String s = JsonUtils.writeValueAsString(metricModel);
//        System.out.println(s);


    }
}
