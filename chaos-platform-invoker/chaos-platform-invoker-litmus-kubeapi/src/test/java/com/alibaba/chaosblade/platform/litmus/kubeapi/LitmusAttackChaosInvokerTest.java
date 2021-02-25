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

/**
 * @author yefei
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = Config.class)
public class LitmusAttackChaosInvokerTest {

    @Autowired
    private LitmusAttackChaosInvoker litmusAttackChaosInvoker;

    @Test
    public void test() throws Exception {
        RequestCommand requestCommand = new RequestCommand();
        requestCommand.setSceneCode("litmuschaos.node-cpu.hog");
        requestCommand.setNamespace("default");

        HashMap<String, String> arguments = new HashMap<>();
        arguments.put("TARGET_NODES", "izuf6gjchf1lak9iqeugulz");
        arguments.put("TOTAL_CHAOS_DURATION", "60");
        requestCommand.setArguments(arguments);

        ResponseCommand responseCommand = litmusAttackChaosInvoker.invoke(requestCommand).get();
        JsonUtils.writeValueAsString(responseCommand);
    }

}
