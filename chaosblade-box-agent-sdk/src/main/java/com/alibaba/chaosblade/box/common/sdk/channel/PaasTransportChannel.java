package com.alibaba.chaosblade.box.common.sdk.channel;

import com.alibaba.chaosblade.box.common.common.domain.response.Response;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.alibaba.chaosblade.box.common.sdk.transport.Request;
import com.alibaba.chaosblade.box.common.sdk.util.StringUtil;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

/**
 * @author Changjun Xiao
 */
public class PaasTransportChannel {

    public static final String CHAOSBLADE_HANDLER = "chaosblade";
    private static final Logger LOGGER = LoggerFactory.getLogger(PaasTransportChannel.class);
    private String domain;

    public PaasTransportChannel(String host, String port) {
        this.domain = host + ":" + port;
    }

    public <R> Response<R> invoke(Request request, Class<?> clazz, int timeout) {
        Response<R> response;
        LOGGER.info("paas invoke domain: {}, request: {}", domain, JSON.toJSONString(request));
        try {
            String handler = request.getHeader("handler");
            if (StringUtil.isBlank(handler)) {
                handler = CHAOSBLADE_HANDLER;
            }
            String result = invoke(domain, handler, createHttpEntity(request), timeout);
            request.removeHeader("timeout");
            try {
                // 转换成 response
                response = JSON.parseObject(result, new TypeReference<Response<R>>(clazz) {
                });
            } catch (Exception e) {
                LOGGER.warn("invoke http failed, result: {}", result, e);
                response = Response.ofFailure(Response.Code.SERVER_ERROR, result);
            }
        } catch (Exception e) {
            LOGGER.warn("invoke chaos agent failed", e);
            response = Response.ofFailure(Response.Code.SERVER_ERROR, e.getMessage());
        }
        LOGGER.info("pass invoke result: {}", JSON.toJSONString(response));
        return response;
    }

    private HttpEntity createHttpEntity(Request request) throws UnsupportedEncodingException {
        ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("body", JSON.toJSONString(request)));

        return new UrlEncodedFormEntity(params, "UTF-8");
    }

    private String invoke(String endpoint, String path, HttpEntity httpEntity, int timeout) throws IOException {
        RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(30000).setConnectTimeout(30000).build();

        HttpPost httpPost =
                new HttpPost("http://" + endpoint + "/" + path);
        RequestConfig config = RequestConfig.custom().setSocketTimeout(timeout).build();
        httpPost.setConfig(config);
        httpPost.setHeader("Accept", "application/json;charset=UTF-8");
        httpPost.setHeader("Content-Type", "application/x-www-form-urlencoded");

        httpPost.setEntity(httpEntity);

        CloseableHttpClient httpClient = HttpClients.custom().setDefaultRequestConfig(requestConfig).build();
        CloseableHttpResponse response = httpClient.execute(httpPost);
        HttpEntity entity = response.getEntity();
        return EntityUtils.toString(entity);
    }
}
