package com.alibaba.chaosblade.platform.http;

import com.alibaba.chaosblade.platform.http.model.reuest.HttpChannelRequest;
import com.alibaba.chaosblade.platform.invoker.ResponseCommand;
import com.alibaba.testable.core.annotation.MockDiagnose;
import com.alibaba.testable.core.annotation.MockMethod;
import com.alibaba.testable.core.model.LogLevel;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.HttpVersion;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.concurrent.FutureCallback;
import org.apache.http.impl.DefaultHttpResponseFactory;
import org.apache.http.impl.nio.client.CloseableHttpAsyncClient;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpCoreContext;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.concurrent.Future;

/**
 * @author yefei
 */
@Slf4j
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = ChaosBladeHttpAttackChaosInvokerTest.Config.class)
public class ChaosBladeHttpAttackChaosInvokerTest {

    @Configuration
    @ComponentScan("com.alibaba.chaosblade.platform.http")
    public static class Config {
    }

    @Autowired
    private ChaosBladeAttackHttpInvoker chaosBladeAttackHttpInvoker;

    @MockDiagnose(LogLevel.VERBOSE)
    public static class Mock {

        @MockMethod
        public Future<HttpResponse> execute(
                CloseableHttpAsyncClient self,
                final HttpUriRequest request,
                final FutureCallback<HttpResponse> callback) {
            DefaultHttpResponseFactory factory = DefaultHttpResponseFactory.INSTANCE;
            final BasicHttpContext localContext = new BasicHttpContext();
            final HttpCoreContext context = HttpCoreContext.adapt(localContext);
            HttpResponse response = factory.newHttpResponse(HttpVersion.HTTP_1_1,
                    HttpStatus.SC_CONTINUE, context);
            //StringEntity stringEntity = new StringEntity("");
            callback.completed(response);
            return null;
        }

        @MockMethod
        public void setHeader(HttpPost self,
                              final String name, final String value) {
            log.info("mock header");
        }

        @MockMethod
        public StringBuilder append(StringBuilder self, String str) {
            return self.append(str);
        }
    }

    @Test
    public void testAttack() throws Exception {
        HttpChannelRequest requestCommand = new HttpChannelRequest();

        requestCommand.setSceneCode("chaosblade.cpu.fullload");
        requestCommand.setHost("192.168.1.1");
        requestCommand.setPort(19527);

        ResponseCommand responseCommand = chaosBladeAttackHttpInvoker.invoke(requestCommand).get();
        Assert.assertTrue(responseCommand.isSuccess());
    }

}
