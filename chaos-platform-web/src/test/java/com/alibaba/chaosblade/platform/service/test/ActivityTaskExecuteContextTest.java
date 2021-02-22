package com.alibaba.chaosblade.platform.service.test;

import cn.hutool.core.collection.CollUtil;
import com.alibaba.chaosblade.platform.cmmon.DeviceMeta;
import com.alibaba.chaosblade.platform.cmmon.constants.ChaosConstant;
import com.alibaba.chaosblade.platform.metric.ChaosPlatformApplication;
import com.alibaba.chaosblade.platform.service.task.ActivityTask;
import com.alibaba.chaosblade.platform.service.task.ActivityTaskExecuteContext;
import com.alibaba.chaosblade.platform.service.task.ActivityTaskExecutePipeline;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

/**
 * @author yefei
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = ChaosPlatformApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ActivityTaskExecuteContextTest {

    @Autowired
    private ActivityTaskExecuteContext activityTaskExecuteContext;

    @Test
    public void test() throws Throwable {
        DeviceMeta deviceMeta = new DeviceMeta();
        ArrayList<DeviceMeta> deviceMetas = CollUtil.newArrayList(deviceMeta);
        deviceMeta.setIp("192.168.0.1");

        // prepare
        ActivityTask prepare = new ActivityTask();
        prepare.setSceneCode("chaosblade.prepare.jvm");
        prepare.setActivityId(0L);
        prepare.setPhase(ChaosConstant.PHASE_PREPARE);
        HashMap<String, String> flags = new HashMap<>();
        flags.put("pid", "21822");
        flags.put("debug", "true");
        prepare.setArguments(flags);
        prepare.setDeviceMetas(deviceMetas);

        // attack
        ActivityTask attack = new ActivityTask();
        prepare.setSceneCode("chaosblade.dubbo.delay");
        attack.setActivityId(0L);
        HashMap<String, String> attackFlags = new HashMap<String, String>();
        attackFlags.put("service", "com.alibaba.demo.HelloService");
        attackFlags.put("consumer", "true");
        attackFlags.put("methodname", "hello");
        attackFlags.put("debug", "true");

        attack.setArguments(flags);
        attack.setPhase(ChaosConstant.PHASE_ATTACK);
        attack.setDeviceMetas(deviceMetas);

        // add task
        ActivityTaskExecutePipeline pipeline = new ActivityTaskExecutePipeline();
        pipeline.addLast(prepare);
        pipeline.addLast(attack);

        activityTaskExecuteContext.fireExecute(pipeline);
        TimeUnit.SECONDS.sleep(1000000);
    }
}
