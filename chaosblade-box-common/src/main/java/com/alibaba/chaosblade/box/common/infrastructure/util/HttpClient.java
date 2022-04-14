package com.alibaba.chaosblade.box.common.infrastructure.util;

import com.alibaba.chaosblade.box.common.common.enums.CommonErrorCode;
import com.alibaba.chaosblade.box.common.infrastructure.exception.ChaosException;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.impl.nio.client.CloseableHttpAsyncClient;
import org.apache.http.impl.nio.client.HttpAsyncClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.concurrent.ExecutionException;
import java.util.function.Consumer;

/**
 * @author haibin
 *
 *
 */
@Slf4j
@Component
public class HttpClient implements DisposableBean {

    private static final String DEFAULT_ENCODING = "UTF-8";

    static CloseableHttpAsyncClient httpclient;

    private static RequestConfig requestConfig;

    static {
        httpclient = HttpAsyncClients.createDefault();
        httpclient.start();
        requestConfig = RequestConfig.custom().setConnectionRequestTimeout(2000)
            .setConnectTimeout(2000)
            .setSocketTimeout(60 * 1000).build();
    }

    public static <T> HttpResponseFuture<T> get(String systemName, String url, TypeReference<T> typeReference) {
        HttpGet httpGet = new HttpGet(url);
        httpGet.setConfig(requestConfig);
        HttpResponseFuture<T> httpResponseFuture = new HttpResponseFuture<>();
        try {
            String result = request(httpGet);
            httpResponseFuture.setResponse(JSON.parseObject(result, typeReference));
        } catch (Exception exception) {
            log.error("Http request failed,systemName:{},method:{},url:{}", systemName, "get", url, exception);
            httpResponseFuture.setThrowable(exception);
        }
        return httpResponseFuture;
    }

    public static <T> HttpResponseFuture<T> postJsonString(String url, String jsonString,
        TypeReference<T> typeReference) {
        HttpPost httpPost = new HttpPost(url);
        httpPost.setConfig(requestConfig);
        HttpResponseFuture<T> httpResponseFuture = new HttpResponseFuture<>();
        try {
            httpPost.addHeader("Content-Type",
                "application/json; charset=" + DEFAULT_ENCODING);
            httpPost.setEntity(new ByteArrayEntity(jsonString.getBytes(DEFAULT_ENCODING)));
            String result = request(httpPost);
            httpResponseFuture.setResponse(JSON.parseObject(result, typeReference));
        } catch (Exception exception) {
            log.error("Http request failed,method:{},url:{}", "post", url, exception);
            httpResponseFuture.setThrowable(exception);
        }
        return httpResponseFuture;
    }

    public static <T> Optional<T> postForm(String url, Map<String, String> paramMap, TypeReference<T> typeReference) {
        HttpPost httpPost = new HttpPost(url);
        httpPost.setConfig(requestConfig);
        try {
            httpPost.addHeader("Content-Type",
                "application/x-www-form-urlencoded; text/html; charset=" + DEFAULT_ENCODING);
            List<NameValuePair> params = new ArrayList<NameValuePair>(paramMap.size());
            paramMap.entrySet().forEach(new Consumer<Entry<String, String>>() {
                @Override
                public void accept(Entry<String, String> stringStringEntry) {
                    params.add(new BasicNameValuePair(stringStringEntry.getKey(), stringStringEntry.getValue()));
                }
            });
            httpPost.setEntity(new UrlEncodedFormEntity(params, DEFAULT_ENCODING));
            String result = request(httpPost);
            return Optional.ofNullable(JSON.parseObject(result, typeReference));
        } catch (Exception exception) {
            log.error("Http request failed,method:{},url:{}", "post", url, exception);
            return Optional.empty();
        }
    }

    private static String request(HttpRequestBase httpRequest)
        throws IOException, ChaosException, ExecutionException, InterruptedException {
        try {
            HttpResponse resp = requestRawResponse(httpRequest);
            return toString(httpRequest, resp.getEntity(), DEFAULT_ENCODING);
        } finally {
            httpRequest.releaseConnection();
        }
    }

    private static HttpResponse requestRawResponse(HttpRequestBase httpRequest) throws IOException,
            ChaosException, ExecutionException, InterruptedException {
        HttpResponse resp = httpclient.execute(httpRequest, null).get();
        int statusCode = resp.getStatusLine().getStatusCode();
        if (HttpStatus.SC_OK != statusCode && HttpStatus.SC_CREATED != statusCode && HttpStatus.SC_ACCEPTED !=
            statusCode) {
            throw new ChaosException(CommonErrorCode.S_REQUEST_OUTER_SYSTEM_FAILED,
                "StatusCode:" + statusCode + ",error:" + EntityUtils.toString(resp.getEntity()));
        }
        return resp;
    }

    public static String toString(HttpRequestBase httpRequest, HttpEntity entity, String defaultCharset) throws
        IOException {
        try {
            return EntityUtils.toString(entity, defaultCharset);
        } finally {
            try {
                EntityUtils.consume(entity);
            } catch (IOException e) {
                httpRequest.abort();
            }
        }
    }

    @Override
    public void destroy() throws Exception {
        httpclient.close();

    }
}
