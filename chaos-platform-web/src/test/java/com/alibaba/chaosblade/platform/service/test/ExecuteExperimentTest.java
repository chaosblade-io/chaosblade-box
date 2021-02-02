package com.alibaba.chaosblade.platform.service.test;

import com.alibaba.chaosblade.platform.service.ExperimentTaskService;
import com.alibaba.chaosblade.platform.metric.ChaosPlatformApplication;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.concurrent.TimeUnit;

/**
 * @author yefei
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = ChaosPlatformApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ExecuteExperimentTest {

    @Autowired
    private ExperimentTaskService experimentTaskService;

    @Test
    public void test() throws Throwable {
        experimentTaskService.createExperimentTask(1352808627974422530L);

        TimeUnit.SECONDS.sleep(100000);
    }
}
