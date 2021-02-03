package com.alibaba.chaosblade.platform.service.test;

import cn.hutool.core.collection.CollUtil;
import com.alibaba.chaosblade.platform.cmmon.DeviceMeta;
import com.alibaba.chaosblade.platform.cmmon.constants.ChaosConstant;
import com.alibaba.chaosblade.platform.service.task.AttackActivityTask;
import com.alibaba.chaosblade.platform.service.task.DefaultActivityTaskExecuteContext;
import com.alibaba.chaosblade.platform.service.task.DefaultActivityTaskExecutePipeline;
import com.alibaba.chaosblade.platform.service.task.PrepareActivityTask;
import com.alibaba.chaosblade.platform.service.model.experiment.activity.ActivityTaskDTO;
import com.alibaba.chaosblade.platform.metric.ChaosPlatformApplication;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
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
    private ApplicationContext applicationContext;

    @Test
    public void test() throws Throwable {
        ActivityTaskDTO activityTaskDTO = new ActivityTaskDTO();
        activityTaskDTO.setActivityId(0L);
        HashMap<String, String> flags = new HashMap<>();
        flags.put("process", "ChaosBladeApplication");
        flags.put("debug", "true");

        activityTaskDTO.setArguments(flags);
        activityTaskDTO.setPhase(ChaosConstant.PHASE_PREPARE);

        ArrayList<DeviceMeta> scopes = CollUtil.newArrayList(
                new DeviceMeta(4L, (byte) 0, "leaf", "192.168.0.1")
        );
        activityTaskDTO.setDeviceMetas(scopes);

        DefaultActivityTaskExecutePipeline pipeline = applicationContext.getBean(DefaultActivityTaskExecutePipeline.class);

        pipeline.addList(applicationContext.getBean(PrepareActivityTask.class, activityTaskDTO));
        pipeline.addList(applicationContext.getBean(PrepareActivityTask.class, activityTaskDTO));


        ActivityTaskDTO attack = new ActivityTaskDTO();
        attack.setActivityId(0L);
        HashMap<String, String> attackFlags = new HashMap<String, String>();
        attackFlags.put("process", "ChaosBladeApplication");
        attackFlags.put("debug", "true");

        attack.setArguments(flags);
        attack.setPhase(ChaosConstant.PHASE_ATTACK);
        attack.setDeviceMetas(scopes);

        pipeline.addList(applicationContext.getBean(AttackActivityTask.class, attack));

        DefaultActivityTaskExecuteContext executeContext = applicationContext.getBean(DefaultActivityTaskExecuteContext.class, pipeline);

        executeContext.fireExecute();
        TimeUnit.SECONDS.sleep(1000000);
    }
}
