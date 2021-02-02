package com.alibaba.chaosblade.platform.cmmon.jackson;

import com.alibaba.chaosblade.platform.cmmon.utils.timer.HashedWheelTimer;
import com.alibaba.chaosblade.platform.cmmon.utils.timer.Timer;
import org.junit.Test;

import java.util.concurrent.TimeUnit;

/**
 * @author yefei
 */
public class HashedWheelTimerTest {

    @Test
    public void simpleTest() throws Exception {
        Timer timer = new HashedWheelTimer();
        timer.newTimeout(timeout -> System.out.println("hello"), 3, TimeUnit.SECONDS);

        Thread.currentThread().join();
    }
}
