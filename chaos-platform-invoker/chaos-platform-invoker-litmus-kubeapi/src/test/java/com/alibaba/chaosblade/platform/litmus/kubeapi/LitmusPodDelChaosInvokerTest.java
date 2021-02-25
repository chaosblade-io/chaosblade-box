package com.alibaba.chaosblade.platform.litmus.kubeapi;

import com.alibaba.chaosblade.platform.cmmon.utils.JsonUtils;
import com.alibaba.chaosblade.platform.invoker.RequestCommand;
import com.alibaba.chaosblade.platform.invoker.ResponseCommand;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.HashMap;
import java.util.concurrent.CompletableFuture;

/**
 * @author yefei
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = Config.class)
public class LitmusPodDelChaosInvokerTest {

    @Autowired
    private LitmusAttackChaosInvoker litmusAttackChaosInvoker;

    @Autowired
    private LitmusRecoverChaosInvoker litmusRecoverChaosInvoker;

    @Test
    public void testAttack() throws Exception {
        RequestCommand requestCommand = new RequestCommand();
        requestCommand.setSceneCode("litmuschaos.pod-pod.delete");
        requestCommand.setNamespace("default");

        HashMap<String, String> arguments = new HashMap<>();

        arguments.put("TOTAL_CHAOS_DURATION", "30");
        arguments.put("CHAOS_INTERVAL", "10");
        arguments.put("TARGET_PODS", "tomcat-754d84b64-76ngq");
        arguments.put("FORCE", "false");
        requestCommand.setArguments(arguments);

        ResponseCommand responseCommand = litmusAttackChaosInvoker.invoke(requestCommand).get();
        JsonUtils.writeValueAsString(responseCommand);
    }

    @Test
    public void testRecover() throws Exception {
        RequestCommand requestCommand = new RequestCommand();
        requestCommand.setSceneCode("litmuschaos.pod-pod.delete");
        requestCommand.setName("034bf0bdc3d442ae8b075bc1510da755");
        requestCommand.setNamespace("default");
        CompletableFuture<ResponseCommand> future = litmusRecoverChaosInvoker.invoke(requestCommand);
        ResponseCommand responseCommand = future.get();
        JsonUtils.writeValueAsString(responseCommand);
    }

}
