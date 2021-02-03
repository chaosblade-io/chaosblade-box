package com.alibaba.chaosblade.platform.collector.prometheus;

import com.alibaba.chaosblade.platform.cmmon.utils.JsonUtils;
import com.alibaba.chaosblade.platform.collector.prometheus.model.PrometheusNode;
import com.alibaba.chaosblade.platform.cmmon.model.PrometheusResponse;
import com.fasterxml.jackson.core.type.TypeReference;
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

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author yefei
 */
public class PrometheusCollectorTest {

    @Test
    public void nodeCollectorTest() throws Exception {
        CloseableHttpAsyncClient httpAsyncClient = HttpAsyncClients.createDefault();
        httpAsyncClient.start();
        HttpPost httpPost = new HttpPost("http://192.168.0.1:32105/api/v1/query_range");
        List<NameValuePair> params = new ArrayList<>();
        params.add(new BasicNameValuePair("query", "kube_pod_info"));
        params.add(new BasicNameValuePair("start", String.valueOf(System.currentTimeMillis() / 1000)));
        params.add(new BasicNameValuePair("end", String.valueOf(System.currentTimeMillis() / 1000)));
        params.add(new BasicNameValuePair("step", "14"));

        try {
            httpPost.setEntity(new UrlEncodedFormEntity(params));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        httpAsyncClient.execute(httpPost, new FutureCallback<HttpResponse>() {
            @Override
            public void completed(HttpResponse httpResponse) {
                try {
                    HttpEntity entity = httpResponse.getEntity();

                    PrometheusResponse<PrometheusNode> response = JsonUtils.readValue(new TypeReference<PrometheusResponse<PrometheusNode>>() {
                    }, EntityUtils.toByteArray(entity));

                    System.out.println(response);
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
