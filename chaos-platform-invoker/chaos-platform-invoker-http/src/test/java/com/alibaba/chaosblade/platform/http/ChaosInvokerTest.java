package com.alibaba.chaosblade.platform.http;

import com.alibaba.chaosblade.platform.invoker.ResponseCommand;
import com.alibaba.chaosblade.platform.http.model.reuest.HttpChannelRequest;
import com.alibaba.chaosblade.platform.http.model.reuest.ModelRequest;
import com.alibaba.chaosblade.platform.http.model.reuest.PrepareCommandRequest;
import com.alibaba.chaosblade.platform.http.model.reuest.StatusCommandRequest;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.HashMap;
import java.util.concurrent.CompletableFuture;

/**
 * @author yefei
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = ChaosInvokerTest.Config.class)
public class ChaosInvokerTest {

    @Autowired
    private ChaosBladeHttpInvoker chaosBladeHttpInvoker;

    private int chaosAgentPort = 19527;

    @Test
    public void pingTest() throws Throwable {
        HttpChannelRequest request = new HttpChannelRequest();
        request.setRequestURL("/ping");
        request.setHost("192.168.1.1");
        request.setPort(chaosAgentPort);

        CompletableFuture<ResponseCommand> future = chaosBladeHttpInvoker.invoke(request);
        future.thenAccept(r -> {
            System.out.println(r);
            Assert.assertTrue(r.isSuccess());
        }).exceptionally((e) -> {
            e.printStackTrace();
            return null;
        }).get();
    }

    @Test
    public void statusTest() throws Throwable {
        StatusCommandRequest request = new StatusCommandRequest();
        request.setHost("192.168.1.1");
        request.setPort(chaosAgentPort);
        request.setType("prepare");
        request.setUid("6dceac58225d6d15");

        CompletableFuture<ResponseCommand> future = chaosBladeHttpInvoker.invoke(request);
        future.thenAccept(r -> {
            System.out.println(r);
            Assert.assertTrue(r.isSuccess());
        }).exceptionally((e) -> {
            e.printStackTrace();
            return null;
        }).get();
    }

    @Test
    public void prepareTest() throws Throwable {
        PrepareCommandRequest prepareCommandRequest = new PrepareCommandRequest();
        prepareCommandRequest.setHost("192.168.1.1");
        prepareCommandRequest.setScope("host");
        prepareCommandRequest.setPort(chaosAgentPort);
        prepareCommandRequest.setType("jvm");
        HashMap<String, String> flags = new HashMap<String, String>();
        flags.put("process", "Test");
        flags.put("debug", "true");
        prepareCommandRequest.setArguments(flags);

        CompletableFuture<ResponseCommand> future = chaosBladeHttpInvoker.invoke(prepareCommandRequest);
        future.thenAccept(r -> Assert.assertTrue(r.isSuccess()))
                .exceptionally((e) -> {
                    e.printStackTrace();
                    return null;
                })
                .get();
    }

    @Test
    public void attackTest() throws Throwable {

        HashMap<String, String> flags = new HashMap<String, String>();
        ModelRequest modelRequest = new ModelRequest();
        modelRequest.setHost("127.0.0.1");
        modelRequest.setScope("host");
        modelRequest.setPort(19527);
        modelRequest.setTarget("dubbo");
        modelRequest.setAction("threadpoolfull");
        flags.put("process", "ChaosBladeApplication");
        flags.put("debug", "true");
        modelRequest.setArguments(flags);

        CompletableFuture<ResponseCommand> future = chaosBladeHttpInvoker.invoke(modelRequest);
        future.thenAccept(r -> System.out.println(r))
                .exceptionally((e) -> {
                    e.printStackTrace();
                    return null;
                })
                .get();
    }

    @Test
    public void attackNodeTest() throws Throwable {

        HashMap<String, String> flags = new HashMap<String, String>();
        ModelRequest modelRequest = new ModelRequest();
        modelRequest.setHost("192.168.0.1");
        modelRequest.setScope("node");
        modelRequest.setPort(19527);
        modelRequest.setTarget("node-cpu");
        modelRequest.setAction("load");
        flags.put("debug", "true");
        flags.put("cpu-percent", "60");
        flags.put("names", "izuf6gjchf1lak9iqeugunz");
        modelRequest.setArguments(flags);

        CompletableFuture<ResponseCommand> future = chaosBladeHttpInvoker.invoke(modelRequest);
        future.thenAccept(r ->
                System.out.println(r)
        )
                .exceptionally((e) -> {
                    e.printStackTrace();
                    return null;
                })
                .get();
    }

    @Configuration
    @ComponentScan("com.alibaba.chaosblade.platform.http")
    public static class Config {
    }
}
