package com.alibaba.chaosblade.platform.metric.prometheus;

import com.alibaba.chaosblade.platform.cmmon.utils.JsonUtils;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.concurrent.FutureCallback;
import org.apache.http.impl.nio.client.CloseableHttpAsyncClient;
import org.apache.http.impl.nio.client.HttpAsyncClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author yefei
 */
public class PrometheusApiTest {

    @Test
    public void testCpuFree() throws Exception {
        CloseableHttpAsyncClient client = HttpAsyncClients.createDefault();
        client.start();

        HttpPost httpPost = new HttpPost("http://192.168.0.1:9090/api/v1/query_range");

        List<NameValuePair> params = new ArrayList<>();
        params.add(new BasicNameValuePair("query", "node_memory_MemFree_bytes"));
        System.out.println(System.currentTimeMillis() / 1000);
        String start = String.valueOf((System.currentTimeMillis() - 5000) / 1000);
        String end = String.valueOf(System.currentTimeMillis() / 1000);
        System.out.println(start + "  " + end);

        params.add(new BasicNameValuePair("start", "1612088042"));

        params.add(new BasicNameValuePair("end", "1612088052"));
        params.add(new BasicNameValuePair("step", "14"));

        httpPost.setEntity(new UrlEncodedFormEntity(params));

        client.execute(httpPost, new FutureCallback<HttpResponse>() {
            @Override
            public void completed(HttpResponse httpResponse) {
                try {
                    HttpEntity entity = httpResponse.getEntity();

                    ByteArrayInputStream inputStream = new ByteArrayInputStream(EntityUtils.toByteArray(entity));
                    JsonNode jsonNode = JsonUtils.reader().readTree(inputStream);

                    ArrayNode arrayNode = (ArrayNode) jsonNode.get("data").get("result").get(0).get("values");
                    for (JsonNode node : arrayNode) {
                        String date = node.get(0).asText();
                        String value = node.get(1).asText();
                        System.out.println(date + value);
                    }
                    System.out.println(EntityUtils.toString(entity));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void failed(Exception e) {
                e.printStackTrace();
            }

            @Override
            public void cancelled() {

            }
        });

        Thread.currentThread().join();
    }
}
